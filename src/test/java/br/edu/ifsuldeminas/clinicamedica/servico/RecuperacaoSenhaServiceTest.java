package br.edu.ifsuldeminas.clinicamedica.servico;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.UsuarioDAO;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecuperacaoSenhaServiceTest {
    private UsuarioDAO dao;
    private Usuario usuario;
    private RecuperacaoSenhaService service;

    @BeforeEach void prepararBancoIsolado() throws SQLException {
        String jdbcUrl = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1";
        dao = new UsuarioDAO() {
            @Override protected Connection abrirConexao() throws SQLException { return DriverManager.getConnection(jdbcUrl); }
        };
        try (Connection con = DriverManager.getConnection(jdbcUrl); Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE usuario (id INT AUTO_INCREMENT PRIMARY KEY, nomeusuario VARCHAR(255) NOT NULL UNIQUE, "
                    + "senhahash VARCHAR(60) NOT NULL, email VARCHAR(255) UNIQUE)");
        }
        dao.registrarUsuario("aluno", "senha-antiga-123", "aluno@example.com");
        usuario = dao.buscarUsuarioPorNomeUsuario("aluno");
        service = new RecuperacaoSenhaService(dao);
    }

    @Test void cadastroNaoGravaSenhaOriginalERejeitaDuplicados() throws SQLException {
        assertTrue(Seguranca.verificarSenha("senha-antiga-123", usuario.getSenhaHash()));
        assertNotEquals("senha-antiga-123", usuario.getSenhaHash());
        assertThrows(SQLException.class, () -> dao.registrarUsuario("aluno", "outra-senha", "outro@example.com"));
        assertThrows(SQLException.class, () -> dao.registrarUsuario("outro", "outra-senha", "aluno@example.com"));
    }

    @Test void redefinePorEmailSemServicoExternoEArmazenaBCrypt() throws SQLException {
        assertTrue(service.redefinir(" ALUNO@Example.com ", "senha-nova-456", "senha-nova-456"));
        Usuario atualizado = dao.buscarUsuarioPorNomeUsuario("aluno");
        assertNotEquals("senha-nova-456", atualizado.getSenhaHash());
        assertTrue(Seguranca.verificarSenha("senha-nova-456", atualizado.getSenhaHash()));
        assertFalse(Seguranca.verificarSenha("senha-antiga-123", atualizado.getSenhaHash()));
    }

    @Test void trocaSomenteContaDoEmailInformado() throws SQLException {
        dao.registrarUsuario("outro", "senha-outro-123", "outro@example.com");
        String hashOutro = dao.buscarUsuarioPorNomeUsuario("outro").getSenhaHash();
        assertTrue(service.redefinir("aluno@example.com", "senha-nova-456", "senha-nova-456"));
        assertEquals(hashOutro, dao.buscarUsuarioPorNomeUsuario("outro").getSenhaHash());
    }

    @Test void emailInexistenteNaoAlteraContas() throws SQLException {
        assertFalse(service.redefinir("desconhecido@example.com", "senha-nova-456", "senha-nova-456"));
        assertEquals(usuario.getSenhaHash(), dao.buscarUsuarioPorNomeUsuario("aluno").getSenhaHash());
    }

    @Test void confirmacaoIncorretaNaoAlteraSenha() throws SQLException {
        assertThrows(IllegalArgumentException.class,
                () -> service.redefinir("aluno@example.com", "senha-nova-456", "outra-senha"));
        assertEquals(usuario.getSenhaHash(), dao.buscarUsuarioPorNomeUsuario("aluno").getSenhaHash());
    }

    @Test void senhaCurtaOuEmailInvalidoNaoAlteraSenha() throws SQLException {
        assertThrows(IllegalArgumentException.class,
                () -> service.redefinir("aluno@example.com", "curta", "curta"));
        assertThrows(IllegalArgumentException.class,
                () -> service.redefinir("aluno@example.com' OR '1'='1", "senha-nova-456", "senha-nova-456"));
        assertFalse(service.redefinir("inexistente'@example.com", "senha-nova-456", "senha-nova-456"));
        assertEquals(usuario.getSenhaHash(), dao.buscarUsuarioPorNomeUsuario("aluno").getSenhaHash());
    }

    @Test void permiteNovaTrocaDiretaNoModoDeAula() throws SQLException {
        assertTrue(service.redefinir("aluno@example.com", "senha-nova-456", "senha-nova-456"));
        assertTrue(service.redefinir("aluno@example.com", "outra-nova-789", "outra-nova-789"));
        assertTrue(Seguranca.verificarSenha("outra-nova-789", dao.buscarUsuarioPorNomeUsuario("aluno").getSenhaHash()));
    }
}

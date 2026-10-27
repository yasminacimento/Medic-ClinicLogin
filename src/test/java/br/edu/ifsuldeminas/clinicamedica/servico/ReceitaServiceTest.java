package br.edu.ifsuldeminas.clinicamedica.servico;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ReceitaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.ItemReceita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Receita;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReceitaServiceTest {
    private String url;
    private ReceitaDao dao;
    private ReceitaService service;

    @BeforeEach void preparar() throws SQLException {
        url = "jdbc:h2:mem:" + UUID.randomUUID() + ";MODE=MySQL;DB_CLOSE_DELAY=-1";
        dao = new ReceitaDao() {
            @Override protected Connection abrirConexao() throws SQLException { return DriverManager.getConnection(url); }
        };
        service = new ReceitaService(dao);
        try (Connection con = DriverManager.getConnection(url); Statement stmt = con.createStatement()) {
            stmt.execute("CREATE TABLE paciente(id_paciente INT PRIMARY KEY, nome VARCHAR(100))");
            stmt.execute("CREATE TABLE medico(id_medico INT PRIMARY KEY, nome VARCHAR(100))");
            stmt.execute("CREATE TABLE consulta(id_consulta INT PRIMARY KEY, paciente_id INT REFERENCES paciente(id_paciente), medico_id INT REFERENCES medico(id_medico))");
            stmt.execute("CREATE TABLE remedio(id_remedio INT PRIMARY KEY, nome VARCHAR(150))");
            stmt.execute("CREATE TABLE receita(id_receita INT AUTO_INCREMENT PRIMARY KEY, data_emissao DATE NOT NULL, observacoes VARCHAR(300), consulta_id INT NOT NULL REFERENCES consulta(id_consulta))");
            stmt.execute("CREATE TABLE item_receita(id_itemReceita INT AUTO_INCREMENT PRIMARY KEY, medicamento INT NOT NULL REFERENCES remedio(id_remedio), dosagem VARCHAR(100), posologia VARCHAR(255), receita_id INT NOT NULL REFERENCES receita(id_receita))");
            stmt.execute("INSERT INTO paciente VALUES(1, 'Paciente de teste')");
            stmt.execute("INSERT INTO medico VALUES(1, 'Médico de teste')");
            stmt.execute("INSERT INTO consulta VALUES(1, 1, 1)");
            stmt.execute("INSERT INTO remedio VALUES(1, 'Medicamento A'), (2, 'Medicamento B'), (3, 'Medicamento C')");
        }
    }

    @Test void gravaPrincipalEDoisItensComAMesmaChaveGerada() throws SQLException {
        Receita receita = receita();
        int id = service.salvar(receita, List.of(item(1), item(2)));
        assertTrue(id > 0);
        assertEquals(id, receita.getIdReceita());
        assertEquals(1, contar("receita"));
        assertEquals(2, contar("item_receita"));
        assertTrue(dao.buscarItensPorReceita(id).stream().allMatch(i -> i.getReceitaId() == id));
        assertEquals("Paciente de teste", dao.buscarPorId(id).getPacienteNome());
        assertEquals(2, dao.buscarTodasReceitas().get(0).getQuantidadeItens());
    }

    @Test void falhaNoSegundoItemDesfazReceitaEPrimeiroItem() throws SQLException {
        Receita receita = receita();
        assertThrows(SQLException.class, () -> service.salvar(receita, List.of(item(1), item(999))));
        assertEquals(0, contar("receita"));
        assertEquals(0, contar("item_receita"));
        assertNull(receita.getIdReceita());
    }

    @Test void falhaNaEdicaoRestauraCabecalhoEItensAnteriores() throws SQLException {
        Receita receita = receita();
        int id = service.salvar(receita, List.of(item(1), item(2)));
        receita.setObservacoes("Alteração que deve ser desfeita");
        assertThrows(SQLException.class, () -> service.salvar(receita, List.of(item(3), item(999))));
        assertEquals("Observação de teste", dao.buscarPorId(id).getObservacoes());
        assertEquals(List.of(1, 2), dao.buscarItensPorReceita(id).stream().map(ItemReceita::getMedicamentoId).collect(java.util.stream.Collectors.toList()));
    }

    @Test void editaReceitaEItensSemAfetarOutraReceita() throws SQLException {
        Receita primeira = receita();
        int id = service.salvar(primeira, List.of(item(1), item(2)));
        int outra = service.salvar(receita(), List.of(item(2)));
        primeira.setObservacoes("Observação atualizada");
        assertEquals(id, service.salvar(primeira, List.of(item(3))));
        assertEquals(2, contar("receita"));
        assertEquals("Observação atualizada", dao.buscarPorId(id).getObservacoes());
        assertEquals(3, dao.buscarItensPorReceita(id).get(0).getMedicamentoId());
        assertEquals(2, dao.buscarItensPorReceita(outra).get(0).getMedicamentoId());
    }

    @Test void excluiItensEPrincipalJuntos() throws SQLException {
        int id = service.salvar(receita(), List.of(item(1), item(2)));
        int outra = service.salvar(receita(), List.of(item(3)));
        dao.excluirComItens(id);
        assertNull(dao.buscarPorId(id));
        assertTrue(dao.buscarItensPorReceita(id).isEmpty());
        assertNotNull(dao.buscarPorId(outra));
        assertEquals(1, contar("item_receita"));
    }

    @Test void rejeitaReceitaSemItensOuComMedicamentoDuplicado() throws SQLException {
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita(), List.of()));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita(), List.of(item(1), item(1))));
        assertEquals(0, contar("receita"));
    }

    @Test void validaCamposELimitesAntesDeGravar() throws SQLException {
        ItemReceita vazio = item(1); vazio.setPosologia(" ");
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita(), List.of(vazio)));
        vazio.setPosologia("x".repeat(256));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita(), List.of(vazio)));
        Receita receita = receita(); receita.setObservacoes("x".repeat(301));
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita, List.of(item(1))));
        receita.setObservacoes(""); receita.setConsultaId(null);
        assertThrows(IllegalArgumentException.class, () -> service.salvar(receita, List.of(item(1))));
        assertEquals(0, contar("receita"));
    }

    private Receita receita() {
        Receita receita = new Receita(); receita.setConsultaId(1);
        receita.setDataEmissao(Date.valueOf(LocalDate.of(2026, 10, 26)));
        receita.setObservacoes("Observação de teste"); return receita;
    }
    private ItemReceita item(int medicamento) {
        ItemReceita item = new ItemReceita(); item.setMedicamentoId(medicamento);
        item.setDosagem("Dosagem de teste"); item.setPosologia("Posologia de teste"); return item;
    }
    private int contar(String tabela) throws SQLException {
        try (Connection con = DriverManager.getConnection(url); Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tabela)) { rs.next(); return rs.getInt(1); }
    }
}

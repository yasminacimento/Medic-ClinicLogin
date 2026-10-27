package br.edu.ifsuldeminas.clinicamedica.servico;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.UsuarioDAO;
import java.sql.SQLException;

/**
 * Demonstracao escolar: permite trocar a senha informando somente o e-mail cadastrado.
 * Nao comprova a posse do e-mail e nao deve ser utilizado em producao.
 */
public class RecuperacaoSenhaService {
    private final UsuarioDAO usuarioDAO;

    public RecuperacaoSenhaService() { this(new UsuarioDAO()); }

    public RecuperacaoSenhaService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public boolean redefinir(String email, String senha, String confirmacao) throws SQLException {
        String emailNormalizado = Seguranca.normalizarEmail(email);
        Seguranca.validarSenha(senha, confirmacao);
        return usuarioDAO.atualizarSenhaPorEmail(emailNormalizado, Seguranca.hashSenha(senha));
    }
}

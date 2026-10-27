package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Usuario;
import br.edu.ifsuldeminas.clinicamedica.servico.Seguranca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Acesso aos usuarios por JDBC, com consultas parametrizadas. */
public class UsuarioDAO {
    protected Connection abrirConexao() throws SQLException {
        return ConnectionFactory.getInstance().getConnection();
    }

    public Usuario buscarUsuarioPorNomeUsuario(String nome) throws SQLException {
        return buscar("SELECT id, nomeusuario, senhahash, email FROM usuario WHERE nomeusuario = ?", nome);
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SQLException {
        return buscar("SELECT id, nomeusuario, senhahash, email FROM usuario WHERE email = ?", email);
    }

    private Usuario buscar(String sql, String valor) throws SQLException {
        try (Connection con = abrirConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, valor);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new Usuario(rs.getInt("id"), rs.getString("nomeusuario"),
                        rs.getString("senhahash"), rs.getString("email")) : null;
            }
        }
    }

    public void registrarUsuario(String nome, String senha, String email) throws SQLException {
        String hash = Seguranca.hashSenha(senha);
        try (Connection con = abrirConexao(); PreparedStatement stmt = con.prepareStatement(
                "INSERT INTO usuario (nomeusuario, senhahash, email) VALUES (?, ?, ?)")) {
            stmt.setString(1, nome);
            stmt.setString(2, hash);
            stmt.setString(3, email);
            stmt.executeUpdate();
        }
    }

    /** Recuperacao simplificada para aula: seleciona a conta pelo e-mail unico. */
    public boolean atualizarSenhaPorEmail(String email, String novaSenhaHash) throws SQLException {
        try (Connection con = abrirConexao(); PreparedStatement stmt = con.prepareStatement(
                "UPDATE usuario SET senhahash = ? WHERE email = ?")) {
            stmt.setString(1, novaSenhaHash);
            stmt.setString(2, email);
            return stmt.executeUpdate() == 1;
        }
    }
}

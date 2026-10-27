/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author seu_nome
 */
public class FuncionarioDao extends GenericoDAO<Funcionario> {

    public void salvar(Funcionario objFuncionario) {
        String sql = "INSERT INTO FUNCIONARIO (NOME, TELEFONE, EMAIL, ENDERECO, CARGO, SALARIO) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        save(sql,
             objFuncionario.getNome(),
             objFuncionario.getTelefone(),
             objFuncionario.getEmail(),
             objFuncionario.getEndereco(),
             objFuncionario.getCargo(),
             objFuncionario.getSalario());
    }

    public void alterar(Funcionario objFuncionario) {
        String sql = "UPDATE FUNCIONARIO SET NOME=?, TELEFONE=?, EMAIL=?, "
                   + "ENDERECO=?, CARGO=?, SALARIO=? WHERE ID_FUNCIONARIO=?";
        
        save(sql,
             objFuncionario.getNome(),
             objFuncionario.getTelefone(),
             objFuncionario.getEmail(),
             objFuncionario.getEndereco(),
             objFuncionario.getCargo(),
             objFuncionario.getSalario(),
             objFuncionario.getId_funcionario());
    }

    public void excluir(Funcionario objFuncionario) {
        String sql = "DELETE FROM FUNCIONARIO WHERE ID_FUNCIONARIO=?";
        save(sql, objFuncionario.getId_funcionario());
    }

    private static class FuncionarioRowMapper implements RowMapper<Funcionario> {
        @Override
        public Funcionario mapRow(ResultSet rs) throws SQLException {
            Funcionario objFuncionario = new Funcionario();

            objFuncionario.setId_funcionario(rs.getInt("id_funcionario"));
            objFuncionario.setNome(rs.getString("nome"));
            objFuncionario.setTelefone(rs.getString("telefone"));
            objFuncionario.setEmail(rs.getString("email"));
            objFuncionario.setEndereco(rs.getString("endereco"));
            objFuncionario.setCargo(rs.getString("cargo"));
            objFuncionario.setSalario(rs.getDouble("salario"));

            // System.out.println("Mapeando o objeto funcionario > " + objFuncionario.toString()); // descomente se quiser debug

            return objFuncionario;
        }
    }

    public List<Funcionario> buscarTodasFuncionarios() {
        String sql = "SELECT * FROM FUNCIONARIO";
        return buscarTodos(sql, new FuncionarioRowMapper());
    }

    public Funcionario buscarFuncionarioPorId(int id) {
        String sql = "SELECT * FROM FUNCIONARIO WHERE ID_FUNCIONARIO = ?";
        return buscarPorId(sql, new FuncionarioRowMapper(), id);
    }
}
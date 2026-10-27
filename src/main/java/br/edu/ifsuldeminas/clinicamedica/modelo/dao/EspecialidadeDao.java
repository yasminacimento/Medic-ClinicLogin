/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Especialidade;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author 17652433606
 */
public class EspecialidadeDao extends GenericoDAO<Especialidade> {

    public void salvar(Especialidade objEspecialidade) {
        String sql = "INSERT INTO ESPECIALIDADE(NOME, DESCRICAO) VALUES(?,?)";
        save(sql, objEspecialidade.getNome(), objEspecialidade.getDescricao());
    }

    public void alterar(Especialidade objEspecialidade) {
        String sql = "UPDATE ESPECIALIDADE SET NOME=?, DESCRICAO=? WHERE ID_ESPECIALIDADE=?";
        save(sql,
                objEspecialidade.getNome(),
                objEspecialidade.getDescricao(),
                objEspecialidade.getId_especialidade());
    }

    public void excluir(Especialidade objEspecialidade) {
        String sql = "DELETE FROM ESPECIALIDADE WHERE ID_ESPECIALIDADE=?";
        save(sql, objEspecialidade.getId_especialidade());
    }

    private static class EspecialidadeRowMapper implements RowMapper<Especialidade> {

        @Override
        public Especialidade mapRow(ResultSet rs) throws SQLException {
            Especialidade objEspecialidade = new Especialidade();

            objEspecialidade.setId_especialidade(rs.getInt("id_especialidade"));
            objEspecialidade.setNome(rs.getString("nome"));
            objEspecialidade.setDescricao(rs.getString("descricao"));

            System.out.println("Mapeando o objeto especialidade > " + objEspecialidade.toString());
            return objEspecialidade;
        }
    }

    public List<Especialidade> buscarTodasEspecialidades() {
        String sql = "SELECT * FROM especialidade ORDER BY nome";  // tente minúsculo
        return buscarTodos(sql, new EspecialidadeRowMapper());
    }

    public Especialidade buscarEspecialidadePorId(int id) {
        String sql = "SELECT * FROM especialidade WHERE id_especialidade = ?";
        return buscarPorId(sql, new EspecialidadeRowMapper(), id);
    }
}

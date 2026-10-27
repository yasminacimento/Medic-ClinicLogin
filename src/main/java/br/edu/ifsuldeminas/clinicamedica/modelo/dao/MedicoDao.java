/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Medico;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Especialidade;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class MedicoDao extends GenericoDAO<Medico> {

    private final EspecialidadeDao especialidadeDao = new EspecialidadeDao();

    public void salvar(Medico medico) {
        String sql = "INSERT INTO MEDICO(NOME, TELEFONE, CRM, ESPECIALIDADE_ID, EMAIL, ENDERECO) VALUES(?,?,?,?,?,?)";
        save(sql, medico.getNome(), medico.getTelefone(), medico.getCrm(),
             medico.getEspecialidadeId(), medico.getEmail(), medico.getEndereco());
    }

    public void alterar(Medico medico) {
        String sql = "UPDATE MEDICO SET NOME=?, TELEFONE=?, CRM=?, ESPECIALIDADE_ID=?, EMAIL=?, ENDERECO=? WHERE ID_MEDICO=?";
        save(sql, medico.getNome(), medico.getTelefone(), medico.getCrm(),
             medico.getEspecialidadeId(), medico.getEmail(), medico.getEndereco(),
             medico.getIdMedico());
    }

    public void excluir(Medico medico) {
        String sql = "DELETE FROM MEDICO WHERE ID_MEDICO=?";
        save(sql, medico.getIdMedico());
    }

    private class MedicoRowMapper implements RowMapper<Medico> {
        @Override
        public Medico mapRow(ResultSet rs) throws SQLException {
            Medico medico = new Medico();

            medico.setIdMedico(rs.getInt("id_medico"));
            medico.setNome(rs.getString("nome"));
            medico.setTelefone(rs.getString("telefone"));
            medico.setCrm(rs.getString("crm"));
            medico.setEspecialidadeId(rs.getInt("especialidade_id"));
            medico.setEmail(rs.getString("email"));
            medico.setEndereco(rs.getString("endereco"));

            // Carrega o objeto Especialidade completo
            if (medico.getEspecialidadeId() != null) {
                medico.setEspecialidade(especialidadeDao.buscarEspecialidadePorId(medico.getEspecialidadeId()));
            }

            return medico;
        }
    }

    public List<Medico> buscarTodos() {
        String sql = "SELECT * FROM MEDICO ORDER BY NOME";
        return buscarTodos(sql, new MedicoRowMapper());
    }

    public Medico buscarPorId(int id) {
        String sql = "SELECT * FROM MEDICO WHERE ID_MEDICO = ?";
        return buscarPorId(sql, new MedicoRowMapper(), id);
    }
}   
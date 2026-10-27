/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Prontuario;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class ProntuarioDao extends GenericoDAO<Prontuario> {

    private final ConsultaDao consultaDao = new ConsultaDao();

    public void salvar(Prontuario objProntuario) {
        String sql = "INSERT INTO PRONTUARIO(DIAGNOSTICO, DESCRICAO_SINTOMAS, DATA_REGISTRO, CONSULTA_ID) "
                   + "VALUES(?,?,?,?)";
        
        save(sql, 
             objProntuario.getDiagnostico(),
             objProntuario.getDescricaoSintomas(),
             objProntuario.getDataRegistro(),
             objProntuario.getConsultaId()
        );
    }

    public void alterar(Prontuario objProntuario) {
        String sql = "UPDATE PRONTUARIO SET DIAGNOSTICO=?, DESCRICAO_SINTOMAS=?, DATA_REGISTRO=?, CONSULTA_ID=? "
                   + "WHERE ID_PRONTUARIO=?";
        
        save(sql, 
             objProntuario.getDiagnostico(),
             objProntuario.getDescricaoSintomas(),
             objProntuario.getDataRegistro(),
             objProntuario.getConsultaId(),
             objProntuario.getIdProntuario()
        );
    }

    public void excluir(Prontuario objProntuario) {
        String sql = "DELETE FROM PRONTUARIO WHERE ID_PRONTUARIO=?";
        save(sql, objProntuario.getIdProntuario());
    }

    private class ProntuarioRowMapper implements RowMapper<Prontuario> {
        @Override
        public Prontuario mapRow(ResultSet rs) throws SQLException {
            Prontuario p = new Prontuario();
            
            p.setIdProntuario(rs.getInt("id_prontuario"));
            p.setDiagnostico(rs.getString("diagnostico"));
            p.setDescricaoSintomas(rs.getString("descricao_sintomas"));
            p.setConsultaId(rs.getInt("consulta_id"));

            if (rs.getDate("data_registro") != null) {
                p.setDataRegistro(rs.getDate("data_registro").toLocalDate());
            }

            // Carrega a consulta
            if (p.getConsultaId() != null) {
                p.setConsulta(consultaDao.buscarConsultaPorId(p.getConsultaId()));
            }

            return p;
        }
    }

    public List<Prontuario> buscarTodosProntuarios() {
        String sql = "SELECT * FROM PRONTUARIO ORDER BY DATA_REGISTRO DESC";
        return buscarTodos(sql, new ProntuarioRowMapper());
    }

    public Prontuario buscarProntuarioPorId(int id) {
        String sql = "SELECT * FROM PRONTUARIO WHERE ID_PRONTUARIO = ?";
        return buscarPorId(sql, new ProntuarioRowMapper(), id);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Medico;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Paciente;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class ConsultaDao extends GenericoDAO<Consulta> {

    private final MedicoDao medicoDao = new MedicoDao();
    private final PacienteDao pacienteDao = new PacienteDao();
    private final ConsultorioDao consultorioDao = new ConsultorioDao();

    public void salvar(Consulta objConsulta) {
        String sql = "INSERT INTO CONSULTA(DATA, HORA, STATUS, MEDICO_ID, PACIENTE_ID, CONSULTORIO_ID) "
                   + "VALUES(?,?,?,?,?,?)";
        
        save(sql, 
             objConsulta.getData(),
             objConsulta.getHora(),
             objConsulta.getStatus(),
             objConsulta.getMedicoId(),
             objConsulta.getPacienteId(),
             objConsulta.getConsultorioId()
        );
    }

    public void alterar(Consulta objConsulta) {
        String sql = "UPDATE CONSULTA SET DATA=?, HORA=?, STATUS=?, MEDICO_ID=?, PACIENTE_ID=?, CONSULTORIO_ID=? "
                   + "WHERE ID_CONSULTA=?";
        
        save(sql, 
             objConsulta.getData(),
             objConsulta.getHora(),
             objConsulta.getStatus(),
             objConsulta.getMedicoId(),
             objConsulta.getPacienteId(),
             objConsulta.getConsultorioId(),
             objConsulta.getIdConsulta()
        );
    }

    public void excluir(Consulta objConsulta) {
        String sql = "DELETE FROM CONSULTA WHERE ID_CONSULTA=?";
        save(sql, objConsulta.getIdConsulta());
    }

    private class ConsultaRowMapper implements RowMapper<Consulta> {
        @Override
        public Consulta mapRow(ResultSet rs) throws SQLException {
            Consulta c = new Consulta();
            
            c.setIdConsulta(rs.getInt("id_consulta"));
            c.setData(rs.getDate("data").toLocalDate());
            c.setHora(rs.getTime("hora").toLocalTime());
            c.setStatus(rs.getString("status"));
            c.setMedicoId(rs.getInt("medico_id"));
            c.setPacienteId(rs.getInt("paciente_id"));
            c.setConsultorioId(rs.getInt("consultorio_id"));

            // Carregar relacionamentos
            if (c.getMedicoId() != null) {
                c.setMedico(medicoDao.buscarPorId(c.getMedicoId()));
            }
            if (c.getPacienteId() != null) {
                c.setPaciente(pacienteDao.buscarPacientePorId(c.getPacienteId()));
            }
            if (c.getConsultorioId() != null) {
                c.setConsultorio(consultorioDao.buscarConsultorioPorId(c.getConsultorioId()));
            }

            return c;
        }
    }

    public List<Consulta> buscarTodasConsultas() {
        String sql = "SELECT * FROM CONSULTA ORDER BY DATA DESC, HORA DESC";
        return buscarTodos(sql, new ConsultaRowMapper());
    }

    public Consulta buscarConsultaPorId(int id) {
        String sql = "SELECT * FROM CONSULTA WHERE ID_CONSULTA = ?";
        return buscarPorId(sql, new ConsultaRowMapper(), id);
    }
}
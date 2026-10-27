/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Paciente;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Convenio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Tulio Dias
 */
public class PacienteDao extends GenericoDAO<Paciente> {

    private final ConvenioDao convenioDao = new ConvenioDao();

    public void salvar(Paciente objPaciente) {
        String sql = "INSERT INTO PACIENTE(NOME, DATA_NASCIMENTO, CPF, CONVENIO_ID, TELEFONE, EMAIL, ENDERECO) VALUES(?,?,?,?,?,?,?)";
        save(sql, 
             objPaciente.getNome(),
             objPaciente.getDataNascimento(),
             objPaciente.getCpf(),
             objPaciente.getConvenioId(),
             objPaciente.getTelefone(),
             objPaciente.getEmail(),
             objPaciente.getEndereco()
        );
    }

    public void alterar(Paciente objPaciente) {
        String sql = "UPDATE PACIENTE SET NOME=?, DATA_NASCIMENTO=?, CPF=?, CONVENIO_ID=?, TELEFONE=?, EMAIL=?, ENDERECO=? WHERE ID_PACIENTE=?";
        save(sql, 
             objPaciente.getNome(),
             objPaciente.getDataNascimento(),
             objPaciente.getCpf(),
             objPaciente.getConvenioId(),
             objPaciente.getTelefone(),
             objPaciente.getEmail(),
             objPaciente.getEndereco(),
             objPaciente.getIdPaciente()
        );
    }

    public void excluir(Paciente objPaciente) {
        String sql = "DELETE FROM PACIENTE WHERE ID_PACIENTE=?";
        save(sql, objPaciente.getIdPaciente());
    }

    private class PacienteRowMapper implements RowMapper<Paciente> {
        @Override
        public Paciente mapRow(ResultSet rs) throws SQLException {
            Paciente objPaciente = new Paciente();
            
            objPaciente.setIdPaciente(rs.getInt("id_paciente"));
            objPaciente.setNome(rs.getString("nome"));
            objPaciente.setCpf(rs.getString("cpf"));
            objPaciente.setTelefone(rs.getString("telefone"));
            objPaciente.setEmail(rs.getString("email"));
            objPaciente.setEndereco(rs.getString("endereco"));

            if (rs.getDate("data_nascimento") != null) {
                objPaciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
            }

            Integer idConvenio = rs.getObject("convenio_id", Integer.class);
            if (idConvenio != null && idConvenio > 0) {
                objPaciente.setConvenioId(idConvenio);
                objPaciente.setConvenio(convenioDao.buscarConvenioPorId(idConvenio));
            }

            return objPaciente;
        }
    }

    public List<Paciente> buscarTodosPacientes() {
        String sql = "SELECT * FROM PACIENTE ORDER BY NOME";
        return buscarTodos(sql, new PacienteRowMapper());
    }

    public Paciente buscarPacientePorId(int id) {
        String sql = "SELECT * FROM PACIENTE WHERE ID_PACIENTE = ?";
        return buscarPorId(sql, new PacienteRowMapper(), id);
    }
}
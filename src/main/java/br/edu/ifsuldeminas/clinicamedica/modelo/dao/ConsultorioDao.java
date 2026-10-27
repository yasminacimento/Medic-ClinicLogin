/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

/**
 *
 * @author 17652433606
 */

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConsultorioDao extends GenericoDAO<Consultorio> {

    public void salvar(Consultorio objConsultorio) {
        String sql = "INSERT INTO CONSULTORIO (NUMERO, ANDAR, CAPACIDADE) VALUES (?, ?, ?)";
        
        save(sql,
             objConsultorio.getNumero(),
             objConsultorio.getAndar(),
             objConsultorio.getCapacidade());
    }

    public void alterar(Consultorio objConsultorio) {
        String sql = "UPDATE CONSULTORIO SET NUMERO=?, ANDAR=?, CAPACIDADE=? WHERE ID_CONSULTORIO=?";
        
        save(sql,
             objConsultorio.getNumero(),
             objConsultorio.getAndar(),
             objConsultorio.getCapacidade(),
             objConsultorio.getId_consultorio());
    }

    public void excluir(Consultorio objConsultorio) {
        String sql = "DELETE FROM CONSULTORIO WHERE ID_CONSULTORIO=?";
        save(sql, objConsultorio.getId_consultorio());
    }

    private static class ConsultorioRowMapper implements RowMapper<Consultorio> {
        @Override
        public Consultorio mapRow(ResultSet rs) throws SQLException {
            Consultorio objConsultorio = new Consultorio();

            objConsultorio.setId_consultorio(rs.getInt("id_consultorio"));
            objConsultorio.setNumero(rs.getInt("numero"));
            objConsultorio.setAndar(rs.getInt("andar"));
            objConsultorio.setCapacidade(rs.getInt("capacidade"));

            return objConsultorio;
        }
    }

    public List<Consultorio> buscarTodasConsultorios() {
        String sql = "SELECT * FROM CONSULTORIO";
        return buscarTodos(sql, new ConsultorioRowMapper());
    }

    public Consultorio buscarConsultorioPorId(int id) {
        String sql = "SELECT * FROM CONSULTORIO WHERE ID_CONSULTORIO = ?";
        return buscarPorId(sql, new ConsultorioRowMapper(), id);
    }
}
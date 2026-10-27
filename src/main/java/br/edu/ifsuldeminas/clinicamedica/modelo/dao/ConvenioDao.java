/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

/**
 *
 * @author 17652433606
 */

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Convenio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConvenioDao extends GenericoDAO<Convenio> {

    public void salvar(Convenio objConvenio) {
        String sql = "INSERT INTO CONVENIO (NOME, REGISTRO_ANS, TELEFONE_CONTATO) VALUES (?, ?, ?)";
        
        save(sql,
             objConvenio.getNome(),
             objConvenio.getRegistro_ans(),
             objConvenio.getTelefone_contato());
    }

    public void alterar(Convenio objConvenio) {
        String sql = "UPDATE CONVENIO SET NOME=?, REGISTRO_ANS=?, TELEFONE_CONTATO=? WHERE ID_CONVENIO=?";
        
        save(sql,
             objConvenio.getNome(),
             objConvenio.getRegistro_ans(),
             objConvenio.getTelefone_contato(),
             objConvenio.getId_convenio());
    }

    public void excluir(Convenio objConvenio) {
        String sql = "DELETE FROM CONVENIO WHERE ID_CONVENIO=?";
        save(sql, objConvenio.getId_convenio());
    }

    private static class ConvenioRowMapper implements RowMapper<Convenio> {
        @Override
        public Convenio mapRow(ResultSet rs) throws SQLException {
            Convenio objConvenio = new Convenio();

            objConvenio.setId_convenio(rs.getInt("id_convenio"));
            objConvenio.setNome(rs.getString("nome"));
            objConvenio.setRegistro_ans(rs.getString("registro_ans"));
            objConvenio.setTelefone_contato(rs.getString("telefone_contato"));

            return objConvenio;
        }
    }

    public List<Convenio> buscarTodasConvenios() {
        String sql = "SELECT * FROM CONVENIO";
        return buscarTodos(sql, new ConvenioRowMapper());
    }

    public Convenio buscarConvenioPorId(Integer id) {
        String sql = "SELECT * FROM CONVENIO WHERE ID_CONVENIO = ?";
        return buscarPorId(sql, new ConvenioRowMapper(), id);
    }
}

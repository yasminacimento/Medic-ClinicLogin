/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.TipoExame;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consultorio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class TipoExameDao extends GenericoDAO<TipoExame> {

    private final FuncionarioDao funcionarioDao = new FuncionarioDao();
    private final ConsultorioDao consultorioDao = new ConsultorioDao();

    public void salvar(TipoExame objTipoExame) {
        String sql = "INSERT INTO TIPO_EXAME(NOME, DESCRICAO, VALOR, FUNCIONARIO_ID, CONSULTORIO_ID) "
                   + "VALUES(?,?,?,?,?)";
        
        save(sql, 
             objTipoExame.getNome(),
             objTipoExame.getDescricao(),
             objTipoExame.getValor(),
             objTipoExame.getFuncionarioId(),
             objTipoExame.getConsultorioId()
        );
    }

    public void alterar(TipoExame objTipoExame) {
        String sql = "UPDATE TIPO_EXAME SET NOME=?, DESCRICAO=?, VALOR=?, FUNCIONARIO_ID=?, CONSULTORIO_ID=? "
                   + "WHERE ID_TIPOEXAME=?";
        
        save(sql, 
             objTipoExame.getNome(),
             objTipoExame.getDescricao(),
             objTipoExame.getValor(),
             objTipoExame.getFuncionarioId(),
             objTipoExame.getConsultorioId(),
             objTipoExame.getIdTipoExame()
        );
    }

    public void excluir(TipoExame objTipoExame) {
        String sql = "DELETE FROM TIPO_EXAME WHERE ID_TIPOEXAME=?";
        save(sql, objTipoExame.getIdTipoExame());
    }

    private class TipoExameRowMapper implements RowMapper<TipoExame> {
        @Override
        public TipoExame mapRow(ResultSet rs) throws SQLException {
            TipoExame te = new TipoExame();
            
            te.setIdTipoExame(rs.getInt("id_tipoExame"));
            te.setNome(rs.getString("nome"));
            te.setDescricao(rs.getString("descricao"));
            te.setValor(rs.getDouble("valor"));
            te.setFuncionarioId(rs.getInt("funcionario_id"));
            te.setConsultorioId(rs.getObject("consultorio_id", Integer.class));

            // Carregar relacionamentos
            if (te.getFuncionarioId() != null) {
                te.setFuncionario(funcionarioDao.buscarFuncionarioPorId(te.getFuncionarioId()));
            }
            if (te.getConsultorioId() != null) {
                te.setConsultorio(consultorioDao.buscarConsultorioPorId(te.getConsultorioId()));
            }

            return te;
        }
    }

    public List<TipoExame> buscarTodosTipoExames() {
        String sql = "SELECT * FROM TIPO_EXAME ORDER BY NOME";
        return buscarTodos(sql, new TipoExameRowMapper());
    }

    public TipoExame buscarPorId(int id) {
        String sql = "SELECT * FROM TIPO_EXAME WHERE ID_TIPOEXAME = ?";
        return buscarPorId(sql, new TipoExameRowMapper(), id);
    }
}
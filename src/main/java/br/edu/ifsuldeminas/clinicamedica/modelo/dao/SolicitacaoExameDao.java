/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.SolicitacaoExame;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Consulta;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.TipoExame;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Funcionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Alexandre
 */
public class SolicitacaoExameDao extends GenericoDAO<SolicitacaoExame> {

    private final ConsultaDao consultaDao = new ConsultaDao();
    private final TipoExameDao tipoExameDao = new TipoExameDao();
    private final FuncionarioDao funcionarioDao = new FuncionarioDao();

    public void salvar(SolicitacaoExame obj) {
        String sql = "INSERT INTO SOLICITACAO_EXAME(DATA, RESULTADO, CONSULTA_ID, TIPO_EXAME_ID, FUNCIONARIO_ID) "
                   + "VALUES(?,?,?,?,?)";
        
        save(sql, obj.getData(), obj.getResultado(), obj.getConsultaId(), 
             obj.getTipoExameId(), obj.getFuncionarioId());
    }

    public void alterar(SolicitacaoExame obj) {
        String sql = "UPDATE SOLICITACAO_EXAME SET DATA=?, RESULTADO=?, CONSULTA_ID=?, TIPO_EXAME_ID=?, FUNCIONARIO_ID=? "
                   + "WHERE ID_SOLICITACAOEXAME=?";
        
        save(sql, obj.getData(), obj.getResultado(), obj.getConsultaId(), 
             obj.getTipoExameId(), obj.getFuncionarioId(), obj.getIdSolicitacaoExame());
    }

    public void excluir(SolicitacaoExame obj) {
        String sql = "DELETE FROM SOLICITACAO_EXAME WHERE ID_SOLICITACAOEXAME=?";
        save(sql, obj.getIdSolicitacaoExame());
    }

    private class SolicitacaoExameRowMapper implements RowMapper<SolicitacaoExame> {
        @Override
        public SolicitacaoExame mapRow(ResultSet rs) throws SQLException {
            SolicitacaoExame se = new SolicitacaoExame();
            
            se.setIdSolicitacaoExame(rs.getInt("id_solicitacaoExame"));
            if (rs.getDate("data") != null) {
                se.setData(rs.getDate("data").toLocalDate());
            }
            se.setResultado(rs.getString("resultado"));
            se.setConsultaId(rs.getInt("consulta_id"));
            se.setTipoExameId(rs.getInt("tipo_exame_id"));
            se.setFuncionarioId(rs.getInt("funcionario_id"));

            // Carregar relacionamentos
            if (se.getConsultaId() != null) {
                se.setConsulta(consultaDao.buscarConsultaPorId(se.getConsultaId()));
            }
            if (se.getTipoExameId() != null) {
                se.setTipoExame(tipoExameDao.buscarPorId(se.getTipoExameId()));
            }
            if (se.getFuncionarioId() != null) {
                se.setFuncionario(funcionarioDao.buscarFuncionarioPorId(se.getFuncionarioId()));
            }

            return se;
        }
    }

    public List<SolicitacaoExame> buscarTodasSolicitacoes() {
        String sql = "SELECT * FROM SOLICITACAO_EXAME ORDER BY DATA DESC";
        return buscarTodos(sql, new SolicitacaoExameRowMapper());
    }

    public SolicitacaoExame buscarPorId(int id) {
        String sql = "SELECT * FROM SOLICITACAO_EXAME WHERE ID_SOLICITACAOEXAME = ?";
        return buscarPorId(sql, new SolicitacaoExameRowMapper(), id);
    }
}
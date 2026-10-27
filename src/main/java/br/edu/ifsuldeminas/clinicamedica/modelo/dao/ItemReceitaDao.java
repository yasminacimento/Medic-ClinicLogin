package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.ItemReceita;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.List;

public class ItemReceitaDao extends GenericoDAO<ItemReceita> {
    public void salvar(ItemReceita item) {
        save("INSERT INTO item_receita(medicamento, dosagem, posologia, receita_id) VALUES(?,?,?,?)",
                item.getMedicamentoId(), item.getDosagem(), item.getPosologia(), item.getReceitaId());
    }
    public void excluir(Integer idItemReceita) {
        save("DELETE FROM item_receita WHERE id_itemReceita=?", idItemReceita);
    }
    public void alterar(ItemReceita item) {
        save("UPDATE item_receita SET medicamento=?, dosagem=?, posologia=?, receita_id=? WHERE id_itemReceita=?",
                item.getMedicamentoId(), item.getDosagem(), item.getPosologia(), item.getReceitaId(), item.getIdItemReceita());
    }
    public int contarPorReceita(Integer idReceita) {
        return contar("SELECT COUNT(*) FROM item_receita WHERE receita_id=?", idReceita);
    }
    public int contarPorMedicamento(Integer idMedicamento) {
        return contar("SELECT COUNT(*) FROM item_receita WHERE medicamento=?", idMedicamento);
    }
    private int contar(String sql, Integer id) {
        try (Connection conexao = ConnectionFactory.getInstance().getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                return resultado.next() ? resultado.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not check linked prescription items.", e);
        }
    }
    private class ItemReceitaRowMapper implements RowMapper<ItemReceita> {
        @Override public ItemReceita mapRow(ResultSet rs) throws SQLException {
            ItemReceita item = new ItemReceita();
            item.setIdItemReceita(rs.getInt("id_itemReceita"));
            item.setReceitaId(rs.getInt("receita_id"));
            item.setMedicamentoId(rs.getInt("medicamento"));
            item.setMedicamentoNome(rs.getString("medicamento_nome"));
            item.setDosagem(rs.getString("dosagem"));
            item.setPosologia(rs.getString("posologia"));
            return item;
        }
    }
    public List<ItemReceita> buscarTodosItens() {
        String sql = "SELECT ir.*, r.nome AS medicamento_nome FROM item_receita ir "
                + "INNER JOIN remedio r ON r.id_remedio = ir.medicamento "
                + "ORDER BY ir.receita_id DESC, ir.id_itemReceita DESC";
        return buscarTodos(sql, new ItemReceitaRowMapper());
    }
}

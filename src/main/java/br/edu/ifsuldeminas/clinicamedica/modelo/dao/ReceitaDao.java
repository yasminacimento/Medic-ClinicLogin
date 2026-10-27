package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.ItemReceita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Receita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** A receita e seus itens sempre usam a mesma conexao e a mesma transacao. */
public class ReceitaDao {
    private static final String CONSULTA = "SELECT r.*, p.nome AS paciente_nome, m.nome AS medico_nome, "
            + "(SELECT COUNT(*) FROM item_receita ir WHERE ir.receita_id=r.id_receita) AS quantidade_itens "
            + "FROM receita r JOIN consulta c ON c.id_consulta=r.consulta_id "
            + "JOIN paciente p ON p.id_paciente=c.paciente_id JOIN medico m ON m.id_medico=c.medico_id ";

    protected Connection abrirConexao() throws SQLException {
        return ConnectionFactory.getInstance().getConnection();
    }

    public List<Receita> buscarTodasReceitas() throws SQLException {
        List<Receita> receitas = new ArrayList<>();
        try (Connection con = abrirConexao();
                PreparedStatement stmt = con.prepareStatement(CONSULTA + "ORDER BY r.id_receita DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { receitas.add(mapear(rs)); }
        }
        return receitas;
    }

    public Receita buscarPorId(int id) throws SQLException {
        try (Connection con = abrirConexao(); PreparedStatement stmt = con.prepareStatement(CONSULTA + "WHERE r.id_receita=?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next() ? mapear(rs) : null; }
        }
    }

    public List<ItemReceita> buscarItensPorReceita(int id) throws SQLException {
        List<ItemReceita> itens = new ArrayList<>();
        String sql = "SELECT ir.*, m.nome FROM item_receita ir JOIN remedio m ON m.id_remedio=ir.medicamento "
                + "WHERE ir.receita_id=? ORDER BY ir.id_itemReceita";
        try (Connection con = abrirConexao(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemReceita item = new ItemReceita();
                    item.setIdItemReceita(rs.getInt("id_itemReceita"));
                    item.setReceitaId(rs.getInt("receita_id"));
                    item.setMedicamentoId(rs.getInt("medicamento"));
                    item.setMedicamentoNome(rs.getString("nome"));
                    item.setDosagem(rs.getString("dosagem"));
                    item.setPosologia(rs.getString("posologia"));
                    itens.add(item);
                }
            }
        }
        return itens;
    }

    public int salvarComItens(Receita receita, List<ItemReceita> itens) throws SQLException {
        try (Connection con = abrirConexao()) {
            con.setAutoCommit(false);
            try {
                int id;
                if (receita.getIdReceita() == null) {
                    // Primeiro grava o registro principal e recupera a chave gerada pelo MySQL.
                    try (PreparedStatement stmt = con.prepareStatement(
                            "INSERT INTO receita(data_emissao, observacoes, consulta_id) VALUES(?,?,?)",
                            Statement.RETURN_GENERATED_KEYS)) {
                        preencher(stmt, receita);
                        stmt.executeUpdate();
                        try (ResultSet rs = stmt.getGeneratedKeys()) {
                            if (!rs.next()) { throw new SQLException("A receita não recebeu um identificador."); }
                            id = rs.getInt(1);
                        }
                    }
                } else {
                    id = receita.getIdReceita();
                    bloquear(con, id);
                    try (PreparedStatement stmt = con.prepareStatement(
                            "UPDATE receita SET data_emissao=?, observacoes=?, consulta_id=? WHERE id_receita=?")) {
                        preencher(stmt, receita);
                        stmt.setInt(4, id);
                        stmt.executeUpdate();
                    }
                    excluirItens(con, id);
                }
                // Depois grava os itens relacionados, ainda sem confirmar a transacao.
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO item_receita(medicamento, dosagem, posologia, receita_id) VALUES(?,?,?,?)")) {
                    for (ItemReceita item : itens) {
                        stmt.setInt(1, item.getMedicamentoId());
                        stmt.setString(2, item.getDosagem());
                        stmt.setString(3, item.getPosologia());
                        stmt.setInt(4, id);
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
                con.commit();
                receita.setIdReceita(id);
                return id;
            } catch (SQLException | RuntimeException ex) {
                try { con.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            }
        }
    }

    public void excluirComItens(int id) throws SQLException {
        try (Connection con = abrirConexao()) {
            con.setAutoCommit(false);
            try {
                bloquear(con, id);
                excluirItens(con, id);
                try (PreparedStatement stmt = con.prepareStatement("DELETE FROM receita WHERE id_receita=?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                con.commit();
            } catch (SQLException ex) {
                try { con.rollback(); } catch (SQLException rollback) { ex.addSuppressed(rollback); }
                throw ex;
            }
        }
    }

    private void preencher(PreparedStatement stmt, Receita receita) throws SQLException {
        stmt.setDate(1, receita.getDataEmissao());
        stmt.setString(2, receita.getObservacoes());
        stmt.setInt(3, receita.getConsultaId());
    }

    private void bloquear(Connection con, int id) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("SELECT id_receita FROM receita WHERE id_receita=? FOR UPDATE")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) { throw new SQLException("A receita não existe mais."); }
            }
        }
    }

    private void excluirItens(Connection con, int id) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement("DELETE FROM item_receita WHERE receita_id=?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Receita mapear(ResultSet rs) throws SQLException {
        Receita receita = new Receita();
        receita.setIdReceita(rs.getInt("id_receita"));
        receita.setDataEmissao(rs.getDate("data_emissao"));
        receita.setObservacoes(rs.getString("observacoes"));
        receita.setConsultaId(rs.getInt("consulta_id"));
        receita.setPacienteNome(rs.getString("paciente_nome"));
        receita.setMedicoNome(rs.getString("medico_nome"));
        receita.setQuantidadeItens(rs.getInt("quantidade_itens"));
        return receita;
    }
}

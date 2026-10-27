package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Remedio;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RemedioDao extends GenericoDAO<Remedio> {
    public Remedio buscarRemedioPorId(int id) throws SQLException {
        try (java.sql.Connection con = ConnectionFactory.getInstance().getConnection();
                java.sql.PreparedStatement stmt = con.prepareStatement("SELECT id_remedio, nome FROM remedio WHERE id_remedio=?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? new RemedioRowMapper().mapRow(rs) : null;
            }
        }
    }
    public void salvar(Remedio remedio) {
        save("INSERT INTO remedio(nome) VALUES(?)", remedio.getNome());
    }

    public void alterar(Remedio remedio) {
        save("UPDATE remedio SET nome=? WHERE id_remedio=?", remedio.getNome(), remedio.getIdRemedio());
    }

    public void excluir(Remedio remedio) {
        save("DELETE FROM remedio WHERE id_remedio=?", remedio.getIdRemedio());
    }

    private class RemedioRowMapper implements RowMapper<Remedio> {
        @Override
        public Remedio mapRow(ResultSet rs) throws SQLException {
            Remedio remedio = new Remedio();
            remedio.setIdRemedio(rs.getInt("id_remedio"));
            remedio.setNome(rs.getString("nome"));
            return remedio;
        }
    }

    public List<Remedio> buscarTodosRemedios() {
        return buscarTodos("SELECT * FROM remedio ORDER BY nome", new RemedioRowMapper());
    }
}

package br.edu.ifsuldeminas.clinicamedica.servico;

import br.edu.ifsuldeminas.clinicamedica.modelo.dao.ReceitaDao;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.ItemReceita;
import br.edu.ifsuldeminas.clinicamedica.modelo.entidade.Receita;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReceitaService {
    private final ReceitaDao dao;
    public ReceitaService() { this(new ReceitaDao()); }
    public ReceitaService(ReceitaDao dao) { this.dao = dao; }

    public void validarCabecalho(Receita receita) {
        if (receita == null || receita.getDataEmissao() == null
                || receita.getConsultaId() == null || receita.getConsultaId() < 1) {
            throw new IllegalArgumentException("Selecione a consulta e informe a data de emissão.");
        }
        if (receita.getObservacoes() != null && receita.getObservacoes().length() > 300) {
            throw new IllegalArgumentException("As observações devem ter até 300 caracteres.");
        }
    }

    public void validarItem(ItemReceita item) {
        if (item == null || item.getMedicamentoId() == null || item.getMedicamentoId() < 1) {
            throw new IllegalArgumentException("Selecione um medicamento da lista.");
        }
        if (item.getDosagem() == null || item.getDosagem().isBlank() || item.getDosagem().length() > 100) {
            throw new IllegalArgumentException("Informe a dosagem, com até 100 caracteres.");
        }
        if (item.getPosologia() == null || item.getPosologia().isBlank() || item.getPosologia().length() > 255) {
            throw new IllegalArgumentException("Informe a posologia, com até 255 caracteres.");
        }
    }

    public int salvar(Receita receita, List<ItemReceita> itens) throws SQLException {
        validarCabecalho(receita);
        if (itens == null || itens.isEmpty()) {
            throw new IllegalArgumentException("Adicione pelo menos um medicamento antes de finalizar.");
        }
        if (itens.size() > 50) { throw new IllegalArgumentException("Uma receita pode ter até 50 medicamentos."); }
        Set<Integer> medicamentos = new HashSet<>();
        for (ItemReceita item : itens) {
            validarItem(item);
            if (!medicamentos.add(item.getMedicamentoId())) {
                throw new IllegalArgumentException("O mesmo medicamento foi incluído mais de uma vez.");
            }
        }
        return dao.salvarComItens(receita, itens);
    }
}

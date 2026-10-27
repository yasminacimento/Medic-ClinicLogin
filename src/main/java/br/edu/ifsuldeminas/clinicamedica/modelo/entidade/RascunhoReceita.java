package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

import br.edu.ifsuldeminas.clinicamedica.servico.Seguranca;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Os dados ficam na sessao ate o usuario finalizar o cadastro completo. */
public class RascunhoReceita implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String identificador = Seguranca.gerarToken();
    private Receita receita;
    private final List<ItemReceita> itens = new ArrayList<>();

    public RascunhoReceita(Receita receita) { this.receita = receita; }
    public String getIdentificador() { return identificador; }
    public Receita getReceita() { return receita; }
    public void setReceita(Receita receita) { this.receita = receita; }
    public List<ItemReceita> getItens() { return itens; }
}

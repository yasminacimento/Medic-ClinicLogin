package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

/** Medicamento cadastrado e reutilizado nos itens de receita. */
public class Remedio {
    private Integer idRemedio;
    private String nome;

    public Integer getIdRemedio() { return idRemedio; }
    public void setIdRemedio(Integer idRemedio) { this.idRemedio = idRemedio; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

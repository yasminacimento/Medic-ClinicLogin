package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

public class ItemReceita implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idItemReceita;
    private Integer receitaId;
    private Integer medicamentoId;
    private String medicamentoNome;
    private String dosagem;
    private String posologia;

    public Integer getIdItemReceita() { return idItemReceita; }
    public void setIdItemReceita(Integer idItemReceita) { this.idItemReceita = idItemReceita; }
    public Integer getReceitaId() { return receitaId; }
    public void setReceitaId(Integer receitaId) { this.receitaId = receitaId; }
    public Integer getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Integer medicamentoId) { this.medicamentoId = medicamentoId; }
    public String getMedicamentoNome() { return medicamentoNome; }
    public void setMedicamentoNome(String medicamentoNome) { this.medicamentoNome = medicamentoNome; }
    public String getDosagem() { return dosagem; }
    public void setDosagem(String dosagem) { this.dosagem = dosagem; }
    public String getPosologia() { return posologia; }
    public void setPosologia(String posologia) { this.posologia = posologia; }
}

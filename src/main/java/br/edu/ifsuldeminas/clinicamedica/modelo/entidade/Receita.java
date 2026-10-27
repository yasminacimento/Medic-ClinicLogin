package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

import java.io.Serializable;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

public class Receita implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer idReceita;
    private Date dataEmissao;
    private String observacoes;
    private Integer consultaId;
    private String pacienteNome;
    private String medicoNome;
    private int quantidadeItens;

    public Integer getIdReceita() { return idReceita; }
    public void setIdReceita(Integer idReceita) { this.idReceita = idReceita; }
    public Date getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(Date dataEmissao) { this.dataEmissao = dataEmissao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Integer getConsultaId() { return consultaId; }
    public void setConsultaId(Integer consultaId) { this.consultaId = consultaId; }
    public String getPacienteNome() { return pacienteNome; }
    public void setPacienteNome(String pacienteNome) { this.pacienteNome = pacienteNome; }
    public String getMedicoNome() { return medicoNome; }
    public void setMedicoNome(String medicoNome) { this.medicoNome = medicoNome; }
    public int getQuantidadeItens() { return quantidadeItens; }
    public void setQuantidadeItens(int quantidadeItens) { this.quantidadeItens = quantidadeItens; }
    public String getDataFormatada() {
        return dataEmissao == null ? "" : dataEmissao.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}

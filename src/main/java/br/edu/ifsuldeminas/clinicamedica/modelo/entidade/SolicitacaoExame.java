/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Alexandre
 */
public class SolicitacaoExame {

    private Integer idSolicitacaoExame;
    private LocalDate data;
    private String resultado;
    private Integer consultaId;
    private Integer tipoExameId;
    private Integer funcionarioId;

    // Relacionamentos
    private Consulta consulta = new Consulta();
    private TipoExame tipoExame = new TipoExame();
    private Funcionario funcionario = new Funcionario();

    public SolicitacaoExame() {}

    public Integer getIdSolicitacaoExame() {
        return idSolicitacaoExame;
    }

    public void setIdSolicitacaoExame(Integer idSolicitacaoExame) {
        this.idSolicitacaoExame = idSolicitacaoExame;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public Integer getTipoExameId() {
        return tipoExameId;
    }

    public void setTipoExameId(Integer tipoExameId) {
        this.tipoExameId = tipoExameId;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getDataFormatada() {
        if (data == null) return "";
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
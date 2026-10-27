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
public class Prontuario {

    private Integer idProntuario;
    private String diagnostico;
    private String descricaoSintomas;
    private LocalDate dataRegistro;
    private Integer consultaId;

    // Relacionamento
    private Consulta consulta = new Consulta();

    public Prontuario() {}

    public Integer getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(Integer idProntuario) {
        this.idProntuario = idProntuario;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDescricaoSintomas() {
        return descricaoSintomas;
    }

    public void setDescricaoSintomas(String descricaoSintomas) {
        this.descricaoSintomas = descricaoSintomas;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Integer consultaId) {
        this.consultaId = consultaId;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getDataRegistroFormatado() {
        if (dataRegistro == null) return "";
        return dataRegistro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

/**
 *
 * @author 17652433606
 */
public class Convenio {
    private Integer id_convenio;
    private String nome;
    private String registro_ans;
    private String telefone_contato;

    public Integer getId_convenio() {
        return id_convenio;
    }

    public void setId_convenio(Integer id_convenio) {
        this.id_convenio = id_convenio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistro_ans() {
        return registro_ans;
    }

    public void setRegistro_ans(String registro_ans) {
        this.registro_ans = registro_ans;
    }

    public String getTelefone_contato() {
        return telefone_contato;
    }

    public void setTelefone_contato(String telefone_contato) {
        this.telefone_contato = telefone_contato;
    }
}

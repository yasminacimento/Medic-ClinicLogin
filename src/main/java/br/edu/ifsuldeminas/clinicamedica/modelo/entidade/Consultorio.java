/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.entidade;

/**
 *
 * @author 17652433606
 */
public class Consultorio {
    private Integer id_consultorio;
    private Integer numero;
    private Integer andar;
    private Integer capacidade;

    public Integer getId_consultorio() {
        return id_consultorio;
    }

    public void setId_consultorio(Integer id_consultorio) {
        this.id_consultorio = id_consultorio;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getAndar() {
        return andar;
    }

    public void setAndar(Integer andar) {
        this.andar = andar;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }
}

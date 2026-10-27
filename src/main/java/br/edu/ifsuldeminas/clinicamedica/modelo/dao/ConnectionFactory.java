/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsuldeminas.clinicamedica.modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Tulio Dias
 */
public class ConnectionFactory {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/clinica_medica?useSSL=false";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    //variável estática que mantém a instância única da ConnectionFactory
    private static ConnectionFactory instance;

    // o construtor é privado para impedir a criação direta de intâncias da classe fora dela
    private ConnectionFactory() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do banco de dados não encontrado", e);
        }
    }

    // método público estático que permite o acesso a instância unica de conecctionFactory
    // padrão SigleTon - Garante que apenas uma instância seja usada em toda aplicação
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        Connection con = getConnection();
        return con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        // RETURN_GENERATED_KEYS -> recupera o último id gerado(autoIncrement) após a inserção de um registro na tabela
    }

}

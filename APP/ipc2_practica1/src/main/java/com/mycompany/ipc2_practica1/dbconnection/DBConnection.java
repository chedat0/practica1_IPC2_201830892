/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.dbconnection;

import com.mycompany.ipc2_practica1.recursos.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author jeffm
 */
public class DBConnection {
    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String SCHEMA = "eventosdb";
    private static final String USER_NAME = "admindba";
    private static final String PASSWORD = "12345";
    
    private static final String URL = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + SCHEMA;
    
    private static DBConnection instance;
    private Connection connection;
    
    private DBConnection(){
        connect();
    }
    
    public static synchronized DBConnection getInstance(){
        if (instance == null){
            instance = new DBConnection();
        }
        return instance;
    }
    
    public void connect() {
        System.out.println("URL de conexion: " + URL);
        try {
            this.connection= DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Esquema: " + connection.getSchema());
            System.out.println("Catalogo: " + connection.getCatalog());
        } catch (SQLException e) {
            System.out.println("Error al conectarse");
            e.printStackTrace();
            
        }                         
    }
    
    public Connection getConnection() throws SQLException {
        // Si la conexión es nula o está cerrada, reconectar
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }
    
    // Método para iniciar transacción
    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }
    
    // Método para commit
    public void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }
    // Método para rollback
    public void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.error("Error en rollback", e);
        }
    }
    
    // Método para probar la conexión
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            Logger.error("Fallo en prueba de conexión", e);
            return false;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.servicios;

import com.mycompany.ipc2_practica1.dbconnection.DBConnection;
import com.mycompany.ipc2_practica1.recursos.Logger;
import java.sql.*;
/**
 *
 * @author jeffm
 */
public class LogServicio {
    private final DBConnection dbConnection;
    
    public LogServicio() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    public void logSuccess(String instruccion, String mensaje) {
        log(instruccion, "EXITOSO", mensaje);
    }
    
    public void logError(String instruccion, String mensaje) {
        log(instruccion, "ERROR", mensaje);
    }
    
    private void log(String instruccion, String resultado, String mensaje) {
        String sql = "INSERT INTO LogProcesamiento (instruccion, resultado, mensaje) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, instruccion);
            pstmt.setString(2, resultado);
            pstmt.setString(3, mensaje);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            Logger.error("Error guardando log en BD", e);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.dao;

import com.mycompany.ipc2_practica1.modelo.entidades.Evento;
import com.mycompany.ipc2_practica1.modelo.enums.TipoEvento;
import com.mycompany.ipc2_practica1.dbconnection.DBConnection;
import com.mycompany.ipc2_practica1.recursos.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class EventoDAO {
    private final DBConnection dbConnection;
    
    public EventoDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    public boolean insertar(Evento evento) {
        String sql = "INSERT INTO Evento (codigo_evento, fecha, tipo_evento, titulo, " +
                    "ubicacion, cupo_maximo, costo_inscripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, evento.getCodigoEvento());
            pstmt.setDate(2, Date.valueOf(evento.getFecha()));
            pstmt.setString(3, evento.getTipoEvento().name());
            pstmt.setString(4, evento.getTitulo());
            pstmt.setString(5, evento.getUbicacion());
            pstmt.setInt(6, evento.getCupoMaximo());
            pstmt.setBigDecimal(7, evento.getCostoInscripcion());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error insertando evento: " + evento.getCodigoEvento(), e);
            return false;
        }
    }
    
    public boolean actualizar(Evento evento) {
        String sql = "UPDATE Evento SET fecha = ?, tipo_evento = ?, titulo = ?, " +
                    "ubicacion = ?, cupo_maximo = ?, costo_inscripcion = ? " +
                    "WHERE codigo_evento = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(evento.getFecha()));
            pstmt.setString(2, evento.getTipoEvento().name());
            pstmt.setString(3, evento.getTitulo());
            pstmt.setString(4, evento.getUbicacion());
            pstmt.setInt(5, evento.getCupoMaximo());
            pstmt.setBigDecimal(6, evento.getCostoInscripcion());
            pstmt.setString(7, evento.getCodigoEvento());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error actualizando evento: " + evento.getCodigoEvento(), e);
            return false;
        }
    }
    
    public boolean eliminar(String codigoEvento) {
        String sql = "DELETE FROM Evento WHERE codigo_evento = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoEvento);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error eliminando evento: " + codigoEvento, e);
            return false;
        }
    }
    
    public Evento buscarPorCodigo(String codigoEvento) {
        String sql = "SELECT * FROM Evento WHERE codigo_evento = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoEvento);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEvento(rs);
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error buscando evento: " + codigoEvento, e);
        }
        
        return null;
    }
    
    public List<Evento> obtenerTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Evento ORDER BY fecha DESC";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                eventos.add(mapearEvento(rs));
            }
            
        } catch (SQLException e) {
            Logger.error("Error obteniendo todos los eventos", e);
        }
        
        return eventos;
    }
    
    public List<Evento> buscar(String criterio) {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Evento WHERE codigo_evento LIKE ? OR titulo LIKE ? " +
                    "OR ubicacion LIKE ? ORDER BY fecha DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + criterio + "%";
            pstmt.setString(1, patron);
            pstmt.setString(2, patron);
            pstmt.setString(3, patron);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    eventos.add(mapearEvento(rs));
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error buscando eventos con criterio: " + criterio, e);
        }
        
        return eventos;
    }
    
    public int obtenerCupoDisponible(String codigoEvento) {
        String sql = "SELECT e.cupo_maximo - COUNT(i.id_inscripcion) as cupo_disponible " +
                    "FROM Evento e " +
                    "LEFT JOIN Inscripcion i ON e.codigo_evento = i.codigo_evento " +
                    "WHERE e.codigo_evento = ? AND i.validada = true " +
                    "GROUP BY e.codigo_evento, e.cupo_maximo";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoEvento);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cupo_disponible");
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error obteniendo cupo disponible: " + codigoEvento, e);
        }
        
        // Si no hay inscripciones, retornar el cupo máximo
        Evento evento = buscarPorCodigo(codigoEvento);
        return evento != null ? evento.getCupoMaximo() : 0;
    }
    
    public boolean existe(String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM Evento WHERE codigo_evento = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigoEvento);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error verificando existencia de evento: " + codigoEvento, e);
        }
        
        return false;
    }
    
    private Evento mapearEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setCodigoEvento(rs.getString("codigo_evento"));
        evento.setFecha(rs.getDate("fecha").toLocalDate());
        evento.setTipoEvento(TipoEvento.valueOf(rs.getString("tipo_evento")));
        evento.setTitulo(rs.getString("titulo"));
        evento.setUbicacion(rs.getString("ubicacion"));
        evento.setCupoMaximo(rs.getInt("cupo_maximo"));
        evento.setCostoInscripcion(rs.getBigDecimal("costo_inscripcion"));
        return evento;
    }
}

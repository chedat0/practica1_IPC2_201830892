/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.dao;

import com.mycompany.ipc2_practica1.dbconnection.DBConnection;
import com.mycompany.ipc2_practica1.modelo.entidades.Participante;
import com.mycompany.ipc2_practica1.modelo.enums.TipoParticipante;
import com.mycompany.ipc2_practica1.recursos.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class ParticipanteDAO {
    private final DBConnection dbConnection;
    
    public ParticipanteDAO() {
        this.dbConnection = DBConnection.getInstance();
    }
    
    public boolean insertar(Participante participante) {
        String sql = "INSERT INTO Participante (nombre_completo, " +
                    "tipo_participante, institucion_de_procedencia, correo_electronico) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                       
            pstmt.setString(1, participante.getNombreCompleto());
            pstmt.setString(2, participante.getTipoParticipante().name());
            pstmt.setString(3, participante.getInstitucionProcedencia());
            pstmt.setString(4, participante.getCorreoElectronico());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error insertando participante: " + participante.getCorreoElectronico(), e);
            return false;
        }
    }
    
    public boolean actualizar(Participante participante) {
        String sql = "UPDATE Participante SET nombre_completo = ?, tipo_participante = ?, " +
                    "institucion_procedencia = ? WHERE correo_electronico = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, participante.getNombreCompleto());
            pstmt.setString(2, participante.getTipoParticipante().name());
            pstmt.setString(3, participante.getInstitucionProcedencia());
            pstmt.setString(4, participante.getCorreoElectronico());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error actualizando participante: " + participante.getCorreoElectronico(), e);
            return false;
        }
    }
    
    public boolean eliminar(String correoElectronico) {
        String sql = "DELETE FROM Participante WHERE correo_electronico = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correoElectronico);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            Logger.error("Error eliminando participante: " + correoElectronico, e);
            return false;
        }
    }
    
    public Participante buscarPorCorreo(String correoElectronico) {
        String sql = "SELECT * FROM Participante WHERE correo_electronico = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correoElectronico);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearParticipante(rs);
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error buscando participante: " + correoElectronico, e);
        }
        
        return null;
    }
    
    public List<Participante> obtenerTodos() {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT * FROM Participante ORDER BY nombre_completo";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                participantes.add(mapearParticipante(rs));
            }
            
        } catch (SQLException e) {
            Logger.error("Error obteniendo todos los participantes", e);
        }
        
        return participantes;
    }
    
    public List<Participante> buscar(String criterio) {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT * FROM Participante WHERE correo_electronico LIKE ? " +
                    "OR nombre_completo LIKE ? OR institucion_procedencia LIKE ? " +
                    "ORDER BY nombre_completo";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String patron = "%" + criterio + "%";
            pstmt.setString(1, patron);
            pstmt.setString(2, patron);
            pstmt.setString(3, patron);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    participantes.add(mapearParticipante(rs));
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error buscando participantes con criterio: " + criterio, e);
        }
        
        return participantes;
    }
    
    public int contarEventosInscritos(String correoElectronico) {
        String sql = "SELECT COUNT(*) FROM Inscripcion WHERE correo_participante = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correoElectronico);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error contando eventos inscritos: " + correoElectronico, e);
        }
        
        return 0;
    }
    
    public boolean existe(String correoElectronico) {
        String sql = "SELECT COUNT(*) FROM Participante WHERE correo_electronico = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, correoElectronico);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            Logger.error("Error verificando existencia de participante: " + correoElectronico, e);
        }
        
        return false;
    }
    
    private Participante mapearParticipante(ResultSet rs) throws SQLException {
        Participante participante = new Participante();
        
        participante.setNombreCompleto(rs.getString("nombre_completo"));
        participante.setTipoParticipante(TipoParticipante.valueOf(rs.getString("tipo_participante")));
        participante.setInstitucionProcedencia(rs.getString("institucion_de_procedencia"));   
        participante.setCorreoElectronico(rs.getString("correo_electronico"));
        return participante;
    }

}

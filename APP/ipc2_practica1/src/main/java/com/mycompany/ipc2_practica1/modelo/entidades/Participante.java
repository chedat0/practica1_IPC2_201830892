/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.entidades;

import com.mycompany.ipc2_practica1.modelo.enums.TipoParticipante;
/**
 *
 * @author jeffm
 */
public class Participante {
    private String correoElectronico;
    private String nombreCompleto;
    private TipoParticipante tipoParticipante;
    private String institucionProcedencia;   
    
    // Constructor vacío
    public Participante() {}
    
    // Constructor completo
    public Participante(String correoElectronico, String nombreCompleto, 
                       TipoParticipante tipoParticipante, String institucionProcedencia) {
        this.correoElectronico = correoElectronico;
        this.nombreCompleto = nombreCompleto;
        this.tipoParticipante = tipoParticipante;
        this.institucionProcedencia = institucionProcedencia;   
    }
    
    // Validaciones
    public boolean isValid() {
        return correoElectronico != null && correoElectronico.matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
               nombreCompleto != null && nombreCompleto.length() <= 45 && !nombreCompleto.trim().isEmpty() &&
               tipoParticipante != null &&
               institucionProcedencia != null && institucionProcedencia.length() <= 150;
    }
    
    // Getters y Setters
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { 
        this.correoElectronico = correoElectronico; 
    }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }
    
    public TipoParticipante getTipoParticipante() { return tipoParticipante; }
    public void setTipoParticipante(TipoParticipante tipoParticipante) { 
        this.tipoParticipante = tipoParticipante; 
    }
    
    public String getInstitucionProcedencia() { return institucionProcedencia; }
    public void setInstitucionProcedencia(String institucionProcedencia) { 
        this.institucionProcedencia = institucionProcedencia; 
    }      
    
    @Override
    public String toString() {
        return String.format("%s - %s", nombreCompleto, correoElectronico);
    }    
}

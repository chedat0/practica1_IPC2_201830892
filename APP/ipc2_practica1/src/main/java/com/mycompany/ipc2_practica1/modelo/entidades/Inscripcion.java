/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.entidades;

import com.mycompany.ipc2_practica1.modelo.enums.TipoInscripcion;
import java.time.LocalDateTime;
/**
 *
 * @author jeffm
 */
public class Inscripcion {
    private int idInscripcion;
    private String correoParticipante;
    private String codigoEvento;
    private TipoInscripcion tipoInscripcion;
    private boolean validada;
    private LocalDateTime fechaInscripcion;
    private LocalDateTime fechaValidacion;
    
    
    //Constructor sin parametros
    public Inscripcion() {
        this.validada = false;
        this.fechaInscripcion = LocalDateTime.now();
    }
    
    // Constructor con parametros
    public Inscripcion(String correoParticipante, String codigoEvento, TipoInscripcion tipoInscripcion) {
        this.correoParticipante = correoParticipante;
        this.codigoEvento = codigoEvento;
        this.tipoInscripcion = tipoInscripcion;
        this.validada = false;
        this.fechaInscripcion = LocalDateTime.now();
    }
    
    //validaciones
    public void validar() {
        this.validada = true;
        this.fechaValidacion = LocalDateTime.now();
    }
    
    public boolean puedeSerValidada() {
        return !validada;
    }
    
    // Getters y Setters
    public int getIdInscripcion() { return idInscripcion; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    
    public String getCorreoParticipante() { return correoParticipante; }
    public void setCorreoParticipante(String correoParticipante) { 
        this.correoParticipante = correoParticipante; 
    }
    
    public String getCodigoEvento() { return codigoEvento; }
    public void setCodigoEvento(String codigoEvento) { this.codigoEvento = codigoEvento; }
    
    public TipoInscripcion getTipoInscripcion() { return tipoInscripcion; }
    public void setTipoInscripcion(TipoInscripcion tipoInscripcion) { 
        this.tipoInscripcion = tipoInscripcion; 
    }
    
    public boolean isValidada() { return validada; }
    public void setValidada(boolean validada) { this.validada = validada; }
    
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDateTime fechaInscripcion) { 
        this.fechaInscripcion = fechaInscripcion; 
    }
    
    public LocalDateTime getFechaValidacion() { return fechaValidacion; }
    public void setFechaValidacion(LocalDateTime fechaValidacion) { 
        this.fechaValidacion = fechaValidacion; 
    }
}

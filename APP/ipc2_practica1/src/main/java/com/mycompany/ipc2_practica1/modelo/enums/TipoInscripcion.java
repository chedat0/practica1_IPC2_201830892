/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.enums;

/**
 *
 * @author jeffm
 */
public enum TipoInscripcion {
    ASISTENTE("Asistente"),
    CONFERENCISTA("Conferencista"),
    TALLERISTA("Tallerista"),
    OTRO("Otro");
    
    private final String descripcion;
    
    TipoInscripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}

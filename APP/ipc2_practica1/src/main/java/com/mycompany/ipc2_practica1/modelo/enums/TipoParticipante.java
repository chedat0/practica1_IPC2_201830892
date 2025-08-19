/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.enums;

/**
 *
 * @author jeffm
 */
public enum TipoParticipante {
    ESTUDIANTE("Estudiante"),
    PROFESIONAL("Profesional"),
    INVITADO("Invitado");
    
    private final String descripcion;
    
    TipoParticipante(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}

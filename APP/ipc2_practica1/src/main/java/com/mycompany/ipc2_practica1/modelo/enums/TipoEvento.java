/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.enums;

/**
 *
 * @author jeffm
 */
public enum TipoEvento {
    CHARLA("Charla"),
    CONGRESO("Congreso"),
    TALLER("Taller"),
    DEBATE("Debate");
    
    private final String descripcion;
    
    TipoEvento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
    
    public static TipoEvento fromString(String text) {
        for (TipoEvento tipo : TipoEvento.values()) {
            if (tipo.name().equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de evento no válido: " + text);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.entidades;

import com.mycompany.ipc2_practica1.modelo.enums.TipoEvento;
import java.math.BigDecimal;
import java.time.LocalDate;
/**
 *
 * @author jeffm
 */
public class Evento {
    private String codigoEvento;
    private LocalDate fecha;
    private TipoEvento tipoEvento;
    private String titulo;
    private String ubicacion;
    private int cupoMaximo;
    private BigDecimal costoInscripcion;
    
    // Constructor vacío 
    public Evento() {}
    
    // Constructor completo
    public Evento(String codigoEvento, LocalDate fecha, TipoEvento tipoEvento, 
                  String titulo, String ubicacion, int cupoMaximo, BigDecimal costoInscripcion) {
        this.codigoEvento = codigoEvento;
        this.fecha = fecha;
        this.tipoEvento = tipoEvento;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.cupoMaximo = cupoMaximo;
        this.costoInscripcion = costoInscripcion;        
    }
    
    // Validaciones
    public boolean isValid() {
        return codigoEvento != null && !codigoEvento.trim().isEmpty() &&
               fecha != null &&
               tipoEvento != null &&
               titulo != null && titulo.length() <= 200 &&
               ubicacion != null && ubicacion.length() <= 150 &&
               cupoMaximo > 0 &&
               costoInscripcion != null && costoInscripcion.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    // Getters y Setters
    public String getCodigoEvento() { return codigoEvento; }
    public void setCodigoEvento(String codigoEvento) { this.codigoEvento = codigoEvento; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public TipoEvento getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(TipoEvento tipoEvento) { this.tipoEvento = tipoEvento; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    
    public BigDecimal getCostoInscripcion() { return costoInscripcion; }
    public void setCostoInscripcion(BigDecimal costoInscripcion) { this.costoInscripcion = costoInscripcion; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s)", codigoEvento, titulo, fecha);
    }

}

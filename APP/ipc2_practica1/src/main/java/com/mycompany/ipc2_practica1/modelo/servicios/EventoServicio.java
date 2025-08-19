/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.servicios;

import com.mycompany.ipc2_practica1.modelo.dao.EventoDAO;
import com.mycompany.ipc2_practica1.modelo.entidades.Evento;
import com.mycompany.ipc2_practica1.modelo.enums.TipoEvento;
import com.mycompany.ipc2_practica1.recursos.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class EventoServicio {
    private final EventoDAO eventoDAO;
    private final DateTimeFormatter dateFormatter;
    
    public EventoServicio() {
        this.eventoDAO = new EventoDAO();
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }
    
    public boolean registrarEvento(String codigo, String fecha, String tipo, 
                                  String titulo, String ubicacion, int cupo, double costo) {
        try {
            // Validar que no exista el evento
            if (eventoDAO.existe(codigo)) {
                Logger.warning("Evento ya existe: " + codigo);
                return false;
            }
            
            // Crear el evento
            Evento evento = new Evento();
            evento.setCodigoEvento(codigo);
            evento.setFecha(LocalDate.parse(fecha, dateFormatter));
            evento.setTipoEvento(TipoEvento.valueOf(tipo.toUpperCase()));
            evento.setTitulo(titulo);
            evento.setUbicacion(ubicacion);
            evento.setCupoMaximo(cupo);
            evento.setCostoInscripcion(BigDecimal.valueOf(costo));
            
            // Validar el evento
            if (!evento.isValid()) {
                Logger.warning("Datos de evento inválidos: " + codigo);
                return false;
            }
            
            // Insertar en base de datos
            boolean resultado = eventoDAO.insertar(evento);
            
            if (resultado) {
                Logger.info("Evento registrado exitosamente: " + codigo);
            }
            
            return resultado;
            
        } catch (Exception e) {
            Logger.error("Error registrando evento: " + codigo, e);
            return false;
        }
    }
    
    public boolean guardar(Evento evento) {
        try {
            if (!evento.isValid()) {
                throw new IllegalArgumentException("Datos del evento inválidos");
            }
            
            if (eventoDAO.existe(evento.getCodigoEvento())) {
                return eventoDAO.actualizar(evento);
            } else {
                return eventoDAO.insertar(evento);
            }
        } catch (Exception e) {
            Logger.error("Error guardando evento", e);
            throw new RuntimeException("Error al guardar evento: " + e.getMessage());
        }
    }
    
    public boolean eliminar(String codigoEvento) {
        try {
            return eventoDAO.eliminar(codigoEvento);
        } catch (Exception e) {
            Logger.error("Error eliminando evento: " + codigoEvento, e);
            throw new RuntimeException("Error al eliminar evento: " + e.getMessage());
        }
    }
    
    public Evento buscarPorCodigo(String codigoEvento) {
        return eventoDAO.buscarPorCodigo(codigoEvento);
    }
    
    public List<Evento> obtenerTodos() {
        return eventoDAO.obtenerTodos();
    }
    
    public List<Evento> buscar(String criterio) {
        return eventoDAO.buscar(criterio);
    }
    
    public int obtenerCupoDisponible(String codigoEvento) {
        return eventoDAO.obtenerCupoDisponible(codigoEvento);
    }
    
    public boolean hayCupoDisponible(String codigoEvento) {
        return obtenerCupoDisponible(codigoEvento) > 0;
    }
    
    public int obtenerTotalInscritos(String codigoEvento) {
        Evento evento = buscarPorCodigo(codigoEvento);
        if (evento != null) {
            return evento.getCupoMaximo() - obtenerCupoDisponible(codigoEvento);
        }
        return 0;
    }
}

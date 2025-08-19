/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.modelo.servicios;

import com.mycompany.ipc2_practica1.modelo.dao.ParticipanteDAO;
import com.mycompany.ipc2_practica1.modelo.entidades.Participante;
import com.mycompany.ipc2_practica1.modelo.enums.TipoParticipante;
import com.mycompany.ipc2_practica1.recursos.Logger;
import java.util.List;
/**
 *
 * @author jeffm
 */
public class ParticipanteServicio {
    private final ParticipanteDAO participanteDAO;
    
    public ParticipanteServicio() {
        this.participanteDAO = new ParticipanteDAO();
    }
    
    public boolean registrarParticipante(String correo, String nombre, 
                                        String tipo, String institucion) {
        try {
            // Validar que no exista el participante
            if (participanteDAO.existe(correo)) {
                Logger.warning("Participante ya existe: " + correo);
                return false;
            }
            
            // Crear el participante
            Participante participante = new Participante();
            participante.setCorreoElectronico(correo);
            participante.setNombreCompleto(nombre);
            participante.setTipoParticipante(TipoParticipante.valueOf(tipo.toUpperCase()));
            participante.setInstitucionProcedencia(institucion);
            
            // Validar el participante
            if (!participante.isValid()) {
                Logger.warning("Datos de participante inválidos: " + correo);
                return false;
            }
            
            // Insertar en base de datos
            boolean resultado = participanteDAO.insertar(participante);
            
            if (resultado) {
                Logger.info("Participante registrado exitosamente: " + correo);
            }
            
            return resultado;
            
        } catch (Exception e) {
            Logger.error("Error registrando participante: " + correo, e);
            return false;
        }
    }
    
    public boolean guardar(Participante participante) {
        try {
            if (!participante.isValid()) {
                throw new IllegalArgumentException("Datos del participante inválidos");
            }
            
            if (participanteDAO.existe(participante.getCorreoElectronico())) {
                return participanteDAO.actualizar(participante);
            } else {
                return participanteDAO.insertar(participante);
            }
        } catch (Exception e) {
            Logger.error("Error guardando participante", e);
            throw new RuntimeException("Error al guardar participante: " + e.getMessage());
        }
    }
    
    public boolean eliminar(String correoElectronico) {
        try {
            return participanteDAO.eliminar(correoElectronico);
        } catch (Exception e) {
            Logger.error("Error eliminando participante: " + correoElectronico, e);
            throw new RuntimeException("Error al eliminar participante: " + e.getMessage());
        }
    }
    
    public Participante buscarPorCorreo(String correoElectronico) {
        return participanteDAO.buscarPorCorreo(correoElectronico);
    }
    
    public List<Participante> obtenerTodos() {
        return participanteDAO.obtenerTodos();
    }
    
    public List<Participante> buscar(String criterio) {
        return participanteDAO.buscar(criterio);
    }
    
    public int contarEventosInscritos(String correoElectronico) {
        return participanteDAO.contarEventosInscritos(correoElectronico);
    }
    
    public boolean existeParticipante(String correoElectronico) {
        return participanteDAO.existe(correoElectronico);
    }
}

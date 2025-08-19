/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.recursos;
import com.mycompany.ipc2_practica1.modelo.servicios.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
/**
 *
 * @author jeffm
 */
public class Parser {
    
    private static final Pattern INSTRUCTION_PATTERN = 
        Pattern.compile("^(\\w+)\\((.*)\\);?$");
    private static final Pattern PARAMETER_PATTERN = 
        Pattern.compile("\"([^\"]*)\"|([^,]+)");
    
    private final EventoServicio eventoService;
    private final ParticipanteServicio participanteService;
    //private final InscripcionServicio inscripcionService;
    //private final ActividadServicio actividadService;
    //private final PagoServicio pagoService;
    //private final AsistenciaServicio asistenciaService;
    //private final CertificadoServicio certificadoService;
    //private final ReporteServicio reporteService;
    private final LogServicio logService;
    
    private String outputPath;
    private int processingDelay;
    private boolean stopProcessing = false;
    
    // Callback para actualizar UI
    private ProcessingCallback callback;
    
    public interface ProcessingCallback {
        void onInstructionProcessed(String instruction, boolean success, String message);
        void onProgressUpdate(int current, int total);
        void onProcessingComplete(int successful, int failed);
    }
    
    public Parser() {
        this.eventoService = new EventoServicio();
        this.participanteService = new ParticipanteServicio();
        //this.inscripcionService = new InscripcionServicio();
        //this.actividadService = new ActividadServicio();
        //this.pagoService = new PagoServicio();
        //this.asistenciaService = new AsistenciaServicio();
        //this.certificadoService = new CertificadoServicio();
        //this.reporteService = new ReporteServicio();
        this.logService = new LogServicio();
    }
    
    public void processFile(String filePath, int delayMs, String outputPath, ProcessingCallback callback) {
        this.processingDelay = delayMs;
        this.outputPath = outputPath;
        this.callback = callback;
        this.stopProcessing = false;
        
        // Crear directorio de salida si no existe
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        // Ejecutar en hilo separado para no bloquear interfaz
        CompletableFuture.runAsync(() -> {
            int successCount = 0;
            int errorCount = 0;
            List<String> instructions = readFile(filePath);
            int total = instructions.size();
            
            for (int i = 0; i < instructions.size() && !stopProcessing; i++) {
                String instruction = instructions.get(i).trim();
                
                if (instruction.isEmpty() || instruction.startsWith("//")) {
                    continue; // Ignorar líneas vacías y comentarios
                }
                
                try {
                    boolean success = processInstruction(instruction);
                    
                    if (success) {
                        successCount++;
                        logService.logSuccess(instruction, "Procesado correctamente");
                        if (callback != null) {
                            callback.onInstructionProcessed(instruction, true, "OK");
                        }
                    } else {
                        errorCount++;
                        logService.logError(instruction, "Error en procesamiento");
                        if (callback != null) {
                            callback.onInstructionProcessed(instruction, false, "Error");
                        }
                    }
                    
                } catch (Exception e) {
                    errorCount++;
                    String errorMsg = "Error: " + e.getMessage();
                    logService.logError(instruction, errorMsg);
                    if (callback != null) {
                        callback.onInstructionProcessed(instruction, false, errorMsg);
                    }
                    Logger.error("Error procesando instrucción: " + instruction, e);
                }
                
                // Actualizar progreso
                if (callback != null) {
                    callback.onProgressUpdate(i + 1, total);
                }
                
                // Aplicar delay
                if (processingDelay > 0) {
                    try {
                        Thread.sleep(processingDelay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
            
            // Notificar finalización
            if (callback != null) {
                callback.onProcessingComplete(successCount, errorCount);
            }
        });
    }
    
    //Leer archivo de carga
    private List<String> readFile(String filePath) {
        List<String> instructions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                instructions.add(line);
            }
        } catch (IOException e) {
            Logger.error("Error leyendo archivo: " + filePath, e);
        }
        return instructions;
    }
    
    //Procesar instrucciones del archivo de carga
    private boolean processInstruction(String instruction) throws Exception {
        Matcher matcher = INSTRUCTION_PATTERN.matcher(instruction);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Formato de instrucción inválido: " + instruction);
        }
        
        String command = matcher.group(1);
        String parameters = matcher.group(2);
        List<String> params = parseParameters(parameters);
        
        switch (command.toUpperCase()) {
            case "REGISTRO_EVENTO":
                return processRegistroEvento(params);
                
            case "REGISTRO_PARTICIPANTE":
                return processRegistroParticipante(params);
                
            /*case "INSCRIPCION":
                return processInscripcion(params);
                
            case "PAGO":
                return processPago(params);
                
            case "VALIDAR_INSCRIPCION":
                return processValidarInscripcion(params);
                
            case "REGISTRO_ACTIVIDAD":
                return processRegistroActividad(params);
                
            case "ASISTENCIA":
                return processAsistencia(params);
                
            case "CERTIFICADO":
                return processCertificado(params);
                
            case "REPORTE_PARTICIPANTES":
                return processReporteParticipantes(params);
                
            case "REPORTE_ACTIVIDADES":
                return processReporteActividades(params);
                
            case "REPORTE_EVENTOS":
                return processReporteEventos(params);
             */   
            default:
                throw new IllegalArgumentException("Comando desconocido: " + command);
        }
    }
    
    private List<String> parseParameters(String parameters) {
        List<String> params = new ArrayList<>();
        Matcher matcher = PARAMETER_PATTERN.matcher(parameters);
        
        while (matcher.find()) {
            String quoted = matcher.group(1);
            String unquoted = matcher.group(2);
            
            if (quoted != null) {
                params.add(quoted);
            } else if (unquoted != null && !unquoted.trim().isEmpty()) {
                params.add(unquoted.trim());
            }
        }
        
        return params;
    }
    
    // PROCESAMIENTO DE CADA TIPO DE INSTRUCCIÓN
    
    private boolean processRegistroEvento(List<String> params) throws Exception {
        if (params.size() != 7) {
            throw new IllegalArgumentException("REGISTRO_EVENTO requiere 7 parámetros");
        }
        
        String codigo = params.get(0);
        String fecha = params.get(1);
        String tipo = params.get(2);
        String titulo = params.get(3);
        String ubicacion = params.get(4);
        int cupo = Integer.parseInt(params.get(5));
        double costo = Double.parseDouble(params.get(6));
        
        return eventoService.registrarEvento(codigo, fecha, tipo, titulo, ubicacion, cupo, costo);
    }
    
    private boolean processRegistroParticipante(List<String> params) throws Exception {
        if (params.size() != 4) {
            throw new IllegalArgumentException("REGISTRO_PARTICIPANTE requiere 4 parámetros");
        }
        
        String nombre = params.get(0);
        String tipo = params.get(1);
        String institucion = params.get(2);
        String correo = params.get(3);
        
        return participanteService.registrarParticipante(correo, nombre, tipo, institucion);
    }
    
    /*private boolean processInscripcion(List<String> params) throws Exception {
        if (params.size() != 3) {
            throw new IllegalArgumentException("INSCRIPCION requiere 3 parámetros");
        }
        
        String correo = params.get(0);
        String codigoEvento = params.get(1);
        String tipoInscripcion = params.get(2);
        
        return inscripcionService.inscribir(correo, codigoEvento, tipoInscripcion);
    }
    
    private boolean processPago(List<String> params) throws Exception {
        if (params.size() != 4) {
            throw new IllegalArgumentException("PAGO requiere 4 parámetros");
        }
        
        String correo = params.get(0);
        String codigoEvento = params.get(1);
        String metodoPago = params.get(2);
        double monto = Double.parseDouble(params.get(3));
        
        return pagoService.registrarPago(correo, codigoEvento, metodoPago, monto);
    }
    
    private boolean processValidarInscripcion(List<String> params) throws Exception {
        if (params.size() != 2) {
            throw new IllegalArgumentException("VALIDAR_INSCRIPCION requiere 2 parámetros");
        }
        
        String correo = params.get(0);
        String codigoEvento = params.get(1);
        
        return inscripcionService.validarInscripcion(correo, codigoEvento);
    }
    
    private boolean processRegistroActividad(List<String> params) throws Exception {
        if (params.size() != 8) {
            throw new IllegalArgumentException("REGISTRO_ACTIVIDAD requiere 8 parámetros");
        }
        
        String codigoActividad = params.get(0);
        String codigoEvento = params.get(1);
        String tipo = params.get(2);
        String titulo = params.get(3);
        String correoEncargado = params.get(4);
        String horaInicio = params.get(5);
        String horaFin = params.get(6);
        int cupo = Integer.parseInt(params.get(7));
        
        return actividadService.registrarActividad(
            codigoActividad, codigoEvento, tipo, titulo, 
            correoEncargado, horaInicio, horaFin, cupo
        );
    }
    
    private boolean processAsistencia(List<String> params) throws Exception {
        if (params.size() != 2) {
            throw new IllegalArgumentException("ASISTENCIA requiere 2 parámetros");
        }
        
        String correo = params.get(0);
        String codigoActividad = params.get(1);
        
        return asistenciaService.registrarAsistencia(correo, codigoActividad);
    }
    
    private boolean processCertificado(List<String> params) throws Exception {
        if (params.size() != 2) {
            throw new IllegalArgumentException("CERTIFICADO requiere 2 parámetros");
        }
        
        String correo = params.get(0);
        String codigoEvento = params.get(1);
        
        String rutaCertificado = certificadoService.generarCertificado(correo, codigoEvento, outputPath);
        return rutaCertificado != null;
    }
    
    private boolean processReporteParticipantes(List<String> params) throws Exception {
        if (params.size() != 3) {
            throw new IllegalArgumentException("REPORTE_PARTICIPANTES requiere 3 parámetros");
        }
        
        String codigoEvento = params.get(0);
        String tipoParticipante = params.get(1).isEmpty() ? null : params.get(1);
        String institucion = params.get(2).isEmpty() ? null : params.get(2);
        
        String rutaReporte = reporteService.generarReporteParticipantes(
            codigoEvento, tipoParticipante, institucion, outputPath
        );
        return rutaReporte != null;
    }
    
    private boolean processReporteActividades(List<String> params) throws Exception {
        if (params.size() != 3) {
            throw new IllegalArgumentException("REPORTE_ACTIVIDADES requiere 3 parámetros");
        }
        
        String codigoEvento = params.get(0);
        String tipoActividad = params.get(1).isEmpty() ? null : params.get(1);
        String correoEncargado = params.get(2).isEmpty() ? null : params.get(2);
        
        String rutaReporte = reporteService.generarReporteActividades(
            codigoEvento, tipoActividad, correoEncargado, outputPath
        );
        return rutaReporte != null;
    }
    
    private boolean processReporteEventos(List<String> params) throws Exception {
        if (params.size() != 5) {
            throw new IllegalArgumentException("REPORTE_EVENTOS requiere 5 parámetros");
        }
        
        String tipoEvento = params.get(0).isEmpty() ? null : params.get(0);
        String fechaInicio = params.get(1).isEmpty() ? null : params.get(1);
        String fechaFin = params.get(2).isEmpty() ? null : params.get(2);
        Integer cupoMin = params.get(3).isEmpty() ? null : Integer.parseInt(params.get(3));
        Integer cupoMax = params.get(4).isEmpty() ? null : Integer.parseInt(params.get(4));
        
        String rutaReporte = reporteService.generarReporteEventos(
            tipoEvento, fechaInicio, fechaFin, cupoMin, cupoMax, outputPath
        );
        return rutaReporte != null;
    }*/
    
    public void stopProcessing() {
        this.stopProcessing = true;
    }
}

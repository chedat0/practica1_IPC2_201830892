/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.recursos;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
/**
 *
 * @author jeffm
 */
public class Reportes {
    private static final String TEMPLATE_PATH = "/templates/";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    /**
     * Genera un certificado HTML para un participante
     */
    public String generarCertificado(String nombreParticipante, String evento, 
                                    String fecha, String outputPath) {
        try {
            String html = getPlantillaCertificado()
                .replace("${nombreParticipante}", nombreParticipante)
                .replace("${evento}", evento)
                .replace("${fecha}", fecha)
                .replace("${fechaEmision}", LocalDateTime.now().format(dateTimeFormatter));
            
            String fileName = "certificado_" + nombreParticipante.replaceAll(" ", "_") + 
                            "_" + System.currentTimeMillis() + ".html";
            String filePath = outputPath + File.separator + fileName;
            
            Files.write(Paths.get(filePath), html.getBytes());
            Logger.info("Certificado generado: " + filePath);
            
            return filePath;
            
        } catch (IOException e) {
            Logger.error("Error generando certificado", e);
            return null;
        }
    }
    
    /**
     * Genera reporte de participantes
     */
    public String generarReporteParticipantes(String tituloEvento, 
                                             List<Map<String, Object>> participantes,
                                             String outputPath) {
        StringBuilder html = new StringBuilder();
        html.append(getCabeceraHTML("Reporte de Participantes - " + tituloEvento));
        
        html.append("<div class='container'>");
        html.append("<h1>Reporte de Participantes</h1>");
        html.append("<h2>").append(tituloEvento).append("</h2>");
        html.append("<p class='date'>Generado: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("</p>");
        
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Correo Electrónico</th>");
        html.append("<th>Tipo</th>");
        html.append("<th>Nombre Completo</th>");
        html.append("<th>Institución de Procedencia</th>");
        html.append("<th>Validado</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        
        for (Map<String, Object> participante : participantes) {
            html.append("<tr>");
            html.append("<td>").append(participante.get("correo")).append("</td>");
            html.append("<td>").append(participante.get("tipo")).append("</td>");
            html.append("<td>").append(participante.get("nombre")).append("</td>");
            html.append("<td>").append(participante.get("institucion")).append("</td>");
            html.append("<td class='center'>")
                .append((boolean)participante.get("validado") ? "✓" : "✗")
                .append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        html.append("<div class='summary'>");
        html.append("<p>Total de participantes: <strong>").append(participantes.size()).append("</strong></p>");
        html.append("</div>");
        
        html.append("</div>");        
        
        return guardarReporte(html.toString(), "reporte_participantes", outputPath);
    }
    
    /**
     * Genera reporte de actividades
     */
    public String generarReporteActividades(String tituloEvento,
                                           List<Map<String, Object>> actividades,
                                           String outputPath) {
        StringBuilder html = new StringBuilder();
        html.append(getCabeceraHTML("Reporte de Actividades - " + tituloEvento));
        
        html.append("<div class='container'>");
        html.append("<h1>Reporte de Actividades</h1>");
        html.append("<h2>").append(tituloEvento).append("</h2>");
        html.append("<p class='date'>Generado: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("</p>");
        
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>Código Actividad</th>");
        html.append("<th>Código Evento</th>");
        html.append("<th>Título</th>");
        html.append("<th>Encargado</th>");
        html.append("<th>Hora Inicio</th>");
        html.append("<th>Cupo Máximo</th>");
        html.append("<th>Participantes</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");
        
        int totalParticipantes = 0;
        for (Map<String, Object> actividad : actividades) {
            int participantes = (int) actividad.get("participantes");
            totalParticipantes += participantes;
            
            html.append("<tr>");
            html.append("<td>").append(actividad.get("codigo")).append("</td>");
            html.append("<td>").append(actividad.get("codigoEvento")).append("</td>");
            html.append("<td>").append(actividad.get("titulo")).append("</td>");
            html.append("<td>").append(actividad.get("encargado")).append("</td>");
            html.append("<td class='center'>").append(actividad.get("horaInicio")).append("</td>");
            html.append("<td class='center'>").append(actividad.get("cupoMaximo")).append("</td>");
            html.append("<td class='center'>").append(participantes).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody>");
        html.append("</table>");
        
        html.append("<div class='summary'>");
        html.append("<p>Total de actividades: <strong>").append(actividades.size()).append("</strong></p>");
        html.append("<p>Total de participantes: <strong>").append(totalParticipantes).append("</strong></p>");
        html.append("</div>");
        
        html.append("</div>");        
        
        return guardarReporte(html.toString(), "reporte_actividades", outputPath);
    }
    
    /**
     * Genera reporte de eventos
     */
    public String generarReporteEventos(List<Map<String, Object>> eventos,
                                       String outputPath) {
        StringBuilder html = new StringBuilder();
        html.append(getCabeceraHTML("Reporte de Eventos"));
        
        html.append("<div class='container'>");
        html.append("<h1>Reporte de Eventos</h1>");
        html.append("<p class='date'>Generado: ").append(LocalDateTime.now().format(dateTimeFormatter)).append("</p>");
        
        for (Map<String, Object> evento : eventos) {
            html.append("<div class='evento-section'>");
            
            // Información del evento
            html.append("<div class='evento-header'>");
            html.append("<h2>").append(evento.get("titulo")).append("</h2>");
            html.append("<div class='evento-info'>");
            html.append("<p><strong>Código:</strong> ").append(evento.get("codigo")).append("</p>");
            html.append("<p><strong>Fecha:</strong> ").append(evento.get("fecha")).append("</p>");
            html.append("<p><strong>Tipo:</strong> ").append(evento.get("tipo")).append("</p>");
            html.append("<p><strong>Ubicación:</strong> ").append(evento.get("ubicacion")).append("</p>");
            html.append("<p><strong>Cupo Máximo:</strong> ").append(evento.get("cupoMaximo")).append("</p>");
            html.append("</div>");
            html.append("</div>");
            
            // Tabla de participantes
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participantes = (List<Map<String, Object>>) evento.get("participantes");
            
            if (participantes != null && !participantes.isEmpty()) {
                html.append("<h3>Participantes Inscritos</h3>");
                html.append("<table>");
                html.append("<thead>");
                html.append("<tr>");
                html.append("<th>Correo</th>");
                html.append("<th>Nombre</th>");
                html.append("<th>Tipo</th>");
                html.append("<th>Método de Pago</th>");
                html.append("<th>Monto Pagado</th>");
                html.append("</tr>");
                html.append("</thead>");
                html.append("<tbody>");
                
                double montoTotal = 0;
                for (Map<String, Object> participante : participantes) {
                    double monto = (double) participante.get("monto");
                    montoTotal += monto;
                    
                    html.append("<tr>");
                    html.append("<td>").append(participante.get("correo")).append("</td>");
                    html.append("<td>").append(participante.get("nombre")).append("</td>");
                    html.append("<td>").append(participante.get("tipo")).append("</td>");
                    html.append("<td>").append(participante.get("metodoPago")).append("</td>");
                    html.append("<td class='right'>Q ").append(String.format("%.2f", monto)).append("</td>");
                    html.append("</tr>");
                }
                
                html.append("</tbody>");
                html.append("</table>");
                
                // Resumen del evento
                html.append("<div class='evento-summary'>");
                html.append("<p><strong>Monto Total:</strong> Q ").append(String.format("%.2f", montoTotal)).append("</p>");
                html.append("<p><strong>Participantes Validados:</strong> ").append(evento.get("validados")).append("</p>");
                html.append("<p><strong>Participantes No Validados:</strong> ").append(evento.get("noValidados")).append("</p>");
                html.append("</div>");
            } else {
                html.append("<p class='no-data'>No hay participantes inscritos en este evento.</p>");
            }
            
            html.append("</div>");
        }
        
        html.append("</div>");       
        
        return guardarReporte(html.toString(), "reporte_eventos", outputPath);
    }
    
    /**
     * Obtiene la cabecera HTML con estilos
     */
    private String getCabeceraHTML(String titulo) {
        return "<!DOCTYPE html>\n" +
               "<html lang='es'>\n" +
               "<head>\n" +
               "    <meta charset='UTF-8'>\n" +
               "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
               "    <title>" + titulo + "</title>\n" +
               "    <style>\n" +
               getEstilosCSS() +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n";
    }
    
    /**
     * Obtiene los estilos CSS para los reportes
     */
    private String getEstilosCSS() {
        return "* { margin: 0; padding: 0; box-sizing: border-box; }\n" +
               "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; " +
               "       background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%); " +
               "       min-height: 100vh; padding: 20px; }\n" +
               ".container { max-width: 1200px; margin: 0 auto; " +
               "            background: white; padding: 30px; " +
               "            border-radius: 10px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); }\n" +
               "h1 { color: #2c3e50; margin-bottom: 10px; font-size: 32px; " +
               "     border-bottom: 3px solid #3498db; padding-bottom: 10px; }\n" +
               "h2 { color: #34495e; margin-bottom: 20px; font-size: 24px; }\n" +
               "h3 { color: #34495e; margin: 20px 0 10px 0; font-size: 18px; }\n" +
               ".date { color: #7f8c8d; font-style: italic; margin-bottom: 30px; }\n" +
               "table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n" +
               "thead { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; }\n" +
               "th, td { padding: 12px; text-align: left; border: 1px solid #ddd; }\n" +
               "th { font-weight: 600; text-transform: uppercase; font-size: 12px; letter-spacing: 0.5px; }\n" +
               "tbody tr:nth-child(even) { background: #f8f9fa; }\n" +
               "tbody tr:hover { background: #e8f4f8; transition: background 0.3s; }\n" +
               ".center { text-align: center; }\n" +
               ".right { text-align: right; }\n" +
               ".summary { margin-top: 30px; padding: 20px; background: #ecf0f1; " +
               "          border-radius: 5px; border-left: 4px solid #3498db; }\n" +
               ".summary p { margin: 5px 0; font-size: 16px; }\n" +
               ".summary strong { color: #2c3e50; }\n" +
               ".evento-section { margin-bottom: 40px; padding: 20px; " +
               "                 border: 1px solid #ddd; border-radius: 8px; }\n" +
               ".evento-header { background: #f8f9fa; padding: 15px; " +
               "                margin-bottom: 20px; border-radius: 5px; }\n" +
               ".evento-info p { margin: 5px 0; }\n" +
               ".evento-summary { margin-top: 20px; padding: 15px; " +
               "                 background: #e8f4f8; border-radius: 5px; }\n" +
               ".no-data { text-align: center; color: #7f8c8d; " +
               "          font-style: italic; padding: 20px; }\n" +
               "@media print { body { background: white; } " +
               "              .container { box-shadow: none; } }\n";
    }
    
    /**
     * Obtiene la plantilla del certificado
     */
    private String getPlantillaCertificado() {
        return "<!DOCTYPE html>\n" +
               "<html lang='es'>\n" +
               "<head>\n" +
               "    <meta charset='UTF-8'>\n" +
               "    <title>Certificado de Participación</title>\n" +
               "    <style>\n" +
               "        @page { size: landscape; margin: 0; }\n" +
               "        body { \n" +
               "            margin: 0; padding: 0;\n" +
               "            width: 297mm; height: 210mm;\n" +
               "            font-family: 'Georgia', serif;\n" +
               "            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n" +
               "            display: flex;\n" +
               "            align-items: center;\n" +
               "            justify-content: center;\n" +
               "        }\n" +
               "        .certificate {\n" +
               "            width: 90%; height: 90%;\n" +
               "            background: white;\n" +
               "            border: 15px solid gold;\n" +
               "            border-radius: 20px;\n" +
               "            box-shadow: 0 0 50px rgba(0,0,0,0.3);\n" +
               "            padding: 60px;\n" +
               "            text-align: center;\n" +
               "            position: relative;\n" +
               "        }\n" +
               "        .certificate::before {\n" +
               "            content: '';\n" +
               "            position: absolute;\n" +
               "            top: 10px; left: 10px; right: 10px; bottom: 10px;\n" +
               "            border: 2px solid gold;\n" +
               "            border-radius: 15px;\n" +
               "        }\n" +
               "        .header {\n" +
               "            font-size: 48px;\n" +
               "            color: #2c3e50;\n" +
               "            margin-bottom: 30px;\n" +
               "            text-transform: uppercase;\n" +
               "            letter-spacing: 5px;\n" +
               "        }\n" +
               "        .subtitle {\n" +
               "            font-size: 24px;\n" +
               "            color: #7f8c8d;\n" +
               "            margin-bottom: 40px;\n" +
               "        }\n" +
               "        .content {\n" +
               "            font-size: 20px;\n" +
               "            line-height: 1.8;\n" +
               "            color: #34495e;\n" +
               "            margin: 40px 0;\n" +
               "        }\n" +
               "        .participant {\n" +
               "            font-size: 36px;\n" +
               "            font-weight: bold;\n" +
               "            color: #2c3e50;\n" +
               "            margin: 30px 0;\n" +
               "            text-decoration: underline;\n" +
               "            text-decoration-color: gold;\n" +
               "        }\n" +
               "        .event {\n" +
               "            font-size: 28px;\n" +
               "            color: #34495e;\n" +
               "            margin: 20px 0;\n" +
               "            font-style: italic;\n" +
               "        }\n" +
               "        .date {\n" +
               "            font-size: 18px;\n" +
               "            color: #7f8c8d;\n" +
               "            margin-top: 40px;\n" +
               "        }\n" +
               "        .signatures {\n" +
               "            display: flex;\n" +
               "            justify-content: space-around;\n" +
               "            margin-top: 60px;\n" +
               "        }\n" +
               "        .signature {\n" +
               "            text-align: center;\n" +
               "        }\n" +
               "        .signature-line {\n" +
               "            width: 200px;\n" +
               "            border-bottom: 2px solid #34495e;\n" +
               "            margin-bottom: 10px;\n" +
               "        }\n" +
               "        .signature-name {\n" +
               "            font-size: 14px;\n" +
               "            color: #7f8c8d;\n" +
               "        }\n" +
               "        .logo {\n" +
               "            position: absolute;\n" +
               "            top: 30px;\n" +
               "            right: 30px;\n" +
               "            font-size: 24px;\n" +
               "            color: gold;\n" +
               "            font-weight: bold;\n" +
               "        }\n" +
               "    </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div class='certificate'>\n" +
               "        <div class='logo'>⧫⧫⧫</div>\n" +
               "        <div class='header'>Certificado de Participación</div>\n" +
               "        <div class='subtitle'>Reino de Hyrule</div>\n" +
               "        <div class='content'>\n" +
               "            Por medio del presente se certifica que\n" +
               "        </div>\n" +
               "        <div class='participant'>${nombreParticipante}</div>\n" +
               "        <div class='content'>\n" +
               "            ha participado satisfactoriamente en el evento\n" +
               "        </div>\n" +
               "        <div class='event'>${evento}</div>\n" +
               "        <div class='content'>\n" +
               "            celebrado el día ${fecha}\n" +
               "        </div>\n" +
               "        <div class='date'>Emitido el: ${fechaEmision}</div>\n" +
               "        <div class='signatures'>\n" +
               "            <div class='signature'>\n" +
               "                <div class='signature-line'></div>\n" +
               "                <div class='signature-name'>Director de Eventos</div>\n" +
               "            </div>\n" +
               "            <div class='signature'>\n" +
               "                <div class='signature-line'></div>\n" +
               "                <div class='signature-name'>Coordinador General</div>\n" +
               "            </div>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "</body>\n" +
               "</html>";
    }
    
    /**
     * Guarda el reporte en un archivo
     */
    private String guardarReporte(String contenido, String prefijo, String outputPath) {
        try {
            String fileName = prefijo + "_" + System.currentTimeMillis() + ".html";
            String filePath = outputPath + File.separator + fileName;
            
            Files.write(Paths.get(filePath), contenido.getBytes());
            Logger.info("Reporte generado: " + filePath);
            
            return filePath;
            
        } catch (IOException e) {
            Logger.error("Error guardando reporte", e);
            return null;
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.recursos;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author jeffm
 */
public class Logger {
    private static final String LOG_FILE = "eventos_hyrule.log";
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public enum Level {
        INFO, WARNING, ERROR, DEBUG
    }
    
    public static void log(Level level, String message, Exception e) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Imprimir en consola
        if (level == Level.ERROR) {
            System.err.println(logMessage);
            if (e != null) {
                e.printStackTrace(System.err);
            }
        } else {
            System.out.println(logMessage);
        }
        
        // Escribir en archivo
        writeToFile(logMessage, e);
    }
    
    private static void writeToFile(String message, Exception e) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(message);
            if (e != null) {
                e.printStackTrace(writer);
            }
        } catch (IOException ioException) {
            System.err.println("Error escribiendo en log: " + ioException.getMessage());
        }
    }
    
    public static void info(String message) {
        log(Level.INFO, message, null);
    }
    
    public static void warning(String message) {
        log(Level.WARNING, message, null);
    }
    
    public static void error(String message, Exception e) {
        log(Level.ERROR, message, e);
    }
    
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }
}

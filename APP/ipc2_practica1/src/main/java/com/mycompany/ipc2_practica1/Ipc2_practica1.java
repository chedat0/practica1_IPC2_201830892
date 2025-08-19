/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ipc2_practica1;

import com.mycompany.ipc2_practica1.dbconnection.DBConnection;
import com.mycompany.ipc2_practica1.recursos.Logger;
import com.mycompany.ipc2_practica1.vista.MainFrame;
import javax.swing.*;
import java.awt.*;





/**
 *
 * @author jeffm
 */
public class Ipc2_practica1 {

    public static void main(String[] args) {
        // Configurar el Look and Feel antes de crear cualquier componente
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel del sistema");
        }
        
        // Aplicar tema monocromático
        aplicarTema();
        
        // Verificar conexión a base de datos ANTES de iniciar la GUI
        if (!verificarConexionBD()) {
            // Si no hay conexión, mostrar error y salir
            JOptionPane.showMessageDialog(null, 
                "No se pudo conectar a la base de datos.\n" +
                "Verifique que MySQL esté ejecutándose y que los datos de conexión sean correctos.\n\n" +
                "Configuración actual:\n" +
                "- Servidor: localhost:3306\n" +
                "- Base de datos: eventosdb\n" +
                "- Usuario: admindba",
                "Error de Conexión", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Si la conexión es exitosa, iniciar la aplicación
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                
                // Mostrar mensaje de bienvenida
                JOptionPane.showMessageDialog(frame,
                    "Conexión a base de datos establecida correctamente.\n" +
                    "¡Bienvenido al Sistema de Eventos!",
                    "Sistema de Eventos - Reino de Hyrule",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                Logger.error("Error al iniciar interfaz", e);
                JOptionPane.showMessageDialog(null, 
                    "Error al iniciar la aplicación: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });     
    }
    /**
     * Verifica la conexión a la base de datos
     * @return true si la conexión es exitosa, false en caso contrario
     */
    private static boolean verificarConexionBD() {
        try {
            System.out.println("========================================");
            System.out.println("    VERIFICANDO CONEXIÓN A BASE DE DATOS");
            System.out.println("========================================");
            
            // Obtener instancia de la conexión
            DBConnection dbConnection = DBConnection.getInstance();
            
            // Probar la conexión
            boolean conectado = dbConnection.testConnection();
            
            if (conectado) {
                System.out.println("✅ Conexión exitosa a la base de datos");
                
                
                return true;
            } else {
                System.err.println("❌ No se pudo establecer conexión con la base de datos");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private static void aplicarTema (){
        Color PRIMARY_DARK = new Color(33, 37, 41);      // #212529
        Color PRIMARY = new Color(52, 58, 64);           // #343A40
        Color SECONDARY = new Color(73, 80, 87);         // #495057
        Color LIGHT = new Color(108, 117, 125);          // #6C757D
        Color LIGHTER = new Color(173, 181, 189);        // #ADB5BD
        Color BACKGROUND = new Color(248, 249, 250);     // #F8F9FA
        Color WHITE = Color.WHITE;
        
        // Configuración general
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", PRIMARY_DARK);
        
        // Botones
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", WHITE);
        UIManager.put("Button.focus", SECONDARY);
        UIManager.put("Button.select", SECONDARY);
        UIManager.put("Button.darkShadow", PRIMARY_DARK);
        
        // Tablas
        UIManager.put("Table.background", WHITE);
        UIManager.put("Table.alternateRowColor", new Color(233, 236, 239));
        UIManager.put("Table.gridColor", LIGHTER);
        UIManager.put("TableHeader.background", PRIMARY);
        UIManager.put("TableHeader.foreground", WHITE);
        
        // Menús
        UIManager.put("MenuBar.background", PRIMARY_DARK);
        UIManager.put("Menu.background", PRIMARY_DARK);
        UIManager.put("Menu.foreground", WHITE);
        UIManager.put("MenuItem.background", PRIMARY_DARK);
        UIManager.put("MenuItem.foreground", WHITE);
        UIManager.put("MenuItem.selectionBackground", SECONDARY);
        
        // Campos de texto
        UIManager.put("TextField.background", WHITE);
        UIManager.put("TextField.foreground", PRIMARY_DARK);
        UIManager.put("TextField.caretForeground", PRIMARY);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(LIGHTER));
        
        // ComboBox
        UIManager.put("ComboBox.background", WHITE);
        UIManager.put("ComboBox.foreground", PRIMARY_DARK);
        UIManager.put("ComboBox.selectionBackground", LIGHT);
        UIManager.put("ComboBox.selectionForeground", WHITE);
        
        // ScrollPane
        UIManager.put("ScrollPane.background", BACKGROUND);
        UIManager.put("ScrollBar.background", BACKGROUND);
        UIManager.put("ScrollBar.thumb", LIGHT);
        
        // InternalFrame
        UIManager.put("InternalFrame.activeTitleBackground", PRIMARY);
        UIManager.put("InternalFrame.activeTitleForeground", WHITE);
        UIManager.put("InternalFrame.inactiveTitleBackground", SECONDARY);
        UIManager.put("InternalFrame.inactiveTitleForeground", WHITE);
        
        // Desktop
        UIManager.put("Desktop.background", new Color(233, 236, 239));
    }
}

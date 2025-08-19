/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.vista.paneles;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import com.mycompany.ipc2_practica1.recursos.Parser;
import com.mycompany.ipc2_practica1.recursos.Logger;
/**
 *
 * @author jeffm
 */
public class PanelArchivo extends JInternalFrame{
    private JTextField txtFilePath;
    private JTextField txtOutputPath;
    private JSpinner spnDelay;
    private JTextArea txtLog;
    private JProgressBar progressBar;
    private JButton btnSelectFile;
    private JButton btnSelectOutput;
    private JButton btnProcess;
    private JButton btnStop;
    
    private Parser fileParser;
    private boolean isProcessing = false;
    
    public PanelArchivo() {
        super("Procesador de Archivos", true, true, true, true);
        fileParser = new Parser();
        initComponents();
        setSize(800, 600);
        setLocation(50, 50);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior con controles
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Archivo de entrada
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Archivo de entrada:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFilePath = new JTextField();
        txtFilePath.setEditable(false);
        topPanel.add(txtFilePath, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        btnSelectFile = new JButton("Examinar...");
        btnSelectFile.addActionListener(e -> selectFile());
        topPanel.add(btnSelectFile, gbc);
        
        // Directorio de salida
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Directorio de salida:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtOutputPath = new JTextField();
        txtOutputPath.setEditable(false);
        topPanel.add(txtOutputPath, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        btnSelectOutput = new JButton("Examinar...");
        btnSelectOutput.addActionListener(e -> selectOutputDirectory());
        topPanel.add(btnSelectOutput, gbc);
        
        // Velocidad de procesamiento
        gbc.gridx = 0; gbc.gridy = 2;
        topPanel.add(new JLabel("Velocidad (ms):"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        SpinnerNumberModel model = new SpinnerNumberModel(500, 0, 5000, 100);
        spnDelay = new JSpinner(model);
        topPanel.add(spnDelay, gbc);
        
        // Botones de control
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnProcess = new JButton("Procesar");
        btnProcess.setBackground(new Color(40, 167, 69));
        btnProcess.setForeground(Color.WHITE);
        btnProcess.setFont(new Font("Arial", Font.BOLD, 12));
        btnProcess.addActionListener(e -> startProcessing());
        
        btnStop = new JButton("Detener");
        btnStop.setBackground(new Color(220, 53, 69));
        btnStop.setForeground(Color.WHITE);
        btnStop.setFont(new Font("Arial", Font.BOLD, 12));
        btnStop.setEnabled(false);
        btnStop.addActionListener(e -> stopProcessing());
        
        buttonPanel.add(btnProcess);
        buttonPanel.add(btnStop);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 3;
        topPanel.add(buttonPanel, gbc);
        
        // Barra de progreso
        gbc.gridy = 4;
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        topPanel.add(progressBar, gbc);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central con log
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(173, 181, 189)),
            "Log de Procesamiento",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12)
        ));
        
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtLog.setBackground(new Color(33, 37, 41));
        txtLog.setForeground(new Color(248, 249, 250));
        
        JScrollPane scrollPane = new JScrollPane(txtLog);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo de instrucciones");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            
            @Override
            public String getDescription() {
                return "Archivos de texto (*.txt)";
            }
        });
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtFilePath.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void selectOutputDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar directorio de salida");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            txtOutputPath.setText(selectedDir.getAbsolutePath());
        }
    }
    
    private void startProcessing() {
        if (txtFilePath.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un archivo de entrada",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (txtOutputPath.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un directorio de salida",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        isProcessing = true;
        btnProcess.setEnabled(false);
        btnStop.setEnabled(true);
        btnSelectFile.setEnabled(false);
        btnSelectOutput.setEnabled(false);
        spnDelay.setEnabled(false);
        progressBar.setValue(0);
        txtLog.setText("Iniciando procesamiento...\n");
        
        String filePath = txtFilePath.getText();
        String outputPath = txtOutputPath.getText();
        int delay = (int) spnDelay.getValue();
        
        fileParser.processFile(filePath, delay, outputPath, new Parser.ProcessingCallback() {
            @Override
            public void onInstructionProcessed(String instruction, boolean success, String message) {
                SwingUtilities.invokeLater(() -> {
                    String status = success ? "✓" : "✗";
                    String color = success ? "[OK]" : "[ERROR]";
                    txtLog.append(String.format("%s %s %s - %s\n", 
                        status, color, instruction, message));
                    txtLog.setCaretPosition(txtLog.getDocument().getLength());
                });
            }
            
            @Override
            public void onProgressUpdate(int current, int total) {
                SwingUtilities.invokeLater(() -> {
                    int progress = (int) ((current * 100.0) / total);
                    progressBar.setValue(progress);
                    progressBar.setString(String.format("%d de %d instrucciones", current, total));
                });
            }
            
            @Override
            public void onProcessingComplete(int successful, int failed) {
                SwingUtilities.invokeLater(() -> {
                    txtLog.append("\n" + "=".repeat(50) + "\n");
                    txtLog.append(String.format("Procesamiento completado:\n"));
                    txtLog.append(String.format("  Exitosas: %d\n", successful));
                    txtLog.append(String.format("  Fallidas: %d\n", failed));
                    txtLog.append(String.format("  Total: %d\n", successful + failed));
                    
                    isProcessing = false;
                    btnProcess.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnSelectFile.setEnabled(true);
                    btnSelectOutput.setEnabled(true);
                    spnDelay.setEnabled(true);
                    
                    JOptionPane.showMessageDialog(PanelArchivo.this,
                        String.format("Procesamiento completado\nExitosas: %d\nFallidas: %d",
                            successful, failed),
                        "Proceso Finalizado",
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }
        });
    }
    
    private void stopProcessing() {
        if (isProcessing) {
            fileParser.stopProcessing();
            txtLog.append("\n*** Procesamiento detenido por el usuario ***\n");
            
            isProcessing = false;
            btnProcess.setEnabled(true);
            btnStop.setEnabled(false);
            btnSelectFile.setEnabled(true);
            btnSelectOutput.setEnabled(true);
            spnDelay.setEnabled(true);
        }
    }
}

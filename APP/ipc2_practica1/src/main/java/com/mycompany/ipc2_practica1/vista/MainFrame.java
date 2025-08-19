/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.mycompany.ipc2_practica1.vista.paneles.*;
import com.mycompany.ipc2_practica1.vista.paneles.PanelArchivo;
import com.mycompany.ipc2_practica1.recursos.Logger;
/**
 *
 * @author jeffm
 */
public class MainFrame extends JFrame {
    private JDesktopPane desktopPane;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JLabel statusLabel;
    
    // Paneles internos
    private EventoPanel eventoPanel;
    private ParticipantePanel participantePanel;
    //private InscripcionPanel inscripcionPanel;
    //private ActividadPanel actividadPanel;
    private PanelArchivo panelArchivo;
    //private ReportePanel reportePanel;
    
    public MainFrame() {
        initComponents();
        setupMenuBar();
        setupToolBar();
        setupStatusBar();
        configureWindow();
    }
    
    private void initComponents() {
        setTitle("Sistema de Eventos - Reino de Hyrule");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Crear desktop pane con fondo personalizado
        desktopPane = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Gradiente monocromático de fondo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(248, 249, 250),
                    getWidth(), getHeight(), new Color(233, 236, 239)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Logo o texto de agua
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setColor(new Color(206, 212, 218, 100));
                g2d.setFont(new Font("Arial", Font.BOLD, 48));
                
                String watermark = "IPC2";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(watermark)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(watermark, x, y);
            }
        };
        
        add(desktopPane, BorderLayout.CENTER);
    }
    
    private void setupMenuBar() {
        menuBar = new JMenuBar();
        
        // Menú Archivo
        JMenu fileMenu = new JMenu("Archivo");
        fileMenu.setMnemonic(KeyEvent.VK_A);
        
        JMenuItem loadFileItem = new JMenuItem("Cargar Archivo", 
            UIManager.getIcon("FileView.fileIcon"));
        loadFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 
            InputEvent.CTRL_DOWN_MASK));
        loadFileItem.addActionListener(e -> showPanelArchivo());
        
        JMenuItem exitItem = new JMenuItem("Salir", 
            UIManager.getIcon("FileView.hardDriveIcon"));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 
            InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> exitApplication());
        
        fileMenu.add(loadFileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Menú Registros
        JMenu registrosMenu = new JMenu("Registros");
        registrosMenu.setMnemonic(KeyEvent.VK_R);
        
        JMenuItem eventosItem = new JMenuItem("Eventos");
        eventosItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, 
            InputEvent.CTRL_DOWN_MASK));
        eventosItem.addActionListener(e -> showEventos());
        
        JMenuItem participantesItem = new JMenuItem("Participantes");
        participantesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 
            InputEvent.CTRL_DOWN_MASK));
        participantesItem.addActionListener(e -> showParticipantes());
        
        /*JMenuItem inscripcionesItem = new JMenuItem("Inscripciones");
        inscripcionesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, 
            InputEvent.CTRL_DOWN_MASK));
        inscripcionesItem.addActionListener(e -> showInscripciones());
        
        JMenuItem actividadesItem = new JMenuItem("Actividades");
        actividadesItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, 
            InputEvent.CTRL_DOWN_MASK));
        actividadesItem.addActionListener(e -> showActividades());*/
        
        registrosMenu.add(eventosItem);
        registrosMenu.add(participantesItem);
        //registrosMenu.add(inscripcionesItem);
        //registrosMenu.add(actividadesItem);
        
        /*// Menú Reportes
        JMenu reportesMenu = new JMenu("Reportes");
        reportesMenu.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem generarReporteItem = new JMenuItem("Generar Reportes");
        generarReporteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, 
            InputEvent.CTRL_DOWN_MASK));
        generarReporteItem.addActionListener(e -> showReportes());
        
        reportesMenu.add(generarReporteItem);*/
        
        // Menú Ventana
        JMenu windowMenu = new JMenu("Ventana");
        windowMenu.setMnemonic(KeyEvent.VK_V);
        
        JMenuItem cascadeItem = new JMenuItem("Cascada");
        cascadeItem.addActionListener(e -> cascadeWindows());
        
        JMenuItem tileItem = new JMenuItem("Mosaico");
        tileItem.addActionListener(e -> tileWindows());
        
        JMenuItem closeAllItem = new JMenuItem("Cerrar Todas");
        closeAllItem.addActionListener(e -> closeAllWindows());
        
        windowMenu.add(cascadeItem);
        windowMenu.add(tileItem);
        windowMenu.addSeparator();
        windowMenu.add(closeAllItem);
        
        // Menú Ayuda
        JMenu helpMenu = new JMenu("Ayuda");
        helpMenu.setMnemonic(KeyEvent.VK_Y);
        
        JMenuItem aboutItem = new JMenuItem("Acerca de");
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        
        // Agregar menús a la barra
        menuBar.add(fileMenu);
        menuBar.add(registrosMenu);
       // menuBar.add(reportesMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void setupToolBar() {
        toolBar = new JToolBar("Herramientas");
        toolBar.setFloatable(false);
        
        // Botones de acceso rápido con iconos
        JButton btnCargarArchivo = createToolButton("📁", "Cargar Archivo");
        btnCargarArchivo.addActionListener(e -> showPanelArchivo());
        
        JButton btnEventos = createToolButton("📅", "Eventos");
        btnEventos.addActionListener(e -> showEventos());
        
        JButton btnParticipantes = createToolButton("👥", "Participantes");
        btnParticipantes.addActionListener(e -> showParticipantes());
        
        /*JButton btnInscripciones = createToolButton("📝", "Inscripciones");
        btnInscripciones.addActionListener(e -> showInscripciones());
        
        JButton btnActividades = createToolButton("🎯", "Actividades");
        btnActividades.addActionListener(e -> showActividades());
        
        JButton btnReportes = createToolButton("📊", "Reportes");
        btnReportes.addActionListener(e -> showReportes());*/
        
        toolBar.add(btnCargarArchivo);
        toolBar.addSeparator();
        toolBar.add(btnEventos);
        toolBar.add(btnParticipantes);
        //toolBar.add(btnInscripciones);
        //toolBar.add(btnActividades);
        //toolBar.addSeparator();
        //toolBar.add(btnReportes);
        
        add(toolBar, BorderLayout.NORTH);
    }
    
    private JButton createToolButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        button.setFocusable(false);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        button.setPreferredSize(new Dimension(45, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(108, 117, 125));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });
        
        return button;
    }
    
    private void setupStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 5, 2, 5),
            BorderFactory.createLineBorder(new Color(173, 181, 189))
        ));
        
        statusLabel = new JLabel("Sistema listo");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel dateLabel = new JLabel(new java.util.Date().toString());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(dateLabel, BorderLayout.EAST);
        
        add(statusPanel, BorderLayout.SOUTH);
        
        // Actualizar fecha cada segundo
        Timer timer = new Timer(1000, e -> {
            dateLabel.setText(new java.util.Date().toString());
        });
        timer.start();
    }
    
    private void configureWindow() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        
        // Icono de la aplicación
        try {
            Image icon = new ImageIcon(getClass().getResource("/icon.png")).getImage();
            setIconImage(icon);
        } catch (Exception e) {
            // Si no hay icono, continuar sin él
        }
    }
    
      // MÉTODOS PARA MOSTRAR PANELES
    
    private void showPanelArchivo() {
        if (panelArchivo == null || panelArchivo.isClosed()) {
            panelArchivo = new PanelArchivo();
            desktopPane.add(panelArchivo);
        }
        panelArchivo.setVisible(true);
        try {
            panelArchivo.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de archivo", e);
        }
    }
    
    private void showEventos() {
        if (eventoPanel == null || eventoPanel.isClosed()) {
            eventoPanel = new EventoPanel();
            desktopPane.add(eventoPanel);
        }
        eventoPanel.setVisible(true);
        try {
            eventoPanel.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de eventos", e);
        }
    }
    
    private void showParticipantes() {
        if (participantePanel == null || participantePanel.isClosed()) {
            participantePanel = new ParticipantePanel();
            desktopPane.add(participantePanel);
        }
        participantePanel.setVisible(true);
        try {
            participantePanel.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de participantes", e);
        }
    }
    
    /*private void showInscripciones() {
        if (inscripcionPanel == null || inscripcionPanel.isClosed()) {
            inscripcionPanel = new InscripcionPanel();
            desktopPane.add(inscripcionPanel);
        }
        inscripcionPanel.setVisible(true);
        try {
            inscripcionPanel.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de inscripciones", e);
        }
    }
    
    private void showActividades() {
        if (actividadPanel == null || actividadPanel.isClosed()) {
            actividadPanel = new ActividadPanel();
            desktopPane.add(actividadPanel);
        }
        actividadPanel.setVisible(true);
        try {
            actividadPanel.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de actividades", e);
        }
    }
    
    private void showReportes() {
        if (reportePanel == null || reportePanel.isClosed()) {
            reportePanel = new ReportePanel();
            desktopPane.add(reportePanel);
        }
        reportePanel.setVisible(true);
        try {
            reportePanel.setSelected(true);
        } catch (Exception e) {
            Logger.error("Error mostrando panel de reportes", e);
        }
    }*/
    
    // MÉTODOS PARA MANEJO DE VENTANAS
    
    private void cascadeWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        int x = 0, y = 0;
        
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                frame.setLocation(x, y);
                x += 30;
                y += 30;
                
                if (x + frame.getWidth() > desktopPane.getWidth()) {
                    x = 0;
                }
                if (y + frame.getHeight() > desktopPane.getHeight()) {
                    y = 0;
                }
            }
        }
    }
    
    private void tileWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        int count = 0;
        
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                count++;
            }
        }
        
        if (count == 0) return;
        
        int cols = (int) Math.ceil(Math.sqrt(count));
        int rows = (int) Math.ceil((double) count / cols);
        int width = desktopPane.getWidth() / cols;
        int height = desktopPane.getHeight() / rows;
        
        int index = 0;
        for (JInternalFrame frame : frames) {
            if (!frame.isIcon()) {
                int row = index / cols;
                int col = index % cols;
                frame.setBounds(col * width, row * height, width, height);
                index++;
            }
        }
    }
    
    private void closeAllWindows() {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        for (JInternalFrame frame : frames) {
            frame.dispose();
        }
    }
    
    private void showAbout() {
        String message = "<html><center>" +
            "<h2>Sistema de Eventos - Reino de Hyrule</h2>" +            
            "<p>Desarrollado por Jefferson Gómez</p>" +
            "<p>201830892</p>" +
            "<p>© 2025 - IPC 2</p>" +
            "<br>" +            
            "</center></html>";
        
        JOptionPane.showMessageDialog(this, message, "Acerca de", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exitApplication() {
        int option = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro que desea salir del sistema?",
            "Confirmar Salida", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (option == JOptionPane.YES_OPTION) {
            Logger.info("Aplicación cerrada por el usuario");
            System.exit(0);
        }
    }
    
    public void updateStatus(String message) {
        statusLabel.setText(message);
    }
    
    
}

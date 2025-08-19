/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ipc2_practica1.vista.paneles;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.mycompany.ipc2_practica1.modelo.entidades.Evento;
import com.mycompany.ipc2_practica1.modelo.enums.TipoEvento;
import com.mycompany.ipc2_practica1.modelo.servicios.EventoServicio;
import com.mycompany.ipc2_practica1.recursos.Logger;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
/**
 *
 * @author jeffm
 */
public class EventoPanel extends JInternalFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCodigo, txtTitulo, txtUbicacion, txtFecha;
    private JComboBox<TipoEvento> cboTipoEvento;
    private JSpinner spnCupo;
    private JFormattedTextField txtCosto;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnBuscar, btnRefrescar;
    private JTextField txtBusqueda;
    
    private EventoServicio eventoService;
    private Evento eventoSeleccionado;
    
    public EventoPanel() {
        super("Gestión de Eventos", true, true, true, true);
        eventoService = new EventoServicio();
        initComponents();
        loadData();
        setSize(900, 650);
        setLocation(100, 50);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel superior - Formulario
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);
        
        // Panel central - Tabla
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Panel inferior - Búsqueda
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(173, 181, 189)),
                "Datos del Evento",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Primera fila
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Código:"), gbc);
        
        gbc.gridx = 1;
        txtCodigo = new JTextField(15);
        txtCodigo.setToolTipText("Formato: EVT-XXXXXXXX");
        panel.add(txtCodigo, gbc);
        
        gbc.gridx = 2;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 3;
        cboTipoEvento = new JComboBox<>(TipoEvento.values());
        panel.add(cboTipoEvento, gbc);
        
        gbc.gridx = 4;
        panel.add(new JLabel("Fecha:"), gbc);
        
        gbc.gridx = 5;
        txtFecha = new JTextField(10);
        txtFecha.setToolTipText("Formato: dd/mm/aaaa");
        panel.add(txtFecha, gbc);
        
        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Título:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtTitulo = new JTextField();
        panel.add(txtTitulo, gbc);
        
        gbc.gridx = 4; gbc.gridwidth = 1;
        panel.add(new JLabel("Cupo:"), gbc);
        
        gbc.gridx = 5;
        SpinnerNumberModel cupoModel = new SpinnerNumberModel(50, 1, 9999, 10);
        spnCupo = new JSpinner(cupoModel);
        panel.add(spnCupo, gbc);
        
        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Ubicación:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3;
        txtUbicacion = new JTextField();
        panel.add(txtUbicacion, gbc);
        
        gbc.gridx = 4; gbc.gridwidth = 1;
        panel.add(new JLabel("Costo Q:"), gbc);
        
        gbc.gridx = 5;
        txtCosto = new JFormattedTextField();
        txtCosto.setValue(0.00);
        txtCosto.setFormatterFactory(new DefaultFormatterFactory(
            new NumberFormatter(new java.text.DecimalFormat("#,##0.00"))
        ));
        panel.add(txtCosto, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnNuevo = createStyledButton("Nuevo", new Color(23, 162, 184));
        btnNuevo.addActionListener(e -> limpiarFormulario());
        
        btnGuardar = createStyledButton("Guardar", new Color(40, 167, 69));
        btnGuardar.addActionListener(e -> guardarEvento());
        
        btnEliminar = createStyledButton("Eliminar", new Color(220, 53, 69));
        btnEliminar.addActionListener(e -> eliminarEvento());
        
        btnRefrescar = createStyledButton("Refrescar", new Color(108, 117, 125));
        btnRefrescar.addActionListener(e -> loadData());
        
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 6;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Configurar modelo de tabla
        String[] columnNames = {"Código", "Fecha", "Tipo", "Título", "Ubicación", "Cupo", "Costo", "Inscritos"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Personalizar header
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(52, 58, 64));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Listener para selección
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    cargarEventoSeleccionado(selectedRow);
                }
            }
        });
        
        // Personalizar renderer para filas alternas
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(173, 181, 189)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        panel.add(new JLabel("Buscar:"));
        
        txtBusqueda = new JTextField(30);
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarEventos();
                }
            }
        });
        panel.add(txtBusqueda);
        
        btnBuscar = createStyledButton("Buscar", new Color(0, 123, 255));
        btnBuscar.addActionListener(e -> buscarEventos());
        panel.add(btnBuscar);
        
        // Label de información
        JLabel lblInfo = new JLabel("Total de eventos: 0");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        panel.add(Box.createHorizontalStrut(50));
        panel.add(lblInfo);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Evento> eventos = eventoService.obtenerTodos();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Evento evento : eventos) {
                Object[] row = {
                    evento.getCodigoEvento(),
                    evento.getFecha().format(formatter),
                    evento.getTipoEvento().getDescripcion(),
                    evento.getTitulo(),
                    evento.getUbicacion(),
                    evento.getCupoMaximo(),
                    String.format("Q %.2f", evento.getCostoInscripcion()),
                    eventoService.obtenerTotalInscritos(evento.getCodigoEvento())
                };
                tableModel.addRow(row);
            }
            
            actualizarContador();
            
        } catch (Exception e) {
            Logger.error("Error cargando eventos", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los eventos: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarEventoSeleccionado(int row) {
        try {
            String codigo = (String) tableModel.getValueAt(row, 0);
            eventoSeleccionado = eventoService.buscarPorCodigo(codigo);
            
            if (eventoSeleccionado != null) {
                txtCodigo.setText(eventoSeleccionado.getCodigoEvento());
                txtTitulo.setText(eventoSeleccionado.getTitulo());
                txtUbicacion.setText(eventoSeleccionado.getUbicacion());
                txtFecha.setText(eventoSeleccionado.getFecha()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                cboTipoEvento.setSelectedItem(eventoSeleccionado.getTipoEvento());
                spnCupo.setValue(eventoSeleccionado.getCupoMaximo());
                txtCosto.setValue(eventoSeleccionado.getCostoInscripcion().doubleValue());
            }
        } catch (Exception e) {
            Logger.error("Error cargando evento seleccionado", e);
        }
    }
    
    private void guardarEvento() {
        try {
            // Validaciones
            if (txtCodigo.getText().trim().isEmpty() || 
                txtTitulo.getText().trim().isEmpty() ||
                txtUbicacion.getText().trim().isEmpty() ||
                txtFecha.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Todos los campos son obligatorios",
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Parsear fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim(), formatter);
            
            // Parsear costo
            String costoStr = txtCosto.getText().replaceAll("[^0-9.]", "");
            BigDecimal costo = new BigDecimal(costoStr.isEmpty() ? "0" : costoStr);
            
            // Crear o actualizar evento
            Evento evento = new Evento();
            evento.setCodigoEvento(txtCodigo.getText().trim());
            evento.setTitulo(txtTitulo.getText().trim());
            evento.setUbicacion(txtUbicacion.getText().trim());
            evento.setFecha(fecha);
            evento.setTipoEvento((TipoEvento) cboTipoEvento.getSelectedItem());
            evento.setCupoMaximo((int) spnCupo.getValue());
            evento.setCostoInscripcion(costo);
            
            boolean resultado = eventoService.guardar(evento);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this, 
                    "Evento guardado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                loadData();
            }
            
        } catch (Exception e) {
            Logger.error("Error guardando evento", e);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar el evento: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarEvento() {
        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un evento para eliminar",
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el evento?\n" + eventoSeleccionado.getTitulo(),
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean resultado = eventoService.eliminar(eventoSeleccionado.getCodigoEvento());
                
                if (resultado) {
                    JOptionPane.showMessageDialog(this, 
                        "Evento eliminado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    loadData();
                }
            } catch (Exception e) {
                Logger.error("Error eliminando evento", e);
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar el evento: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarEventos() {
        String criterio = txtBusqueda.getText().trim();
        
        if (criterio.isEmpty()) {
            loadData();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<Evento> eventos = eventoService.buscar(criterio);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Evento evento : eventos) {
                Object[] row = {
                    evento.getCodigoEvento(),
                    evento.getFecha().format(formatter),
                    evento.getTipoEvento().getDescripcion(),
                    evento.getTitulo(),
                    evento.getUbicacion(),
                    evento.getCupoMaximo(),
                    String.format("Q %.2f", evento.getCostoInscripcion()),
                    eventoService.obtenerTotalInscritos(evento.getCodigoEvento())
                };
                tableModel.addRow(row);
            }
            
            actualizarContador();
            
        } catch (Exception e) {
            Logger.error("Error buscando eventos", e);
            JOptionPane.showMessageDialog(this, 
                "Error en la búsqueda: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtUbicacion.setText("");
        txtFecha.setText("");
        cboTipoEvento.setSelectedIndex(0);
        spnCupo.setValue(50);
        txtCosto.setValue(0.00);
        eventoSeleccionado = null;
        table.clearSelection();
        txtCodigo.requestFocus();
    }
    
    private void actualizarContador() {
        Component[] components = ((JPanel) getContentPane().getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de eventos: " + tableModel.getRowCount());
                break;
            }
        }
    }
}

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
import java.util.List;
import com.mycompany.ipc2_practica1.modelo.entidades.Participante;
import com.mycompany.ipc2_practica1.modelo.enums.TipoParticipante;
import com.mycompany.ipc2_practica1.modelo.servicios.ParticipanteServicio;
import com.mycompany.ipc2_practica1.recursos.Logger;
/**
 *
 * @author jeffm
 */
public class ParticipantePanel extends JInternalFrame{
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCorreo, txtNombre, txtInstitucion;
    private JComboBox<TipoParticipante> cboTipoParticipante;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnBuscar, btnRefrescar;
    private JTextField txtBusqueda;
    
    private ParticipanteServicio participanteServicio;
    private Participante participanteSeleccionado;
    
    public ParticipantePanel() {
        super("Gestión de Participantes", true, true, true, true);
        participanteServicio = new ParticipanteServicio();
        initComponents();
        loadData();
        setSize(850, 600);
        setLocation(150, 100);
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
                "Datos del Participante",
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
        panel.add(new JLabel("Correo Electrónico:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtCorreo = new JTextField();
        txtCorreo.setToolTipText("ejemplo@dominio.com");
        panel.add(txtCorreo, gbc);
        
        gbc.gridx = 3; gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo:"), gbc);
        
        gbc.gridx = 4;
        cboTipoParticipante = new JComboBox<>(TipoParticipante.values());
        panel.add(cboTipoParticipante, gbc);
        
        // Segunda fila
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Nombre Completo:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 4;
        txtNombre = new JTextField();
        txtNombre.setDocument(new JTextFieldLimit(45));
        panel.add(txtNombre, gbc);
        
        // Tercera fila
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Institución:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 4;
        txtInstitucion = new JTextField();
        txtInstitucion.setDocument(new JTextFieldLimit(150));
        panel.add(txtInstitucion, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnNuevo = createStyledButton("Nuevo", new Color(23, 162, 184));
        btnNuevo.addActionListener(e -> limpiarFormulario());
        
        btnGuardar = createStyledButton("Guardar", new Color(40, 167, 69));
        btnGuardar.addActionListener(e -> guardarParticipante());
        
        btnEliminar = createStyledButton("Eliminar", new Color(220, 53, 69));
        btnEliminar.addActionListener(e -> eliminarParticipante());
        
        btnRefrescar = createStyledButton("Refrescar", new Color(108, 117, 125));
        btnRefrescar.addActionListener(e -> loadData());
        
        buttonPanel.add(btnNuevo);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnRefrescar);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 5;
        panel.add(buttonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Configurar modelo de tabla
        String[] columnNames = {"Correo", "Nombre", "Tipo", "Institución", "Eventos Inscritos"};
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
        
        // Ajustar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Listener para selección
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    cargarParticipanteSeleccionado(selectedRow);
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
                
                // Centrar columna de eventos inscritos
                if (column == 4) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
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
        txtBusqueda.setToolTipText("Buscar por nombre, correo o institución");
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarParticipantes();
                }
            }
        });
        panel.add(txtBusqueda);
        
        btnBuscar = createStyledButton("Buscar", new Color(0, 123, 255));
        btnBuscar.addActionListener(e -> buscarParticipantes());
        panel.add(btnBuscar);
        
        // Label de información
        JLabel lblInfo = new JLabel("Total de participantes: 0");
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
            List<Participante> participantes = participanteServicio.obtenerTodos();
            
            for (Participante participante : participantes) {
                Object[] row = {
                    participante.getCorreoElectronico(),
                    participante.getNombreCompleto(),
                    participante.getTipoParticipante().getDescripcion(),
                    participante.getInstitucionProcedencia(),
                    participanteServicio.contarEventosInscritos(participante.getCorreoElectronico())
                };
                tableModel.addRow(row);
            }
            
            actualizarContador();
            
        } catch (Exception e) {
            Logger.error("Error cargando participantes", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar los participantes: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cargarParticipanteSeleccionado(int row) {
        try {
            String correo = (String) tableModel.getValueAt(row, 0);
            participanteSeleccionado = participanteServicio.buscarPorCorreo(correo);
            
            if (participanteSeleccionado != null) {
                txtCorreo.setText(participanteSeleccionado.getCorreoElectronico());
                txtNombre.setText(participanteSeleccionado.getNombreCompleto());
                txtInstitucion.setText(participanteSeleccionado.getInstitucionProcedencia());
                cboTipoParticipante.setSelectedItem(participanteSeleccionado.getTipoParticipante());
            }
        } catch (Exception e) {
            Logger.error("Error cargando participante seleccionado", e);
        }
    }
    
    private void guardarParticipante() {
        try {
            // Validaciones
            if (txtCorreo.getText().trim().isEmpty() || 
                txtNombre.getText().trim().isEmpty() ||
                txtInstitucion.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, 
                    "Todos los campos son obligatorios",
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar formato de correo
            String correo = txtCorreo.getText().trim();
            if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, 
                    "El formato del correo electrónico no es válido",
                    "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Crear o actualizar participante
            Participante participante = new Participante();
            participante.setCorreoElectronico(correo);
            participante.setNombreCompleto(txtNombre.getText().trim());
            participante.setInstitucionProcedencia(txtInstitucion.getText().trim());
            participante.setTipoParticipante((TipoParticipante) cboTipoParticipante.getSelectedItem());
            
            boolean resultado = participanteServicio.guardar(participante);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this, 
                    "Participante guardado correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                loadData();
            }
            
        } catch (Exception e) {
            Logger.error("Error guardando participante", e);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar el participante: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void eliminarParticipante() {
        if (participanteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un participante para eliminar",
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el participante?\n" + 
            participanteSeleccionado.getNombreCompleto(),
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean resultado = participanteServicio.eliminar(
                    participanteSeleccionado.getCorreoElectronico()
                );
                
                if (resultado) {
                    JOptionPane.showMessageDialog(this, 
                        "Participante eliminado correctamente",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    loadData();
                }
            } catch (Exception e) {
                Logger.error("Error eliminando participante", e);
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar el participante: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscarParticipantes() {
        String criterio = txtBusqueda.getText().trim();
        
        if (criterio.isEmpty()) {
            loadData();
            return;
        }
        
        try {
            tableModel.setRowCount(0);
            List<Participante> participantes = participanteServicio.buscar(criterio);
            
            for (Participante participante : participantes) {
                Object[] row = {
                    participante.getCorreoElectronico(),
                    participante.getNombreCompleto(),
                    participante.getTipoParticipante().getDescripcion(),
                    participante.getInstitucionProcedencia(),
                    participanteServicio.contarEventosInscritos(participante.getCorreoElectronico())
                };
                tableModel.addRow(row);
            }
            
            actualizarContador();
            
        } catch (Exception e) {
            Logger.error("Error buscando participantes", e);
            JOptionPane.showMessageDialog(this, 
                "Error en la búsqueda: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarFormulario() {
        txtCorreo.setText("");
        txtNombre.setText("");
        txtInstitucion.setText("");
        cboTipoParticipante.setSelectedIndex(0);
        participanteSeleccionado = null;
        table.clearSelection();
        txtCorreo.requestFocus();
    }
    
    private void actualizarContador() {
        Component[] components = ((JPanel) getContentPane().getComponent(2)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && ((JLabel) comp).getText().startsWith("Total")) {
                ((JLabel) comp).setText("Total de participantes: " + tableModel.getRowCount());
                break;
            }
        }
    }
    
    // Clase interna para limitar caracteres en JTextField
    private class JTextFieldLimit extends javax.swing.text.PlainDocument {
        private int limit;
        
        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }
        
        @Override
        public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) 
                throws javax.swing.text.BadLocationException {
            if (str == null) return;
            
            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }
}

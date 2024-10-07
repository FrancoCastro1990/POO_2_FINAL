package org.duocuc.view;
import org.duocuc.controller.EquipoController;
import org.duocuc.model.Equipo;
import org.duocuc.model.Laptop;
import org.duocuc.model.Desktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EquipoPanel extends JPanel {
    private EquipoController controller;
    private JTextField txtId, txtModelo, txtCpu, txtDiscoDuro, txtRam, txtPrecio;
    private JTextField txtPotenciaFuente, txtFactorForma, txtTamanoPantalla, txtPuertosUsb;
    private JCheckBox chkEsTouch;
    private JComboBox<String> cmbTipoEquipo;
    private JButton btnGuardar, btnBuscar, btnActualizar, btnEliminar, btnLimpiar;
    private JTable tblEquipos;
    private DefaultTableModel tableModel;
    private JPanel desktopPanel, laptopPanel;

    public EquipoPanel(EquipoController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de formulario común
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        txtId = new JTextField();
        txtId.setEditable(false);
        txtModelo = new JTextField();
        txtCpu = new JTextField();
        txtDiscoDuro = new JTextField();
        txtRam = new JTextField();
        txtPrecio = new JTextField();
        cmbTipoEquipo = new JComboBox<>(new String[]{"Desktop", "Laptop"});

        formPanel.add(new JLabel("ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Modelo:"));
        formPanel.add(txtModelo);
        formPanel.add(new JLabel("CPU:"));
        formPanel.add(txtCpu);
        formPanel.add(new JLabel("Disco Duro (GB):"));
        formPanel.add(txtDiscoDuro);
        formPanel.add(new JLabel("RAM (GB):"));
        formPanel.add(txtRam);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(txtPrecio);
        formPanel.add(new JLabel("Tipo:"));
        formPanel.add(cmbTipoEquipo);

        // Panel específico para Desktop
        desktopPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        txtPotenciaFuente = new JTextField();
        txtFactorForma = new JTextField();
        desktopPanel.add(new JLabel("Potencia Fuente:"));
        desktopPanel.add(txtPotenciaFuente);
        desktopPanel.add(new JLabel("Factor Forma:"));
        desktopPanel.add(txtFactorForma);

        // Panel específico para Laptop
        laptopPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        txtTamanoPantalla = new JTextField();
        chkEsTouch = new JCheckBox("Es Touch");
        txtPuertosUsb = new JTextField();
        laptopPanel.add(new JLabel("Tamaño Pantalla:"));
        laptopPanel.add(txtTamanoPantalla);
        laptopPanel.add(new JLabel("Es Touch:"));
        laptopPanel.add(chkEsTouch);
        laptopPanel.add(new JLabel("Puertos USB:"));
        laptopPanel.add(txtPuertosUsb);

        // Panel de formulario completo
        JPanel fullFormPanel = new JPanel(new BorderLayout());
        fullFormPanel.add(formPanel, BorderLayout.NORTH);
        fullFormPanel.add(desktopPanel, BorderLayout.CENTER);
        fullFormPanel.add(laptopPanel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnBuscar = new JButton("Buscar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnBuscar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);

        // Tabla de equipos
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Modelo", "CPU", "Disco Duro", "RAM", "Precio"}, 0);
        tblEquipos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblEquipos);

        // Agregar componentes al panel principal
        add(fullFormPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Agregar listeners
        btnGuardar.addActionListener(e -> guardarEquipo());
        btnBuscar.addActionListener(e -> buscarEquipo());
        btnActualizar.addActionListener(e -> actualizarEquipo());
        btnEliminar.addActionListener(e -> eliminarEquipo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        cmbTipoEquipo.addActionListener(e -> cambiarTipoEquipo());

        // Configuración inicial
        cambiarTipoEquipo();
        cargarEquipos();
    }

    private void cambiarTipoEquipo() {
        String tipo = (String) cmbTipoEquipo.getSelectedItem();
        desktopPanel.setVisible("Desktop".equals(tipo));
        laptopPanel.setVisible("Laptop".equals(tipo));
    }

    private void guardarEquipo() {
        try {
            Equipo equipo;
            String tipo = (String) cmbTipoEquipo.getSelectedItem();
            if ("Desktop".equals(tipo)) {
                equipo = new Desktop();
                ((Desktop) equipo).setPotenciaFuente(Integer.parseInt(txtPotenciaFuente.getText()));
                ((Desktop) equipo).setFactorForma(txtFactorForma.getText());
            } else {
                equipo = new Laptop();
                ((Laptop) equipo).setTamanoPantalla(Double.parseDouble(txtTamanoPantalla.getText()));
                ((Laptop) equipo).setTouch(chkEsTouch.isSelected());
                ((Laptop) equipo).setPuertosUsb(Integer.parseInt(txtPuertosUsb.getText()));
            }
            equipo.setModelo(txtModelo.getText());
            equipo.setCpu(txtCpu.getText());
            equipo.setDiscoDuro(Integer.parseInt(txtDiscoDuro.getText()));
            equipo.setRam(Integer.parseInt(txtRam.getText()));
            equipo.setPrecio(Double.parseDouble(txtPrecio.getText()));

            controller.registrarEquipo(equipo);
            JOptionPane.showMessageDialog(this, "Equipo guardado exitosamente.");
            limpiarFormulario();
            cargarEquipos();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEquipo() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del equipo a buscar:");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Equipo equipo = controller.buscarEquipo(id);
                if (equipo != null) {
                    mostrarEquipoEnFormulario(equipo);
                } else {
                    JOptionPane.showMessageDialog(this, "Equipo no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarEquipo() {
        try {
            Equipo equipo;
            String tipo = (String) cmbTipoEquipo.getSelectedItem();
            if ("Desktop".equals(tipo)) {
                equipo = new Desktop();
                ((Desktop) equipo).setPotenciaFuente(Integer.parseInt(txtPotenciaFuente.getText()));
                ((Desktop) equipo).setFactorForma(txtFactorForma.getText());
            } else {
                equipo = new Laptop();
                ((Laptop) equipo).setTamanoPantalla(Double.parseDouble(txtTamanoPantalla.getText()));
                ((Laptop) equipo).setTouch(chkEsTouch.isSelected());
                ((Laptop) equipo).setPuertosUsb(Integer.parseInt(txtPuertosUsb.getText()));
            }
            equipo.setId(Integer.parseInt(txtId.getText()));
            equipo.setModelo(txtModelo.getText());
            equipo.setCpu(txtCpu.getText());
            equipo.setDiscoDuro(Integer.parseInt(txtDiscoDuro.getText()));
            equipo.setRam(Integer.parseInt(txtRam.getText()));
            equipo.setPrecio(Double.parseDouble(txtPrecio.getText()));

            controller.actualizarEquipo(equipo);
            JOptionPane.showMessageDialog(this, "Equipo actualizado exitosamente.");
            limpiarFormulario();
            cargarEquipos();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEquipo() {
        if (!txtId.getText().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este equipo?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminarEquipo(Integer.parseInt(txtId.getText()));
                    JOptionPane.showMessageDialog(this, "Equipo eliminado exitosamente.");
                    limpiarFormulario();
                    cargarEquipos();
                } catch (SQLException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un equipo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtModelo.setText("");
        txtCpu.setText("");
        txtDiscoDuro.setText("");
        txtRam.setText("");
        txtPrecio.setText("");
        txtPotenciaFuente.setText("");
        txtFactorForma.setText("");
        txtTamanoPantalla.setText("");
        chkEsTouch.setSelected(false);
        txtPuertosUsb.setText("");
        cmbTipoEquipo.setSelectedIndex(0);
        cambiarTipoEquipo();
    }

    private void cargarEquipos() {
        try {
            List<Equipo> equipos = controller.listarEquipos();
            tableModel.setRowCount(0);
            for (Equipo equipo : equipos) {
                tableModel.addRow(new Object[]{
                        equipo.getId(),
                        equipo instanceof Desktop ? "Desktop" : "Laptop",
                        equipo.getModelo(),
                        equipo.getCpu(),
                        equipo.getDiscoDuro(),
                        equipo.getRam(),
                        equipo.getPrecio()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarEquipoEnFormulario(Equipo equipo) {
        txtId.setText(String.valueOf(equipo.getId()));
        txtModelo.setText(equipo.getModelo());
        txtCpu.setText(equipo.getCpu());
        txtDiscoDuro.setText(String.valueOf(equipo.getDiscoDuro()));
        txtRam.setText(String.valueOf(equipo.getRam()));
        txtPrecio.setText(String.valueOf(equipo.getPrecio()));

        if (equipo instanceof Desktop) {
            cmbTipoEquipo.setSelectedItem("Desktop");
            Desktop desktop = (Desktop) equipo;
            txtPotenciaFuente.setText(String.valueOf(desktop.getPotenciaFuente()));
            txtFactorForma.setText(desktop.getFactorForma());
        } else if (equipo instanceof Laptop) {
            cmbTipoEquipo.setSelectedItem("Laptop");
            Laptop laptop = (Laptop) equipo;
            txtTamanoPantalla.setText(String.valueOf(laptop.getTamanoPantalla()));
            chkEsTouch.setSelected(laptop.isTouch());
            txtPuertosUsb.setText(String.valueOf(laptop.getPuertosUsb()));
        }
        cambiarTipoEquipo();
    }
}
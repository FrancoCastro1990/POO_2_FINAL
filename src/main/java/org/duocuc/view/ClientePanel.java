package org.duocuc.view;

import org.duocuc.controller.ClienteController;
import org.duocuc.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ClientePanel extends JPanel {
    private ClienteController controller;
    private JTextField txtRut, txtNombre, txtDireccion, txtComuna, txtCorreo, txtTelefono;
    private JButton btnGuardar, btnBuscar, btnActualizar, btnEliminar, btnLimpiar;
    private JTable tblClientes;
    private DefaultTableModel tableModel;

    public ClientePanel(ClienteController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        txtRut = new JTextField();
        txtNombre = new JTextField();
        txtDireccion = new JTextField();
        txtComuna = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();

        formPanel.add(new JLabel("RUT:"));
        formPanel.add(txtRut);
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Dirección:"));
        formPanel.add(txtDireccion);
        formPanel.add(new JLabel("Comuna:"));
        formPanel.add(txtComuna);
        formPanel.add(new JLabel("Correo:"));
        formPanel.add(txtCorreo);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);

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

        // Tabla de clientes
        tableModel = new DefaultTableModel(new Object[]{"RUT", "Nombre", "Dirección", "Comuna", "Correo", "Teléfono"}, 0);
        tblClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblClientes);

        // Agregar componentes al panel principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Agregar listeners
        btnGuardar.addActionListener(e -> guardarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Cargar clientes al iniciar
        cargarClientes();
    }

    private void guardarCliente() {
        try {
            Cliente cliente = new Cliente(
                    txtRut.getText(),
                    txtNombre.getText(),
                    txtDireccion.getText(),
                    txtComuna.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText()
            );
            controller.registrarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
            limpiarFormulario();
            cargarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        String rut = JOptionPane.showInputDialog(this, "Ingrese el RUT del cliente a buscar:");
        if (rut != null && !rut.isEmpty()) {
            try {
                Cliente cliente = controller.buscarCliente(rut);
                if (cliente != null) {
                    txtRut.setText(cliente.getRut());
                    txtNombre.setText(cliente.getNombreCompleto());
                    txtDireccion.setText(cliente.getDireccion());
                    txtComuna.setText(cliente.getComuna());
                    txtCorreo.setText(cliente.getCorreoElectronico());
                    txtTelefono.setText(cliente.getTelefono());
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarCliente() {
        try {
            Cliente cliente = new Cliente(
                    txtRut.getText(),
                    txtNombre.getText(),
                    txtDireccion.getText(),
                    txtComuna.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText()
            );
            controller.actualizarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.");
            limpiarFormulario();
            cargarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        String rut = txtRut.getText();
        if (!rut.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    controller.eliminarCliente(rut);
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.");
                    limpiarFormulario();
                    cargarClientes();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        txtRut.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtComuna.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = controller.listarClientes();
            tableModel.setRowCount(0);
            for (Cliente cliente : clientes) {
                tableModel.addRow(new Object[]{
                        cliente.getRut(),
                        cliente.getNombreCompleto(),
                        cliente.getDireccion(),
                        cliente.getComuna(),
                        cliente.getCorreoElectronico(),
                        cliente.getTelefono()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

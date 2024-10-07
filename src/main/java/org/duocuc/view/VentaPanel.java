package org.duocuc.view;

import org.duocuc.controller.ClienteController;
import org.duocuc.controller.EquipoController;
import org.duocuc.controller.VentaController;
import org.duocuc.model.Cliente;
import org.duocuc.model.Equipo;
import org.duocuc.model.Venta;
import org.duocuc.patterns.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentaPanel extends JPanel {
    private VentaController ventaController;
    private ClienteController clienteController;
    private EquipoController equipoController;
    private JComboBox<String> cmbClientes;
    private JComboBox<String> cmbEquipos;
    private JTextField txtFecha;
    private JTextField txtPrecio;
    private JComboBox<String> cmbTipoDescuento;
    private JTextField txtDescuento;
    private JTextField txtPrecioFinal;
    private JButton btnRegistrarVenta;
    private JButton btnAplicarDescuento;
    private JTable tblVentas;
    private DefaultTableModel tableModel;

    public VentaPanel(VentaController ventaController, ClienteController clienteController, EquipoController equipoController) {
        this.ventaController = ventaController;
        this.clienteController = clienteController;
        this.equipoController = equipoController;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        cmbClientes = new JComboBox<>();
        cmbEquipos = new JComboBox<>();
        txtFecha = new JTextField(new Date().toString());
        txtFecha.setEditable(false);
        txtPrecio = new JTextField();
        txtPrecio.setEditable(false);
        cmbTipoDescuento = new JComboBox<>(new String[]{"Sin Descuento", "Porcentaje", "Monto Fijo"});
        txtDescuento = new JTextField("0");
        txtPrecioFinal = new JTextField();
        txtPrecioFinal.setEditable(false);

        formPanel.add(new JLabel("Cliente:"));
        formPanel.add(cmbClientes);
        formPanel.add(new JLabel("Equipo:"));
        formPanel.add(cmbEquipos);
        formPanel.add(new JLabel("Fecha:"));
        formPanel.add(txtFecha);
        formPanel.add(new JLabel("Precio:"));
        formPanel.add(txtPrecio);
        formPanel.add(new JLabel("Tipo de Descuento:"));
        formPanel.add(cmbTipoDescuento);
        formPanel.add(new JLabel("Descuento (%):"));
        formPanel.add(txtDescuento);
        formPanel.add(new JLabel("Precio Final:"));
        formPanel.add(txtPrecioFinal);


        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRegistrarVenta = new JButton("Registrar Venta");
        btnAplicarDescuento = new JButton("Aplicar Descuento");
        buttonPanel.add(btnRegistrarVenta);
        buttonPanel.add(btnAplicarDescuento);

        // Tabla de ventas
        tableModel = new DefaultTableModel(new Object[]{"ID", "Cliente", "Equipo", "Fecha", "Precio Final"}, 0);
        tblVentas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblVentas);

        // Agregar componentes al panel principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Agregar listeners
        btnRegistrarVenta.addActionListener(e -> registrarVenta());
        btnAplicarDescuento.addActionListener(e -> aplicarDescuento());
        cmbEquipos.addActionListener(e -> actualizarPrecio());

        // Cargar datos iniciales
        cargarClientes();
        cargarEquipos();
        cargarVentas();
    }

    private void cargarClientes() {
        try {
            List<Cliente> clientes = clienteController.listarClientes();
            cmbClientes.removeAllItems();
            for (Cliente cliente : clientes) {
                cmbClientes.addItem(cliente.getRut() + " - " + cliente.getNombreCompleto());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEquipos() {
        try {
            List<Equipo> equipos = equipoController.listarEquipos();
            cmbEquipos.removeAllItems();
            for (Equipo equipo : equipos) {
                cmbEquipos.addItem(equipo.getId() + " - " + equipo.getModelo());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPrecio() {
        String selectedEquipo = (String) cmbEquipos.getSelectedItem();
        if (selectedEquipo != null && !selectedEquipo.isEmpty()) {
            int equipoId = Integer.parseInt(selectedEquipo.split(" - ")[0]);
            try {
                Equipo equipo = equipoController.buscarEquipo(equipoId);
                if (equipo != null) {
                    txtPrecio.setText(String.format("%.2f", equipo.getPrecio()));
                    aplicarDescuento();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al obtener precio del equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Descuento crearDescuento() {
        String tipoDescuento = (String) cmbTipoDescuento.getSelectedItem();
        double valorDescuento = Double.parseDouble(txtDescuento.getText());
        Descuento descuentoBase = new DescuentoBase();

        switch (tipoDescuento) {
            case "Porcentaje":
                return new DescuentoPorcentaje(descuentoBase, valorDescuento);
            case "Monto Fijo":
                return new DescuentoMontoFijo(descuentoBase, valorDescuento);
            default:
                return descuentoBase;
        }
    }

    private void aplicarDescuento() {
        try {
            // Crear un NumberFormat con la configuración regional adecuada
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse(txtPrecio.getText());

            double precioOriginal = number.doubleValue();
            Descuento descuento = crearDescuento();
            double precioFinal = descuento.aplicarDescuento(precioOriginal);
            txtPrecioFinal.setText(String.format("%.2f", precioFinal));
        } catch (ParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en los valores de precio o descuento.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void registrarVenta() {
        try {
            String selectedCliente = (String) cmbClientes.getSelectedItem();
            String selectedEquipo = (String) cmbEquipos.getSelectedItem();
            if (selectedCliente == null || selectedEquipo == null) {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un cliente y un equipo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String rutCliente = selectedCliente.split(" - ")[0];
            int equipoId = Integer.parseInt(selectedEquipo.split(" - ")[0]);

            Cliente cliente = clienteController.buscarCliente(rutCliente);
            Equipo equipo = equipoController.buscarEquipo(equipoId);

            if (cliente == null || equipo == null) {
                JOptionPane.showMessageDialog(this, "Error al obtener los datos del cliente o equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = format.parse((txtPrecioFinal.getText()));

            double precioFinal = number.doubleValue();
            Date fechaVenta = new Date(); // Usar la fecha actual

            Venta venta = new Venta(cliente, equipo, fechaVenta, precioFinal);

            VentaCommand command = new RegistrarVentaCommand(ventaController, venta);
            command.execute();
            //ventaController.registrarVenta(venta);

            JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarVentas();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error en el formato de los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void limpiarFormulario() {
        cmbClientes.setSelectedIndex(-1);
        cmbEquipos.setSelectedIndex(-1);
        txtFecha.setText(new Date().toString());
        txtPrecio.setText("");
        txtDescuento.setText("0");
        txtPrecioFinal.setText("");
    }

    private void cargarVentas() {
        try {
            List<Venta> ventas = ventaController.listarVentas();
            tableModel.setRowCount(0);
            for (Venta venta : ventas) {
                tableModel.addRow(new Object[]{
                        venta.getId(),
                        venta.getCliente().getNombreCompleto(),
                        venta.getEquipo().getModelo(),
                        venta.getFechaHora(),
                        venta.getPrecioFinal()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
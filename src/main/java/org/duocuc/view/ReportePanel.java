package org.duocuc.view;
import org.duocuc.controller.VentaController;
import org.duocuc.model.ReporteVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class ReportePanel extends JPanel {
    private VentaController ventaController;
    private JComboBox<String> cmbTipoEquipo;
    private JButton btnGenerarReporte;
    private JTable tblReporte;
    private DefaultTableModel tableModel;
    private JLabel lblTotalVentas;
    private JLabel lblMontoTotal;

    public ReportePanel(VentaController ventaController) {
        this.ventaController = ventaController;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior para filtros y botones
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cmbTipoEquipo = new JComboBox<>(new String[]{"Todos", "Desktop", "Laptop"});
        btnGenerarReporte = new JButton("Generar Reporte");
        topPanel.add(new JLabel("Tipo de Equipo:"));
        topPanel.add(cmbTipoEquipo);
        topPanel.add(btnGenerarReporte);

        // Panel para la tabla de reporte
        tableModel = new DefaultTableModel(new Object[]{"Modelo", "Cliente", "Teléfono", "Correo", "Precio"}, 0);
        tblReporte = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblReporte);

        // Panel inferior para totales
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalVentas = new JLabel("Total Ventas: 0");
        lblMontoTotal = new JLabel("Monto Total: $0.00");
        bottomPanel.add(lblTotalVentas);
        bottomPanel.add(Box.createHorizontalStrut(20)); // Espacio entre labels
        bottomPanel.add(lblMontoTotal);

        // Agregar componentes al panel principal
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Agregar listener al botón
        btnGenerarReporte.addActionListener(e -> generarReporte());
    }

    private void generarReporte() {
        String tipoEquipo = (String) cmbTipoEquipo.getSelectedItem();
        try {
            List<ReporteVenta> reporteVentas;
            if ("Todos".equals(tipoEquipo)) {
                reporteVentas = ventaController.generarReporteEquiposVendidos(null);
            } else {
                reporteVentas = ventaController.generarReporteEquiposVendidos(tipoEquipo);
            }

            actualizarTablaReporte(reporteVentas);
            actualizarTotales(tipoEquipo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaReporte(List<ReporteVenta> reporteVentas) {
        tableModel.setRowCount(0);
        for (ReporteVenta reporte : reporteVentas) {
            tableModel.addRow(new Object[]{
                    reporte.getModeloEquipo(),
                    reporte.getNombreCliente(),
                    reporte.getTelefonoCliente(),
                    reporte.getCorreoCliente(),
                    String.format("$%.2f", reporte.getPrecioEquipo())
            });
        }
    }

    private void actualizarTotales(String tipoEquipo) {
        try {
            int totalVentas = ventaController.contarVentas();
            double montoTotal = ventaController.calcularMontoTotalVentas();

            if (!"Todos".equals(tipoEquipo)) {
                totalVentas = tableModel.getRowCount(); // Contar solo las filas mostradas en la tabla
                montoTotal = 0;
                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    try {
                        String precioStr = ((String) tableModel.getValueAt(i, 4)).replace("$", "").trim();
                        Number number = format.parse(precioStr);
                        montoTotal += number.doubleValue();
                    } catch (ParseException e) {
                        System.err.println("Error al parsear el precio en la fila " + i + ": " + e.getMessage());
                        // Opcionalmente, puedes mostrar un mensaje al usuario o manejar el error de otra manera
                    }
                }
            }

            lblTotalVentas.setText("Total Ventas: " + totalVentas);
            lblMontoTotal.setText(String.format(Locale.getDefault(), "Monto Total: $%,.2f", montoTotal));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al calcular totales: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
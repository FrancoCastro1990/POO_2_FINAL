package org.duocuc.controller;

import org.duocuc.model.ReporteVenta;
import org.duocuc.model.Venta;
import org.duocuc.model.VentaDAO;

import java.sql.SQLException;
import java.util.List;
public class VentaController {
    private VentaDAO ventaDAO;

    public VentaController() {
        this.ventaDAO = new VentaDAO();
    }

    public void registrarVenta(Venta venta) throws SQLException {
        ventaDAO.registrarVenta(venta);
    }

    public List<Venta> listarVentas() throws SQLException {
        return ventaDAO.listarVentas();
    }

    public Venta buscarVenta(int id) throws SQLException {
        return ventaDAO.buscarVentaPorId(id);
    }

    public List<Venta> listarVentasPorTipoEquipo(String tipoEquipo) throws SQLException {
        return ventaDAO.listarVentasPorTipoEquipo(tipoEquipo);
    }

    public double calcularMontoTotalVentas() throws SQLException {
        return ventaDAO.calcularMontoTotalVentas();
    }

    public int contarVentas() throws SQLException {
        return ventaDAO.contarVentas();
    }

    // Método para generar el reporte de equipos vendidos
    public List<ReporteVenta> generarReporteEquiposVendidos(String tipoEquipo) throws SQLException {
        return ventaDAO.generarReporteEquiposVendidos(tipoEquipo);
    }
}

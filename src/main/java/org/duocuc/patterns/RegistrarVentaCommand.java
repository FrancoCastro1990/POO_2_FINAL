package org.duocuc.patterns;

import org.duocuc.controller.VentaController;
import org.duocuc.model.Venta;

import java.sql.SQLException;

public class RegistrarVentaCommand implements VentaCommand {
    private VentaController ventaController;
    private Venta venta;

    public RegistrarVentaCommand(VentaController ventaController, Venta venta) {
        this.ventaController = ventaController;
        this.venta = venta;
    }

    @Override
    public void execute() {
        try {
            ventaController.registrarVenta(venta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
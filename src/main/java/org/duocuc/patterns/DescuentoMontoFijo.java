package org.duocuc.patterns;

public class DescuentoMontoFijo implements Descuento {
    private Descuento descuentoBase;
    private double montoFijo;

    public DescuentoMontoFijo(Descuento descuentoBase, double montoFijo) {
        this.descuentoBase = descuentoBase;
        this.montoFijo = montoFijo;
    }

    @Override
    public double aplicarDescuento(double precio) {
        double precioConDescuentoBase = descuentoBase.aplicarDescuento(precio);
        return Math.max(0, precioConDescuentoBase - montoFijo);
    }
}

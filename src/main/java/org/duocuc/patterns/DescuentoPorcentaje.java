package org.duocuc.patterns;

public class DescuentoPorcentaje implements Descuento {
    private Descuento descuentoBase;
    private double porcentaje;

    public DescuentoPorcentaje(Descuento descuentoBase, double porcentaje) {
        this.descuentoBase = descuentoBase;
        this.porcentaje = porcentaje;
    }

    @Override
    public double aplicarDescuento(double precio) {
        double precioConDescuentoBase = descuentoBase.aplicarDescuento(precio);
        return precioConDescuentoBase * (1 - porcentaje / 100);
    }
}
package org.duocuc.patterns;

public class DescuentoBase implements Descuento {
    @Override
    public double aplicarDescuento(double precio) {
        return precio; // Sin descuento
    }
}
package org.duocuc.model;

public class Laptop extends Equipo {
    private double tamanoPantalla;
    private boolean esTouch;
    private int puertosUsb;

    public Laptop() {}

    public Laptop(String modelo, String cpu, int discoDuro, int ram, double precio, double tamanoPantalla, boolean esTouch, int puertosUsb) {
        super(modelo, cpu, discoDuro, ram, precio);
        this.tamanoPantalla = tamanoPantalla;
        this.esTouch = esTouch;
        this.puertosUsb = puertosUsb;
    }

    // Getters y setters específicos
    public double getTamanoPantalla() { return tamanoPantalla; }
    public void setTamanoPantalla(double tamanoPantalla) { this.tamanoPantalla = tamanoPantalla; }
    public boolean isTouch() { return esTouch; }
    public void setTouch(boolean esTouch) { this.esTouch = esTouch; }
    public int getPuertosUsb() { return puertosUsb; }
    public void setPuertosUsb(int puertosUsb) { this.puertosUsb = puertosUsb; }

    @Override
    public String getTipo() {
        return "Laptop";
    }
}
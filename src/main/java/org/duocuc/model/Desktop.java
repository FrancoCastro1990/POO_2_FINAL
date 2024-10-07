package org.duocuc.model;

public class Desktop extends Equipo {
    private int potenciaFuente;
    private String factorForma;

    public Desktop() {}

    public Desktop(String modelo, String cpu, int discoDuro, int ram, double precio, int potenciaFuente, String factorForma) {
        super(modelo, cpu, discoDuro, ram, precio);
        this.potenciaFuente = potenciaFuente;
        this.factorForma = factorForma;
    }

    // Getters y setters específicos
    public int getPotenciaFuente() { return potenciaFuente; }
    public void setPotenciaFuente(int potenciaFuente) { this.potenciaFuente = potenciaFuente; }
    public String getFactorForma() { return factorForma; }
    public void setFactorForma(String factorForma) { this.factorForma = factorForma; }

    @Override
    public String getTipo() {
        return "Desktop";
    }
}
package org.duocuc.model;

public abstract class Equipo {
    protected int id;
    protected String modelo;
    protected String cpu;
    protected int discoDuro;
    protected int ram;
    protected double precio;

    public Equipo() {}

    public Equipo(String modelo, String cpu, int discoDuro, int ram, double precio) {
        this.modelo = modelo;
        this.cpu = cpu;
        this.discoDuro = discoDuro;
        this.ram = ram;
        this.precio = precio;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }
    public int getDiscoDuro() { return discoDuro; }
    public void setDiscoDuro(int discoDuro) { this.discoDuro = discoDuro; }
    public int getRam() { return ram; }
    public void setRam(int ram) { this.ram = ram; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public abstract String getTipo();
}
package org.duocuc.model;

import java.util.Date;

public class Venta {
    private int id;
    private Cliente cliente;
    private Equipo equipo;
    private Date fechaHora;
    private double precioFinal;

    public Venta() {}

    public Venta(Cliente cliente, Equipo equipo, Date fechaHora, double precioFinal) {
        this.cliente = cliente;
        this.equipo = equipo;
        this.fechaHora = fechaHora;
        this.precioFinal = precioFinal;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }
    public Date getFechaHora() { return fechaHora; }
    public void setFechaHora(Date fechaHora) { this.fechaHora = fechaHora; }
    public double getPrecioFinal() { return precioFinal; }
    public void setPrecioFinal(double precioFinal) { this.precioFinal = precioFinal; }
}

package org.duocuc.model;

public class ReporteVenta {
    private String modeloEquipo;
    private String nombreCliente;
    private String telefonoCliente;
    private String correoCliente;
    private double precioEquipo;

    public ReporteVenta() {
    }

    public ReporteVenta(String modeloEquipo, String nombreCliente, String telefonoCliente, String correoCliente, double precioEquipo) {
        this.modeloEquipo = modeloEquipo;
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.precioEquipo = precioEquipo;
    }

    public String getModeloEquipo() {
        return modeloEquipo;
    }

    public void setModeloEquipo(String modeloEquipo) {
        this.modeloEquipo = modeloEquipo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public double getPrecioEquipo() {
        return precioEquipo;
    }

    public void setPrecioEquipo(double precioEquipo) {
        this.precioEquipo = precioEquipo;
    }
}
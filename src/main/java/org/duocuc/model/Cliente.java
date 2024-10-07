package org.duocuc.model;

public class Cliente {
    private String rut;
    private String nombreCompleto;
    private String direccion;
    private String comuna;
    private String correoElectronico;
    private String telefono;

    public Cliente() {}

    public Cliente(String rut, String nombreCompleto, String direccion, String comuna, String correoElectronico, String telefono) {
        this.rut = rut;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.comuna = comuna;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
    }

    // Getters y setters
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
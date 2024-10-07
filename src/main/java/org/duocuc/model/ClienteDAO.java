package org.duocuc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void guardarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (rut, nombre_completo, direccion, comuna, correo_electronico, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getRut());
            pstmt.setString(2, cliente.getNombreCompleto());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getComuna());
            pstmt.setString(5, cliente.getCorreoElectronico());
            pstmt.setString(6, cliente.getTelefono());
            pstmt.executeUpdate();
        }
    }

    public Cliente buscarClientePorRut(String rut) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE rut = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rut);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getString("rut"),
                            rs.getString("nombre_completo"),
                            rs.getString("direccion"),
                            rs.getString("comuna"),
                            rs.getString("correo_electronico"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }

    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new Cliente(
                        rs.getString("rut"),
                        rs.getString("nombre_completo"),
                        rs.getString("direccion"),
                        rs.getString("comuna"),
                        rs.getString("correo_electronico"),
                        rs.getString("telefono")
                ));
            }
        }
        return clientes;
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre_completo = ?, direccion = ?, comuna = ?, correo_electronico = ?, telefono = ? WHERE rut = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombreCompleto());
            pstmt.setString(2, cliente.getDireccion());
            pstmt.setString(3, cliente.getComuna());
            pstmt.setString(4, cliente.getCorreoElectronico());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.setString(6, cliente.getRut());
            pstmt.executeUpdate();
        }
    }

    public void eliminarCliente(String rut) throws SQLException {
        String sql = "DELETE FROM clientes WHERE rut = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rut);
            pstmt.executeUpdate();
        }
    }
}

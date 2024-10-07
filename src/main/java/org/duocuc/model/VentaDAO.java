package org.duocuc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    public void registrarVenta(Venta venta) throws SQLException {
        String sql = "INSERT INTO ventas (cliente_rut, equipo_id, fecha_hora, precio_final) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, venta.getCliente().getRut());
            pstmt.setInt(2, venta.getEquipo().getId());
            pstmt.setTimestamp(3, new Timestamp(venta.getFechaHora().getTime()));
            pstmt.setDouble(4, venta.getPrecioFinal());
            pstmt.executeUpdate();
        }
    }

    public List<Venta> listarVentas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, c.*, e.*, " +
                "d.potencia_fuente, d.factor_forma, " +
                "l.tamano_pantalla, l.es_touch, l.puertos_usb " +
                "FROM ventas v " +
                "JOIN clientes c ON v.cliente_rut = c.rut " +
                "JOIN equipos e ON v.equipo_id = e.id " +
                "LEFT JOIN desktops d ON e.id = d.equipo_id " +
                "LEFT JOIN laptops l ON e.id = l.equipo_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("c.rut"),
                        rs.getString("c.nombre_completo"),
                        rs.getString("c.direccion"),
                        rs.getString("c.comuna"),
                        rs.getString("c.correo_electronico"),
                        rs.getString("c.telefono")
                );

                Equipo equipo;
                if ("Desktop".equals(rs.getString("e.tipo"))) {
                    equipo = new Desktop();
                    ((Desktop) equipo).setPotenciaFuente(rs.getInt("potencia_fuente"));
                    ((Desktop) equipo).setFactorForma(rs.getString("factor_forma"));
                } else {
                    equipo = new Laptop();
                    ((Laptop) equipo).setTamanoPantalla(rs.getDouble("tamano_pantalla"));
                    ((Laptop) equipo).setTouch(rs.getBoolean("es_touch"));
                    ((Laptop) equipo).setPuertosUsb(rs.getInt("puertos_usb"));
                }
                equipo.setId(rs.getInt("e.id"));
                equipo.setModelo(rs.getString("e.modelo"));
                equipo.setCpu(rs.getString("e.cpu"));
                equipo.setDiscoDuro(rs.getInt("e.disco_duro"));
                equipo.setRam(rs.getInt("e.ram"));
                equipo.setPrecio(rs.getDouble("e.precio"));

                Venta venta = new Venta(
                        cliente,
                        equipo,
                        rs.getTimestamp("v.fecha_hora"),
                        rs.getDouble("v.precio_final")
                );
                venta.setId(rs.getInt("v.id"));
                ventas.add(venta);
            }
        }
        return ventas;
    }


    public Venta buscarVentaPorId(int id) throws SQLException {
        String sql = "SELECT v.*, c.*, e.* FROM ventas v " +
                "JOIN clientes c ON v.cliente_rut = c.rut " +
                "JOIN equipos e ON v.equipo_id = e.id " +
                "WHERE v.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("c.rut"),
                            rs.getString("c.nombre_completo"),
                            rs.getString("c.direccion"),
                            rs.getString("c.comuna"),
                            rs.getString("c.correo_electronico"),
                            rs.getString("c.telefono")
                    );

                    Equipo equipo;
                    if ("Desktop".equals(rs.getString("e.tipo"))) {
                        equipo = new Desktop();
                        ((Desktop) equipo).setPotenciaFuente(rs.getInt("potencia_fuente"));
                        ((Desktop) equipo).setFactorForma(rs.getString("factor_forma"));
                    } else {
                        equipo = new Laptop();
                        ((Laptop) equipo).setTamanoPantalla(rs.getDouble("tamano_pantalla"));
                        ((Laptop) equipo).setTouch(rs.getBoolean("es_touch"));
                        ((Laptop) equipo).setPuertosUsb(rs.getInt("puertos_usb"));
                    }
                    equipo.setId(rs.getInt("e.id"));
                    equipo.setModelo(rs.getString("e.modelo"));
                    equipo.setCpu(rs.getString("e.cpu"));
                    equipo.setDiscoDuro(rs.getInt("e.disco_duro"));
                    equipo.setRam(rs.getInt("e.ram"));
                    equipo.setPrecio(rs.getDouble("e.precio"));

                    Venta venta = new Venta(
                            cliente,
                            equipo,
                            rs.getTimestamp("v.fecha_hora"),
                            rs.getDouble("v.precio_final")
                    );
                    venta.setId(rs.getInt("v.id"));
                    return venta;
                }
            }
        }
        return null;
    }

    public List<Venta> listarVentasPorTipoEquipo(String tipoEquipo) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, c.*, e.*, " +
                "CASE WHEN e.tipo = 'Desktop' THEN d.potencia_fuente ELSE l.tamano_pantalla END AS dato_especifico1, " +
                "CASE WHEN e.tipo = 'Desktop' THEN d.factor_forma ELSE l.es_touch END AS dato_especifico2, " +
                "CASE WHEN e.tipo = 'Laptop' THEN l.puertos_usb ELSE NULL END AS puertos_usb " +
                "FROM ventas v " +
                "JOIN clientes c ON v.cliente_rut = c.rut " +
                "JOIN equipos e ON v.equipo_id = e.id " +
                "LEFT JOIN desktops d ON e.id = d.equipo_id " +
                "LEFT JOIN laptops l ON e.id = l.equipo_id " +
                "WHERE e.tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipoEquipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente(
                            rs.getString("c.rut"),
                            rs.getString("c.nombre_completo"),
                            rs.getString("c.direccion"),
                            rs.getString("c.comuna"),
                            rs.getString("c.correo_electronico"),
                            rs.getString("c.telefono")
                    );

                    Equipo equipo;
                    if ("Desktop".equals(tipoEquipo)) {
                        equipo = new Desktop();
                        ((Desktop) equipo).setPotenciaFuente(rs.getInt("dato_especifico1"));
                        ((Desktop) equipo).setFactorForma(rs.getString("dato_especifico2"));
                    } else {
                        equipo = new Laptop();
                        ((Laptop) equipo).setTamanoPantalla(rs.getDouble("dato_especifico1"));
                        ((Laptop) equipo).setTouch(rs.getBoolean("dato_especifico2"));
                        ((Laptop) equipo).setPuertosUsb(rs.getInt("puertos_usb"));
                    }
                    equipo.setId(rs.getInt("e.id"));
                    equipo.setModelo(rs.getString("e.modelo"));
                    equipo.setCpu(rs.getString("e.cpu"));
                    equipo.setDiscoDuro(rs.getInt("e.disco_duro"));
                    equipo.setRam(rs.getInt("e.ram"));
                    equipo.setPrecio(rs.getDouble("e.precio"));

                    Venta venta = new Venta(
                            cliente,
                            equipo,
                            rs.getTimestamp("v.fecha_hora"),
                            rs.getDouble("v.precio_final")
                    );
                    venta.setId(rs.getInt("v.id"));
                    ventas.add(venta);
                }
            }
        }
        return ventas;
    }

    public double calcularMontoTotalVentas() throws SQLException {
        String sql = "SELECT SUM(precio_final) AS total FROM ventas";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    public int contarVentas() throws SQLException {
        String sql = "SELECT COUNT(*) AS cantidad FROM ventas";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        }
        return 0;
    }

//    public List<ReporteVenta> generarReporteEquiposVendidos(String tipoEquipo) throws SQLException {
//        List<ReporteVenta> reporte = new ArrayList<>();
//        String sql = "SELECT e.modelo, c.nombre_completo, c.telefono, c.correo_electronico, v.precio_final " +
//                "FROM ventas v " +
//                "JOIN clientes c ON v.cliente_rut = c.rut " +
//                "JOIN equipos e ON v.equipo_id = e.id " +
//                "WHERE e.tipo = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, tipoEquipo);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    ReporteVenta item = new ReporteVenta(
//                            rs.getString("modelo"),
//                            rs.getString("nombre_completo"),
//                            rs.getString("telefono"),
//                            rs.getString("correo_electronico"),
//                            rs.getDouble("precio_final")
//                    );
//                    reporte.add(item);
//                }
//            }
//        }
//        return reporte;
//    }

    public List<ReporteVenta> generarReporteEquiposVendidos(String tipoEquipo) throws SQLException {
        String sql;
        if (tipoEquipo == null || tipoEquipo.isEmpty()) {
            // Consulta para todos los tipos de equipo
            sql = "SELECT e.modelo, c.nombre_completo, c.telefono, c.correo_electronico, v.precio_final " +
                    "FROM ventas v " +
                    "JOIN clientes c ON v.cliente_rut = c.rut " +
                    "JOIN equipos e ON v.equipo_id = e.id";
        } else {
            // Consulta filtrada por tipo de equipo
            sql = "SELECT e.modelo, c.nombre_completo, c.telefono, c.correo_electronico, v.precio_final " +
                    "FROM ventas v " +
                    "JOIN clientes c ON v.cliente_rut = c.rut " +
                    "JOIN equipos e ON v.equipo_id = e.id " +
                    "WHERE e.tipo = ?";
        }

        List<ReporteVenta> reporteVentas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (tipoEquipo != null && !tipoEquipo.isEmpty()) {
                pstmt.setString(1, tipoEquipo);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ReporteVenta reporte = new ReporteVenta(
                            rs.getString("modelo"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getDouble("precio_final")
                    );
                    reporteVentas.add(reporte);
                }
            }
        }
        return reporteVentas;
    }
}
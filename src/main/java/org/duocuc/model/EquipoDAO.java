package org.duocuc.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO {

    public void guardarEquipo(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO equipos (modelo, cpu, disco_duro, ram, precio, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, equipo.getModelo());
            pstmt.setString(2, equipo.getCpu());
            pstmt.setInt(3, equipo.getDiscoDuro());
            pstmt.setInt(4, equipo.getRam());
            pstmt.setDouble(5, equipo.getPrecio());
            pstmt.setString(6, equipo instanceof Desktop ? "Desktop" : "Laptop");
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int equipoId = generatedKeys.getInt(1);
                    if (equipo instanceof Desktop) {
                        guardarDesktop((Desktop) equipo, equipoId);
                    } else if (equipo instanceof Laptop) {
                        guardarLaptop((Laptop) equipo, equipoId);
                    }
                }
            }
        }
    }

    private void guardarDesktop(Desktop desktop, int equipoId) throws SQLException {
        String sql = "INSERT INTO desktops (equipo_id, potencia_fuente, factor_forma) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipoId);
            pstmt.setInt(2, desktop.getPotenciaFuente());
            pstmt.setString(3, desktop.getFactorForma());
            pstmt.executeUpdate();
        }
    }

    private void guardarLaptop(Laptop laptop, int equipoId) throws SQLException {
        String sql = "INSERT INTO laptops (equipo_id, tamano_pantalla, es_touch, puertos_usb) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, equipoId);
            pstmt.setDouble(2, laptop.getTamanoPantalla());
            pstmt.setBoolean(3, laptop.isTouch());
            pstmt.setInt(4, laptop.getPuertosUsb());
            pstmt.executeUpdate();
        }
    }

    public Equipo buscarEquipoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM equipos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    if ("Desktop".equals(tipo)) {
                        return cargarDesktop(rs);
                    } else if ("Laptop".equals(tipo)) {
                        return cargarLaptop(rs);
                    }
                }
            }
        }
        return null;
    }

    private Desktop cargarDesktop(ResultSet rs) throws SQLException {
        Desktop desktop = new Desktop();
        cargarDatosBasicosEquipo(desktop, rs);
        String sql = "SELECT * FROM desktops WHERE equipo_id = ?";
        try (PreparedStatement pstmt = rs.getStatement().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, rs.getInt("id"));
            try (ResultSet rsDesktop = pstmt.executeQuery()) {
                if (rsDesktop.next()) {
                    desktop.setPotenciaFuente(rsDesktop.getInt("potencia_fuente"));
                    desktop.setFactorForma(rsDesktop.getString("factor_forma"));
                }
            }
        }
        return desktop;
    }

    private Laptop cargarLaptop(ResultSet rs) throws SQLException {
        Laptop laptop = new Laptop();
        cargarDatosBasicosEquipo(laptop, rs);
        String sql = "SELECT * FROM laptops WHERE equipo_id = ?";
        try (PreparedStatement pstmt = rs.getStatement().getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, rs.getInt("id"));
            try (ResultSet rsLaptop = pstmt.executeQuery()) {
                if (rsLaptop.next()) {
                    laptop.setTamanoPantalla(rsLaptop.getDouble("tamano_pantalla"));
                    laptop.setTouch(rsLaptop.getBoolean("es_touch"));
                    laptop.setPuertosUsb(rsLaptop.getInt("puertos_usb"));
                }
            }
        }
        return laptop;
    }

    private void cargarDatosBasicosEquipo(Equipo equipo, ResultSet rs) throws SQLException {
        equipo.setId(rs.getInt("id"));
        equipo.setModelo(rs.getString("modelo"));
        equipo.setCpu(rs.getString("cpu"));
        equipo.setDiscoDuro(rs.getInt("disco_duro"));
        equipo.setRam(rs.getInt("ram"));
        equipo.setPrecio(rs.getDouble("precio"));
    }

    public List<Equipo> listarEquipos() throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT * FROM equipos";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                if ("Desktop".equals(tipo)) {
                    equipos.add(cargarDesktop(rs));
                } else if ("Laptop".equals(tipo)) {
                    equipos.add(cargarLaptop(rs));
                }
            }
        }
        return equipos;
    }

    public void actualizarEquipo(Equipo equipo) throws SQLException {
        String sql = "UPDATE equipos SET modelo = ?, cpu = ?, disco_duro = ?, ram = ?, precio = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, equipo.getModelo());
            pstmt.setString(2, equipo.getCpu());
            pstmt.setInt(3, equipo.getDiscoDuro());
            pstmt.setInt(4, equipo.getRam());
            pstmt.setDouble(5, equipo.getPrecio());
            pstmt.setInt(6, equipo.getId());
            pstmt.executeUpdate();

            if (equipo instanceof Desktop) {
                actualizarDesktop((Desktop) equipo);
            } else if (equipo instanceof Laptop) {
                actualizarLaptop((Laptop) equipo);
            }
        }
    }

    private void actualizarDesktop(Desktop desktop) throws SQLException {
        String sql = "UPDATE desktops SET potencia_fuente = ?, factor_forma = ? WHERE equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, desktop.getPotenciaFuente());
            pstmt.setString(2, desktop.getFactorForma());
            pstmt.setInt(3, desktop.getId());
            pstmt.executeUpdate();
        }
    }

    private void actualizarLaptop(Laptop laptop) throws SQLException {
        String sql = "UPDATE laptops SET tamano_pantalla = ?, es_touch = ?, puertos_usb = ? WHERE equipo_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, laptop.getTamanoPantalla());
            pstmt.setBoolean(2, laptop.isTouch());
            pstmt.setInt(3, laptop.getPuertosUsb());
            pstmt.setInt(4, laptop.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarEquipo(int id) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Equipo> listarEquiposPorTipo(String tipo) throws SQLException {
        List<Equipo> equipos = new ArrayList<>();
        String sql = "SELECT e.*, " +
                "CASE WHEN e.tipo = 'Desktop' THEN d.potencia_fuente ELSE l.tamano_pantalla END AS dato_especifico1, " +
                "CASE WHEN e.tipo = 'Desktop' THEN d.factor_forma ELSE l.es_touch END AS dato_especifico2, " +
                "CASE WHEN e.tipo = 'Laptop' THEN l.puertos_usb ELSE NULL END AS puertos_usb " +
                "FROM equipos e " +
                "LEFT JOIN desktops d ON e.id = d.equipo_id " +
                "LEFT JOIN laptops l ON e.id = l.equipo_id " +
                "WHERE e.tipo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Equipo equipo;
                    if ("Desktop".equals(tipo)) {
                        Desktop desktop = new Desktop();
                        desktop.setPotenciaFuente(rs.getInt("dato_especifico1"));
                        desktop.setFactorForma(rs.getString("dato_especifico2"));
                        equipo = desktop;
                    } else {
                        Laptop laptop = new Laptop();
                        laptop.setTamanoPantalla(rs.getDouble("dato_especifico1"));
                        laptop.setTouch(rs.getBoolean("dato_especifico2"));
                        laptop.setPuertosUsb(rs.getInt("puertos_usb"));
                        equipo = laptop;
                    }
                    equipo.setId(rs.getInt("id"));
                    equipo.setModelo(rs.getString("modelo"));
                    equipo.setCpu(rs.getString("cpu"));
                    equipo.setDiscoDuro(rs.getInt("disco_duro"));
                    equipo.setRam(rs.getInt("ram"));
                    equipo.setPrecio(rs.getDouble("precio"));
                    equipos.add(equipo);
                }
            }
        }
        return equipos;
    }
}
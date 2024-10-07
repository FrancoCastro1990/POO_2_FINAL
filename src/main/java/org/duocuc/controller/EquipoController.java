package org.duocuc.controller;

import org.duocuc.model.Equipo;
import org.duocuc.model.EquipoDAO;

import java.sql.SQLException;
import java.util.List;

public class EquipoController {
    private EquipoDAO equipoDAO;

    public EquipoController() {
        this.equipoDAO = new EquipoDAO();
    }

    public void registrarEquipo(Equipo equipo) throws SQLException {
        equipoDAO.guardarEquipo(equipo);
    }

    public Equipo buscarEquipo(int id) throws SQLException {
        return equipoDAO.buscarEquipoPorId(id);
    }

    public List<Equipo> listarEquipos() throws SQLException {
        return equipoDAO.listarEquipos();
    }

    public void actualizarEquipo(Equipo equipo) throws SQLException {
        equipoDAO.actualizarEquipo(equipo);
    }

    public void eliminarEquipo(int id) throws SQLException {
        equipoDAO.eliminarEquipo(id);
    }

    public List<Equipo> listarDesktops() throws SQLException {
        return equipoDAO.listarEquiposPorTipo("Desktop");
    }

    public List<Equipo> listarLaptops() throws SQLException {
        return equipoDAO.listarEquiposPorTipo("Laptop");
    }
}

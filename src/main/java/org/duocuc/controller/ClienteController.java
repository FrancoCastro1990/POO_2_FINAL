package org.duocuc.controller;

import org.duocuc.model.Cliente;
import org.duocuc.model.ClienteDAO;

import java.sql.SQLException;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public void registrarCliente(Cliente cliente) throws SQLException {
        clienteDAO.guardarCliente(cliente);
    }

    public Cliente buscarCliente(String rut) throws SQLException {
        return clienteDAO.buscarClientePorRut(rut);
    }

    public List<Cliente> listarClientes() throws SQLException {
        return clienteDAO.listarClientes();
    }

    public void actualizarCliente(Cliente cliente) throws SQLException {
        clienteDAO.actualizarCliente(cliente);
    }

    public void eliminarCliente(String rut) throws SQLException {
        clienteDAO.eliminarCliente(rut);
    }
}
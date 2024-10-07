package org.duocuc.view;

import org.duocuc.controller.ClienteController;
import org.duocuc.controller.EquipoController;
import org.duocuc.controller.VentaController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ClienteController clienteController;
    private EquipoController equipoController;
    private VentaController ventaController;

    public MainFrame() {
        super("Sistema de Ventas Computec");

        this.clienteController = new ClienteController();
        this.equipoController = new EquipoController();
        this.ventaController = new VentaController();

        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", new ClientePanel(clienteController));
        tabbedPane.addTab("Equipos", new EquipoPanel(equipoController));
        tabbedPane.addTab("Ventas", new VentaPanel(ventaController, clienteController, equipoController));
        tabbedPane.addTab("Reportes", new ReportePanel(ventaController));

        add(tabbedPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}

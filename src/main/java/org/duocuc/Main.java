package org.duocuc;

import org.duocuc.model.DatabaseConnection;
import org.duocuc.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // Inicializar la conexión a la base de datos
//        try {
//            DatabaseConnection.getConnection();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//        }

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
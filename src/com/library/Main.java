package com.library;

import com.library.dao.DatabaseManager;
import com.library.ui.LoginPage;
import javax.swing.SwingUtilities;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Test Database Connection
        new Thread(() -> {
            try (Connection conn = DatabaseManager.getConnection()) {
                if (conn != null) {
                    System.out.println("✅ CONNECTED: Successfully connected to remote MySQL at 10.10.2.33!");
                }
            } catch (Exception e) {
                System.err.println("❌ CONNECTION FAILED:");
                e.printStackTrace();
            }
        }).start();

        // Launch UI
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}
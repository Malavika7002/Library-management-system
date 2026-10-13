package com.library.ui;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    public StudentDashboard(String username) {
        setTitle("Library Management System - Student Portal");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Student Dashboard, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(welcomeLabel, BorderLayout.CENTER);
        add(panel);
    }
}
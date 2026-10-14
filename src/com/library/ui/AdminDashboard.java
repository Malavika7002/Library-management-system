package com.library.ui;

import com.library.dao.AdminDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Library Management System — Admin Dashboard");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab(" Add New Student ", createAddStudentPanel());
        tabbedPane.addTab(" Add New Admin ", createAddAdminPanel());

        add(tabbedPane);
    }

    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField admField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Admission No:"), gbc);
        gbc.gridx = 1; panel.add(admField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1; panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);

        JButton saveBtn = new JButton("Register Student");
        saveBtn.setBackground(ThemeManager.COLOR_NAVY_BG);
        saveBtn.setForeground(ThemeManager.TEXT_WHITE);
        saveBtn.setFont(ThemeManager.FONT_BOLD);

        saveBtn.addActionListener(e -> {
            String adm = admField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String pass = new String(passField.getPassword());

            if (adm.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all mandatory fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (AdminDAO.addStudent(adm, name, email, phone, pass)) {
                JOptionPane.showMessageDialog(this, "Student account registered successfully!");
                admField.setText(""); nameField.setText(""); emailField.setText(""); phoneField.setText(""); passField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create student account. ID may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        return panel;
    }

    private JPanel createAddAdminPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField adminIdField = new JTextField(15);
        JTextField nameField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Admin ID:"), gbc);
        gbc.gridx = 1; panel.add(adminIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; panel.add(passField, gbc);

        JButton saveBtn = new JButton("Register Admin");
        saveBtn.setBackground(ThemeManager.COLOR_BUTTON);
        saveBtn.setForeground(ThemeManager.TEXT_WHITE);
        saveBtn.setFont(ThemeManager.FONT_BOLD);

        saveBtn.addActionListener(e -> {
            String adminId = adminIdField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());

            if (adminId.isEmpty() || name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all mandatory fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (AdminDAO.addAdmin(adminId, name, email, pass)) {
                JOptionPane.showMessageDialog(this, "Admin account registered successfully!");
                adminIdField.setText(""); nameField.setText(""); emailField.setText(""); passField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create admin account. Admin ID may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        return panel;
    }
}
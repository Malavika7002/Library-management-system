package com.library.ui;

import com.library.dao.UserDAO;
import com.library.ui.AdminDashboard;
import com.library.student.ui.StudentDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainCardPanel;

    // Form inputs
    private JTextField studentIdField;
    private JPasswordField studentPassField;

    private JTextField adminIdField;
    private JPasswordField adminPassField;

    public LoginPage() {
        setTitle("Library Management System — Authentication");
        setSize(520, 480);
        setMinimumSize(new Dimension(450, 420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Enable maximize, restore, and minimize controls
        setResizable(true);

        // Root container with Top Header & Card Center
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(ThemeManager.COLOR_SKIN_BG);

        // Top Header (#022435)
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 18));
        headerPanel.setBackground(ThemeManager.COLOR_NAVY_BG);

        JLabel headerLabel = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        headerLabel.setForeground(ThemeManager.TEXT_WHITE);
        headerLabel.setFont(ThemeManager.FONT_HEADER);
        headerPanel.add(headerLabel);

        rootPanel.add(headerPanel, BorderLayout.NORTH);

        // Card Layout Setup for Page Switching
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setOpaque(false);

        // Add Pages to Card Panel
        mainCardPanel.add(createRoleSelectionPage(), "PAGE_ROLE_SELECTION");
        mainCardPanel.add(createStudentLoginPage(), "PAGE_STUDENT_LOGIN");
        mainCardPanel.add(createAdminLoginPage(), "PAGE_ADMIN_LOGIN");

        // Wrapper to center card panels when window is maximized
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(ThemeManager.COLOR_SKIN_BG);
        centerWrapper.add(mainCardPanel);

        rootPanel.add(centerWrapper, BorderLayout.CENTER);
        add(rootPanel);
    }

    // ==========================================
    // PAGE 1: Select Role (Student or Admin)
    // ==========================================
    private JPanel createRoleSelectionPage() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 260));
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ThemeManager.COLOR_BUTTON, 2),
                " Select Portal ", 0, 0, ThemeManager.FONT_BOLD, ThemeManager.COLOR_NAVY_BG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel promptLabel = new JLabel("Please select your account type to proceed:", SwingConstants.CENTER);
        promptLabel.setFont(ThemeManager.FONT_PLAIN);

        JButton studentRoleBtn = new JButton("Student Login");
        studentRoleBtn.setFont(ThemeManager.FONT_BOLD);
        studentRoleBtn.setBackground(ThemeManager.COLOR_BUTTON);
        studentRoleBtn.setForeground(ThemeManager.TEXT_WHITE);
        studentRoleBtn.setFocusPainted(false);
        studentRoleBtn.setPreferredSize(new Dimension(200, 40));

        JButton adminRoleBtn = new JButton("Admin Login");
        adminRoleBtn.setFont(ThemeManager.FONT_BOLD);
        adminRoleBtn.setBackground(ThemeManager.COLOR_NAVY_BG);
        adminRoleBtn.setForeground(ThemeManager.TEXT_WHITE);
        adminRoleBtn.setFocusPainted(false);
        adminRoleBtn.setPreferredSize(new Dimension(200, 40));

        // Event Handlers for Navigation
        studentRoleBtn.addActionListener(e -> cardLayout.show(mainCardPanel, "PAGE_STUDENT_LOGIN"));
        adminRoleBtn.addActionListener(e -> cardLayout.show(mainCardPanel, "PAGE_ADMIN_LOGIN"));

        gbc.gridx = 0; gbc.gridy = 0;
        card.add(promptLabel, gbc);
        gbc.gridy = 1;
        card.add(studentRoleBtn, gbc);
        gbc.gridy = 2;
        card.add(adminRoleBtn, gbc);

        return card;
    }

    // ==========================================
    // PAGE 2A: Student Login Details
    // ==========================================
    private JPanel createStudentLoginPage() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 290));
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ThemeManager.COLOR_BUTTON, 2),
                " Student Portal Login ", 0, 0, ThemeManager.FONT_BOLD, ThemeManager.COLOR_NAVY_BG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(new JLabel("Student ID / Admission No:"), gbc);
        gbc.gridx = 1;
        studentIdField = new JTextField(12);
        card.add(studentIdField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        card.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        studentPassField = new JPasswordField(12);
        card.add(studentPassField, gbc);

        // Login Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(ThemeManager.COLOR_BUTTON);
        loginBtn.setForeground(ThemeManager.TEXT_WHITE);
        loginBtn.setFont(ThemeManager.FONT_BOLD);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> {
            String admissionNo = studentIdField.getText().trim();
            String password = new String(studentPassField.getPassword());

            if (admissionNo.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both admission number and password!");
                return;
            }

            if (UserDAO.authenticateStudent(admissionNo, password)) {
                dispose();
                new StudentDashboard(admissionNo).setVisible(true); // <--- Passed admissionNo here
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admission Number or Password!");
            }
        });
        card.add(loginBtn, gbc);

        // Forgot Password
        gbc.gridy = 3;
        JButton forgotPassBtn = createLinkButton("Forgot Password?");
        forgotPassBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Password reset link sent to your registered email provided by Admin.",
                "Student Password Recovery", JOptionPane.INFORMATION_MESSAGE));
        card.add(forgotPassBtn, gbc);

        // Back to Role Selection Button
        gbc.gridy = 4;
        JButton backBtn = createLinkButton("← Back to Role Selection");
        backBtn.addActionListener(e -> cardLayout.show(mainCardPanel, "PAGE_ROLE_SELECTION"));
        card.add(backBtn, gbc);

        return card;
    }

    // ==========================================
    // PAGE 2B: Admin Login Details
    // ==========================================
    private JPanel createAdminLoginPage() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 290));
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ThemeManager.COLOR_NAVY_BG, 2),
                " Admin Portal Login ", 0, 0, ThemeManager.FONT_BOLD, ThemeManager.COLOR_NAVY_BG));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Admin ID
        gbc.gridx = 0; gbc.gridy = 0;
        card.add(new JLabel("Admin ID:"), gbc);
        gbc.gridx = 1;
        adminIdField = new JTextField(12);
        card.add(adminIdField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        card.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        adminPassField = new JPasswordField(12);
        card.add(adminPassField, gbc);

// Login Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginBtn = new JButton("Login as Admin");
        loginBtn.setBackground(ThemeManager.COLOR_NAVY_BG);
        loginBtn.setForeground(ThemeManager.TEXT_WHITE);
        loginBtn.setFont(ThemeManager.FONT_BOLD);
        loginBtn.setFocusPainted(false);

        loginBtn.addActionListener(e -> {
            String adminId = adminIdField.getText().trim();
            String password = new String(adminPassField.getPassword());

            if (adminId.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both Admin ID and Password.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (UserDAO.authenticateAdmin(adminId, password)) {
                dispose();
                new AdminDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin ID or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });
        card.add(loginBtn, gbc);

        // Forgot Password
        gbc.gridy = 3;
        JButton forgotPassBtn = createLinkButton("Forgot Password?");
        forgotPassBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Admin recovery email link sent to primary registered recovery email.",
                "Admin Password Recovery", JOptionPane.INFORMATION_MESSAGE));
        card.add(forgotPassBtn, gbc);

        // Back to Role Selection Button
        gbc.gridy = 4;
        JButton backBtn = createLinkButton("← Back to Role Selection");
        backBtn.addActionListener(e -> cardLayout.show(mainCardPanel, "PAGE_ROLE_SELECTION"));
        card.add(backBtn, gbc);

        return card;
    }

    private JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(ThemeManager.COLOR_BUTTON);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

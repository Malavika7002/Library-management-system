package com.library.ui;

import com.library.dao.AdminDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class AdminDashboard extends JFrame {

    private static final Color CARD_BG = new Color(25, 35, 60);
    private static final Color ACCENT_BLUE = new Color(52, 152, 219);
    private static final Color ACCENT_GREEN = new Color(46, 204, 113);
    private static final Color ACCENT_RED = new Color(231, 76, 60);
    private static final Color ACCENT_PURPLE = new Color(155, 89, 182);

    // Dynamic Dashboard Components for Auto-Refresh
    private JLabel availableBooksLbl;
    private JLabel booksIssuedLbl;
    private JLabel overdueReturnsLbl;
    private JLabel totalStudentsLbl;

    private DefaultTableModel issueModel;
    private DefaultTableModel alertModel;

    public AdminDashboard() {
        setTitle("Library Management System — Admin Dashboard");
        setSize(1100, 750);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(ThemeManager.COLOR_SKIN_BG);

        // --- Top Navigation Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.COLOR_NAVY_BG);
        headerPanel.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel titleLabel = new JLabel("ADMINISTRATOR PORTAL");
        titleLabel.setFont(ThemeManager.FONT_HEADER);
        titleLabel.setForeground(ThemeManager.TEXT_WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(180, 40, 40));
        logoutBtn.setForeground(ThemeManager.TEXT_WHITE);
        logoutBtn.setFont(ThemeManager.FONT_BOLD);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage().setVisible(true);
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        // --- Tabbed Navigation Container ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(ThemeManager.FONT_BOLD);

        tabbedPane.addTab(" Overview Dashboard ", createAnalyticsDashboardPanel());
        tabbedPane.addTab(" Issue Book to Student ", createIssueBookPanel());
        tabbedPane.addTab(" Add New Student ", createAddStudentPanel());
        tabbedPane.addTab(" Add New Admin ", createAddAdminPanel());

        // Auto-refresh listener when clicking back to Overview tab
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                refreshDashboard();
            }
        });

        rootPanel.add(headerPanel, BorderLayout.NORTH);
        rootPanel.add(tabbedPane, BorderLayout.CENTER);

        add(rootPanel);
    }

    // =========================================================================
    // TAB 1: Analytics Dashboard
    // =========================================================================
    private JPanel createAnalyticsDashboardPanel() {
        JPanel container = new JPanel(new BorderLayout(15, 15));
        container.setBackground(ThemeManager.COLOR_SKIN_BG);
        container.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Top Stat Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setOpaque(false);

        availableBooksLbl = new JLabel("0");
        booksIssuedLbl = new JLabel("0");
        overdueReturnsLbl = new JLabel("0");
        totalStudentsLbl = new JLabel("0");

        statsPanel.add(createStatCard("Available Books", availableBooksLbl, ACCENT_GREEN));
        statsPanel.add(createStatCard("Books Issued", booksIssuedLbl, ACCENT_BLUE));
        statsPanel.add(createStatCard("Overdue Returns", overdueReturnsLbl, ACCENT_RED));
        statsPanel.add(createStatCard("Total Students", totalStudentsLbl, ACCENT_PURPLE));

        // 2. Middle Grid Tables
        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 15, 15));
        mainGrid.setOpaque(false);

        // Recent Issues Table
        JPanel leftCard = createCardContainer("Recent Book Issues");
        String[] issueCols = {"Student ID", "Book Title", "Issue Date", "Due Date"};
        issueModel = new DefaultTableModel(issueCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable issueTable = createStyledTable(issueModel);
        leftCard.add(new JScrollPane(issueTable), BorderLayout.CENTER);

        // Alerts & Status Table
        JPanel rightCard = createCardContainer("Library Alerts & Status");
        String[] alertCols = {"Status", "Student ID", "Book Title"};
        alertModel = new DefaultTableModel(alertCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable alertTable = createStyledTable(alertModel);
        rightCard.add(new JScrollPane(alertTable), BorderLayout.CENTER);

        mainGrid.add(leftCard);
        mainGrid.add(rightCard);

        container.add(statsPanel, BorderLayout.NORTH);
        container.add(mainGrid, BorderLayout.CENTER);

        // Populate initial data
        refreshDashboard();

        return container;
    }

    private JPanel createStatCard(String title, JLabel valueLbl, Color highlightColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(highlightColor, 1),
                new EmptyBorder(12, 15, 12, 15)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        titleLbl.setForeground(Color.LIGHT_GRAY);

        valueLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLbl.setForeground(highlightColor);

        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCardContainer(String headerTitle) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 70, 95), 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel titleLbl = new JLabel(headerTitle);
        titleLbl.setFont(ThemeManager.FONT_BOLD);
        titleLbl.setForeground(ThemeManager.TEXT_WHITE);

        card.add(titleLbl, BorderLayout.NORTH);
        return card;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setForeground(Color.WHITE);
        table.setBackground(CARD_BG);
        table.setGridColor(new Color(60, 70, 95));
        table.setRowHeight(26);

        JTableHeader header = table.getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(ThemeManager.COLOR_NAVY_BG);
        header.setFont(header.getFont().deriveFont(Font.BOLD));

        return table;
    }

    public void refreshDashboard() {
        if (availableBooksLbl == null) return;

        availableBooksLbl.setText(String.valueOf(AdminDAO.getAvailableBooksCount()));
        booksIssuedLbl.setText(String.valueOf(AdminDAO.getBooksIssuedCount()));
        overdueReturnsLbl.setText(String.valueOf(AdminDAO.getOverdueReturnsCount()));
        totalStudentsLbl.setText(String.valueOf(AdminDAO.getTotalStudentsCount()));

        issueModel.setRowCount(0);
        List<Object[]> issues = AdminDAO.getRecentIssues();
        for (Object[] row : issues) issueModel.addRow(row);

        alertModel.setRowCount(0);
        List<Object[]> alerts = AdminDAO.getLibraryAlerts();
        for (Object[] row : alerts) alertModel.addRow(row);
    }

    // =========================================================================
    // TAB 2: Issue Book to Student (NEW PAGE)
    // =========================================================================
    private JPanel createIssueBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 70, 95), 1),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField admField = new JTextField(15);
        JTextField bookIdField = new JTextField(15);
        JTextField issueDateField = new JTextField(LocalDate.now().toString(), 15);
        JTextField dueDateField = new JTextField(LocalDate.now().plusDays(14).toString(), 15);

        JLabel lbl1 = new JLabel("Student Admission No (e.g. ADM001):"); lbl1.setForeground(Color.WHITE);
        JLabel lbl2 = new JLabel("Book ID (e.g. 1):"); lbl2.setForeground(Color.WHITE);
        JLabel lbl3 = new JLabel("Issue Date (YYYY-MM-DD):"); lbl3.setForeground(Color.WHITE);
        JLabel lbl4 = new JLabel("Due Date (YYYY-MM-DD):"); lbl4.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lbl1, gbc);
        gbc.gridx = 1; formPanel.add(admField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lbl2, gbc);
        gbc.gridx = 1; formPanel.add(bookIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lbl3, gbc);
        gbc.gridx = 1; formPanel.add(issueDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lbl4, gbc);
        gbc.gridx = 1; formPanel.add(dueDateField, gbc);

        JButton issueBtn = new JButton("Confirm & Issue Book");
        issueBtn.setBackground(ACCENT_GREEN);
        issueBtn.setForeground(Color.WHITE);
        issueBtn.setFont(ThemeManager.FONT_BOLD);
        issueBtn.setFocusPainted(false);

        issueBtn.addActionListener(e -> {
            String adm = admField.getText().trim();
            String bIdStr = bookIdField.getText().trim();
            String issueDate = issueDateField.getText().trim();
            String dueDate = dueDateField.getText().trim();

            if (adm.isEmpty() || bIdStr.isEmpty() || issueDate.isEmpty() || dueDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int bookId = Integer.parseInt(bIdStr);
                String result = AdminDAO.issueBook(adm, bookId, issueDate, dueDate);

                if ("SUCCESS".equals(result)) {
                    JOptionPane.showMessageDialog(this, "Book successfully issued to student " + adm + "!");
                    admField.setText("");
                    bookIdField.setText("");
                    refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Issue Book Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Book ID must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(issueBtn, gbc);

        panel.add(formPanel);
        return panel;
    }


    // =========================================================================
    // TAB 3: Add New Student
    // =========================================================================
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
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (AdminDAO.addStudent(adm, name, email, phone, pass)) {
                JOptionPane.showMessageDialog(this, "Student account registered successfully!");
                admField.setText(""); nameField.setText(""); emailField.setText(""); phoneField.setText(""); passField.setText("");
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create student account. ID may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        return panel;
    }

    // =========================================================================
    // TAB 4: Add New Admin
    // =========================================================================
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
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
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
package com.library.student.ui;

import java.awt.Color;
import javax.swing.table.JTableHeader;
import com.library.dao.BookDAO;
import com.library.student.dao.StudentDAO;
import com.library.student.model.Book;
import com.library.student.model.BorrowRecord;
import com.library.ui.ThemeManager;
import com.library.student.model.Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private String currentStudentId; // Holds the logged-in student's ID (e.g. "ADM001")
    private StudentDAO studentDAO;


    // --- Color Palette Constants ---
    private static final Color BACKGROUND_COLOR = new Color(2, 36, 53);
    private static final Color PRIMARY_COLOR = new Color(2, 36, 53);
    private static final Color TEXT_DARK = Color.WHITE; // or new Color(220, 220, 220)
    private static final Color CARD_BG = new Color(25, 35, 60);

    // --- UI Component References ---
    private JTextField searchField;
    private JComboBox<String> categoryCombo;
    private JComboBox<String> typeCombo;
    private JComboBox<String> availabilityCombo;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;


    // ADD THIS CONSTRUCTOR HEADER LINE HERE:
    public StudentDashboard(String admissionNo) {
        this.currentStudentId = admissionNo;

        setTitle("Library Management System - Student Portal");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Styled Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.WHITE);

        // Add Tabs
        tabbedPane.addTab(" Catalog & Search ", createSearchPanel());
        tabbedPane.addTab(" My Borrowed Books ", createBorrowRecordsPanel());
        tabbedPane.addTab(" My Profile ", createProfilePanel(this.currentStudentId));

        add(tabbedPane);
    } // CLOSE THE CONSTRUCTOR HERE

private JPanel createSearchPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBackground(new Color(2,36,53)); // Dark background
    panel.setBorder(new EmptyBorder(15, 15, 15, 15));

    // --- Search Filter Controls ---
    JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
    filterPanel.setOpaque(false);

    // Labels with explicit WHITE foreground
    JLabel keywordLabel = new JLabel("Keyword:");
    keywordLabel.setForeground(Color.WHITE);

    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setForeground(Color.WHITE);

    JLabel typeLabel = new JLabel("Type:");
    typeLabel.setForeground(Color.WHITE);

    // Input components
    JTextField searchField = new JTextField(15);
    JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "All", "Computer Science", "Programming", "Biography", "General", "Current Affairs"
    });
    JComboBox<String> typeCombo = new JComboBox<>(new String[]{
            "All", "Book", "Magazine", "Newspaper"
    });

    // Add controls to panel
    filterPanel.add(keywordLabel);
    filterPanel.add(searchField);
    filterPanel.add(categoryLabel);
    filterPanel.add(categoryCombo);
    filterPanel.add(typeLabel);
    filterPanel.add(typeCombo);

    // --- Table Setup ---
    String[] columns = {"ID", "Title", "Author", "Category", "Type", "Status"};
    DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable bookTable = new JTable(tableModel);

    // Style Table for White Text on Dark Navy Background
    bookTable.setForeground(Color.WHITE);
    bookTable.setBackground(new Color(25, 35, 60));
    bookTable.setGridColor(new Color(60, 70, 95));
    bookTable.setRowHeight(24);

    // Style Table Header
    JTableHeader header = bookTable.getTableHeader();
    header.setForeground(Color.WHITE);
    header.setBackground(new Color(15, 25, 45));
    header.setFont(header.getFont().deriveFont(Font.BOLD));

    JScrollPane scrollPane = new JScrollPane(bookTable);
    scrollPane.getViewport().setBackground(new Color(25, 35, 60));

    // --- Data Loading Logic ---
    Runnable loadData = () -> {
        tableModel.setRowCount(0);
        String keyword = searchField.getText();
        String category = (String) categoryCombo.getSelectedItem();
        String type = (String) typeCombo.getSelectedItem();

        List<Book> books = BookDAO.searchBooks(keyword, category, type);
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.getType(),
                    b.getStatus()
            });
        }
    };

    // --- Instant Filter Event Listeners ---
    // 1. Auto-filter on ComboBox selection changes
    categoryCombo.addActionListener(e -> loadData.run());
    typeCombo.addActionListener(e -> loadData.run());

    // 2. Auto-filter while typing in search field
    searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { loadData.run(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { loadData.run(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { loadData.run(); }
    });

    // Initial load
    loadData.run();

    panel.add(filterPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
}

private JPanel createBorrowRecordsPanel() {
    JPanel panel = new JPanel(new GridLayout(2, 1, 15, 15));
    panel.setBackground(ThemeManager.COLOR_SKIN_BG); // or new Color(2, 36, 53)
    panel.setBorder(new EmptyBorder(20, 20, 20, 20));



    // --- 1. Active Handed-Out Books Panel ---
    JPanel activePanel = new JPanel(new BorderLayout());
    activePanel.setBackground(new Color(25, 35, 60));
    activePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    "Currently Handed-Out Books",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new Font("Segoe UI", Font.BOLD, 12), Color.WHITE
            ),
            new EmptyBorder(10, 10, 10, 10)
    ));

    String[] activeCols = {"Borrow ID", "Book Title", "Borrow Date", "Due Date", "Renewals"};
    DefaultTableModel handedOutTableModel = new DefaultTableModel(activeCols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    JTable activeTable = new JTable(handedOutTableModel);
    activeTable.setForeground(Color.WHITE);
    activeTable.setBackground(new Color(25, 35, 60));
    activeTable.setGridColor(new Color(60, 70, 95));
    activeTable.setRowHeight(24);

    JTableHeader activeHeader = activeTable.getTableHeader();
    activeHeader.setForeground(Color.WHITE);
    activeHeader.setBackground(new Color(15, 25, 45));
    activeHeader.setFont(activeHeader.getFont().deriveFont(Font.BOLD));

    JScrollPane activeScroll = new JScrollPane(activeTable);
    activeScroll.getViewport().setBackground(new Color(25, 35, 60));
    activePanel.add(activeScroll, BorderLayout.CENTER);

    // --- 2. Past-Read Books History Panel ---
    JPanel pastPanel = new JPanel(new BorderLayout());
    pastPanel.setBackground(new Color(25, 35, 60));
    pastPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 1),
                    "Past-Read Books History",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION,
                    new Font("Segoe UI", Font.BOLD, 12), Color.WHITE
            ),
            new EmptyBorder(10, 10, 10, 10)
    ));

    String[] pastCols = {"Borrow ID", "Book Title", "Borrow Date", "Due Date", "Status"};
    DefaultTableModel pastReadTableModel = new DefaultTableModel(pastCols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };

    JTable pastTable = new JTable(pastReadTableModel);
    pastTable.setForeground(Color.WHITE);
    pastTable.setBackground(new Color(25, 35, 60));
    pastTable.setGridColor(new Color(60, 70, 95));
    pastTable.setRowHeight(24);

    JTableHeader pastHeader = pastTable.getTableHeader();
    pastHeader.setForeground(Color.WHITE);
    pastHeader.setBackground(new Color(15, 25, 45));
    pastHeader.setFont(pastHeader.getFont().deriveFont(Font.BOLD));

    JScrollPane pastScroll = new JScrollPane(pastTable);
    pastScroll.getViewport().setBackground(new Color(25, 35, 60));
    pastPanel.add(pastScroll, BorderLayout.CENTER);

    // --- 3. Load Data into Table Models ---
    List<Object[]> activeBooks = BookDAO.getActiveBorrowedBooks(currentStudentId);
    for (Object[] row : activeBooks) {
        handedOutTableModel.addRow(row);
    }

    List<Object[]> pastBooks = BookDAO.getPastBorrowedBooks(currentStudentId);
    for (Object[] row : pastBooks) {
        pastReadTableModel.addRow(row);
    }

    panel.add(activePanel);
    panel.add(pastPanel);

    return panel;
}

private JButton createStyledButton(String text, Color bg) {
    JButton btn = new JButton(text);
    btn.setBackground(bg);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    btn.setBorder(new EmptyBorder(8, 16, 8, 16));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    return btn;
}

private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
    label.setForeground(TEXT_DARK);
    return label;
}

private void styleInput(JComponent comp) {
    comp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    comp.setPreferredSize(new Dimension(125, 30));
    comp.setBackground(Color.WHITE);
}

private void styleTable(JTable table) {
    table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    table.setRowHeight(30);
    table.setForeground(TEXT_DARK);
    table.setGridColor(new Color(47, 72, 109));
    table.setSelectionBackground(new Color(0, 0, 0));
    table.setSelectionForeground(TEXT_DARK);

    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    table.getTableHeader().setBackground(new Color(243, 234, 224));
    table.getTableHeader().setForeground(TEXT_DARK);
    table.getTableHeader().setPreferredSize(new Dimension(0, 32));
}

private void loadSearchResults() {
    searchTableModel.setRowCount(0);
    String keyword = searchField.getText().trim();
    String category = (String) categoryCombo.getSelectedItem();
    String type = (String) typeCombo.getSelectedItem();
    int availParam = -1;
    String statusSelection = (String) availabilityCombo.getSelectedItem();
    if ("Available".equals(statusSelection)) availParam = 1;
    else if ("Checked Out".equals(statusSelection)) availParam = 0;

    List<Book> books = studentDAO.searchBooks(keyword, category, type, availParam);
    for (Book b : books) {
        searchTableModel.addRow(new Object[]{
                b.getBookId(), b.getTitle(), b.getAuthor(), b.getCategory(), b.getType(),
                b.getStatus()
        });
    }
}

    private DefaultTableModel activeTableModel;
    private DefaultTableModel pastTableModel;

    private void loadBorrowRecords() {
        if (activeTableModel == null || pastTableModel == null) return;

        activeTableModel.setRowCount(0);
        pastTableModel.setRowCount(0);

        List<Object[]> activeBooks = BookDAO.getActiveBorrowedBooks(currentStudentId);
        for (Object[] row : activeBooks) {
            activeTableModel.addRow(row);
        }

        List<Object[]> pastBooks = BookDAO.getPastBorrowedBooks(currentStudentId);
        for (Object[] row : pastBooks) {
            pastTableModel.addRow(row);
        }
    }

private JPanel createProfilePanel(String studentId) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(BACKGROUND_COLOR);
    panel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Fetch student profile details
    com.library.student.model.Student student = StudentDAO.getStudentProfile(studentId);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.anchor = GridBagConstraints.WEST;

    // Helper method or explicit formatting for White labels
    // Admission No
    gbc.gridx = 0;
    gbc.gridy = 0;
    JLabel admissionLabel = new JLabel("Admission No:");
    admissionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    admissionLabel.setForeground(Color.WHITE);
    panel.add(admissionLabel, gbc);

    gbc.gridx = 1;
    JLabel admissionValue = new JLabel(student != null ? student.getAdmissionNo() : studentId);
    admissionValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    admissionValue.setForeground(Color.WHITE);
    panel.add(admissionValue, gbc);

    // Name
    gbc.gridx = 0;
    gbc.gridy = 1;
    JLabel nameLabel = new JLabel("Name:");
    nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    nameLabel.setForeground(Color.WHITE);
    panel.add(nameLabel, gbc);

    gbc.gridx = 1;
    JLabel nameValue = new JLabel(student != null ? student.getName() : "N/A");
    nameValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    nameValue.setForeground(Color.WHITE);
    panel.add(nameValue, gbc);

    // Email
    gbc.gridx = 0;
    gbc.gridy = 2;
    JLabel emailLabel = new JLabel("Email:");
    emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    emailLabel.setForeground(Color.WHITE);
    panel.add(emailLabel, gbc);

    gbc.gridx = 1;
    JLabel emailValue = new JLabel(student != null ? student.getEmail() : "N/A");
    emailValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    emailValue.setForeground(Color.WHITE);
    panel.add(emailValue, gbc);

    // Phone
    gbc.gridx = 0;
    gbc.gridy = 3;
    JLabel phoneLabel = new JLabel("Phone Number:");
    phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    phoneLabel.setForeground(Color.WHITE);
    panel.add(phoneLabel, gbc);

    gbc.gridx = 1;
    JLabel phoneValue = new JLabel(student != null ? student.getPhoneNumber() : "N/A");
    phoneValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    phoneValue.setForeground(Color.WHITE);
    panel.add(phoneValue, gbc);

    return panel;
}
}
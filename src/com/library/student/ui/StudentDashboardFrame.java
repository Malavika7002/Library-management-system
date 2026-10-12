package com.library.student.ui;

import com.library.student.dao.StudentDAO;
import com.library.student.model.Book;
import com.library.student.model.BorrowRecord;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {
    private int currentStudentId = 101;
    private StudentDAO studentDAO;

    // --- Aesthetic Color Palette ---
    private final Color PRIMARY_COLOR = new Color(47, 72, 109);
    private final Color ACCENT_GREEN = new Color(47, 72, 109);
    private final Color ACCENT_ORANGE = new Color(47, 72, 109);
    private final Color BACKGROUND_COLOR = new Color(2, 36, 53, 255);
    private final Color CARD_BG = new Color(243, 234, 224);
    private final Color TEXT_DARK = new Color(0, 0, 0);
    private final Color BORDER_COLOR = new Color(0, 0, 0);
    private JTextField searchField;
    private JComboBox<String> categoryCombo, typeCombo, availabilityCombo;
    private JTable searchTable;
    private DefaultTableModel searchTableModel;

    private JTable activeTable, pastTable;
    private DefaultTableModel activeTableModel, pastTableModel;

    public StudentDashboardFrame() {
        studentDAO = new StudentDAO();
        setTitle("Library Management System — Student Portal");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BACKGROUND_COLOR);

        // Styled Tabbed Pane with Tab-switching color listener
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(BACKGROUND_COLOR);

        tabbedPane.addTab("  Catalog & Search  ", createSearchPanel());
        tabbedPane.addTab("  My Borrowed Books  ", createBorrowRecordsPanel());

        // Dynamic Tab Selection Colors (Active tab light blue with black text, inactive sage with white text)
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex == 0) {
                tabbedPane.setBackgroundAt(0, new Color(2, 36, 53, 255));
                tabbedPane.setForegroundAt(0, Color.BLACK);
                tabbedPane.setBackgroundAt(1, PRIMARY_COLOR);
                tabbedPane.setForegroundAt(1, Color.WHITE);
            } else {
                tabbedPane.setBackgroundAt(1, new Color(2, 36, 53, 255));
                tabbedPane.setForegroundAt(1, Color.BLACK);
                tabbedPane.setBackgroundAt(0, PRIMARY_COLOR);
                tabbedPane.setForegroundAt(0, Color.WHITE);
            }
        });
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top Filter Bar Card with Custom Categories & Newspaper Type
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));

        searchField = new JTextField(12);
        categoryCombo = new JComboBox<>(new String[]{"All", "Textbooks & Academics", "Reference Books", "Fiction & Novel", "Non-Fiction"});
        typeCombo = new JComboBox<>(new String[]{"All", "Book", "Magazine", "Newspaper"});
        availabilityCombo = new JComboBox<>(new String[]{"All", "Available", "Checked Out"});
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);

        styleInput(searchField);
        styleInput(categoryCombo);
        styleInput(typeCombo);
        styleInput(availabilityCombo);

        filterPanel.add(createLabel("Keyword:"));
        filterPanel.add(searchField);
        filterPanel.add(createLabel("Category:"));
        filterPanel.add(categoryCombo);
        filterPanel.add(createLabel("Type:"));
        filterPanel.add(typeCombo);
        filterPanel.add(createLabel("Status:"));
        filterPanel.add(availabilityCombo);
        filterPanel.add(searchButton);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Center Table inside a Card Panel
        searchTableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Category", "Type", "Status"}, 0);
        searchTable = new JTable(searchTableModel);
        styleTable(searchTable);

        JScrollPane scrollPane = new JScrollPane(searchTable);
        scrollPane.getViewport().setBackground(CARD_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton wishlistBtn = createStyledButton(" Add to Wishlist", ACCENT_GREEN);
        JButton preBookBtn = createStyledButton("Pre-Book Item", ACCENT_ORANGE);

        actionPanel.add(wishlistBtn);
        actionPanel.add(preBookBtn);
        panel.add(actionPanel, BorderLayout.SOUTH);

        // Action Listeners
        searchButton.addActionListener(e -> loadSearchResults());

        wishlistBtn.addActionListener(e -> {
            int selectedRow = searchTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) searchTableModel.getValueAt(selectedRow, 0);
                boolean success = studentDAO.addToWishlist(currentStudentId, bookId);
                JOptionPane.showMessageDialog(this, success ? "Added to Wishlist successfully!" : "Error adding item.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item from the table first.");
            }
        });

        preBookBtn.addActionListener(e -> {
            int selectedRow = searchTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) searchTableModel.getValueAt(selectedRow, 0);
                boolean success = studentDAO.preBookItem(currentStudentId, bookId);
                JOptionPane.showMessageDialog(this, success ? "Pre-booking successful!" : "Error reserving item.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item from the table first.");
            }
        });

        loadSearchResults();
        return panel;
    }

    private JPanel createBorrowRecordsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Active Handed-Out Books Table Panel
        JPanel activePanel = new JPanel(new BorderLayout());
        activePanel.setBackground(CARD_BG);
        activePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true), " Currently Handed-Out Books ",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12), PRIMARY_COLOR
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        activeTableModel = new DefaultTableModel(new String[]{"Borrow ID", "Book Title", "Borrow Date", "Due Date", "Renewals"}, 0);
        activeTable = new JTable(activeTableModel);
        styleTable(activeTable);

        JScrollPane activeScroll = new JScrollPane(activeTable);
        activeScroll.getViewport().setBackground(CARD_BG);
        activePanel.add(activeScroll, BorderLayout.CENTER);

        JButton renewBtn = createStyledButton(" Request Renewal (Max 3)", PRIMARY_COLOR);
        JPanel renewWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        renewWrapper.setBackground(CARD_BG);
        renewWrapper.add(renewBtn);
        activePanel.add(renewWrapper, BorderLayout.SOUTH);

        // Past-Read Books Table Panel
        JPanel pastPanel = new JPanel(new BorderLayout());
        pastPanel.setBackground(CARD_BG);
        pastPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1, true), " Past-Read Books History ",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12), new Color(47, 72, 109)
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        pastTableModel = new DefaultTableModel(new String[]{"Borrow ID", "Book Title", "Borrow Date", "Due Date", "Status"}, 0);
        pastTable = new JTable(pastTableModel);
        styleTable(pastTable);

        JScrollPane pastScroll = new JScrollPane(pastTable);
        pastScroll.getViewport().setBackground(CARD_BG);
        pastPanel.add(pastScroll, BorderLayout.CENTER);

        panel.add(activePanel);
        panel.add(pastPanel);

        renewBtn.addActionListener(e -> {
            int selectedRow = activeTable.getSelectedRow();
            if (selectedRow != -1) {
                int borrowId = (int) activeTableModel.getValueAt(selectedRow, 0);
                boolean success = studentDAO.renewBook(borrowId);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Book successfully renewed for 14 days!");
                    loadBorrowRecords();
                } else {
                    JOptionPane.showMessageDialog(this, "Renewal failed! Maximum limit of 3 renewals reached.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an active item to renew.");
            }
        });

        loadBorrowRecords();
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
                    b.getAvailabilityStatus() == 1 ? "Available" : "Checked Out"
            });
        }
    }

    private void loadBorrowRecords() {
        activeTableModel.setRowCount(0);
        pastTableModel.setRowCount(0);

        List<BorrowRecord> activeBooks = studentDAO.getBorrowRecords(currentStudentId, "Active");
        for (BorrowRecord r : activeBooks) {
            activeTableModel.addRow(new Object[]{r.getBorrowId(), r.getBookTitle(), r.getBorrowDate(), r.getDueDate(), r.getRenewalCount()});
        }

        List<BorrowRecord> pastBooks = studentDAO.getBorrowRecords(currentStudentId, "Returned");
        for (BorrowRecord r : pastBooks) {
            pastTableModel.addRow(new Object[]{r.getBorrowId(), r.getBookTitle(), r.getBorrowDate(), r.getDueDate(), r.getStatus()});
        }
    }
}
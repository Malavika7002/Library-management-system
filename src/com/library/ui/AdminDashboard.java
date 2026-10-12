package com.library.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentArea;

    public AdminDashboard() {
        setTitle("Library Management System — Admin Portal");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Root Container
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(ThemeManager.COLOR_SKIN_BG);

        // Top Header (#022435)
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(ThemeManager.COLOR_NAVY_BG);
        topHeader.setPreferredSize(new Dimension(1200, 50));
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JLabel brandLabel = new JLabel("Library Management System");
        brandLabel.setFont(ThemeManager.FONT_HEADER);
        brandLabel.setForeground(ThemeManager.TEXT_WHITE);

        JLabel timeLabel = new JLabel("Admin Control Center");
        timeLabel.setFont(ThemeManager.FONT_PLAIN);
        timeLabel.setForeground(new Color(200, 215, 230));

        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        headerLeft.setOpaque(false);
        headerLeft.add(brandLabel);
        headerLeft.add(timeLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(ThemeManager.COLOR_BUTTON);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(ThemeManager.FONT_BOLD);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            LoginPage loginPage = new LoginPage();
            if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                loginPage.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
            loginPage.setVisible(true);
            dispose();
        });

        topHeader.add(headerLeft, BorderLayout.WEST);
        topHeader.add(logoutBtn, BorderLayout.EAST);
        root.add(topHeader, BorderLayout.NORTH);

        // Left Sidebar Navigation
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // Central Dynamic Content Area (CardLayout)
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(ThemeManager.COLOR_SKIN_BG);

        // Add Pages
        contentArea.add(createDashboardTab(), "NAV_DASHBOARD");
        contentArea.add(createBookDetailsTab(), "NAV_BOOKS");
        contentArea.add(createStudentDetailsTab(), "NAV_STUDENTS");
        contentArea.add(createNewAdminTab(), "NAV_NEW_ADMIN");
        contentArea.add(createSettingsTab(), "NAV_SETTINGS");

        root.add(contentArea, BorderLayout.CENTER);
        add(root);
    }

    // Left Navigation Column
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(ThemeManager.COLOR_NAVY_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        String[] menuItems = {
                "Dashboard", "NAV_DASHBOARD",
                "Book Details", "NAV_BOOKS",
                "Student Details", "NAV_STUDENTS",
                "New Admin", "NAV_NEW_ADMIN",
                "Settings", "NAV_SETTINGS"
        };

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < menuItems.length; i += 2) {
            String label = menuItems[i];
            String cardName = menuItems[i + 1];

            JToggleButton btn = new JToggleButton(label);
            btn.setMaximumSize(new Dimension(180, 42));
            btn.setFont(ThemeManager.FONT_BOLD);
            btn.setForeground(Color.WHITE);
            btn.setBackground(ThemeManager.COLOR_NAVY_BG);
            btn.setFocusPainted(false);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

            if (i == 0) btn.setSelected(true);

            btn.addActionListener(e -> cardLayout.show(contentArea, cardName));
            group.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        return sidebar;
    }

    // ========================================================
    // Dashboard Tab Matching Design Screen Layout
    // ========================================================
    private JPanel createDashboardTab() {
        JPanel dashboard = new JPanel(new GridBagLayout());
        dashboard.setBackground(ThemeManager.COLOR_SKIN_BG);
        dashboard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.BOTH;

        // TOP STATS ROW (Available Books, Books Issued, Overdue Returns, New Books)
        JPanel statsRow = new JPanel(new GridLayout(1, 4, 10, 10));
        statsRow.setOpaque(false);
        statsRow.add(createStatCard("Available Books", "5,846"));
        statsRow.add(createStatCard("Books Issued", "200"));
        statsRow.add(createStatCard("Overdue Returns", "25"));
        statsRow.add(createStatCard("New Books", "540"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0; gbc.weighty = 0.15;
        dashboard.add(statsRow, gbc);

        // LEFT COLUMN (Recent Issues & Library Alerts)
        JPanel leftCol = new JPanel(new GridLayout(2, 1, 10, 10));
        leftCol.setOpaque(false);

        // Recent Issues Table Card
        JPanel recentIssuesCard = createCardPanel("Recent Issues");
        String[] issueCols = {"Member ID", "Book ID", "Issue Date", "Return Date"};
        Object[][] issueData = {
                {"#586475", "PAM-TXT-0099", "14/04/2026", "08/05/2026"},
                {"#841234", "ENG-TXT-0102", "24/04/2026", "08/05/2026"},
                {"#457821", "EEE-409-0300", "24/04/2026", "08/05/2026"},
                {"#564233", "CSE-LAB-0021", "24/04/2026", "08/05/2026"}
        };
        recentIssuesCard.add(createStyledTable(issueData, issueCols), BorderLayout.CENTER);

        // Library Alerts Table Card
        JPanel alertsCard = createCardPanel("Library Alerts");
        String[] alertCols = {"Returns", "Member ID", "Book ID", "Members"};
        Object[][] alertData = {
                {"Today", "#893467", "PHY-TXT-0042", "Student"},
                {"Today", "#457560", "ENG-TXT-0023", "Student"},
                {"Overdue", "#784511", "DBA-TXT-0505", "Student"},
                {"Recent", "#982476", "ECE-TXT-0100", "Professor"}
        };
        alertsCard.add(createStyledTable(alertData, alertCols), BorderLayout.CENTER);

        leftCol.add(recentIssuesCard);
        leftCol.add(alertsCard);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.weighty = 0.85;
        dashboard.add(leftCol, gbc);

        // RIGHT COLUMN (Weekly Books Analytics & Events/Reservations)
        JPanel rightCol = new JPanel(new GridLayout(2, 1, 10, 10));
        rightCol.setOpaque(false);

        // Weekly Books Analytics Donut Chart
        JPanel chartCard = createCardPanel("Weekly Books Analytics");
        chartCard.add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 4;
                int cy = getHeight() / 2 - 60;
                int size = 120;

                // Donut segments using theme colors
                g2d.setColor(ThemeManager.COLOR_NAVY_BG);
                g2d.fillArc(cx, cy, size, size, 0, 170);
                g2d.setColor(ThemeManager.COLOR_BUTTON);
                g2d.fillArc(cx, cy, size, size, 170, 90);
                g2d.setColor(new Color(0xB8, 0x86, 0x0B));
                g2d.fillArc(cx, cy, size, size, 260, 60);
                g2d.setColor(new Color(0xB2, 0x22, 0x22));
                g2d.fillArc(cx, cy, size, size, 320, 40);

                // Cutout inner circle (Donut shape)
                g2d.setColor(Color.WHITE);
                g2d.fillOval(cx + 30, cy + 30, 60, 60);

                // Legend Text
                int lx = cx + size + 30;
                int ly = cy + 20;

                g2d.setFont(ThemeManager.FONT_PLAIN);
                drawLegendItem(g2d, lx, ly, ThemeManager.COLOR_NAVY_BG, "48% Books Issued");
                drawLegendItem(g2d, lx, ly + 25, ThemeManager.COLOR_BUTTON, "25% New Books Arrived");
                drawLegendItem(g2d, lx, ly + 50, new Color(0xB8, 0x86, 0x0B), "32% Book Overdue");
                drawLegendItem(g2d, lx, ly + 75, new Color(0xB2, 0x22, 0x22), "5% Books Lost");
            }

            private void drawLegendItem(Graphics2D g2, int x, int y, Color color, String text) {
                g2.setColor(color);
                g2.fillRect(x, y, 12, 12);
                g2.setColor(ThemeManager.COLOR_NAVY_BG);
                g2.drawString(text, x + 20, y + 11);
            }
        }, BorderLayout.CENTER);

        // Events & Reservations Card
        JPanel eventsCard = createCardPanel("Events & Reservations");
        String[] eventCols = {"Event / Reserve", "Details", "Timing / Status"};
        Object[][] eventData = {
                {"Campus Book Club", "Meetup on 25/04", "2:30 PM to 3:30 PM"},
                {"#543845 Reserved", "War and Peace", "To be returned on 25/04"},
                {"#875430 Reserved", "The Republic by Plato", "To be returned on 04/05"}
        };
        eventsCard.add(createStyledTable(eventData, eventCols), BorderLayout.CENTER);

        rightCol.add(chartCard);
        rightCol.add(eventsCard);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.5; gbc.weighty = 0.85;
        dashboard.add(rightCol, gbc);

        return dashboard;
    }

    // Helper Panel for Card Structure
    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ThemeManager.COLOR_BUTTON, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel header = new JLabel(title);
        header.setFont(ThemeManager.FONT_BOLD);
        header.setForeground(ThemeManager.COLOR_NAVY_BG);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        card.add(header, BorderLayout.NORTH);
        return card;
    }

    // Custom Styled Table matching Reference Palette
    private JScrollPane createStyledTable(Object[][] data, String[] cols) {
        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setFont(ThemeManager.FONT_PLAIN);
        table.setRowHeight(24);
        table.getTableHeader().setFont(ThemeManager.FONT_BOLD);
        table.getTableHeader().setBackground(ThemeManager.COLOR_NAVY_BG);
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        return scrollPane;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(ThemeManager.COLOR_BUTTON, 1));

        JLabel tLabel = new JLabel(title, SwingConstants.CENTER);
        tLabel.setFont(ThemeManager.FONT_BOLD);

        JLabel vLabel = new JLabel(value, SwingConstants.CENTER);
        vLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        vLabel.setForeground(ThemeManager.COLOR_NAVY_BG);

        card.add(tLabel);
        card.add(vLabel);
        return card;
    }

    // Other Navigation Sub-Pages
    private JPanel createBookDetailsTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.add(new JLabel("Book Details Content"));
        return panel;
    }

    private JPanel createStudentDetailsTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.add(new JLabel("Student Details Content"));
        return panel;
    }

    private JPanel createNewAdminTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);
        panel.add(new JLabel("New Admin Registration Content"));
        return panel;
    }

    private JPanel createSettingsTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBackground(ThemeManager.COLOR_SKIN_BG);

        JCheckBox darkThemeToggle = new JCheckBox("Enable Dark Mode Theme");
        darkThemeToggle.setFont(ThemeManager.FONT_BOLD);
        darkThemeToggle.setBackground(ThemeManager.COLOR_SKIN_BG);
        darkThemeToggle.addActionListener(e -> {
            ThemeManager.setDarkMode(darkThemeToggle.isSelected());
            SwingUtilities.updateComponentTreeUI(this);
        });

        panel.add(darkThemeToggle);
        return panel;
    }
}


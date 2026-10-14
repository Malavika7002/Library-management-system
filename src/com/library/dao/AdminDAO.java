package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // Issue/Borrow a new book to a student
    // Issue/Borrow a new book to a student
// Issue/Borrow a new book to a student
    // Issue/Borrow a new book to a student
    public static String issueBook(String admissionNo, int bookId, String borrowDateStr, String dueDateStr) {
        String insertSql = "INSERT INTO borrow_records (admission_no, book_id, borrow_date, due_date, renewals_left, status) VALUES (?, ?, ?, ?, 3, 'Borrowed')";
        String updateBookSql = "UPDATE books SET status = 'Borrowed' WHERE book_id = ?";

        try (Connection conn = DatabaseManager.getConnection()) {
            if (conn == null) {
                return "Database connection error.";
            }

            // 1. Verify Student Exists
            String checkStudent = "SELECT admission_no FROM students WHERE admission_no = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStudent)) {
                checkStmt.setString(1, admissionNo);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Student Admission No '" + admissionNo + "' does not exist in database.";
                }
            }

            // 2. Verify Book Exists
            String checkBook = "SELECT book_id, status FROM books WHERE book_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkBook)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    return "Book ID '" + bookId + "' does not exist in database.";
                }
            }

            // Parse Date Strings
            java.sql.Date borrowDate = java.sql.Date.valueOf(borrowDateStr);
            java.sql.Date dueDate = java.sql.Date.valueOf(dueDateStr);

            // 3. Insert Record & Update Book Status
            try (PreparedStatement stmt1 = conn.prepareStatement(insertSql);
                 PreparedStatement stmt2 = conn.prepareStatement(updateBookSql)) {

                stmt1.setString(1, admissionNo);
                stmt1.setInt(2, bookId);
                stmt1.setDate(3, borrowDate);
                stmt1.setDate(4, dueDate);
                int rowsInserted = stmt1.executeUpdate();

                stmt2.setInt(1, bookId);
                stmt2.executeUpdate();

                if (rowsInserted > 0) {
                    return "SUCCESS";
                }
            }
        } catch (IllegalArgumentException e) {
            return "Invalid Date Format! Use YYYY-MM-DD.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "SQL Error: " + e.getMessage();
        }
        return "Unknown error occurred.";
    }

    // Update borrow record details (Due Date, Renewal Count, and Status)
    public static boolean updateBorrowRecord(int borrowId, String newDueDate, int renewalsLeft, String status) {
        String sql = "UPDATE borrow_records SET due_date = ?, renewals_left = ?, status = ? WHERE borrow_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newDueDate);
            stmt.setInt(2, renewalsLeft);
            stmt.setString(3, status);
            stmt.setInt(4, borrowId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register a new Student account
    public static boolean addStudent(String admissionNo, String name, String email, String phone, String password) {
        String sql = "INSERT INTO students (admission_no, name, email, phone_number, password_hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admissionNo);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register a new Admin account
    public static boolean addAdmin(String adminId, String name, String email, String password) {
        String sql = "INSERT INTO admin (admin_id, name, email, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminId);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, password);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- Stats Counters ---
    public static int getAvailableBooksCount() {
        return getCountQuery("SELECT COUNT(*) FROM books WHERE status = 'Available'");
    }

    public static int getBooksIssuedCount() {
        return getCountQuery("SELECT COUNT(*) FROM borrow_records WHERE status = 'Borrowed'");
    }

    public static int getOverdueReturnsCount() {
        return getCountQuery("SELECT COUNT(*) FROM borrow_records WHERE status = 'Borrowed' AND due_date < CURDATE()");
    }

    public static int getTotalStudentsCount() {
        return getCountQuery("SELECT COUNT(*) FROM students");
    }

    private static int getCountQuery(String sql) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // --- Recent Issues List ---
    public static List<Object[]> getRecentIssues() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT br.admission_no, b.title, br.borrow_date, br.due_date " +
                "FROM borrow_records br JOIN books b ON br.book_id = b.book_id " +
                "ORDER BY br.borrow_id DESC LIMIT 6";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("admission_no"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- Library Alerts List ---
    public static List<Object[]> getLibraryAlerts() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT br.status, br.admission_no, b.title " +
                "FROM borrow_records br JOIN books b ON br.book_id = b.book_id " +
                "ORDER BY br.borrow_id DESC LIMIT 6";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("status"),
                        rs.getString("admission_no"),
                        rs.getString("title")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
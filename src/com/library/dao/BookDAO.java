package com.library.dao;

import com.library.student.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static List<Book> searchBooks(String keyword, String category, String itemType) {
        List<Book> books = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE 1=1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(title) LIKE ? OR LOWER(author) LIKE ?)");
        }
        if (category != null && !category.equals("All")) {
            sql.append(" AND category = ?");
        }
        if (itemType != null && !itemType.equals("All")) {
            sql.append(" AND type = ?"); // Changed from item_type to type
        }

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.toLowerCase() + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            if (category != null && !category.equals("All")) {
                stmt.setString(paramIndex++, category);
            }
            if (itemType != null && !itemType.equals("All")) {
                stmt.setString(paramIndex++, itemType);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("type"),
                        rs.getString("status")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
    // Fetch Currently Handed-Out Books
    public static List<Object[]> getActiveBorrowedBooks(String admissionNo) {
        List<Object[]> records = new ArrayList<>();
        String sql = "SELECT br.borrow_id, b.title, br.borrow_date, br.due_date, br.renewals_left " +
                "FROM borrow_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.admission_no = ? AND br.status = 'Borrowed'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admissionNo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new Object[]{
                        rs.getInt("borrow_id"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getInt("renewals_left")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // Fetch Past-Read Books History
    public static List<Object[]> getPastBorrowedBooks(String admissionNo) {
        List<Object[]> records = new ArrayList<>();
        String sql = "SELECT br.borrow_id, b.title, br.borrow_date, br.due_date, br.status " +
                "FROM borrow_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.admission_no = ? AND br.status != 'Borrowed'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admissionNo);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new Object[]{
                        rs.getInt("borrow_id"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
package com.library.student.dao;
import com.library.student.model.Book;
import com.library.student.model.BorrowRecord;
import com.library.dao.DatabaseManager;
import com.library.student.model.Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
public class StudentDAO {
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "password");
    }

    public List<Book> searchBooks(String keyword, String category, String type, int availability) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE (title LIKE ? OR author LIKE ?) " +
                "AND (? = 'All' OR category = ?) " +
                "AND (? = 'All' OR type = ?) " +
                "AND (? = -1 OR availability_status = ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, category);
            pstmt.setString(4, category);
            pstmt.setString(5, type);
            pstmt.setString(6, type);
            pstmt.setInt(7, availability);
            pstmt.setInt(8, availability);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getString("type"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<BorrowRecord> getBorrowRecords(int studentId, String status) {
        List<BorrowRecord> records = new ArrayList<>();
        String query = "SELECT br.*, b.title FROM borrow_records br " +
                "JOIN books b ON br.book_id = b.book_id " +
                "WHERE br.student_id = ? AND br.status = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(new BorrowRecord(
                        rs.getInt("borrow_id"),
                        rs.getString("student_id"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getInt("renewal_count"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public boolean renewBook(int borrowId) {
        String checkQuery = "SELECT renewal_count FROM borrow_records WHERE borrow_id = ?";
        String updateQuery = "UPDATE borrow_records SET due_date = DATE_ADD(due_date, INTERVAL 14 DAY), renewal_count = renewal_count + 1 WHERE borrow_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, borrowId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int renewals = rs.getInt("renewal_count");
                if (renewals >= 3) return false;

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, borrowId);
                    updateStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addToWishlist(int studentId, int bookId) {
        String query = "INSERT INTO wishlist (student_id, book_id) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean preBookItem(int studentId, int bookId) {
        String query = "INSERT INTO reservations (book_id, student_id, status) VALUES (?, ?, 'Active')";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Student getStudentProfile(String admissionNo) {
        String sql = "SELECT * FROM students WHERE admission_no = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admissionNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getString("admission_no"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean updatePassword(String studentId, String newPassword) {
        String sql = "UPDATE students SET password = ? WHERE admission_no = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, studentId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDAO {

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
}

package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Authenticate Student
    public static boolean authenticateStudent(String admissionNo, String password) {
        String sql = "SELECT password_hash FROM students WHERE admission_no = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admissionNo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                return password.equals(storedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Authenticate Admin
    public static boolean authenticateAdmin(String adminId, String password) {
        String sql = "SELECT password_hash FROM admin WHERE admin_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adminId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                return password.equals(storedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
package com.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    // Her Wi-Fi IP address from earlier step
    private static final String HOST_IP = "10.10.2.33";
    private static final String PORT = "3306";
    private static final String DB_NAME = "library_db";

    private static final String URL = "jdbc:mysql://" + HOST_IP + ":" + PORT + "/" + DB_NAME +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "lib_user";
    private static final String PASSWORD = "your_password"; // Replace with password set in MySQL

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
            return null;
        }
    }
}
package com.petfoodmonitoring.app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    //3 fields
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "tesda_project";
    private static final String URL = SERVER_URL + DB_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String LEGACY_DRIVER = "com.mysql.jdbc.Driver";
    private static boolean connectionMessageShown = false;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            try {
                Class.forName(LEGACY_DRIVER);
            } catch (ClassNotFoundException legacyException) {
                System.out.println("MySQL JDBC driver not found. Please add the MySQL Connector/J library.");
            }
        }
    }

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (!connectionMessageShown) {
                System.out.println("Database Connected Successfully!");
                connectionMessageShown = true;
            }

            return conn;
        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean createDatabaseIfNotExists() {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database checked/created successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to create database: " + e.getMessage());
            return false;
        }
    }
}
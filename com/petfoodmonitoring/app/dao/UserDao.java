package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public boolean register(User user) {
        if (emailExists(user.getEmail())) {
            System.out.println("Email is already registered.");
            return false;
        }

        return addUser(user);
    }

    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Login failed: " + e.getMessage());
        }

        return null;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, email);

            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to check email: " + e.getMessage());
            return true;
        }
    }

    public boolean addUser(User user) {
        String sql = "INSERT INTO users(first_name, last_name, email, password, phone_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getPhoneNumber());

            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add user: " + e.getMessage());
            return false;
        }
    }

    public void viewUsers() {
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\n========== USERS ==========");

            while (rs.next()) {
                printUser(rs);
                System.out.println("------------------------------");
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view users: " + e.getMessage());
        }
    }

    public void searchUser(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n========== USER FOUND ==========");
                    printUser(rs);
                } else {
                    System.out.println("\nNo user found with ID " + id);
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to search user: " + e.getMessage());
        }
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, password=?, phone_number=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getFirstName());
            pst.setString(2, user.getLastName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getPhoneNumber());
            pst.setInt(6, user.getId());

            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            handleDeleteError(e);
            return false;
        }
    }

    private void printUser(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("First Name: " + rs.getString("first_name"));
        System.out.println("Last Name: " + rs.getString("last_name"));
        System.out.println("Email: " + rs.getString("email"));
        System.out.println("Phone: " + rs.getString("phone_number"));
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone_number")
        );
    }

    private void handleDeleteError(Exception e) {
        String message = e.getMessage();

        if (message != null && message.contains("foreign key constraint")) {
            System.out.println("\n======================================");
            System.out.println("Cannot delete this user.");
            System.out.println("Reason:");
            System.out.println("This user still has registered pets.");
            System.out.println("Please delete or transfer the pets first.");
            System.out.println("======================================");
        } else {
            System.out.println("Database Error: " + message);
        }
    }
}

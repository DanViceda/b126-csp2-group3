package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FeedingHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedingHistoryDao {

    public boolean addHistory(FeedingHistory history) {
        String sql = "INSERT INTO feeding_history(schedule_id, feeding_date, feeding_time, status, remarks) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, history.getScheduleId());
            pst.setDate(2, history.getFeedingDate());
            pst.setString(3, history.getFeedingTime());
            pst.setString(4, history.getStatus());
            pst.setString(5, history.getRemarks());
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add feeding history: " + e.getMessage());
            return false;
        }
    }

    public void viewHistory(int userId) {
        String sql = "SELECT h.*, p.pet_name, f.food_name "
                + "FROM feeding_history h "
                + "LEFT JOIN schedule s ON h.schedule_id = s.id "
                + "LEFT JOIN pets p ON s.pet_id = p.id "
                + "LEFT JOIN food f ON s.food_id = f.id "
                + "WHERE p.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("\n========== FEEDING HISTORY ==========");
                while (rs.next()) {
                    printHistory(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view feeding history: " + e.getMessage());
        }
    }

    private void printHistory(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Pet: " + rs.getString("pet_name"));
        System.out.println("Food: " + rs.getString("food_name"));
        System.out.println("Feeding Date: " + rs.getDate("feeding_date"));
        System.out.println("Feeding Time: " + rs.getString("feeding_time"));
        System.out.println("Status: " + rs.getString("status"));
        System.out.println("Remarks: " + rs.getString("remarks"));
        System.out.println("------------------------------");
    }
}

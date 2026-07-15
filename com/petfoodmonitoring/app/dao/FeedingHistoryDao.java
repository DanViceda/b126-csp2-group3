package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FeedingHistory;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.TablePrinter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            ConsoleHelper.error("Failed to add feeding history: " + e.getMessage());
            return false;
        }
    }

    public void viewHistory(int userId) {
        String sql = "SELECT h.*, p.pet_name, f.food_name "
                + "FROM feeding_history h "
                + "LEFT JOIN schedule s ON h.schedule_id = s.id "
                + "LEFT JOIN pets p ON s.pet_id = p.id "
                + "LEFT JOIN food f ON s.food_id = f.id "
                + "WHERE p.user_id = ? AND f.user_id = ? ORDER BY h.feeding_date DESC, h.id DESC";
        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("pet_name"),
                        rs.getString("food_name"),
                        String.valueOf(rs.getDate("feeding_date")),
                        rs.getString("feeding_time"),
                        rs.getString("status"),
                        rs.getString("remarks")
                    });
                }
            }

            ConsoleHelper.header("FEEDING HISTORY");
            TablePrinter.print(new String[]{"ID", "Pet", "Food", "Date", "Time", "Status", "Remarks"}, rows);
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to view feeding history: " + e.getMessage());
        }
    }
}

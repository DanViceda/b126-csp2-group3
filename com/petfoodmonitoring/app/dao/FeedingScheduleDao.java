package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FeedingSchedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedingScheduleDao {

    public boolean addSchedule(FeedingSchedule schedule) {
        String sql = "INSERT INTO schedule(pet_id, food_id, feeding_time, quantity, frequency) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setScheduleValues(pst, schedule);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add schedule: " + e.getMessage());
            return false;
        }
    }

    public void viewSchedules(int userId) {
        String sql = "SELECT s.*, p.pet_name, f.food_name "
                + "FROM schedule s "
                + "LEFT JOIN pets p ON s.pet_id = p.id "
                + "LEFT JOIN food f ON s.food_id = f.id "
                + "WHERE p.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("\n========== FEEDING SCHEDULES ==========");
                while (rs.next()) {
                    printSchedule(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view schedules: " + e.getMessage());
        }
    }

    public boolean updateSchedule(FeedingSchedule schedule, int userId) {
        String sql = "UPDATE schedule s JOIN pets p ON s.pet_id = p.id "
                + "SET s.pet_id=?, s.food_id=?, s.feeding_time=?, s.quantity=?, s.frequency=? "
                + "WHERE s.id=? AND p.user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setScheduleValues(pst, schedule);
            pst.setInt(6, schedule.getId());
            pst.setInt(7, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update schedule: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteSchedule(int id, int userId) {
        String sql = "DELETE s FROM schedule s JOIN pets p ON s.pet_id = p.id WHERE s.id = ? AND p.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to delete schedule: " + e.getMessage());
            return false;
        }
    }

    private void setScheduleValues(PreparedStatement pst, FeedingSchedule schedule) throws SQLException {
        pst.setInt(1, schedule.getPetId());
        pst.setInt(2, schedule.getFoodId());
        pst.setString(3, schedule.getFeedingTime());
        pst.setDouble(4, schedule.getQuantity());
        pst.setString(5, schedule.getFrequency());
    }

    private void printSchedule(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Pet: " + rs.getString("pet_name"));
        System.out.println("Food: " + rs.getString("food_name"));
        System.out.println("Feeding Time: " + rs.getString("feeding_time"));
        System.out.println("Quantity: " + rs.getDouble("quantity"));
        System.out.println("Frequency: " + rs.getString("frequency"));
        System.out.println("------------------------------");
    }
}

package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FeedingSchedule;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.TablePrinter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FeedingScheduleDao {

    public boolean addSchedule(FeedingSchedule schedule) {
        String sql = "INSERT INTO schedule(pet_id, food_id, feeding_time, quantity, frequency) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setScheduleValues(pst, schedule);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to add schedule: " + e.getMessage());
            return false;
        }
    }

    public void viewSchedules(int userId) {
        String sql = "SELECT s.*, p.pet_name, f.food_name "
                + "FROM schedule s "
                + "LEFT JOIN pets p ON s.pet_id = p.id "
                + "LEFT JOIN food f ON s.food_id = f.id "
                + "WHERE p.user_id = ? ORDER BY s.id";
        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("pet_name"),
                        rs.getString("food_name"),
                        rs.getString("feeding_time"),
                        String.format("%.2f", rs.getDouble("quantity")),
                        rs.getString("frequency")
                    });
                }
            }

            ConsoleHelper.header("FEEDING SCHEDULES");
            TablePrinter.print(new String[]{"ID", "Pet", "Food", "Time", "Quantity", "Frequency"}, rows);
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to view schedules: " + e.getMessage());
        }
    }

    public FeedingSchedule findScheduleById(int id, int userId) {
        String sql = "SELECT s.* FROM schedule s JOIN pets p ON s.pet_id = p.id WHERE s.id = ? AND p.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapSchedule(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to find schedule: " + e.getMessage());
        }

        return null;
    }

    public String getPetNameForSchedule(int scheduleId, int userId) {
        String sql = "SELECT p.pet_name FROM schedule s JOIN pets p ON s.pet_id = p.id WHERE s.id = ? AND p.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, scheduleId);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("pet_name");
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to read schedule pet: " + e.getMessage());
        }

        return "Unknown Pet";
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
            ConsoleHelper.error("Failed to update schedule: " + e.getMessage());
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
            ConsoleHelper.error("Failed to delete schedule: " + e.getMessage());
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

    private FeedingSchedule mapSchedule(ResultSet rs) throws SQLException {
        return new FeedingSchedule(
                rs.getInt("id"),
                rs.getString("feeding_time"),
                rs.getDouble("quantity"),
                rs.getString("frequency"),
                rs.getInt("pet_id"),
                rs.getInt("food_id")
        );
    }
}

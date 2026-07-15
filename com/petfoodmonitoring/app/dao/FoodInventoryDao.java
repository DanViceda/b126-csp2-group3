package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FoodInventory;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.TablePrinter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FoodInventoryDao {

    public boolean addInventory(FoodInventory inventory, int userId) {
        String sql = "INSERT INTO inventory(food_id, quantity_available, unit, last_updated) "
                + "SELECT ?, ?, ?, ? FROM food WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, inventory.getFoodId());
            pst.setDouble(2, inventory.getQuantityAvailable());
            pst.setString(3, inventory.getUnit());
            pst.setTimestamp(4, inventory.getLastUpdated());
            pst.setInt(5, inventory.getFoodId());
            pst.setInt(6, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to add inventory: " + e.getMessage());
            return false;
        }
    }

    public void viewInventory(int userId) {
        String sql = "SELECT i.*, f.food_name, f.brand FROM inventory i "
                + "JOIN food f ON i.food_id = f.id "
                + "WHERE f.user_id = ? ORDER BY i.id";
        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                            String.valueOf(rs.getInt("id")),
                            rs.getString("food_name"),
                            formatQuantity(rs.getDouble("quantity_available"), rs.getString("unit")),
                            rs.getString("unit"),
                            rs.getString("brand")
                    });
                }
            }

            ConsoleHelper.header("FOOD INVENTORY");
            TablePrinter.print(new String[]{"ID", "Food Name", "Quantity", "Unit", "Brand"}, rows);
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to view inventory: " + e.getMessage());
        }
    }

    public FoodInventory findInventoryById(int id, int userId) {
        String sql = "SELECT i.* FROM inventory i JOIN food f ON i.food_id = f.id WHERE i.id = ? AND f.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapInventory(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to find inventory: " + e.getMessage());
        }

        return null;
    }

    public FoodInventory findInventoryByFoodId(int foodId, int userId) {
        String sql = "SELECT i.* FROM inventory i JOIN food f ON i.food_id = f.id "
                + "WHERE i.food_id = ? AND f.user_id = ? ORDER BY i.id LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, foodId);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapInventory(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to find inventory by food: " + e.getMessage());
        }

        return null;
    }

    public boolean updateInventory(FoodInventory inventory, int userId) {
        String sql = "UPDATE inventory i JOIN food f ON i.id = ? AND f.id = ? AND f.user_id = ? "
                + "SET i.food_id=?, i.quantity_available=?, i.unit=?, i.last_updated=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, inventory.getId());
            pst.setInt(2, inventory.getFoodId());
            pst.setInt(3, userId);
            pst.setInt(4, inventory.getFoodId());
            pst.setDouble(5, inventory.getQuantityAvailable());
            pst.setString(6, inventory.getUnit());
            pst.setTimestamp(7, inventory.getLastUpdated());
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to update inventory: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStockQuantity(int id, double quantity, int userId) {
        String sql = "UPDATE inventory i JOIN food f ON i.food_id = f.id "
                + "SET i.quantity_available=?, i.last_updated=? "
                + "WHERE i.id=? AND f.user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDouble(1, quantity);
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setInt(3, id);
            pst.setInt(4, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to update stock: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteInventory(int id, int userId) {
        String sql = "DELETE i FROM inventory i JOIN food f ON i.food_id = f.id WHERE i.id = ? AND f.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to delete inventory: " + e.getMessage());
            return false;
        }
    }

    private String formatQuantity(double quantity, String unit) {
        if (unit != null && unit.equalsIgnoreCase("pcs")) {
            return String.format("%.0f", quantity);
        }

        return String.format("%.2f", quantity);
    }

    private FoodInventory mapInventory(ResultSet rs) throws SQLException {
        return new FoodInventory(
                rs.getInt("id"),
                rs.getDouble("quantity_available"),
                rs.getString("unit"),
                rs.getTimestamp("last_updated"),
                rs.getInt("food_id")
        );
    }
}

package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.FoodInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FoodInventoryDao {

    public boolean addInventory(FoodInventory inventory) {
        String sql = "INSERT INTO inventory(food_id, quantity_available, unit, last_updated) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setInventoryValues(pst, inventory);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add inventory: " + e.getMessage());
            return false;
        }
    }

    public void viewInventory() {
        String sql = "SELECT i.*, f.food_name FROM inventory i LEFT JOIN food f ON i.food_id = f.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\n========== FOOD INVENTORY ==========");
            while (rs.next()) {
                printInventory(rs);
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view inventory: " + e.getMessage());
        }
    }

    public boolean updateInventory(FoodInventory inventory) {
        String sql = "UPDATE inventory SET food_id=?, quantity_available=?, unit=?, last_updated=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setInventoryValues(pst, inventory);
            pst.setInt(5, inventory.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update inventory: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStockQuantity(int id, double quantity) {
        String sql = "UPDATE inventory SET quantity_available=?, last_updated=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setDouble(1, quantity);
            pst.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pst.setInt(3, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update stock: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteInventory(int id) {
        String sql = "DELETE FROM inventory WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to delete inventory: " + e.getMessage());
            return false;
        }
    }

    private void setInventoryValues(PreparedStatement pst, FoodInventory inventory) throws SQLException {
        pst.setInt(1, inventory.getFoodId());
        pst.setDouble(2, inventory.getQuantityAvailable());
        pst.setString(3, inventory.getUnit());
        pst.setTimestamp(4, inventory.getLastUpdated());
    }

    private void printInventory(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Food: " + rs.getString("food_name"));
        System.out.println("Quantity Available: " + rs.getDouble("quantity_available"));
        System.out.println("Unit: " + rs.getString("unit"));
        System.out.println("Last Updated: " + rs.getTimestamp("last_updated"));
        System.out.println("------------------------------");
    }
}

package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.PetFood;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.TablePrinter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetFoodDao {

    public boolean addFood(PetFood food) {
        String sql = "INSERT INTO food(food_name, brand, `type`, flavor, expiration_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setFoodValues(pst, food);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to add food: " + e.getMessage());
            return false;
        }
    }

    public void viewFoods() {
        String sql = "SELECT * FROM food ORDER BY id";
        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("food_name"),
                    rs.getString("brand"),
                    rs.getString("type"),
                    rs.getString("flavor"),
                    String.valueOf(rs.getDate("expiration_date"))
                });
            }

            ConsoleHelper.header("PET FOOD LIST");
            TablePrinter.print(new String[]{"ID", "Food Name", "Brand", "Type", "Flavor", "Expiration"}, rows);
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to view food: " + e.getMessage());
        }
    }

    public PetFood findFoodById(int id) {
        String sql = "SELECT * FROM food WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapFood(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to find food: " + e.getMessage());
        }

        return null;
    }

    public boolean updateFood(PetFood food) {
        String sql = "UPDATE food SET food_name=?, brand=?, `type`=?, flavor=?, expiration_date=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setFoodValues(pst, food);
            pst.setInt(6, food.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to update food: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFood(int id) {
        String sql = "DELETE FROM food WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to delete food: " + e.getMessage());
            return false;
        }
    }

    private void setFoodValues(PreparedStatement pst, PetFood food) throws SQLException {
        pst.setString(1, food.getFoodName());
        pst.setString(2, food.getBrand());
        pst.setString(3, food.getFoodType());
        pst.setString(4, food.getFlavor());
        pst.setDate(5, food.getExpirationDate());
    }

    private PetFood mapFood(ResultSet rs) throws SQLException {
        return new PetFood(
                rs.getInt("id"),
                rs.getString("food_name"),
                rs.getString("brand"),
                rs.getString("type"),
                rs.getString("flavor"),
                rs.getDate("expiration_date")
        );
    }
}

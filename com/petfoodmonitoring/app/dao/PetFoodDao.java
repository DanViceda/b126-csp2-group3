package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.PetFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetFoodDao {

    public boolean addFood(PetFood food) {
        String sql = "INSERT INTO food(food_name, brand, `type`, flavor, expiration_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setFoodValues(pst, food);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add food: " + e.getMessage());
            return false;
        }
    }

    public void viewFoods() {
        String sql = "SELECT * FROM food";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            System.out.println("\n========== PET FOOD ==========");
            while (rs.next()) {
                printFood(rs);
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view food: " + e.getMessage());
        }
    }

    public boolean updateFood(PetFood food) {
        String sql = "UPDATE food SET food_name=?, brand=?, `type`=?, flavor=?, expiration_date=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setFoodValues(pst, food);
            pst.setInt(6, food.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update food: " + e.getMessage());
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
            System.out.println("Failed to delete food: " + e.getMessage());
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

    private void printFood(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Food Name: " + rs.getString("food_name"));
        System.out.println("Brand: " + rs.getString("brand"));
        System.out.println("Type: " + rs.getString("type"));
        System.out.println("Flavor: " + rs.getString("flavor"));
        System.out.println("Expiration Date: " + rs.getDate("expiration_date"));
        System.out.println("------------------------------");
    }
}

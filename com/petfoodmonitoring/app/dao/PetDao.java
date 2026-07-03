package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.Pet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetDao {

    public boolean addPet(Pet pet) {
        String sql = "INSERT INTO pets(pet_name, species, breed, age, weight, gender, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setPetValues(pst, pet);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to add pet: " + e.getMessage());
            return false;
        }
    }

    public void viewPets(int userId) {
        String sql = "SELECT * FROM pets WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                System.out.println("\n========== PETS ==========");
                while (rs.next()) {
                    printPet(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to view pets: " + e.getMessage());
        }
    }

    public boolean updatePet(Pet pet) {
        String sql = "UPDATE pets SET pet_name=?, species=?, breed=?, age=?, weight=?, gender=? WHERE id=? AND user_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, pet.getPetName());
            pst.setString(2, pet.getSpecies());
            pst.setString(3, pet.getBreed());
            pst.setInt(4, pet.getAge());
            pst.setDouble(5, pet.getWeight());
            pst.setString(6, pet.getGender());
            pst.setInt(7, pet.getId());
            pst.setInt(8, pet.getUserId());

            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to update pet: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePet(int id, int userId) {
        String sql = "DELETE FROM pets WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Failed to delete pet: " + e.getMessage());
            return false;
        }
    }

    private void setPetValues(PreparedStatement pst, Pet pet) throws SQLException {
        pst.setString(1, pet.getPetName());
        pst.setString(2, pet.getSpecies());
        pst.setString(3, pet.getBreed());
        pst.setInt(4, pet.getAge());
        pst.setDouble(5, pet.getWeight());
        pst.setString(6, pet.getGender());
        pst.setInt(7, pet.getUserId());
    }

    private void printPet(ResultSet rs) throws SQLException {
        System.out.println("ID: " + rs.getInt("id"));
        System.out.println("Pet Name: " + rs.getString("pet_name"));
        System.out.println("Species: " + rs.getString("species"));
        System.out.println("Breed: " + rs.getString("breed"));
        System.out.println("Gender: " + rs.getString("gender"));
        System.out.println("Age: " + rs.getInt("age"));
        System.out.println("Weight: " + rs.getDouble("weight"));
        System.out.println("------------------------------");
    }
}

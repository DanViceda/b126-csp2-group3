package com.petfoodmonitoring.app.dao;

import com.petfoodmonitoring.app.config.DBConnection;
import com.petfoodmonitoring.app.model.Pet;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.TablePrinter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PetDao {

    public boolean addPet(Pet pet) {
        String sql = "INSERT INTO pets(pet_name, species, breed, age, weight, gender, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            setPetValues(pst, pet);
            return pst.executeUpdate() > 0;
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to add pet: " + e.getMessage());
            return false;
        }
    }

    public void viewPets(int userId) {
        String sql = "SELECT * FROM pets WHERE user_id = ? ORDER BY id";
        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    rows.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("pet_name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getString("gender"),
                        String.valueOf(rs.getInt("age")),
                        String.format("%.2f kg", rs.getDouble("weight"))
                    });
                }
            }

            ConsoleHelper.header("PET LIST");
            TablePrinter.print(new String[]{"ID", "Name", "Species", "Breed", "Gender", "Age", "Weight"}, rows);
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to view pets: " + e.getMessage());
        }
    }

    public Pet findPetById(int id, int userId) {
        String sql = "SELECT * FROM pets WHERE id = ? AND user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.setInt(2, userId);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapPet(rs);
                }
            }
        } catch (SQLException | NullPointerException e) {
            ConsoleHelper.error("Failed to find pet: " + e.getMessage());
        }

        return null;
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
            ConsoleHelper.error("Failed to update pet: " + e.getMessage());
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
            ConsoleHelper.error("Failed to delete pet: " + e.getMessage());
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

    private Pet mapPet(ResultSet rs) throws SQLException {
        return new Pet(
                rs.getInt("id"),
                rs.getString("pet_name"),
                rs.getString("species"),
                rs.getString("breed"),
                rs.getInt("age"),
                rs.getDouble("weight"),
                rs.getString("gender"),
                rs.getInt("user_id")
        );
    }
}

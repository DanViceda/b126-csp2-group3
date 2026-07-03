package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.PetDao;
import com.petfoodmonitoring.app.model.Pet;
import com.petfoodmonitoring.app.utils.InputHelper;
import com.petfoodmonitoring.app.utils.Validator;

public class PetController {

    private final PetDao petDao = new PetDao();

    public void start(int userId) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addPet(userId);
                    break;
                case 2:
                    petDao.viewPets(userId);
                    break;
                case 3:
                    updatePet(userId);
                    break;
                case 4:
                    deletePet(userId);
                    break;
                case 5:
                    running = false;
                    InputHelper.clearScreen();
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 5.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n========== MANAGE PETS ==========");
        System.out.println("1. Add Pet");
        System.out.println("2. View Pets");
        System.out.println("3. Update Pet");
        System.out.println("4. Delete Pet");
        System.out.println("5. Back");
    }

    private void addPet(int userId) {
        Pet pet = readPetDetails(userId);

        if (petDao.addPet(pet)) {
            System.out.println("\nPet added successfully.");
        } else {
            System.out.println("\nPet was not added.");
        }
    }

    private void updatePet(int userId) {
        petDao.viewPets(userId);
        Pet pet = readPetDetails(userId);
        pet.setId(InputHelper.getInt("Enter Pet ID to update: "));

        if (petDao.updatePet(pet)) {
            System.out.println("\nPet updated successfully.");
        } else {
            System.out.println("\nPet not found or update failed.");
        }
    }

    private void deletePet(int userId) {
        petDao.viewPets(userId);
        int id = InputHelper.getInt("Enter Pet ID to delete: ");

        if (InputHelper.getConfirmation("Delete this pet? (Y/N): ") && petDao.deletePet(id, userId)) {
            System.out.println("\nPet deleted successfully.");
        } else {
            System.out.println("\nDelete operation was not completed.");
        }
    }

    private Pet readPetDetails(int userId) {
        Pet pet = new Pet();
        pet.setPetName(getRequiredText("Pet Name: "));
        pet.setSpecies(getRequiredText("Species: "));
        pet.setBreed(getRequiredText("Breed: "));
        pet.setGender(getValidName("Gender: "));
        pet.setAge(InputHelper.getInt("Age: "));
        pet.setWeight(InputHelper.getDouble("Weight: "));
        pet.setUserId(userId);
        return pet;
    }

    private String getRequiredText(String prompt) {
        return InputHelper.getString(prompt);
    }

    private String getValidName(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (Validator.isValidName(value)) {
                return value;
            }

            System.out.println("Use letters and spaces only.");
        }
    }
}

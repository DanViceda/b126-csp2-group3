package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.PetDao;
import com.petfoodmonitoring.app.model.Pet;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
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
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 5.");
            }
        }
    }

    private void showMenu() {
        ConsoleHelper.boxedMenu("MANAGE PETS", new String[]{
            "1. Add Pet",
            "2. View Pets",
            "3. Update Pet",
            "4. Delete Pet",
            "5. Back"
        });
    }

    private void addPet(int userId) {
        ConsoleHelper.header("ADD PET");
        Pet pet = readPetDetails(userId);

        if (petDao.addPet(pet)) {
            ConsoleHelper.success("Pet added successfully.");
        } else {
            ConsoleHelper.error("Pet was not added.");
        }
    }

    private void updatePet(int userId) {
        ConsoleHelper.header("UPDATE PET");
        petDao.viewPets(userId);
        int id = InputHelper.getInt("Enter Pet ID to update: ");
        Pet pet = petDao.findPetById(id, userId);

        if (pet == null) {
            ConsoleHelper.error("Pet not found.");
            return;
        }

        System.out.println("\nPress Enter without typing anything to keep the current value.");
        pet.setPetName(getOptionalText("Current Name: " + pet.getPetName(), "Enter New Name: ", pet.getPetName()));
        pet.setSpecies(getOptionalText("Current Species: " + pet.getSpecies(), "Enter New Species: ", pet.getSpecies()));
        pet.setBreed(getOptionalText("Current Breed: " + pet.getBreed(), "Enter New Breed: ", pet.getBreed()));
        pet.setGender(getOptionalValidName("Current Gender: " + pet.getGender(), "Enter New Gender: ", pet.getGender()));
        pet.setAge(getOptionalAge("Current Age: " + pet.getAge(), "Enter New Age: ", pet.getAge()));
        pet.setWeight(getOptionalWeight("Current Weight: " + String.format("%.2f kg", pet.getWeight()), "Enter New Weight: ", pet.getWeight()));

        if (petDao.updatePet(pet)) {
            ConsoleHelper.success("Pet updated successfully.");
        } else {
            ConsoleHelper.error("Pet not found or update failed.");
        }
    }

    private void deletePet(int userId) {
        ConsoleHelper.header("DELETE PET");
        petDao.viewPets(userId);
        int id = InputHelper.getInt("Enter Pet ID to delete: ");

        if (InputHelper.getConfirmation("Delete this pet? (Y/N): ") && petDao.deletePet(id, userId)) {
            ConsoleHelper.success("Pet deleted successfully.");
        } else {
            ConsoleHelper.info("Delete operation was not completed.");
        }
    }

    private Pet readPetDetails(int userId) {
        Pet pet = new Pet();
        pet.setPetName(InputHelper.getString("Pet Name: "));
        pet.setSpecies(InputHelper.getString("Species: "));
        pet.setBreed(InputHelper.getString("Breed: "));
        pet.setGender(getValidName("Gender: "));
        pet.setAge(getValidAge("Age: "));
        pet.setWeight(getPetWeight());
        pet.setUserId(userId);
        return pet;
    }

    private int getValidAge(String prompt) {
        while (true) {
            int age = InputHelper.getInt(prompt);

            if (age >= 0) {
                return age;
            }

            ConsoleHelper.error("Age cannot be negative.");
        }
    }

    private double getPetWeight() {
        printWeightGuide();
        return InputHelper.getPositiveDouble("Enter Pet Weight: ");
    }

    private void printWeightGuide() {
        System.out.println();
        System.out.println("Enter Pet Weight (Kilograms only)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("3");
        System.out.println("3.5");
        System.out.println("7.25");
        System.out.println();
        System.out.println("Do not include:");
        System.out.println("kg");
        System.out.println("lbs");
        System.out.println("letters");
        System.out.println("special characters");
        System.out.println();
    }

    private String getValidName(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (Validator.isValidName(value)) {
                return value;
            }

            ConsoleHelper.error("Use letters and spaces only.");
        }
    }

    private String getOptionalText(String currentMessage, String prompt, String currentValue) {
        System.out.println(currentMessage);
        String value = InputHelper.getOptionalString(prompt + "(Press Enter to keep): ");
        return value.isEmpty() ? currentValue : value;
    }

    private String getOptionalValidName(String currentMessage, String prompt, String currentValue) {
        while (true) {
            System.out.println(currentMessage);
            String value = InputHelper.getOptionalString(prompt + "(Press Enter to keep): ");

            if (value.isEmpty()) {
                return currentValue;
            }

            if (Validator.isValidName(value)) {
                return value;
            }

            ConsoleHelper.error("Use letters and spaces only.");
        }
    }

    private int getOptionalAge(String currentMessage, String prompt, int currentValue) {
        while (true) {
            System.out.println(currentMessage);
            Integer value = InputHelper.getOptionalInt(prompt + "(Press Enter to keep): ");

            if (value == null) {
                return currentValue;
            }

            if (value >= 0) {
                return value;
            }

            ConsoleHelper.error("Age cannot be negative.");
        }
    }

    private double getOptionalWeight(String currentMessage, String prompt, double currentValue) {
        printWeightGuide();
        System.out.println(currentMessage);
        Double value = InputHelper.getOptionalPositiveDouble(prompt + "(Press Enter to keep): ");
        return value == null ? currentValue : value;
    }
}

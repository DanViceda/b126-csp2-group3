package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.PetFood;
import com.petfoodmonitoring.app.utils.InputHelper;

public class FoodController {

    private final PetFoodDao foodDao = new PetFoodDao();

    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addFood();
                    break;
                case 2:
                    foodDao.viewFoods();
                    break;
                case 3:
                    updateFood();
                    break;
                case 4:
                    deleteFood();
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
        System.out.println("\n========== MANAGE FOOD ==========");
        System.out.println("1. Add Food");
        System.out.println("2. View Food");
        System.out.println("3. Update Food");
        System.out.println("4. Delete Food");
        System.out.println("5. Back");
    }

    private void addFood() {
        if (foodDao.addFood(readFoodDetails())) {
            System.out.println("\nFood added successfully.");
        } else {
            System.out.println("\nFood was not added.");
        }
    }

    private void updateFood() {
        foodDao.viewFoods();
        PetFood food = readFoodDetails();
        food.setId(InputHelper.getInt("Enter Food ID to update: "));

        if (foodDao.updateFood(food)) {
            System.out.println("\nFood updated successfully.");
        } else {
            System.out.println("\nFood not found or update failed.");
        }
    }

    private void deleteFood() {
        foodDao.viewFoods();
        int id = InputHelper.getInt("Enter Food ID to delete: ");

        if (InputHelper.getConfirmation("Delete this food? (Y/N): ") && foodDao.deleteFood(id)) {
            System.out.println("\nFood deleted successfully.");
        } else {
            System.out.println("\nDelete operation was not completed.");
        }
    }

    private PetFood readFoodDetails() {
        PetFood food = new PetFood();
        food.setFoodName(InputHelper.getString("Food Name: "));
        food.setBrand(InputHelper.getString("Brand: "));
        food.setFoodType(InputHelper.getString("Type: "));
        food.setFlavor(InputHelper.getString("Flavor: "));
        food.setExpirationDate(InputHelper.getDate("Expiration Date (YYYY-MM-DD): "));
        return food;
    }
}

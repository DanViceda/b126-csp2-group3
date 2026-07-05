package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.PetFood;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
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
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 5.");
            }
        }
    }

    private void showMenu() {
        ConsoleHelper.boxedMenu("MANAGE FOOD", new String[]{
            "1. Add Food",
            "2. View Food",
            "3. Update Food",
            "4. Delete Food",
            "5. Back"
        });
    }

    private void addFood() {
        ConsoleHelper.header("ADD FOOD");

        if (foodDao.addFood(readFoodDetails())) {
            ConsoleHelper.success("Food added successfully.");
        } else {
            ConsoleHelper.error("Food was not added.");
        }
    }

    private void updateFood() {
        ConsoleHelper.header("UPDATE FOOD");
        foodDao.viewFoods();
        int id = InputHelper.getInt("Enter Food ID to update: ");
        PetFood food = foodDao.findFoodById(id);

        if (food == null) {
            ConsoleHelper.error("Food not found.");
            return;
        }

        System.out.println("\nPress Enter without typing anything to keep the current value.");
        food.setFoodName(getOptionalText("Current Food Name: " + food.getFoodName(), "Enter New Food Name: ", food.getFoodName()));
        food.setBrand(getOptionalText("Current Brand: " + food.getBrand(), "Enter New Brand: ", food.getBrand()));
        food.setFoodType(getOptionalText("Current Type: " + food.getFoodType(), "Enter New Type: ", food.getFoodType()));
        food.setFlavor(getOptionalText("Current Flavor: " + food.getFlavor(), "Enter New Flavor: ", food.getFlavor()));
        System.out.println("Current Expiration Date: " + food.getExpirationDate());
        food.setExpirationDate(InputHelper.getOptionalDate("Enter New Expiration Date (YYYY-MM-DD, Press Enter to keep): ", food.getExpirationDate()));

        if (foodDao.updateFood(food)) {
            ConsoleHelper.success("Food updated successfully.");
        } else {
            ConsoleHelper.error("Food not found or update failed.");
        }
    }

    private void deleteFood() {
        ConsoleHelper.header("DELETE FOOD");
        foodDao.viewFoods();
        int id = InputHelper.getInt("Enter Food ID to delete: ");

        if (InputHelper.getConfirmation("Delete this food? (Y/N): ") && foodDao.deleteFood(id)) {
            ConsoleHelper.success("Food deleted successfully.");
        } else {
            ConsoleHelper.info("Delete operation was not completed.");
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

    private String getOptionalText(String currentMessage, String prompt, String currentValue) {
        System.out.println(currentMessage);
        String value = InputHelper.getOptionalString(prompt + "(Press Enter to keep): ");
        return value.isEmpty() ? currentValue : value;
    }
}

package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FoodInventoryDao;
import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.FoodInventory;
import com.petfoodmonitoring.app.utils.InputHelper;

import java.sql.Timestamp;

public class InventoryController {

    private final FoodInventoryDao inventoryDao = new FoodInventoryDao();
    private final PetFoodDao foodDao = new PetFoodDao();

    public void start() {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addInventory();
                    break;
                case 2:
                    inventoryDao.viewInventory();
                    break;
                case 3:
                    updateInventory();
                    break;
                case 4:
                    updateStockQuantity();
                    break;
                case 5:
                    deleteInventory();
                    break;
                case 6:
                    running = false;
                    InputHelper.clearScreen();
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 6.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n========== FOOD INVENTORY ==========");
        System.out.println("1. Add Inventory");
        System.out.println("2. View Inventory");
        System.out.println("3. Update Inventory");
        System.out.println("4. Update Stock Quantity");
        System.out.println("5. Delete Inventory");
        System.out.println("6. Back");
    }

    private void addInventory() {
        if (inventoryDao.addInventory(readInventoryDetails())) {
            System.out.println("\nInventory added successfully.");
        } else {
            System.out.println("\nInventory was not added.");
        }
    }

    private void updateInventory() {
        inventoryDao.viewInventory();
        FoodInventory inventory = readInventoryDetails();
        inventory.setId(InputHelper.getInt("Enter Inventory ID to update: "));

        if (inventoryDao.updateInventory(inventory)) {
            System.out.println("\nInventory updated successfully.");
        } else {
            System.out.println("\nInventory not found or update failed.");
        }
    }

    private void updateStockQuantity() {
        inventoryDao.viewInventory();
        int id = InputHelper.getInt("Enter Inventory ID: ");
        double quantity = InputHelper.getDouble("New Quantity Available: ");

        if (inventoryDao.updateStockQuantity(id, quantity)) {
            System.out.println("\nStock quantity updated successfully.");
        } else {
            System.out.println("\nStock quantity update failed.");
        }
    }

    private void deleteInventory() {
        inventoryDao.viewInventory();
        int id = InputHelper.getInt("Enter Inventory ID to delete: ");

        if (InputHelper.getConfirmation("Delete this inventory record? (Y/N): ") && inventoryDao.deleteInventory(id)) {
            System.out.println("\nInventory deleted successfully.");
        } else {
            System.out.println("\nDelete operation was not completed.");
        }
    }

    private FoodInventory readInventoryDetails() {
        foodDao.viewFoods();

        FoodInventory inventory = new FoodInventory();
        inventory.setFoodId(InputHelper.getInt("Food ID: "));
        inventory.setQuantityAvailable(InputHelper.getDouble("Quantity Available: "));
        inventory.setUnit(InputHelper.getString("Unit: "));
        inventory.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        return inventory;
    }
}

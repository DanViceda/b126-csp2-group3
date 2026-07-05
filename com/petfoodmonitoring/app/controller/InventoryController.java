package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FoodInventoryDao;
import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.FoodInventory;
import com.petfoodmonitoring.app.model.PetFood;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.InputHelper;

import java.sql.Timestamp;

public class InventoryController {

    private static final String UNIT_KG = "kg";
    private static final String UNIT_PCS = "pcs";

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
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 6.");
            }
        }
    }

    private void showMenu() {
        ConsoleHelper.boxedMenu("FOOD INVENTORY", new String[]{
            "1. Add Inventory",
            "2. View Inventory",
            "3. Update Inventory",
            "4. Update Stock Quantity",
            "5. Delete Inventory",
            "6. Back"
        });
    }

    private void addInventory() {
        ConsoleHelper.header("ADD INVENTORY");

        if (inventoryDao.addInventory(readInventoryDetails())) {
            ConsoleHelper.success("Inventory added successfully.");
        } else {
            ConsoleHelper.error("Inventory was not added.");
        }
    }

    private void updateInventory() {
        ConsoleHelper.header("UPDATE INVENTORY");
        inventoryDao.viewInventory();
        int id = InputHelper.getInt("Enter Inventory ID to update: ");
        FoodInventory inventory = inventoryDao.findInventoryById(id);

        if (inventory == null) {
            ConsoleHelper.error("Inventory record not found.");
            return;
        }

        PetFood food = foodDao.findFoodById(inventory.getFoodId());

        if (food == null) {
            ConsoleHelper.error("Linked food item was not found.");
            return;
        }

        System.out.println("\nPress Enter without typing anything to keep the current value.");
        food.setFoodName(getOptionalText("Current Food Name: " + food.getFoodName(), "Enter New Food Name: ", food.getFoodName()));
        food.setFoodType(getOptionalText("Current Food Type: " + food.getFoodType(), "Enter New Food Type: ", food.getFoodType()));
        food.setBrand(getOptionalText("Current Brand: " + food.getBrand(), "Enter New Brand: ", food.getBrand()));

        String previousUnit = normalizeUnit(inventory.getUnit());
        System.out.println("Current Inventory Unit: " + inventory.getUnit());
        String selectedUnit = selectInventoryUnitOptional(inventory.getUnit());
        boolean unitChanged = !selectedUnit.equalsIgnoreCase(previousUnit);
        inventory.setUnit(selectedUnit);

        System.out.println("Current Quantity: " + formatQuantity(inventory.getQuantityAvailable(), previousUnit));
        Double quantity = unitChanged ? getRequiredQuantityForUnit(selectedUnit) : getOptionalQuantityForUnit(selectedUnit);
        inventory.setQuantityAvailable(quantity == null ? inventory.getQuantityAvailable() : quantity);
        inventory.setLastUpdated(new Timestamp(System.currentTimeMillis()));

        if (!foodDao.updateFood(food)) {
            ConsoleHelper.error("Food details were not updated.");
            return;
        }

        if (inventoryDao.updateInventory(inventory)) {
            ConsoleHelper.success("Inventory updated successfully.");
        } else {
            ConsoleHelper.error("Inventory not found or update failed.");
        }
    }

    private void updateStockQuantity() {
        ConsoleHelper.header("UPDATE STOCK QUANTITY");
        inventoryDao.viewInventory();
        int id = InputHelper.getInt("Enter Inventory ID: ");
        FoodInventory inventory = inventoryDao.findInventoryById(id);

        if (inventory == null) {
            ConsoleHelper.error("Inventory record not found.");
            return;
        }

        double quantity = getQuantityForUnit(inventory.getUnit());

        if (inventoryDao.updateStockQuantity(id, quantity)) {
            ConsoleHelper.success("Stock quantity updated successfully.");
        } else {
            ConsoleHelper.error("Stock quantity update failed.");
        }
    }

    private void deleteInventory() {
        ConsoleHelper.header("DELETE INVENTORY");
        inventoryDao.viewInventory();
        int id = InputHelper.getInt("Enter Inventory ID to delete: ");

        if (InputHelper.getConfirmation("Delete this inventory record? (Y/N): ") && inventoryDao.deleteInventory(id)) {
            ConsoleHelper.success("Inventory deleted successfully.");
        } else {
            ConsoleHelper.info("Delete operation was not completed.");
        }
    }

    private FoodInventory readInventoryDetails() {
        foodDao.viewFoods();

        FoodInventory inventory = new FoodInventory();
        inventory.setFoodId(InputHelper.getInt("Food ID: "));
        inventory.setUnit(selectInventoryUnitRequired());
        inventory.setQuantityAvailable(getQuantityForUnit(inventory.getUnit()));
        inventory.setLastUpdated(new Timestamp(System.currentTimeMillis()));
        return inventory;
    }

    private String selectInventoryUnitRequired() {
        while (true) {
            System.out.println();
            System.out.println("Select Inventory Unit");
            System.out.println("1. Kilograms (kg)");
            System.out.println("2. Pieces (pcs)");
            int choice = InputHelper.getInt("Choice: ");

            if (choice == 1) {
                return UNIT_KG;
            }

            if (choice == 2) {
                return UNIT_PCS;
            }

            ConsoleHelper.error("Invalid choice. Please select 1 or 2.");
        }
    }

    private String selectInventoryUnitOptional(String currentUnit) {
        while (true) {
            System.out.println("Select New Inventory Unit");
            System.out.println("1. Kilograms (kg)");
            System.out.println("2. Pieces (pcs)");
            String choice = InputHelper.getOptionalString("Choice (Press Enter to keep): ");

            if (choice.isEmpty()) {
                return normalizeUnit(currentUnit);
            }

            if (choice.equals("1")) {
                return UNIT_KG;
            }

            if (choice.equals("2")) {
                return UNIT_PCS;
            }

            ConsoleHelper.error("Invalid choice. Please select 1, 2, or press Enter to keep.");
        }
    }

    private double getQuantityForUnit(String unit) {
        if (isPiecesUnit(unit)) {
            return getWholeNumberQuantity("Quantity Available (pcs): ");
        }

        return InputHelper.getPositiveDouble("Quantity Available (kg): ");
    }

    private double getRequiredQuantityForUnit(String unit) {
        ConsoleHelper.info("Inventory unit changed. Please enter a new quantity for the selected unit.");
        return getQuantityForUnit(unit);
    }

    private Double getOptionalQuantityForUnit(String unit) {
        if (isPiecesUnit(unit)) {
            Integer value = getOptionalWholeNumberQuantity("Enter New Quantity (pcs, Press Enter to keep): ");
            return value == null ? null : value.doubleValue();
        }

        return InputHelper.getOptionalPositiveDouble("Enter New Quantity (kg, Press Enter to keep): ");
    }

    private int getWholeNumberQuantity(String prompt) {
        while (true) {
            int value = InputHelper.getInt(prompt);

            if (value >= 0) {
                return value;
            }

            ConsoleHelper.error("Pieces cannot be negative.");
        }
    }

    private Integer getOptionalWholeNumberQuantity(String prompt) {
        while (true) {
            Integer value = InputHelper.getOptionalInt(prompt);

            if (value == null || value >= 0) {
                return value;
            }

            ConsoleHelper.error("Pieces cannot be negative.");
        }
    }

    private String getOptionalText(String currentMessage, String prompt, String currentValue) {
        System.out.println(currentMessage);
        String value = InputHelper.getOptionalString(prompt + "(Press Enter to keep): ");
        return value.isEmpty() ? currentValue : value;
    }

    private String normalizeUnit(String unit) {
        return isPiecesUnit(unit) ? UNIT_PCS : UNIT_KG;
    }

    private boolean isPiecesUnit(String unit) {
        return unit != null && unit.equalsIgnoreCase(UNIT_PCS);
    }

    private String formatQuantity(double quantity, String unit) {
        return isPiecesUnit(unit) ? String.format("%.0f pcs", quantity) : String.format("%.2f kg", quantity);
    }
}

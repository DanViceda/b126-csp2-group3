package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FeedingHistoryDao;
import com.petfoodmonitoring.app.dao.FeedingScheduleDao;
import com.petfoodmonitoring.app.dao.FoodInventoryDao;
import com.petfoodmonitoring.app.model.FeedingHistory;
import com.petfoodmonitoring.app.model.FeedingSchedule;
import com.petfoodmonitoring.app.model.FoodInventory;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.InputHelper;

import java.sql.Date;

public class FeedingHistoryController {

    private static final double KILOGRAMS_PER_CUP = 0.10;

    private final FeedingHistoryDao historyDao = new FeedingHistoryDao();
    private final FeedingScheduleDao scheduleDao = new FeedingScheduleDao();
    private final FoodInventoryDao inventoryDao = new FoodInventoryDao();

    public void start(int userId) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    historyDao.viewHistory(userId);
                    break;
                case 2:
            feedPet(userId);
                    break;
                case 3:
                    addHistory(userId);
                    break;
                case 4:
                    running = false;
                    InputHelper.clearScreen();
                    break;
                default:
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 4.");
            }
        }
    }

    private void showMenu() {
        ConsoleHelper.boxedMenu("FEEDING HISTORY", new String[]{
            "1. View Feeding Records",
            "2. Feed Pet",
            "3. Add Manual Feeding Record",
            "4. Back"
        });
    }

    private void feedPet(int userId) {
        ConsoleHelper.header("FEED PET");
        scheduleDao.viewSchedules(userId);
        int scheduleId = InputHelper.getInt("Enter Schedule ID: ");
        FeedingSchedule schedule = scheduleDao.findScheduleById(scheduleId, userId);

        if (schedule == null) {
            ConsoleHelper.error("Schedule not found.");
            return;
        }

        FoodInventory inventory = inventoryDao.findInventoryByFoodId(schedule.getFoodId(), userId);

        if (inventory == null) {
            ConsoleHelper.error("No inventory record found for this schedule's food.");
            return;
        }

        String petName = scheduleDao.getPetNameForSchedule(scheduleId, userId);
        System.out.println("Pet Name: " + petName);

        if (isPiecesUnit(inventory.getUnit())) {
            feedUsingPieces(schedule, inventory, userId);
        } else if (isKilogramUnit(inventory.getUnit())) {
            feedUsingKilograms(schedule, inventory, userId);
        } else {
            ConsoleHelper.error("Unsupported inventory unit: " + inventory.getUnit() + ". Please update the unit to kg or pcs.");
        }
    }

    private void feedUsingKilograms(FeedingSchedule schedule, FoodInventory inventory, int userId) {
        double cups = getGreaterThanZeroDouble("Enter Number of Cups: ");
        double kilogramsFed = cups * KILOGRAMS_PER_CUP;
        double currentInventory = inventory.getQuantityAvailable();
        double remainingInventory = currentInventory - kilogramsFed;

        System.out.println();
        System.out.println("Conversion:");
        System.out.println("1 Cup = " + String.format("%.2f kg", KILOGRAMS_PER_CUP));
        System.out.println("Food Consumed: " + String.format("%.2f kg", kilogramsFed));
        System.out.println("Inventory: " + String.format("%.2f kg", currentInventory));

        if (remainingInventory < 0) {
            ConsoleHelper.error("Insufficient stock. Required " + String.format("%.2f kg", kilogramsFed)
                    + " but only " + String.format("%.2f kg", currentInventory) + " is available.");
            return;
        }

        System.out.println("Remaining: " + String.format("%.2f kg", remainingInventory));
        saveFeedingTransaction(schedule, inventory, remainingInventory, userId,
                String.format("Fed %.2f cups / %.2f kg", cups, kilogramsFed));
    }

    private void feedUsingPieces(FeedingSchedule schedule, FoodInventory inventory, int userId) {
        int piecesUsed = getPositivePieces("Enter Number of Pieces Used: ");
        int currentInventory = (int) inventory.getQuantityAvailable();
        int remainingInventory = currentInventory - piecesUsed;

        System.out.println();
        System.out.println("Inventory: " + currentInventory + " pcs");
        System.out.println("Pieces Used: " + piecesUsed);

        if (remainingInventory < 0) {
            ConsoleHelper.error("Insufficient stock. Required " + piecesUsed + " pcs but only " + currentInventory + " pcs is available.");
            return;
        }

        System.out.println("Remaining: " + remainingInventory + " pcs");
        saveFeedingTransaction(schedule, inventory, remainingInventory, userId,
                "Fed " + piecesUsed + " pcs");
    }

    private void saveFeedingTransaction(FeedingSchedule schedule, FoodInventory inventory, double remainingInventory, int userId, String remarks) {
        if (!InputHelper.getConfirmation("Save this feeding transaction? (Y/N): ")) {
            ConsoleHelper.info("Feeding transaction was cancelled.");
            return;
        }

        if (!inventoryDao.updateStockQuantity(inventory.getId(), remainingInventory, userId)) {
            ConsoleHelper.error("Feeding was not saved because inventory could not be updated.");
            return;
        }

        FeedingHistory history = new FeedingHistory();
        history.setScheduleId(schedule.getId());
        history.setFeedingDate(new Date(System.currentTimeMillis()));
        history.setFeedingTime(schedule.getFeedingTime());
        history.setStatus("Completed");
        history.setRemarks(remarks);

        if (historyDao.addHistory(history)) {
            ConsoleHelper.success("Feeding transaction saved successfully.");
        } else {
            ConsoleHelper.error("Inventory was deducted, but feeding history was not saved.");
        }
    }

    private double getGreaterThanZeroDouble(String prompt) {
        while (true) {
            double value = InputHelper.getPositiveDouble(prompt);

            if (value > 0) {
                return value;
            }

            ConsoleHelper.error("Value must be greater than zero.");
        }
    }

    private int getPositivePieces(String prompt) {
        while (true) {
            int value = InputHelper.getInt(prompt);

            if (value > 0) {
                return value;
            }

            ConsoleHelper.error("Pieces used must be greater than zero.");
        }
    }

    private boolean isKilogramUnit(String unit) {
        return unit != null
                && (unit.equalsIgnoreCase("kg")
                || unit.equalsIgnoreCase("kilogram")
                || unit.equalsIgnoreCase("kilograms"));
    }

    private boolean isPiecesUnit(String unit) {
        return unit != null && unit.equalsIgnoreCase("pcs");
    }

    private void addHistory(int userId) {
        ConsoleHelper.header("ADD MANUAL FEEDING RECORD");
        scheduleDao.viewSchedules(userId);

        FeedingHistory history = new FeedingHistory();
        history.setScheduleId(InputHelper.getInt("Schedule ID: "));
        history.setFeedingDate(InputHelper.getDate("Feeding Date (YYYY-MM-DD): "));
        history.setFeedingTime(InputHelper.getString("Feeding Time (HH:MM): "));
        history.setStatus(InputHelper.getString("Status: "));
        history.setRemarks(InputHelper.getString("Remarks: "));

        if (historyDao.addHistory(history)) {
            ConsoleHelper.success("Feeding record added successfully.");
        } else {
            ConsoleHelper.error("Feeding record was not added.");
        }
    }
}

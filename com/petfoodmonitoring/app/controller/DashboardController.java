package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.model.User;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.InputHelper;

public class DashboardController {

    private final PetController petController = new PetController();
    private final FoodController foodController = new FoodController();
    private final InventoryController inventoryController = new InventoryController();
    private final FeedingScheduleController scheduleController = new FeedingScheduleController();
    private final FeedingHistoryController historyController = new FeedingHistoryController();

    public void start(User user) {
        boolean loggedIn = true;

        while (loggedIn) {
            showDashboard();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    petController.start(user.getId());
                    break;
                case 2:
                    foodController.start(user.getId());
                    break;
                case 3:
                    inventoryController.start(user.getId());
                    break;
                case 4:
                    scheduleController.start(user.getId());
                    break;
                case 5:
                    historyController.start(user.getId());
                    break;
                case 6:
                    loggedIn = false;
                    InputHelper.clearScreen();
                    System.out.println("You have been logged out.");
                    break;
                default:
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 6.");
            }
        }
    }

    private void showDashboard() {
        ConsoleHelper.boxedMenu("DASHBOARD", new String[]{
            "1. Manage Pets",
            "2. Manage Food",
            "3. Food Inventory",
            "4. Feeding Schedule",
            "5. Feeding History",
            "6. Logout"
        });
    }
}

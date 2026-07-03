package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.model.User;
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
                    foodController.start();
                    break;
                case 3:
                    inventoryController.start();
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
                    System.out.println("Invalid choice. Please select from 1 to 6.");
            }
        }
    }

    private void showDashboard() {
        System.out.println("\n=============================");
        System.out.println("Dashboard");
        System.out.println("=============================");
        System.out.println("1. Manage Pets");
        System.out.println("2. Manage Food");
        System.out.println("3. Food Inventory");
        System.out.println("4. Feeding Schedule");
        System.out.println("5. Feeding History");
        System.out.println("6. Logout");
    }
}

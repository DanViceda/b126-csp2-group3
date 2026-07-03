package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FeedingScheduleDao;
import com.petfoodmonitoring.app.dao.PetDao;
import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.FeedingSchedule;
import com.petfoodmonitoring.app.utils.InputHelper;

public class FeedingScheduleController {

    private final FeedingScheduleDao scheduleDao = new FeedingScheduleDao();
    private final PetDao petDao = new PetDao();
    private final PetFoodDao foodDao = new PetFoodDao();

    public void start(int userId) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addSchedule(userId);
                    break;
                case 2:
                    scheduleDao.viewSchedules(userId);
                    break;
                case 3:
                    updateSchedule(userId);
                    break;
                case 4:
                    deleteSchedule(userId);
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
        System.out.println("\n========== FEEDING SCHEDULE ==========");
        System.out.println("1. Add Schedule");
        System.out.println("2. View Schedules");
        System.out.println("3. Update Schedule");
        System.out.println("4. Delete Schedule");
        System.out.println("5. Back");
    }

    private void addSchedule(int userId) {
        if (scheduleDao.addSchedule(readScheduleDetails(userId))) {
            System.out.println("\nSchedule added successfully.");
        } else {
            System.out.println("\nSchedule was not added.");
        }
    }

    private void updateSchedule(int userId) {
        scheduleDao.viewSchedules(userId);
        FeedingSchedule schedule = readScheduleDetails(userId);
        schedule.setId(InputHelper.getInt("Enter Schedule ID to update: "));

        if (scheduleDao.updateSchedule(schedule, userId)) {
            System.out.println("\nSchedule updated successfully.");
        } else {
            System.out.println("\nSchedule not found or update failed.");
        }
    }

    private void deleteSchedule(int userId) {
        scheduleDao.viewSchedules(userId);
        int id = InputHelper.getInt("Enter Schedule ID to delete: ");

        if (InputHelper.getConfirmation("Delete this schedule? (Y/N): ") && scheduleDao.deleteSchedule(id, userId)) {
            System.out.println("\nSchedule deleted successfully.");
        } else {
            System.out.println("\nDelete operation was not completed.");
        }
    }

    private FeedingSchedule readScheduleDetails(int userId) {
        petDao.viewPets(userId);
        foodDao.viewFoods();

        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setPetId(InputHelper.getInt("Pet ID: "));
        schedule.setFoodId(InputHelper.getInt("Food ID: "));
        schedule.setFeedingTime(InputHelper.getString("Feeding Time (HH:MM): "));
        schedule.setQuantity(InputHelper.getDouble("Quantity: "));
        schedule.setFrequency(InputHelper.getString("Frequency: "));
        return schedule;
    }
}

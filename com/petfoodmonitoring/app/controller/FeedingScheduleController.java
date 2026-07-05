package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FeedingScheduleDao;
import com.petfoodmonitoring.app.dao.PetDao;
import com.petfoodmonitoring.app.dao.PetFoodDao;
import com.petfoodmonitoring.app.model.FeedingSchedule;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
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
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 5.");
            }
        }
    }

    private void showMenu() {
        ConsoleHelper.boxedMenu("FEEDING SCHEDULE", new String[]{
            "1. Add Schedule",
            "2. View Schedules",
            "3. Update Schedule",
            "4. Delete Schedule",
            "5. Back"
        });
    }

    private void addSchedule(int userId) {
        ConsoleHelper.header("ADD FEEDING SCHEDULE");

        if (scheduleDao.addSchedule(readScheduleDetails(userId))) {
            ConsoleHelper.success("Schedule added successfully.");
        } else {
            ConsoleHelper.error("Schedule was not added.");
        }
    }

    private void updateSchedule(int userId) {
        ConsoleHelper.header("UPDATE FEEDING SCHEDULE");
        scheduleDao.viewSchedules(userId);
        int id = InputHelper.getInt("Enter Schedule ID to update: ");
        FeedingSchedule schedule = scheduleDao.findScheduleById(id, userId);

        if (schedule == null) {
            ConsoleHelper.error("Schedule not found.");
            return;
        }

        petDao.viewPets(userId);
        foodDao.viewFoods();
        System.out.println("\nPress Enter without typing anything to keep the current value.");

        System.out.println("Current Pet ID: " + schedule.getPetId());
        Integer petId = InputHelper.getOptionalInt("Enter New Pet ID (Press Enter to keep): ");
        schedule.setPetId(petId == null ? schedule.getPetId() : petId);

        System.out.println("Current Food ID: " + schedule.getFoodId());
        Integer foodId = InputHelper.getOptionalInt("Enter New Food ID (Press Enter to keep): ");
        schedule.setFoodId(foodId == null ? schedule.getFoodId() : foodId);

        System.out.println("Current Feeding Time: " + schedule.getFeedingTime());
        String feedingTime = InputHelper.getOptionalString("Enter New Feeding Time (Press Enter to keep): ");
        schedule.setFeedingTime(feedingTime.isEmpty() ? schedule.getFeedingTime() : feedingTime);

        System.out.println("Current Quantity: " + String.format("%.2f", schedule.getQuantity()));
        Double quantity = InputHelper.getOptionalPositiveDouble("Enter New Quantity (Press Enter to keep): ");
        schedule.setQuantity(quantity == null ? schedule.getQuantity() : quantity);

        System.out.println("Current Frequency: " + schedule.getFrequency());
        String frequency = InputHelper.getOptionalString("Enter New Frequency (Press Enter to keep): ");
        schedule.setFrequency(frequency.isEmpty() ? schedule.getFrequency() : frequency);

        if (scheduleDao.updateSchedule(schedule, userId)) {
            ConsoleHelper.success("Schedule updated successfully.");
        } else {
            ConsoleHelper.error("Schedule not found or update failed.");
        }
    }

    private void deleteSchedule(int userId) {
        ConsoleHelper.header("DELETE FEEDING SCHEDULE");
        scheduleDao.viewSchedules(userId);
        int id = InputHelper.getInt("Enter Schedule ID to delete: ");

        if (InputHelper.getConfirmation("Delete this schedule? (Y/N): ") && scheduleDao.deleteSchedule(id, userId)) {
            ConsoleHelper.success("Schedule deleted successfully.");
        } else {
            ConsoleHelper.info("Delete operation was not completed.");
        }
    }

    private FeedingSchedule readScheduleDetails(int userId) {
        petDao.viewPets(userId);
        foodDao.viewFoods();

        FeedingSchedule schedule = new FeedingSchedule();
        schedule.setPetId(InputHelper.getInt("Pet ID: "));
        schedule.setFoodId(InputHelper.getInt("Food ID: "));
        schedule.setFeedingTime(InputHelper.getString("Feeding Time (HH:MM): "));
        schedule.setQuantity(InputHelper.getPositiveDouble("Quantity: "));
        schedule.setFrequency(InputHelper.getString("Frequency: "));
        return schedule;
    }
}

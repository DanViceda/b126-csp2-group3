package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.FeedingHistoryDao;
import com.petfoodmonitoring.app.dao.FeedingScheduleDao;
import com.petfoodmonitoring.app.model.FeedingHistory;
import com.petfoodmonitoring.app.utils.InputHelper;

public class FeedingHistoryController {

    private final FeedingHistoryDao historyDao = new FeedingHistoryDao();
    private final FeedingScheduleDao scheduleDao = new FeedingScheduleDao();

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
                    addHistory(userId);
                    break;
                case 3:
                    running = false;
                    InputHelper.clearScreen();
                    break;
                default:
                    System.out.println("Invalid choice. Please select from 1 to 3.");
            }           
        }
    }

    private void showMenu() {
        System.out.println("\n========== FEEDING HISTORY ==========");
        System.out.println("1. View Completed Feeding Records");
        System.out.println("2. Add Feeding Record");
        System.out.println("3. Back");
    }

    private void addHistory(int userId) {
        scheduleDao.viewSchedules(userId);

        FeedingHistory history = new FeedingHistory();
        history.setScheduleId(InputHelper.getInt("Schedule ID: "));
        history.setFeedingDate(InputHelper.getDate("Feeding Date (YYYY-MM-DD): "));
        history.setFeedingTime(InputHelper.getString("Feeding Time (HH:MM): "));
        history.setStatus(InputHelper.getString("Status: "));
        history.setRemarks(InputHelper.getString("Remarks: "));

        if (historyDao.addHistory(history)) {
            System.out.println("\nFeeding record added successfully.");
        } else {
            System.out.println("\nFeeding record was not added.");
        }
    }
}

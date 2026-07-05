package com.petfoodmonitoring.app.controller;

import com.petfoodmonitoring.app.dao.UserDao;
import com.petfoodmonitoring.app.model.User;
import com.petfoodmonitoring.app.utils.ConsoleHelper;
import com.petfoodmonitoring.app.utils.InputHelper;
import com.petfoodmonitoring.app.utils.Validator;

public class UserController {

    private final UserDao userDao = new UserDao();
    private final DashboardController dashboardController = new DashboardController();

    public void start() {
        boolean running = true;

        while (running) {
            showMainMenu();
            int choice = InputHelper.getInt("Choose an option: ");

            switch (choice) {
                case 1:
                    showWelcomeScreen();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    login();
                    break;
                case 4:
                    if (InputHelper.getConfirmation("Are you sure you want to exit? (Y/N): ")) {
                        running = false;
                        System.out.println("Thank you for using Pet Food Monitoring App. Goodbye!");
                    } else {
                        InputHelper.clearScreen();
                    }
                    break;
                default:
                    ConsoleHelper.error("Invalid choice. Please select from 1 to 4.");
            }
        }
    }

    private void showMainMenu() {
        ConsoleHelper.boxedMenu("PET FOOD MONITORING APP", new String[]{
            "1. Start Application",
            "2. Register",
            "3. Login",
            "4. Exit"
        });
    }

    private void showWelcomeScreen() {
        InputHelper.clearScreen();
        System.out.println("=========================================");
        System.out.println(" Welcome to Pet Food Monitoring App");
        System.out.println("=========================================");
        System.out.println();
        System.out.println("This application helps pet owners monitor their pet food inventory,");
        System.out.println("manage feeding schedules, and record feeding history.");
        System.out.println();
        System.out.println("Features:");
        System.out.println("- Manage Pets");
        System.out.println("- Manage Pet Food");
        System.out.println("- Monitor Food Inventory");
        System.out.println("- Feeding Schedule");
        System.out.println("- Feeding History");
        InputHelper.pressEnterToContinue();
        InputHelper.clearScreen();
    }

    private void register() {
        ConsoleHelper.header("REGISTER");

        String firstName = getValidName("First Name: ");
        String lastName = getValidName("Last Name: ");
        String phoneNumber = getValidPhoneNumber("Phone Number: ");
        String email = getValidEmail("Email: ");
        String password = getValidPassword("Password: ");

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setPassword(password);

        if (userDao.register(user)) {
            System.out.println("\nRegistration successful. You may now login.");
        } else {
            System.out.println("\nRegistration failed.");
        }
    }

    private void login() {
        ConsoleHelper.header("LOGIN");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        User user = userDao.login(email, password);

        if (user != null) {
            System.out.println("\nLogin successful. Welcome, " + user.getFullName() + "!");
            dashboardController.start(user);
        } else {
            System.out.println("\nInvalid email or password.");
        }
    }

    private String getValidName(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (Validator.isValidName(value)) {
                return value;
            }

            System.out.println("Name must contain letters and spaces only.");
        }
    }

    private String getValidEmail(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (!Validator.isValidEmail(value)) {
                System.out.println("Email must contain '@' and '.'.");
            } else if (userDao.emailExists(value)) {
                System.out.println("This email is already registered. Please use another email.");
            } else {
                return value;
            }
        }
    }

    private String getValidPassword(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (Validator.isValidPassword(value)) {
                return value;
            }

            System.out.println(Validator.getPasswordValidationMessage(value));
        }
    }

    private String getValidPhoneNumber(String prompt) {
        while (true) {
            String value = InputHelper.getString(prompt);

            if (Validator.isValidPhoneNumber(value)) {
                return value;
            }

            System.out.println("Phone number must be exactly 11 digits and start with 09.");
        }
    }
}

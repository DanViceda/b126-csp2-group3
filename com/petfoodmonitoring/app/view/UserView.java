package com.petfoodmonitoring.app.view;

import com.petfoodmonitoring.app.model.User;
import com.petfoodmonitoring.app.utils.InputHelper;
import com.petfoodmonitoring.app.utils.Validator;

public class UserView {

    public int showUserMenu() {
        System.out.println("\n=================================");
        System.out.println("         MAIN MENU");
        System.out.println("=================================");
        System.out.println("1. Add User");
        System.out.println("2. Update User");
        System.out.println("3. Delete User");
        System.out.println("4. View Users");
        System.out.println("5. Search User");
        System.out.println("6. Exit");

        return InputHelper.getInt("Choose an option: ");
    }

    public String getFirstName() {
        return getValidName("Enter First Name: ");
    }

    public String getLastName() {
        return getValidName("Enter Last Name: ");
    }

    public String getEmail() {
        return getValidEmail("Enter Email: ");
    }

    public String getPassword() {
        return getValidPassword("Enter Password: ");
    }

    public String getPhoneNumber() {
        return getValidPhoneNumber("Enter Phone Number: ");
    }

    public int getUserId() {
        return InputHelper.getInt("Enter User ID: ");
    }

    public User getUpdatedUser() {
        User user = new User();

        user.setId(InputHelper.getInt("Enter User ID: "));
        user.setFirstName(getValidName("Enter New First Name: "));
        user.setLastName(getValidName("Enter New Last Name: "));
        user.setEmail(getValidEmail("Enter New Email: "));
        user.setPassword(getValidPassword("Enter New Password: "));
        user.setPhoneNumber(getValidPhoneNumber("Enter New Phone Number: "));

        return user;
    }

    public int getDeleteUserId() {
        return InputHelper.getInt("Enter User ID to Delete: ");
    }

    public boolean confirmExit() {
        return InputHelper.getConfirmation("Are you sure you want to exit? (Y/N): ");
    }

    private String getValidName(String prompt) {
        while (true) {
            String name = InputHelper.getString(prompt);

            if (Validator.isValidName(name)) {
                return name;
            }

            System.out.println("Invalid name. Use letters and single spaces only. Numbers and special characters are not allowed.");
        }
    }

    private String getValidEmail(String prompt) {
        while (true) {
            String email = InputHelper.getString(prompt);

            if (Validator.isValidEmail(email)) {
                return email;
            }

            System.out.println("Invalid email. Email must contain '@' and '.', with text before and after them.");
        }
    }

    private String getValidPassword(String prompt) {
        while (true) {
            String password = InputHelper.getString(prompt);

            if (Validator.isValidPassword(password)) {
                return password;
            }

            System.out.println(Validator.getPasswordValidationMessage(password));
        }
    }

    private String getValidPhoneNumber(String prompt) {
        while (true) {
            String phoneNumber = InputHelper.getString(prompt);

            if (Validator.isValidPhoneNumber(phoneNumber)) {
                return phoneNumber;
            }

            System.out.println("Invalid phone number. Use exactly 11 digits and start with 09. Example: 09722153162");
        }
    }
}

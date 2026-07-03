package com.petfoodmonitoring.app.utils;

public class Validator {

    private Validator() {
    }

    public static boolean isValidName(String value) {
        return value != null && value.matches("[A-Za-z]+( [A-Za-z]+)*");
    }

    public static boolean isValidEmail(String value) {
        return value != null
                && value.contains("@")
                && value.contains(".")
                && value.indexOf("@") > 0
                && value.lastIndexOf(".") > value.indexOf("@") + 1
                && value.lastIndexOf(".") < value.length() - 1;
    }

    public static boolean isValidPassword(String value) {
        return value != null
                && value.length() >= 8
                && value.matches(".*[A-Z].*")
                && value.matches(".*[a-z].*")
                && value.matches(".*[0-9].*")
                && value.matches(".*[^A-Za-z0-9].*");
    }

    public static String getPasswordValidationMessage(String value) {
        if (value == null || value.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        if (!value.matches(".*[A-Z].*")) {
            return "Password must contain at least 1 uppercase letter.";
        }

        if (!value.matches(".*[a-z].*")) {
            return "Password must contain at least 1 lowercase letter.";
        }

        if (!value.matches(".*[0-9].*")) {
            return "Password must contain at least 1 number.";
        }

        if (!value.matches(".*[^A-Za-z0-9].*")) {
            return "Password must contain at least 1 special character.";
        }

        return "";
    }

    public static boolean isValidPhoneNumber(String value) {
        return value != null && value.matches("09[0-9]{9}");
    }
}

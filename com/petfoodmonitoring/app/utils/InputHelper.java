package com.petfoodmonitoring.app.utils;

import java.util.Scanner;
import java.sql.Date;

public class InputHelper {

    private static final Scanner SCANNER = new Scanner(System.in);

    private InputHelper() {
    }

    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static String getOptionalString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    public static Integer getOptionalInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = SCANNER.nextLine().trim();

                if (value.isEmpty()) {
                    return null;
                }

                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number or press Enter to keep the current value.");
            }
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }

    public static double getPositiveDouble(String prompt) {
        while (true) {
            double value = getDouble(prompt);

            if (value >= 0) {
                return value;
            }

            System.out.println("Value cannot be negative. Please try again.");
        }
    }

    public static Double getOptionalPositiveDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = SCANNER.nextLine().trim();

                if (value.isEmpty()) {
                    return null;
                }

                double parsedValue = Double.parseDouble(value);

                if (parsedValue >= 0) {
                    return parsedValue;
                }

                System.out.println("Value cannot be negative. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number or press Enter to keep the current value.");
            }
        }
    }

    public static long getLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static Date getDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Date.valueOf(SCANNER.nextLine().trim());
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid date using YYYY-MM-DD format.");
            }
        }
    }

    public static Date getOptionalDate(String prompt, Date currentValue) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = SCANNER.nextLine().trim();

                if (value.isEmpty()) {
                    return currentValue;
                }

                return Date.valueOf(value);
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter a valid date using YYYY-MM-DD format or press Enter to keep the current value.");
            }
        }
    }

    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt);
            String answer = SCANNER.nextLine().trim();

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            }

            if (answer.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Please enter Y or N only.");
        }
    }

    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        SCANNER.nextLine();
    }
}

package com.petfoodmonitoring.app.utils;

public class ConsoleHelper {

    private ConsoleHelper() {
    }

    public static void header(String title) {
        int width = Math.max(42, title.length() + 10);
        String border = repeat("=", width);
        System.out.println();
        System.out.println(border);
        System.out.println(center(title, width));
        System.out.println(border);
    }

    public static void boxedMenu(String title, String[] options) {
        int width = 44;
        System.out.println();
        System.out.println("+" + repeat("=", width) + "+");
        System.out.println("|" + center(title, width) + "|");
        System.out.println("+" + repeat("=", width) + "+");

        for (String option : options) {
            System.out.println("| " + padRight(option, width - 2) + " |");
        }

        System.out.println("+" + repeat("=", width) + "+");
    }

    public static void success(String message) {
        System.out.println("\n[SUCCESS] " + message);
    }

    public static void error(String message) {
        System.out.println("\n[ERROR] " + message);
    }

    public static void info(String message) {
        System.out.println("\n[INFO] " + message);
    }

    public static String padRight(String value, int width) {
        String text = value == null ? "" : value;
        StringBuilder builder = new StringBuilder(text);

        while (builder.length() < width) {
            builder.append(' ');
        }

        return builder.toString();
    }

    private static String center(String value, int width) {
        int padding = width - value.length();
        int left = padding / 2;
        int right = padding - left;
        return repeat(" ", left) + value + repeat(" ", right);
    }

    private static String repeat(String value, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append(value);
        }

        return builder.toString();
    }
}

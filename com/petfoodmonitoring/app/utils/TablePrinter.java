package com.petfoodmonitoring.app.utils;

import java.util.List;

public class TablePrinter {

    private TablePrinter() {
    }

    public static void print(String[] headers, List<String[]> rows) {
        if (rows == null || rows.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        int[] widths = getColumnWidths(headers, rows);
        printBorder(widths);
        printRow(headers, widths);
        printBorder(widths);

        for (String[] row : rows) {
            printRow(row, widths);
        }

        printBorder(widths);
    }

    private static int[] getColumnWidths(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                String value = i < row.length && row[i] != null ? row[i] : "";
                widths[i] = Math.max(widths[i], value.length());
            }
        }

        return widths;
    }

    private static void printBorder(int[] widths) {
        StringBuilder builder = new StringBuilder("+");

        for (int width : widths) {
            builder.append(repeat("-", width + 2)).append("+");
        }

        System.out.println(builder.toString());
    }

    private static void printRow(String[] row, int[] widths) {
        StringBuilder builder = new StringBuilder("|");

        for (int i = 0; i < widths.length; i++) {
            String value = i < row.length && row[i] != null ? row[i] : "";
            builder.append(' ').append(ConsoleHelper.padRight(value, widths[i])).append(" |");
        }

        System.out.println(builder.toString());
    }

    private static String repeat(String value, int count) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append(value);
        }

        return builder.toString();
    }
}

package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final String NOW = "Сейчас";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate of(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate of(String date) {
        if (date.equalsIgnoreCase(NOW)) {
            return LocalDate.now();
        }
        String[] dateArray = date.split("/");
        int month = Integer.parseInt(dateArray[0]);
        int year = Integer.parseInt(dateArray[1]);
        return LocalDate.of(year, month, 1);
    }

    public static String dateFormat(LocalDate localDate) {
        if (localDate.format(DATE_TIME_FORMATTER).equals(LocalDate.now().format(DATE_TIME_FORMATTER))) {
            return NOW;
        }
        return localDate.format(DATE_TIME_FORMATTER);
    }


}

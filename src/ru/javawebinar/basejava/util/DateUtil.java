package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year,month,1);
    }
    public static LocalDate of(int year, int month) {
        return LocalDate.of(year,month,1);
    }

    public static String dateFormat(LocalDate localDate) {
        if (localDate.isEqual(LocalDate.now())) {
            return "Сейчас";
        }
        return localDate.format(DateTimeFormatter.ofPattern("MM/yyyy"));
    }
}

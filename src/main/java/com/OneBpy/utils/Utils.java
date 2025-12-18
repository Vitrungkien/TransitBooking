package com.OneBpy.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Utils {
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(String date) {
        return date.isEmpty() ? null : LocalDate.parse(date);
    }

    public static int countPrevDayFromToday(LocalDate localDate) {
        LocalDate today = LocalDate.now();
        return localDate.compareTo(today);
    }
}

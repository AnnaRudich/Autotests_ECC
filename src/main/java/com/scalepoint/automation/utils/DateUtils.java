package com.scalepoint.automation.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by bza on 9/25/2017.
 */
public class DateUtils {

    public static final String ISO8601 = "yyyy-MM-dd HH:mm:SS";

    public static LocalDate getDateFromString(String date){
        return getDateFromString(date, "dd-MM-yyyy");
    }

    public static LocalDate getDateFromString(String date, String pattern){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateToString(LocalDate date){
        return localDateToString(date, "dd-MM-yyyy");
    }

    public static String localDateToString(LocalDate date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateToString(LocalDateTime date, String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}

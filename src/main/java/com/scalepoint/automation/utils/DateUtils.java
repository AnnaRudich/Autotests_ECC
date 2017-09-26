package com.scalepoint.automation.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by bza on 9/25/2017.
 */
public class DateUtils {

    public static LocalDate getDateFromString(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public static String localDateToString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}

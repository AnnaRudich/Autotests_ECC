package com.scalepoint.automation.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OperationalUtils {

    public static Double toNumber(String s) {
        String currentLocale = Configuration.getLocale().getValue();
        String value = clearString(s);
        Locale locale = Locale.GERMAN;

        switch (currentLocale) {
            case "gb":
            case "ch":
                locale = Locale.ENGLISH;
        }

        NumberFormat formatter = NumberFormat.getInstance(locale);
        try {
            return formatter.parse(value).doubleValue();
        } catch (ParseException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static String format(Double value) {
        Locale locale = new Locale.Builder().setLanguage("da").setRegion("DK").build();
        return NumberFormat.getNumberInstance(locale).format(value);
    }

    public static String clearString(String s) {
        //return s.replaceAll("[^\\d\\.]*", "");
        return s.replaceAll("[^0-9.,]+", "");
    }

    public static double getDoubleValue(String input) {
        String[] array = input.split(" ");
        return Double.parseDouble((array[array.length - 1]).replaceAll("\\.", "").replace(",", "."));
    }

    public static String unifyStr(String string) {
        return string.trim().toLowerCase().replaceAll("[-\\s+/,]", "");
    }

    public static void assertEqualsDouble(Double actualAmount, Double expectedAmount) {
        String actual = toString(actualAmount);
        String expected = toString(expectedAmount);
        assertEquals(actual, expected, String.format("Actual: %s Expected: %s", actualAmount, expectedAmount));
    }

    public static void assertEqualsDouble(Double actualAmount, Double expectedAmount, String message) {
        String actual = toString(actualAmount);
        String expected = toString(expectedAmount);
        assertEquals(actual, expected, String.format(message, actualAmount, expectedAmount));
    }

    public static void assertEqualsDoubleWithTolerance(Double actualAmount, Double expectedAmount) {
        assertEqualsDoubleWithTolerance(actualAmount,expectedAmount,"Actual: %s Expected: %s");
    }

    public static void assertEqualsDoubleWithTolerance(Double actualAmount, Double expectedAmount, String message) {
        int tolerance = 2;
        assertTrue(Math.abs(actualAmount-expectedAmount) <= Math.pow(10,-tolerance), String.format(message, actualAmount, expectedAmount) );
    }

    public static String toString(Double actualAmount) {
        return String.format("%.2f", actualAmount);
    }

    public static String toStringWithComma(Double actualAmount) {
        return String.format("%.2f", actualAmount).replace(".",",");
    }
}


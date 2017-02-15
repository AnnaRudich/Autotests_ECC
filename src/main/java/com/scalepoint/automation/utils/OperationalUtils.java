package com.scalepoint.automation.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.testng.Assert.assertEquals;

public class OperationalUtils {

    public static Double toNumber(String s) {
        Double result;
        String locale = Configuration.getLocale().getValue();
        if (locale.equalsIgnoreCase("nl")
                || locale.equalsIgnoreCase("es")) {
            s = clearString(s);
            try {
                NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
                result = formatter.parse(s).doubleValue();
            } catch (ParseException e) {
                result = -1d;
            }
        } else if (locale.equalsIgnoreCase("gb")
                || locale.equalsIgnoreCase("ch")) {
            s = clearString(s);
            try {
                NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
                result = formatter.parse(s).doubleValue();
            } catch (ParseException e) {
                result = -1d;
            }
        } else {
            try {
                NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
                return formatter.parse(s).doubleValue();
            } catch (ParseException e) {
                return -1d;
            }
        }
        return result;
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

    public static String toString(Double actualAmount) {
        return String.format("%.2f", actualAmount);
    }
}


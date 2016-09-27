package com.scalepoint.automation.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class OperationalUtils {

    public static Double doubleString(String s) {
        return Double.parseDouble(s);
    }

    public static Double getValueByPercent(Double value, Double percent) {
        return (value * percent) / 100;
    }

    public static Double toNumber(String s) {
        Double result;
        String locale = Configuration.getLocale();
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

    public static String valueToNumber(String s) {
        Double d;
        String locale = Configuration.getLocale();
        if (locale.equalsIgnoreCase("gb")
                || locale.equalsIgnoreCase("ch")) {
            s = clearString(s);
            try {
                NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);
                d = formatter.parse(s).doubleValue();
            } catch (ParseException e) {
                d = -1d;
            }
        } else {
            try {
                NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
                d = formatter.parse(s).doubleValue();
            } catch (ParseException e) {
                d = -1d;
            }
        }
        return d.toString();
    }

    public static String toPriceString(Double d) {

        String pattern = "#,###.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        if (Configuration.getLocale().toLowerCase().equals("gb")) {
            return decimalFormat.format(d).replaceAll(",00", "\\.00").replaceAll(" ", ",");
        } else {
            return decimalFormat.format(d);
        }

    }

    public static Double round(Double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String toClearString(Double d) {

        //Double d1 = round(d,2);
        return Double.toString(d).replaceAll("[^\\d\\.]*", "");
    }

    public static String toStandardFormat(String number) {
        String locale = Configuration.getLocale();
        if (locale.toLowerCase().equals("dk") || locale.toLowerCase().equals("se")
                || locale.toLowerCase().equals("nl")) {
            return number.replace(",", ".");
        } else {
            return number;
        }
    }

    public static String clearString(String s) {
        //return s.replaceAll("[^\\d\\.]*", "");
        return s.replaceAll("[^0-9.,]+", "");
    }

    public static String unifyStr(String string) {
        return string.trim().toLowerCase().replaceAll("[-\\s+/,]", "");
    }

    public static Double getCashValue(Double price, Double icDiscount) {
        return (price - (price * icDiscount) / 100);
    }

    public static Double getCashValueRD(Double price, Double icDiscount) {
        Double result = price - (price * icDiscount) / 100;
        BigDecimal bigDecimal = new BigDecimal(result);
        return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static Double getFaceValue(Double price, Double comDiscount, Double icDiscount) {
        double result = getCashValue(price, icDiscount) / (1 - comDiscount / 100);
        BigDecimal bigDecimal = new BigDecimal(result);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double getFaceValue(Double cashValue, Double discount) {
        double result = cashValue / (1 - discount / 100);
        BigDecimal bigDecimal = new BigDecimal((double) (int) result);
        return bigDecimal.setScale(2, BigDecimal.ROUND_UNNECESSARY).doubleValue();
    }

    public static Double getFaceValueRC(Double cashValue, Double discount) {
        double result = cashValue / (1 - discount / 100);
        BigDecimal bigDecimal = new BigDecimal(result);
        return bigDecimal.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
    }

    double roundResult(double d, int precise) {

        precise = 10 ^ precise;
        d = d * precise;
        int i = (int) Math.round(d);
        return (double) i / precise;

    }

    public static String getMoneyFromStringWithCurrency(String text) {
        String locale = Configuration.getLocale();
        if (locale.equalsIgnoreCase("gb")
                || locale.equalsIgnoreCase("nl")
                || locale.equalsIgnoreCase("es")
                || locale.equalsIgnoreCase("ch")) {
            return text.split(" ")[1];
        } else {
            return text.split(" ")[0];
        }
    }

    public static double getDoubleValue(String input) {
        String[] array = input.split(" ");
        return Double.parseDouble((array[array.length - 1]).replaceAll("\\.", "").replace(",", "."));
    }
}


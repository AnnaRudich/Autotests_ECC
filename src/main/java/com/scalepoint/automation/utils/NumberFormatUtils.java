package com.scalepoint.automation.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatUtils {

    public static Double formatDoubleToHaveTwoDigits(Double value){

        return formatBigDecimalToHaveTwoDigits(value).doubleValue();

    }

    public static BigDecimal formatBigDecimalToHaveTwoDigits(Double value){

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal formatBigDecimalToHaveTwoDigits(String value){

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }
}

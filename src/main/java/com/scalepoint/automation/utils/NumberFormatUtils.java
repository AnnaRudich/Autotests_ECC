package com.scalepoint.automation.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatUtils {

    public static Double formatDoubleToHaveTwoDigits(Double value){

        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();

    }
}

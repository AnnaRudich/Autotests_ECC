package com.scalepoint.automation.utils;

import java.text.DecimalFormat;

public class NumberFormatUtils {

    public static Double formatDoubleToHaveTwoDigits(Double value){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }
}

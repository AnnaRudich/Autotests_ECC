package com.scalepoint.automation.utils;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;

public class JavascriptHelper {

    private static final String DATA_JS_UTILS_JS = "js/utils.js";

    public static void initializeCommonFunctions(WebDriver driver) {
        try {
            JavascriptExecutor scriptExecutor = (JavascriptExecutor) driver;
            InputStream resourceAsStream = JavascriptHelper.class.getClassLoader().getResourceAsStream(DATA_JS_UTILS_JS);
            String script = IOUtils.toString(resourceAsStream, "UTF-8");
            scriptExecutor.executeScript(script);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

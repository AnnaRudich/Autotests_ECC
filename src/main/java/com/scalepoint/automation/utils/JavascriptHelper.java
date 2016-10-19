package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.driver.Browser;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.io.InputStream;

public class JavascriptHelper {

    public enum Snippet {
        SID_GROUPS_LOADED("sid_groups_loaded.js");

        private String fileName;

        Snippet(String fileName) {
            this.fileName = fileName;
        }
    }

    private static final String DATA_JS_UTILS_JS = "js/utils.js";
    private static final String SNIPPETS_DIR = "js/snippets/";

    public static void initializeCommonFunctions() {
        try {
            JavascriptExecutor scriptExecutor = (JavascriptExecutor) Browser.driver();
            InputStream resourceAsStream = JavascriptHelper.class.getClassLoader().getResourceAsStream(DATA_JS_UTILS_JS);
            String script = IOUtils.toString(resourceAsStream, "UTF-8");
            scriptExecutor.executeScript(script);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadSnippet(Snippet snippet) {
        try {
            JavascriptExecutor scriptExecutor = (JavascriptExecutor) Browser.driver();
            InputStream resourceAsStream = JavascriptHelper.class.getClassLoader().getResourceAsStream(SNIPPETS_DIR + snippet.fileName);
            String script = IOUtils.toString(resourceAsStream, "UTF-8");
            scriptExecutor.executeAsyncScript(script);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

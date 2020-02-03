package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

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
    private static final int DEFAULT_TIMEOUT = 30;

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

    public static boolean isImagePresent(WebElement imageElement) {
        Object result = ((JavascriptExecutor) Browser.driver()).executeScript(
                "return arguments[0].complete && " +
                        "typeof arguments[0].naturalWidth != \"undefined\" && " +
                        "arguments[0].naturalWidth > 0", imageElement);
        boolean loaded = false;
        if (result instanceof Boolean) {
            loaded = (Boolean) result;
        }
        return loaded;
    }

    public static void blur(){
        ((JavascriptExecutor) Browser.driver()).executeScript("!!document.activeElement ? document.activeElement.blur() : 0");
    }
}

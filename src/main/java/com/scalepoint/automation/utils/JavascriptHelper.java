package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static ArrayList<String> getDownloadedFiles() {

        WebDriver driver = Browser.driver();
        ArrayList filesFound;

        try {

            if(!driver.getCurrentUrl().startsWith("chrome://downloads")) {

                driver.get("chrome://downloads/");
            }

            filesFound =  (ArrayList)  ((JavascriptExecutor)driver).executeScript(
                    "return  document.querySelector('downloads-manager')  "+
                            " .shadowRoot.querySelector('#downloadsList')         "+
                            " .items.filter(e => e.state === 'COMPLETE')          "+
                            " .map(e => e.filePath || e.file_path || e.fileUrl || e.file_url); ","");

        } catch (Exception e) {

            throw  new RuntimeException(e);
        }

        return filesFound;
    }

    public static String getFileContent(String path) {

        WebDriver driver = Browser.driver();
        String fileContent;

        try {

            if(!driver.getCurrentUrl().startsWith("chrome://downloads")) {

                driver.get("chrome://downloads/");
            }

            WebElement elem = (WebElement) ((JavascriptExecutor) driver).executeScript(
                    "var input = window.document.createElement('INPUT'); "+
                            "input.setAttribute('type', 'file'); "+
                            "input.hidden = true; "+
                            "input.onchange = function (e) { e.stopPropagation() }; "+
                            "return window.document.documentElement.appendChild(input); "
                    ,"" );

            elem.sendKeys(path);

            fileContent = (String) ((JavascriptExecutor) driver).executeAsyncScript(
                    "var input = arguments[0], callback = arguments[1]; "+
                            "var reader = new FileReader(); "+
                            "reader.onload = function (ev) { callback(reader.result) }; "+
                            "reader.onerror = function (ex) { callback(ex.message) }; "+
                            "reader.readAsDataURL(input.files[0]); "+
                            "input.remove(); "
                    , elem);

            if (!fileContent.startsWith("data:")){

                throw new RuntimeException("Failed to get file content");
            }

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

        return fileContent;
    }

    public static List<ZipEntry> getZipEntries(String content) throws IOException {

        byte[] decoder = Base64.decodeBase64(content.substring(content.indexOf("base64,")+7));

        ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(decoder));

        List<ZipEntry> entries = new LinkedList<>();

        ZipEntry entry;

        while ((entry = zipStream.getNextEntry()) != null){

            entries.add(entry);
        }

        return entries;
    }
}

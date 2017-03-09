package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ExtComboBox extends ExtElement {

    public ExtComboBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * FtSelect option by visible Text
     *
     * @param visibleText option text which should be selected from list of combo box
     */
    public void select(String visibleText) {
        Object[] args = {getRootElement(), visibleText};
        String js =
                "var id = arguments[0].id," +
                        "cmp = Ext.getCmp(id)," +
                        "store = cmp.getStore()," +
                        "option = arguments[1]," +
                        "index = store.findBy(function(rec){ return rec.get(cmp.displayField).indexOf(option) > -1 });" +
                        "cmp.select(store.getAt(index));";
        ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
    }

    /**
     * FtSelect option by index
     *
     * @param index place number of option text which should be selected from list of combo box
     */
    public void select(int index) {
        Object[] args = {getRootElement(), index};
        String js =
                "var id = arguments[0].id," +
                        "cmp = Ext.getCmp(id)," +
                        "store = cmp.getStore()," +
                        "index = arguments[1];" +
                        "cmp.select(store.getAt(index));";
        ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
    }

    @SuppressWarnings("unchecked")
    public List<String> getComboBoxOptions() {
        Object[] args = {getRootElement()};
        String js =
                "var id = arguments[0].id," +
                        "cmp = Ext.getCmp(id)," +
                        "store = cmp.getStore()," +
                        "options = [];" +
                        "store.each(function(rec){ options.push(rec.get(cmp.displayField)) });" +
                        "return options;";
        return (List<String>) ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
    }

    private WebElement getInput() {
        return getWrappedElement().findElement(By.tagName("input"));
    }

    public String getValue() {
        return getInput().getAttribute("value");
    }

    public boolean isEnabled() {
        return getInput().isEnabled();
    }
}

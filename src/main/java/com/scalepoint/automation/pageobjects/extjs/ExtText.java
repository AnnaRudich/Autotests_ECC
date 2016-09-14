package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class ExtText extends ExtElement {

    public ExtText(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void enter(String text) {
        Object[] args = {getWrappedElement(), text};
        String js =
                "var id = arguments[0].id," +
                        "value = arguments[1]," +
                        "cmp = Ext.getCmp(id);" +
                        "cmp.setValue(value);" +
                        "cmp.fireEvent('blur', cmp);";
        ((JavascriptExecutor) Browser.driver()).executeScript(js, args);
    }

    public void sendKeys(CharSequence keys) {
        getWrappedElement().sendKeys(keys);
    }

    public void clear() {
        getWrappedElement().clear();
    }

    public String getText() {
        return getWrappedElement().getAttribute("value");
    }
}

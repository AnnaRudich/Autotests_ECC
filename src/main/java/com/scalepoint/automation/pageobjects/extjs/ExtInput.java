package com.scalepoint.automation.pageobjects.extjs;

import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class ExtInput extends ExtElement {

    public ExtInput(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void enter(String text) {
        getWrappedElement().clear();
        getWrappedElement().sendKeys(text);
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

    public void setValue(String value) {
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].value=arguments[1];", getWrappedElement(), value);
    }
}

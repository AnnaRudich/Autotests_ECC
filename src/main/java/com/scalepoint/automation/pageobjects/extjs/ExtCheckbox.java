package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtCheckbox extends ExtElement {

    public ExtCheckbox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    public void set(boolean state) {
        if (state != isSelected()) {
            getWrappedElement().findElement(By.tagName("input")).click();
        }
    }

    public boolean isSelected() {
        return getWrappedElement().getAttribute("class").contains("x-form-cb-checked");
    }
}

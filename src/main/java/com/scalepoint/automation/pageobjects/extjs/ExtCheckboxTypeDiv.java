package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtCheckboxTypeDiv extends ExtCheckboxType{

    public ExtCheckboxTypeDiv(WebElement wrappedElement) {
        super(wrappedElement, By.cssSelector("[data-ref='displayEl']"));
    }
}

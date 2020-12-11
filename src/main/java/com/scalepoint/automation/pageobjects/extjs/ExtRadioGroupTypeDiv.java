package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtRadioGroupTypeDiv extends ExtRadioGroupType {

    public ExtRadioGroupTypeDiv(WebElement wrappedElement) {
        super(wrappedElement, By.cssSelector("[data-ref='displayEl']"));
    }
}


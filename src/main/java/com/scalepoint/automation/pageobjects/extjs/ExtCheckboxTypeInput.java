package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtCheckboxTypeInput extends ExtCheckboxType{

    public ExtCheckboxTypeInput(WebElement wrappedElement) {
        super(wrappedElement, By.tagName("input"));
    }
}

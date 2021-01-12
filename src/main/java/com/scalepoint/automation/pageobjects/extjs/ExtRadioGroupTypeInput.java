package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtRadioGroupTypeInput extends ExtRadioGroupType {

    public ExtRadioGroupTypeInput(WebElement wrappedElement) {
        super(wrappedElement, By.tagName("input"));
    }
}


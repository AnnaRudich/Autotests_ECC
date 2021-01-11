package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AgeMonthsComboBox extends ExtComboBox{

    public AgeMonthsComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("#age-months-combobox-trigger-picker"),
                By.cssSelector("#age-months-combobox-picker [data-boundview]"));
    }
}

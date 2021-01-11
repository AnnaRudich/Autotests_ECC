package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DamageTypeComboBox extends ExtComboBox{

    public DamageTypeComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("#damage-type-combobox-trigger-picker"),
                By.cssSelector("#damage-type-combobox-picker [data-boundview]"));
    }
}

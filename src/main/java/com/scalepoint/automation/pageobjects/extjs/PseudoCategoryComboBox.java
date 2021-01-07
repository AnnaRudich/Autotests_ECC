package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PseudoCategoryComboBox extends ExtComboBox{

    public PseudoCategoryComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("#pseudocategory-combobox-picker [data-boundview]"));
    }
}

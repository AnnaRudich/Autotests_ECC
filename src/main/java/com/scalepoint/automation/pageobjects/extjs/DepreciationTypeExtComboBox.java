package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DepreciationTypeExtComboBox extends ExtComboBox{

    public DepreciationTypeExtComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("#depreciation-type-combobox-trigger-picker"),
                By.cssSelector("#depreciation-type-combobox-picker [data-boundview]"));
    }
}

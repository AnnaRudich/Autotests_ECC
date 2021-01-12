package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AddManualValuationTypeComboBox extends ExtComboBox{

    public AddManualValuationTypeComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("[data-componentid$=picker] [data-boundview]"));
    }
}

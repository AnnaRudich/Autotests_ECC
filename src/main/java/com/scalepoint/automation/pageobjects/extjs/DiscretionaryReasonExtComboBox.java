package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DiscretionaryReasonExtComboBox extends ExtComboBox{

    public DiscretionaryReasonExtComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("#discretionary-reason-combobox-picker [data-boundview]"));
    }
}

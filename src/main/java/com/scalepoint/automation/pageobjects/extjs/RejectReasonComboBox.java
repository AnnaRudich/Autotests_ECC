package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RejectReasonComboBox extends ExtComboBox{

    public RejectReasonComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("#reject-reason-combobox-trigger-picker"),
                By.cssSelector("#reject-reason-combobox-picker [data-boundview]"));
    }
}

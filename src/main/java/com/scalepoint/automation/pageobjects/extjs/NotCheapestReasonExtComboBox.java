package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class NotCheapestReasonExtComboBox extends ExtComboBox{

    public NotCheapestReasonExtComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("#not-cheapest-reason-combo-picker [data-boundview]"));
    }
}

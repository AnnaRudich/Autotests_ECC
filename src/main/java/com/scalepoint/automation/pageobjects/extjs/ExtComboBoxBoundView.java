package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtComboBoxBoundView extends ExtComboBox{

    public ExtComboBoxBoundView(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("[data-componentid$=picker] [data-boundview]"));
    }
}

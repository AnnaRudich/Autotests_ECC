package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtComboBoxDivBoundList extends ExtComboBox {

    public ExtComboBoxDivBoundList(WebElement wrappedElement) {

        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector(".x-boundlist-item"));
    }
}

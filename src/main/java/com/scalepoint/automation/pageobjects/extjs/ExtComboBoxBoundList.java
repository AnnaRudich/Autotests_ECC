package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ExtComboBoxBoundList extends ExtComboBox {

    public ExtComboBoxBoundList(WebElement wrappedElement) {

        super(
                wrappedElement,
                By.cssSelector("[role=button]"),
                By.cssSelector(".x-boundlist-item"));
    }
}

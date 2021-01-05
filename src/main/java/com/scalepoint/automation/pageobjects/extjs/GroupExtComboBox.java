package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class GroupExtComboBox extends ExtComboBox {

    public GroupExtComboBox(WebElement wrappedElement) {

        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("#group-combobox-picker .x-boundlist-item"));
    }
}

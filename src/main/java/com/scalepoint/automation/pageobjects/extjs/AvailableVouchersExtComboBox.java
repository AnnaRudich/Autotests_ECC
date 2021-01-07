package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AvailableVouchersExtComboBox extends ExtComboBox {

    public AvailableVouchersExtComboBox(WebElement wrappedElement) {

        super(
                wrappedElement,
                By.cssSelector("[id$=trigger-picker]"),
                By.cssSelector("#available-vouchers-combobox-picker .x-boundlist-item"));
    }
}

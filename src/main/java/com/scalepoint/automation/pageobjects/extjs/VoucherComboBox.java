package com.scalepoint.automation.pageobjects.extjs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class VoucherComboBox extends ExtComboBox{

    public VoucherComboBox(WebElement wrappedElement) {
        super(
                wrappedElement,
                By.cssSelector("#vouchers-combobox-trigger-picker"),
                By.cssSelector("#vouchers-combobox-picker [data-boundview]"));
    }
}

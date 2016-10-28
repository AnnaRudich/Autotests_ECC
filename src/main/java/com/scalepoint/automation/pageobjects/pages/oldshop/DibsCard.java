package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DibsCard extends Page {

    @FindBy(name = "cardno")
    private WebElement cardNumberField;

    @FindBy(name = "expmon")
    private WebElement expMonthInput;

    @FindBy(name = "expyear")
    private WebElement expYearInput;

    @FindBy(id = "cardHolderName")
    private WebElement cardHolderNameField;

    @FindBy(name = "cvc")
    private WebElement cvcField;

    @FindBy(xpath = "//input[@name='pay']")
    private WebElement submitButton;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "https://payment.architrade.com/payment/card.pfml";
    }

    public DibsAccept submitCardData(String number, String month, String year, String cvc) {
        setValue(cardNumberField, number);
        setValue(expMonthInput, month);
        setValue(expYearInput, year);
        setValue(cvcField, cvc);
        submitButton.click();
        return at(DibsAccept.class);
    }
}

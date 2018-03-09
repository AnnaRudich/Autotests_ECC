package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

public class DibsPayType extends Page {

    @FindBy(xpath = "//a[contains(text(),'MasterCard')]")
    private WebElement masterCardOption;

    @FindBy(xpath = "//a[contains(text(),'Dankort')]")
    private WebElement dankortOption;

    @FindBy(xpath = "//input[@name='pay']")
    private WebElement submitButton;

    @FindBy(xpath = "//input[@name='finish']")
    private WebElement continueOption;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "https://payment.architrade.com/payment/paytype.pml";
    }

    public DibsCard selectDankortOption() {
        dankortOption.click();
        return at(DibsCard.class);
    }
}

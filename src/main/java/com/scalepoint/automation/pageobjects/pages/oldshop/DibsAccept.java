package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DibsAccept extends Page {

    @FindBy(xpath = "//input[@name='finish']")
    private WebElement continueOption;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "https://payment.architrade.com/payment/accept.pml";
    }

    public DibsSuccessPage acceptAndBackToShop() {
        continueOption.click();
        return at(DibsSuccessPage.class);
    }
}

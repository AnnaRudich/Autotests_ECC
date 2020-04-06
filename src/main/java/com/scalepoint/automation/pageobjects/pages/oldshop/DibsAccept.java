package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DibsAccept extends Page {

    @FindBy(xpath = "//input[@name='finish']")
    private WebElement continueOption;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
    }

    @Override
    protected String getRelativeUrl() {
        return "https://payment.architrade.com/payment/accept.pml";
    }

    public CustomerDetailsPage acceptAndBackToEcc() {
        continueOption.click();
        return at(CustomerDetailsPage.class);
    }

    public OrderConfirmationPage accept(){
        continueOption.click();
        return at(OrderConfirmationPage.class);
    }
}

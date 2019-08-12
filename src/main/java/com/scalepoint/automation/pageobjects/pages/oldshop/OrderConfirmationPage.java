package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class OrderConfirmationPage extends Page {

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement checkout_button;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(checkout_button);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "/webshop/jsp/shop/order_confirmation.jsp";
    }
}

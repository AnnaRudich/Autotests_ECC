package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@EccPage
public class ShopDepositPage extends Page {

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement checkOutButton;

    @FindBy(xpath = "//input[@id='deposit_method_external_creditcard']")
    private WebElement creditCardBox;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(checkOutButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/deposit.jsp";
    }

    public ShopOrderVerificationPage selectBankTransfer() {
        creditCardBox.click();
        checkOutButton.click();
        return at(ShopOrderVerificationPage.class);
    }

    public ShopOrderVerificationPage selectCreditCard() {
        creditCardBox.click();
        checkOutButton.click();
        return at(ShopOrderVerificationPage.class);
    }
}

package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.ClaimSpecificPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
@ClaimSpecificPage
public class ShopDepositPage extends Page {

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement checkOutButton;

    @FindBy(xpath = "//input[@id='deposit_method_external_creditcard']")
    private WebElement creditCardBox;

    @FindBy(xpath = "//input[@id='deposit_method_banktransfer']")
    private WebElement bankTransferBox;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(checkOutButton);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/deposit.jsp";
    }

    public ShopOrderVerificationPage selectBankTransfer() {
        bankTransferBox.click();
        checkOutButton.click();
        return at(ShopOrderVerificationPage.class);
    }

    public ShopOrderVerificationPage selectCreditCard() {
        creditCardBox.click();
        checkOutButton.click();
        return at(ShopOrderVerificationPage.class);
    }
}

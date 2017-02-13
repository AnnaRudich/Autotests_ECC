package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class ShopShoppingCartPage extends Page {

    @FindBy(xpath = "//div[@class='checkout_button'][1]/a")
    private WebElement checkOutButton;


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(checkOutButton);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/shopping_cart.jsp";
    }

    /**
     * The method selects Check Out option and waits for Order Verification page is displayed
     */
    public ShopCashPayoutPage toCashPayoutPage() {
        checkOutButton.click();
        return at(ShopCashPayoutPage.class);
    }

    public ShopOrderVerificationPage toOrderVerificationPage() {
        checkOutButton.click();
        return at(ShopOrderVerificationPage.class);
    }

    public ShopDepositPage toDepositPage() {
        checkOutButton.click();
        return at(ShopDepositPage.class);
    }
}


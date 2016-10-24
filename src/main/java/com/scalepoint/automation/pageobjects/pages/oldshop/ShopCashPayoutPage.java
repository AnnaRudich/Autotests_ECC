package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Radio;

@EccPage
public class ShopCashPayoutPage extends Page {

    @FindBy(xpath=".//a[contains(@onclick, 'CashPayout')]")
    private WebElement proceedCheckout;

    @FindBy(id = "withdrawal_option_keep_on_account")
    private Radio keepMoneyOnAccount;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(proceedCheckout);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/shop/cash_payout.jsp";
    }

    public ShopOrderVerificationPage keepMoneyOnAccountAndProceed() {
        keepMoneyOnAccount.click();
        proceedCheckout.click();
        return at(ShopOrderVerificationPage.class);
    }
}

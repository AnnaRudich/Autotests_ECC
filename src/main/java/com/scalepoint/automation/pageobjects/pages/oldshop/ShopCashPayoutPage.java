package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Radio;

import java.util.Random;

@EccPage
public class ShopCashPayoutPage extends Page {

    @FindBy(xpath = ".//a[contains(@onclick, 'CashPayout')]")
    private WebElement proceedCheckout;

    @FindBy(id = "withdrawal_option_keep_on_account")
    private Radio keepMoneyOnAccount;

    @FindBy(xpath = "//div[@class='WithdrawalView']/div[@class='checkout_button']/a")
    private WebElement checkoutWithdrawButton;

    @FindBy(xpath = "//div[@class='DepositView']/div[@class='checkout_button']/a")
    private WebElement checkoutDepositButton;

    @FindBy(xpath = "//input[@id='withdrawal_option_partial_withdrawal']")
    private WebElement withdrawRadioButton;

    @FindBy(xpath = "//input[@id='withdrawal_method_banktransfer']")
    private WebElement bankTransferRadioButton;

    @FindBy(id = "WithdrawalView_regNumber")
    private WebElement bankSortCode;

    @FindBy(id = "WithdrawalView_accountNumber")
    private WebElement accountNumber;

    @FindBy(id = "withdrawal_method_cheque")
    private WebElement checkOption;


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

    public ShopOrderVerificationPage withdrawMoney() {
        Wait.waitForDisplayed(By.xpath("//input[@id='withdrawal_option_partial_withdrawal']"));
        withdrawRadioButton.click();
        selectBankTransferAndAddBankInfo();
        checkoutWithdrawButton.click();
        return at(ShopOrderVerificationPage.class);
    }

    public void selectBankTransferAndAddBankInfo() {
        bankTransferRadioButton.click();
        bankTransferRadioButton.click();
        bankSortCode.sendKeys(Integer.toString(new Random().nextInt(100000000)));
        accountNumber.sendKeys(Integer.toString(new Random().nextInt(100000000)));
    }
}

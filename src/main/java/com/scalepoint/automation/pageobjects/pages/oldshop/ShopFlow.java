package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.modules.oldshop.AccountBox;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.data.entity.payments.Dankort;

@SuppressWarnings("AccessStaticViaInstance")
public abstract class ShopFlow extends Page {

    public CustomerDetailsPage checkoutProduct() {
        return getAccountBox().
                toShoppingCart().
                toOrderVerificationPage().
                selectAgreeOption().
                selectPlaceMyOrderOption();
    }

    public CustomerDetailsPage checkoutProductWithdrawalWizard() {
        return submitWithdrawalWizard().
                selectPlaceMyOrderOption();
    }

    public OrderConfirmationPage checkoutProductWithdrawalMail() {
        return submitWithdrawalWizard().
                selectPlaceMyOrderOptionMail();
    }

    public ShopOrderVerificationPage submitWithdrawalWizard(){
        return getAccountBox().
                toShoppingCart().
                toCashPayoutPage().
                withdrawMoney().
                selectAgreeOption();
    }

    public CustomerDetailsPage checkoutWithBankTransfer() {
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectBankTransfer().
                selectAgreeOption().
                selectPlaceMyOrderOption();
    }

    public CustomerDetailsPage checkoutWithCreditCardWizard(Dankort dankort) {
        return submitWithCreditCard(dankort).
                acceptAndBackToEcc();
    }

    public OrderConfirmationPage checkoutWithCreditCardMail(Dankort dankort){
        return submitWithCreditCard(dankort)
                .accept();
    }

    public DibsAccept submitWithCreditCard(Dankort dankort){
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectCreditCard().
                selectAgreeOption().
                toDIBSPage().
                selectDankortOption().
                submitCardData(dankort.getNumber(), dankort.getExpMonth(), dankort.getExpYear(), dankort.getCvc());
    }

    protected abstract AccountBox getAccountBox();
}

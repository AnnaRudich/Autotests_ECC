package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.modules.oldshop.AccountBox;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;

@SuppressWarnings("AccessStaticViaInstance")
public abstract class ShopFlow extends Page {

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


    protected abstract AccountBox getAccountBox();
}

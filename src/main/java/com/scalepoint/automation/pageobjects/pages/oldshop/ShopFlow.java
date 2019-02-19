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

    public CustomerDetailsPage checkoutProductWithdrawal() {
        return getAccountBox().
                toShoppingCart().
                toCashPayoutPage().
                withdrawMoney().
                selectAgreeOption().
                selectPlaceMyOrderOption();
    }

    public CustomerDetailsPage checkoutWithBankTransfer() {
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectBankTransfer().
                selectAgreeOption().
                selectPlaceMyOrderOption();
    }

    public CustomerDetailsPage checkoutWithCreditCard(Dankort dankort) {
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectCreditCard().
                selectAgreeOption().
                toDIBSPage().
                selectDankortOption().
                submitCardData(dankort.getNumber(), dankort.getExpMonth(), dankort.getExpYear(), dankort.getCvc()).
                acceptAndBackToEcc();
    }

    protected abstract AccountBox getAccountBox();
}

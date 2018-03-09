package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.modules.oldshop.AccountBox;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;

@SuppressWarnings("AccessStaticViaInstance")
public abstract class ShopFlow extends Page {

    public MyPage checkoutProduct() {
        return getAccountBox().
                toShoppingCart().
                toOrderVerificationPage().
                selectAgreeOption().
                selectPlaceMyOrderOption().
                to(MyPage.class);
    }

    public MyPage checkoutProductWithdrawal() {
        return getAccountBox().
                toShoppingCart().
                toCashPayoutPage().
                withdrawMoney().
                selectAgreeOption().
                selectPlaceMyOrderOption().
                to(MyPage.class);
    }

    public CustomerDetailsPage checkoutWithBankTransfer() {
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectBankTransfer().
                selectAgreeOption().
                selectPlaceMyOrderOption();
    }

    public CustomerDetailsPage checkoutWithCreditCard(String number, String month, String year, String cvc) {
        return getAccountBox().
                toShoppingCart().
                toDepositPage().
                selectCreditCard().
                selectAgreeOption().
                toDIBSPage().
                selectDankortOption().
                submitCardData(number, month, year, cvc).
                acceptAndBackToEcc();
    }

    protected abstract AccountBox getAccountBox();
}

package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopProductSearchPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.OrderDetails;
import com.scalepoint.automation.utils.data.entity.TextSearch;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.payments.Dankort;
import com.scalepoint.automation.utils.data.entity.payments.Payments;
import org.testng.Assert;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
public class OrderDetailsTests extends BaseTest {

    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * THEN: There 5 lines with 0,00 amount each:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  0,00
     * Scalepoint har betalt til leverandør (Varekøb) :  0,00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Verify Order page existance")
    public void charlie540_ordersPageIsEmpty(User user, Claim claim, OrderDetails orderDetails) {
        String companyName = user.getCompanyName();

        OrderDetailsPage ordersPage = loginAndCreateClaim(user, claim).
                toOrdersDetailsPage();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(companyName));
        Assert.assertEquals(ordersPage.getIdemnityValue(), 0.0d, "Idemnity value is not 0");

        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());
        Assert.assertEquals(ordersPage.getOrderedItemsValue(), 0.0d, "Ordered value is 0");

        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0d, "Withdraw value is 0");

        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());
        Assert.assertEquals(ordersPage.getDepositValue(), 0.0d, "Deposits value is 0");

        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());
        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0d, "Remaining value is 0");
    }

    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN: Add voucher on settlement page (Face value=900)
     * WHEN: Go to shop and buy voucher
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  900.00
     * Scalepoint har betalt til leverandør (Varekøb) :  900.00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Make product order")
    public void charlie540_ordersPageWhenWeBuyVoucher(User user, Claim claim, ClaimItem claimItem, OrderDetails orderDetails) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .fillCategory(claimItem.getCategoryBorn())
                .fillSubCategory(claimItem.getSubcategoryBornBabyudstyr())
                .fillNewPrice(900.00)
                .fillDescription(claimItem.getTextFieldSP());

        Double activeValuation = dialog.getCashCompensationValue();
        OrderDetailsPage ordersPage = dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .addFirstRecommendedItemToCart()
                .checkoutProduct()
                .openRecentClaim()
                .toOrdersDetailsPage();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(Math.abs(ordersPage.getIdemnityValue() - activeValuation), 0.0, "Idemnity value " + ordersPage.getIdemnityValue() + " must be equal to cashValue " + activeValuation + " of the voucher");

        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());
        Assert.assertEquals((ordersPage.getOrderedItemsValue() - activeValuation), 0.0, "Ordered value must be equal cashValue of the voucher");

        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value must be 0");

        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());
        Assert.assertEquals(ordersPage.getDepositValue(), 0.0, "Deposits value must be 0");

        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());
        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value must be 0");
    }


    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN:  Add cash on settlement page = 5000
     * WHEN: Go to shop and buy product for 300 kr
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  5000.00
     * Scalepoint har betalt til leverandør (Varekøb) :  300.00
     * Scalepoint har betalt til kunde (Udbetalinger) :  4700,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Make voucher order")
    public void charlie540_ordersPageWhenWeWithdrawMoney(User user, Claim claim, ClaimItem claimItem, OrderDetails orderDetails) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .fillCategory(claimItem.getCategoryBorn())
                .fillSubCategory(claimItem.getSubcategoryBornBabyudstyr())
                .fillNewPrice(5000.00)
                .fillDescription(claimItem.getTextFieldSP())
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE);

        Double activeValuation = dialog.getCashCompensationValue();
        ShopProductSearchPage searchPage = dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .toProductSearchPage();

        double productPrice = searchPage.getProductPrice(1);
        OrderDetailsPage ordersPage = searchPage
                .addProductToCart(1)
                .checkoutProductWithdrawal()
                .openRecentClaim()
                .toOrdersDetailsPage();

        double withdrawValue = activeValuation - productPrice;

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));

        Assert.assertEquals(ordersPage.getIdemnityValue() - activeValuation, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") must be equal to price=" + activeValuation);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());

        Assert.assertEquals(ordersPage.getOrderedItemsValue() - productPrice, 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " must be product price=" + productPrice);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());

        Assert.assertEquals(ordersPage.getWithdrawValue() - withdrawValue, 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") must be equals to " + withdrawValue);
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());

        Assert.assertEquals(ordersPage.getDepositValue(), 0.0, "Deposits value(" + ordersPage.getDepositValue() + " must be 0");
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());

        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " must be 0");
    }


    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN: Settlement page is empty
     * WHEN: Go to shop and buy product
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  0
     * Scalepoint har betalt til leverandør (Varekøb) :  350
     * Scalepoint har betalt til kunde (Udbetalinger) :  0
     * Kunde har betalt til Scalepoint (Indbetalinger) :  350
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CC-4202 ME: Order page; Order: excess amount")
    public void charlie540_ordersPageWhenWeUseBankAccount(User user, Claim claim, Payments payments, OrderDetails orderDetails) {
        ShopProductSearchPage shopProductSearchPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .toProductSearchPage();

        Double productPrice = shopProductSearchPage.getProductPrice(0);
        Dankort dankort = payments.getDankort();
        OrderDetailsPage ordersPage = shopProductSearchPage
                .addProductToCart(0)
                .checkoutWithBankTransfer(dankort.getNumber(), dankort.getExpMonth(), dankort.getExpYear(), dankort.getCvc())
                .toOrdersDetailsPage();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));

        Assert.assertEquals(ordersPage.getIdemnityValue(), 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is 0");
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());

        Assert.assertEquals(ordersPage.getOrderedItemsValue() - productPrice, 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " is product price=" + productPrice);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());

        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());

        Assert.assertEquals(ordersPage.getDepositValue() - productPrice, 0.0, "Deposits value(" + ordersPage.getDepositValue() + " is equal to " + productPrice);
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());

        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is 0");
    }

    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN:  Add product on settlement page = 129
     * WHEN: Go to shop and buy product
     * WHEN: Complete claim with/without mail
     * THEN: The state on Order page is the following:
     * Tryg har betalt til Scalepoint (Erstatning) :  129,00
     * Scalepoint har betalt til leverandør (Varekøb) :  129,00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Complete claim (2)")
    public void charlie540_ordersPageWhenWeRecompleteAfterCreditCardPayment(User user, Claim claim, OrderDetails orderDetails, Payments payments) {
        ShopProductSearchPage shopProductSearchPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .toProductSearchPage();

        Double productPrice = shopProductSearchPage.getProductPrice(0);
        Dankort dankort = payments.getDankort();
        OrderDetailsPage ordersPage = shopProductSearchPage
                .addProductToCart(0)
                .checkoutWithBankTransfer(dankort.getNumber(), dankort.getExpMonth(), dankort.getExpYear(), dankort.getCvc())
                .reopenClaim()
                .toCompleteClaimPage()
                .completeWithEmail()
                .openRecentClaim()
                .toOrdersDetailsPage();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));

        Assert.assertEquals(ordersPage.getIdemnityValue(), 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is 0");
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());

        Assert.assertEquals(ordersPage.getOrderedItemsValue() - productPrice, 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " is product price=" + productPrice);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());

        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());

        Assert.assertEquals(ordersPage.getDepositValue() - productPrice, 0.0, "Deposits value(" + ordersPage.getDepositValue() + " is equal to " + productPrice);
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());

        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is 0");
    }


    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN:  Add cash on settlement page = 5000
     * WHEN: Go to shop and buy product for 300 kr
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  5000.00
     * Scalepoint har betalt til leverandør (Varekøb) :  300.00
     * Scalepoint har betalt til kunde (Udbetalinger) :  4700,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Cancel order")
    public void charlie540_6_ordersPageWhenWeCancelOrder(User user, Claim claim, OrderDetails orderDetails, TextSearch textSearch) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage(textSearch.getCatProduct1())
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .replaceAllItems()
                .toOrdersDetailsPage()
                .cancelItem();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is equal to price=" + price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());
        Assert.assertEquals(ordersPage.getOrderedItemsValue(), 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " is 0");
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());
        Assert.assertEquals(ordersPage.getDepositValue(), 0.0, "Deposits value(" + ordersPage.getDepositValue() + " is 0");
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());
        Assert.assertEquals(ordersPage.getRemainingValue() - price, 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is equal to " + price);
    }


    /**
     * GIVEN: SP User
     * WHEN: User navigates to Order page
     * WHEN:  Add product on settlement page = 129
     * WHEN: Go to shop and buy product
     * WHEN: Complete claim with/without mail
     * THEN: The state on Order page is the following:
     * Tryg har betalt til Scalepoint (Erstatning) :  129,00
     * Scalepoint har betalt til leverandør (Varekøb) :  129,00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0,00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0,00
     * Tilbageværende erstatning :  0,00
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Complete claim (1)")
    public void charlie540_ordersPageWhenWeRecompleteAfterOrder(User user, Claim claim, OrderDetails orderDetails, TextSearch textSearch) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage(textSearch.getCatProduct1())
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .toProductSearchPage()
                .searchForProduct(textSearch.getCatProduct1())
                .addProductToCart(0)
                .checkoutProduct()
                .openRecentClaim()
                .reopenClaim()
                .toCompleteClaimPage()
                .completeWithEmail()
                .openRecentClaim()
                .toOrdersDetailsPage();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value("+ordersPage.getIdemnityValue()+") is equal to price="+price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItems());
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawalls());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value("+ordersPage.getWithdrawValue()+") is equals to 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDeposits());
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingIdemnity());
        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value("+ordersPage.getRemainingValue()+" is 0");
    }
}

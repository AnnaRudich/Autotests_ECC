package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopProductSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.translations.OrderDetails;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.INVOICE_PRICE_LOWER_THAN_MARKET_PRICE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.ORDERABLE;
import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY_FALSE;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-540")
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
            description = "CHARLIE-540 ME: Order page; Verify Order page default")
    public void charlie540_ordersPageIsEmpty(User user, Claim claim, Translations translations) {
        String companyName = user.getCompanyName();

        OrderDetailsPage ordersPage = loginAndCreateClaim(user, claim).
                toOrdersDetailsPage();

        OrderDetails orderDetails = translations.getOrderDetails();

        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(companyName));
        Assert.assertEquals(ordersPage.getIdemnityValue(), 0.0d, "Idemnity value is not 0");

        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        Assert.assertEquals(ordersPage.getOrderedItemsValue(), 0.0d, "Ordered value is 0");

        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0d, "Withdraw value is 0");

        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
        Assert.assertEquals(ordersPage.getDepositValue(), 0.0d, "Deposits value is 0");

        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0d, "Remaining value is 0");
    }

    /**
     * GIVEN: SP User
     * WHEN: User creates claim
     * WHEN:  Add manual line on settlement page = 5000
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
            description = "CHARLIE-540 ME: Order page; Make product order using shop product search")
    public void charlie540_ordersPageWhenWeWithdrawMoney(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_LOWER_THAN_MARKET_PRICE));

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(productInfo.getInvoicePrice() + 1000)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensationValue();
        ShopProductSearchPage searchPage = dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .goToShop()
                .toProductSearchPage();

        int productIndex = 0;
        ShopProductSearchPage searchForProductPage = searchPage
                .searchForProduct(productInfo.getModel());
        double productPrice = searchPage.getProductPrice(productIndex);
        OrderDetailsPage ordersPage = searchForProductPage
                .addProductToCart(productIndex)
                .checkoutProductWithdrawalWizard()
                .toOrdersDetailsPage();

        double withdrawValue = activeValuation - productPrice;

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));

        Assert.assertEquals(ordersPage.getIdemnityValue() - activeValuation, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") must be equal to price=" + activeValuation);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());

        Assert.assertEquals(ordersPage.getOrderedItemsValue() - productPrice, 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " must be product price=" + productPrice);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());

        Assert.assertEquals(ordersPage.getWithdrawValue() - withdrawValue, 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") must be equals to " + withdrawValue);
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());

        Assert.assertEquals(ordersPage.getDepositValue(), 0.0, "Deposits value(" + ordersPage.getDepositValue() + " must be 0");
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());

        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " must be 0");
    }



    @Test(dataProvider = "testDataProvider",
            description = "CC-4202 ME: Order page; Order: excess amount. Bank transfer")
    public void charlie540_ordersPageWhenWeUseBankTransfer(User user, Claim claim, Translations translations) {
        ShopProductSearchPage shopProductSearchPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .goToShop()
                .toProductSearchPage();

        Double productPrice = shopProductSearchPage.getProductPrice(0);
        OrderDetailsPage ordersPage = shopProductSearchPage
                .addProductToCart(0)
                .checkoutWithBankTransfer()
                .toOrdersDetailsPage();

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));

        Assert.assertEquals(ordersPage.getIdemnityValue(), 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is 0");
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());

        Assert.assertEquals(ordersPage.getOrderedItemsValue() - productPrice, 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " is product price=" + productPrice);
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());

        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());

        Assert.assertEquals(ordersPage.getDepositValue() - productPrice, 0.0, "Deposits value(" + ordersPage.getDepositValue() + " is equal to " + productPrice);
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());

        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is 0");
    }


    /**
     * GIVEN: SP User
     * WHEN: User creates claim
     * WHEN:  Add line on settlement 949.00
     * WHEN: Make an order using Replacement
     * WHEN: Cancel order
     * THEN: The state on Order page is the following:
     * Scalepoint har betalt til Scalepoint (Erstatning) :  949.00
     * Scalepoint har betalt til leverandør (Varekøb) :  0.00
     * Scalepoint har betalt til kunde (Udbetalinger) :  0.00
     * Kunde har betalt til Scalepoint (Indbetalinger) :  0.00
     * Tilbageværende erstatning :  949.00
     */
    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Cancel order")
    public void charlie540_6_ordersPageWhenWeCancelOrder(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimItem.getSetDialogTextMatch())
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .replaceAllItems()
                .toOrdersDetailsPage()
                .cancelItem();

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is equal to price=" + price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        Assert.assertEquals(ordersPage.getOrderedItemsValue(), 0.0, "Ordered value(" + ordersPage.getOrderedItemsValue() + " is 0");
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
        Assert.assertEquals(ordersPage.getDepositValue(), 0.0, "Deposits value(" + ordersPage.getDepositValue() + " is 0");
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        Assert.assertEquals(ordersPage.getRemainingValue() - price, 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is equal to " + price);
    }

    @RequiredSetting(type = FTSetting.CPR_NUMBER_ON_REPLACEMENT_REQUIRED, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, enabled = false)
    @RequiredSetting(type = FTSetting.DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-540 ME: Order page; Recomplete claim")
    public void charlie540_ordersPageWhenWeRecompleteAfterOrder(User user, Claim claim, ClaimItem claimItem, Translations translations) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimItem.getSetDialogTextMatch())
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct();
        Double price = settlementDialog.getCashCompensationValue();

        OrderDetailsPage ordersPage = settlementDialog.closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .replaceAllItems()
                .reopenClaim()
                .toCompleteClaimPage()
                .completeWithEmail(claim, databaseApi, false)
                .openRecentClaim()
                .toOrdersDetailsPage();

        OrderDetails orderDetails = translations.getOrderDetails();
        Assert.assertEquals(ordersPage.getLegendItemText(), orderDetails.getTotalText());
        Assert.assertEquals(ordersPage.getIdemnityText(), orderDetails.getIndemnity(user.getCompanyName()));
        Assert.assertEquals(ordersPage.getIdemnityValue() - price, 0.0, "Idemnity value(" + ordersPage.getIdemnityValue() + ") is equal to price=" + price);
        Assert.assertEquals(ordersPage.getOrderedItemsText(), orderDetails.getOrderedItemsText());
        Assert.assertEquals(ordersPage.getWithdrawText(), orderDetails.getWithdrawallsText());
        Assert.assertEquals(ordersPage.getWithdrawValue(), 0.0, "Withdraw value(" + ordersPage.getWithdrawValue() + ") is equals to 0");
        Assert.assertEquals(ordersPage.getDepositText(), orderDetails.getDepositsText());
        Assert.assertEquals(ordersPage.getRemainingIdemnityText(), orderDetails.getRemainingCompensationText());
        Assert.assertEquals(ordersPage.getRemainingValue(), 0.0, "Remaining value(" + ordersPage.getRemainingValue() + " is 0");
    }
}



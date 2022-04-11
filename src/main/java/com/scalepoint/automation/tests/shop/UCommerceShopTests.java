package com.scalepoint.automation.tests.shop;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.ucommerce.CreateOrderService;
import com.scalepoint.automation.services.ucommerce.GetBalanceService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
public class UCommerceShopTests extends BaseTest {

    private final Double orderedProductPrice = Constants.PRICE_100;
    private final Double orderedVoucherPrice = Constants.PRICE_100;
    private final Double extraPayAmount = Constants.PRICE_50;

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProduct_positiveBalance(User user, Claim claim, ClaimItem claimItem) {

        XpriceInfo productInfo = getXPriceInfoForProduct();

        SettlementDialog dialog = loginAndCreateClaim(user, claim)
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new CreateOrderService().createOrderForProduct(productInfo, claim.getClaimNumber());



        new OrderDetailsPage()
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage -> {
                    orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedProductPrice);//voucher
                    orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProductVoucherOnly(User user, Claim claim, ClaimItem claimItem) {

        XpriceInfo productInfo = getXpricesForConditions(DatabaseApi.PriceConditions.PRODUCT_AS_VOUCHER_ONLY, DatabaseApi.PriceConditions.ORDERABLE);

        SettlementDialog dialog = loginAndCreateClaim(user, claim)
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new CreateOrderService().createOrderForProduct(productInfo, claim.getClaimNumber());



        new OrderDetailsPage()
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage -> {
                    orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedProductPrice);//voucher
                    orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProductWithExtraPay(User user, Claim claim, ClaimItem claimItem) {
        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new CreateOrderService().createOrderForProductExtraPay(voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new OrderDetailsPage()
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage -> {
                    orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedProductPrice);
                    orderDetailsPage.assertAmountScalepointHasPaidToSupplier(orderedProductPrice+extraPayAmount);
                    orderDetailsPage.assertAmountCustomerHasPaidToScalepoint(extraPayAmount);
                    orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "create order with physical voucher and verify orderTotals")
    public void orderPhysicalVoucher(User user, Claim claim, ClaimItem claimItem) {
        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new CreateOrderService().createOrderForVoucher(voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new OrderDetailsPage()
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage -> {
                    orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedVoucherPrice);
                    orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "create order with Evoucher and verify orderTotals")
    public void orderEVoucher(User user, Claim claim, ClaimItem claimItem) {
        Boolean isEvoucher = true;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new CreateOrderService().createOrderForVoucher(voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new OrderDetailsPage()
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage -> {
                    orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedVoucherPrice);
                    orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint")
    public void verifyGetBalance(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true);

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(activeValuation);
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @RequiredSetting(type = FTSetting.SETTLE_WITHOUT_MAIL)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint for cancelled claim")
    public void verifyGetBalanceCancelledClaim(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double balance = dialog.getCashCompensation();

        MyPage myPage = dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithoutEmail();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        myPage.openRecentClaim()
                .cancelClaim();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(0.0);
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint for cancelled order")
    public void verifyGetBalanceCancelledItem(User user, Claim claim, ClaimItem claimItem) {

        XpriceInfo productInfo = getXPriceInfoForProduct();

        SettlementDialog dialog = loginAndCreateClaim(user, claim)
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double balance = dialog.getCashCompensation();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toOrdersDetailsPage();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        new CreateOrderService().createOrderForProduct(productInfo, claim.getClaimNumber());

        OrderDetailsPage orderDetailsPage =  Page.at(OrderDetailsPage.class)
                .refreshPageToGetOrders()
                .doAssert(orderDetailsPage1 -> {
                    orderDetailsPage1.assertRemainingCompensationTotal(balance - orderedProductPrice);//voucher
                    orderDetailsPage1.assertCompensationAmount(balance);
                });

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(0.0);

        orderDetailsPage
                .showOrder()
                .cancelItemByIndex(0)
                .addInternalNote("Autotest", OrderDetailsPage.class);

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint for reopened claim")
    public void verifyGetBalanceReopenClaim(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double balance = dialog.getCashCompensation();

        MyPage myPage = dialog
                .closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true);

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        settlementPage = myPage
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        dialog = settlementPage
                .openSidAndFill(sid ->
                        sid
                                .withCategory(claimItem.getCategoryMobilePhones())
                                .withNewPrice(300.00)
                                .withText("Second item")
                                .withValuation(NEW_PRICE)
                );

        balance = balance + dialog.getCashCompensation();

        dialog
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, false);

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);
    }

    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @RequiredSetting(type = FTSetting.SETTLE_WITHOUT_MAIL)
    @Test(groups = {TestGroups.SHOP, TestGroups.UCOMMERCE_SHOP}, dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint for saved claim")
    public void verifyGetBalanceSavedClaim(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double balance = dialog.getCashCompensation();

        MyPage myPage = dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithoutEmail();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        settlementPage = myPage
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);

        dialog = settlementPage
                .openSidAndFill(sid ->
                        sid
                                .withCategory(claimItem.getCategoryMobilePhones())
                                .withNewPrice(300.00)
                                .withText("Second item")
                                .withValuation(NEW_PRICE)
                );

        dialog
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .saveClaim(false);

        new GetBalanceService()
                .getBalance(claim.getClaimNumber())
                .assertBalanceIs(balance);
    }
}

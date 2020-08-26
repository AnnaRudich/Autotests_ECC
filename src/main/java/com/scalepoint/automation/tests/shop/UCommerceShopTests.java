package com.scalepoint.automation.tests.shop;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.ucommerce.CreateOrderService;
import com.scalepoint.automation.services.ucommerce.GetBalanceService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
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
    @Test(dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProduct_positiveBalance(User user, Claim claim, ClaimItem claimItem) {

        XpriceInfo productInfo = getXPriceInfoForProduct();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensationValue();

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
    @Test(dataProvider = "testDataProvider",
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

        Double activeValuation = dialog.getCashCompensationValue();

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
    @Test(dataProvider = "testDataProvider",
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

        Double activeValuation = dialog.getCashCompensationValue();

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
    @Test(dataProvider = "testDataProvider",
            description = "verify data received from getBalance endpoint")
    public void verifyGetBalance(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(100.00)
                .setDescription(claimItem.getTextFieldSP())
                .setValuation(NEW_PRICE);

        Double activeValuation = dialog.getCashCompensationValue();

        dialog.closeSidWithOk(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true);
                new GetBalanceService()
                        .getBalance(claim.getClaimNumber())
                        .assertBalanceIs(activeValuation);
        }
}

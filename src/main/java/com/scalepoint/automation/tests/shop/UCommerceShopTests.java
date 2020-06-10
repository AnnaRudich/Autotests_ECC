package com.scalepoint.automation.tests.shop;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.CreateOrderService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
public class UCommerceShopTests extends BaseTest {

    private final Double orderedProductPrice = Constants.PRICE_100;
    private final Double orderedVoucherPrice = Constants.PRICE_100;
    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProduct_positiveBalance(User user, Claim claim, ClaimItem claimItem) {

        XpriceInfo productInfo = getXPriceInfoForProduct();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(900.00)
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
                        orderDetailsPage.assertRemainingCompensationTotal(activeValuation - orderedProductPrice);
                        orderDetailsPage.assertCompensationAmount(activeValuation);
                });
    }
    @FeatureToggleSetting(type = FeatureIds.JAXBUTILS_USE_SCHEMAS, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "create order with physical voucher and verify orderTotals")
    public void orderPhisicalVoucher(User user, Claim claim, ClaimItem claimItem) {
        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        SettlementDialog dialog = settlementPage
                .openSid()
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(900.00)
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
}

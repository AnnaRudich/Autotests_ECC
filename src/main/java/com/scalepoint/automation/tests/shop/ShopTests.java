package com.scalepoint.automation.tests.shop;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.OrderDetailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.CreateOrderService;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ProductToOrderInShop;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;

public class ShopTests extends BaseTest {
    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderProduct(User user, Claim claim, ClaimItem claimItem, ProductToOrderInShop productToOrderInShop) {

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
                .doAssert(orderDetailsPage -> orderDetailsPage
                        .assertRemainingCompensationTotal(activeValuation - Constants.PRICE_100));


    }

    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(dataProvider = "testDataProvider",
            description = "create order with product and verify orderTotals")
    public void orderVoucher(User user, Claim claim, ClaimItem claimItem, ProductToOrderInShop productToOrderInShop) {

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

        new CreateOrderService().createOrderForVoucher(claim.getClaimNumber());

        new OrderDetailsPage()
                .doAssert(orderDetailsPage -> orderDetailsPage
                        .assertRemainingCompensationTotal(activeValuation - Constants.PRICE_100));
    }
}

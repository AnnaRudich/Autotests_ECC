package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;
import static com.scalepoint.automation.utils.Constants.DEPRECIATION_10;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-529")
public class SettlementSummaryTests extends BaseTest {

    /**
     * GIVEN: Existing group G1, category C1 and New voucher V1, existing depreciation D1
     * WHEN: User adds claim line with G1, C1, V1 and D1
     * WHEN: and navigates to Settlement Summary page
     * THEN: Result Value is correct
     * THEN: Result Total New Price is correct
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3034 Settlement Summary Result value and Total New Price result value are correct for claim with depreciated claim line")
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3034_setSummaryCheck(User user, Claim claim, ClaimItem item, Voucher voucher) {
        SidCalculator.VoucherValuationWithDepreciation voucherValuation =
                SidCalculator.calculateVoucherValuation(PRICE_2400, Constants.VOUCHER_DISCOUNT_10, Constants.DEPRECIATION_10);
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(item.getCategoryBabyItems())
                            .withNewPrice(PRICE_2400)
                            .withDepreciation(DEPRECIATION_10)
                            .withVoucher(voucher.getExistingVoucher_10());
                })
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(summary -> {
                    summary.assertClaimSumValueIs(voucherValuation.getCashCompensationWithDepreciation());
                });
    }

    /**
     * GIVEN: Existing group G1, category C1 and New voucher V1, existing depreciation D1, existing product P1
     * WHEN: User adds claim line with G1, C1, and V1, D1
     * WHEN: and adds product P1
     * WHEN: and navigates to Settlement Summary page
     * THEN: Result Value is correct
     * THEN: Result Total New Price is correct
     * WHEN: user navigates to settlement page
     * WHEN: and remove V1 claim line
     * WHEN: and navigates to Settlement Summary page
     * THEN: Result Value is correct
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3034 Settlement Summary Result value is correct for multi string claim")
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3034_setSummaryCheckMultipleItems(User user, Claim claim, ClaimItem item, Voucher voucher) {
        SidCalculator.VoucherValuationWithDepreciation voucherValuation =
                SidCalculator.calculateVoucherValuation(PRICE_2400, Constants.VOUCHER_DISCOUNT_10, Constants.DEPRECIATION_10);
        ProductInfo productInfo = SolrApi.findProduct(getXpricesForConditions(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE, INVOICE_PRICE_EQUALS_MARKET_PRICE));

        double lowestPrice = productInfo.getLowestPrice();

        double totalPrice = BigDecimal.valueOf(voucherValuation.getCashCompensationWithDepreciation() + lowestPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(item.getCategoryBabyItems())
                            .withNewPrice(PRICE_2400)
                            .withDepreciation(DEPRECIATION_10)
                            .withVoucher(voucher.getExistingVoucher_10());
                })
                .closeSidWithOk()
                .toTextSearchPage()
                .searchBySku(productInfo.getSku())
                .openSidForFirstProduct()
                .closeSidWithOk()
                .getSettlementSummary()
                .doAssert(summary -> {
                    summary.assertClaimSumValueIs(totalPrice);
                });
    }


}

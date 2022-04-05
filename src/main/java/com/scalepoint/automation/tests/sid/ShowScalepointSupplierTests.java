package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-589")
@RequiredSetting(type = FTSetting.SHOW_MARKET_PRICE)
public class ShowScalepointSupplierTests extends BaseTest {

    /**
     * GIVEN: FT "Show Scalepoint Supplier" OFF
     * AND: ClaimHandler(CH) created/opened claim
     * AND: Product Price selected as valuation
     * WHEN: CH add Product from the Catalog
     * THEN: Scalepoint Supplier name is not displayed in the SID
     */
    @Test(groups = {TestGroups.SID, TestGroups.SHOW_SCALEPOINT_SUPPLIER},
            dataProvider = "testDataProvider",
            description = "CHARLIE-589 FT 'Show Scalepoint Supplier' is OFF, Scalepoint Supplier name is not displayed in the SID")
    @RequiredSetting(type = FTSetting.SHOW_SCALEPOINT_SUPPLIER, enabled = false)
    public void charlie_589_1_showScalepointSupplierNameDisabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .doAssert(sid ->
                        sid.assertScalepointSupplierNotVisible());
    }

    /**
     * GIVEN: FT "Show Scalepoint Supplier" ON
     * AND: ClaimHandler(CH) created/opened claim
     * AND: Product Price selected as valuation
     * WHEN: CH add Product from the Catalog
     * THEN: Scalepoint Supplier name is displayed in the SID
     */
    @RequiredSetting(type = FTSetting.SHOW_SCALEPOINT_SUPPLIER)
    @Test(groups = {TestGroups.SID, TestGroups.SHOW_SCALEPOINT_SUPPLIER},
            dataProvider = "testDataProvider",
            description = "CHARLIE-589 FT 'Show Scalepoint Supplier' is ON, Scalepoint Supplier name is displayed in the SID")
    public void charlie_589_2_showScalepointSupplierNameEnabled(User user, Claim claim, ClaimItem claimItem) {
        TextSearchPage textSearchPage = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst();
        ProductInfo product = SolrApi.findProduct(textSearchPage.getFirstProductId());

        textSearchPage
                .openSidForFirstProduct()
                .doAssert(sid -> sid.assertScalepointSupplierVisible(product.getSupplierName()));
    }
}

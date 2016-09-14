package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FT;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ShowMarketPricesTests extends BaseTest {

    /**
     * GIVEN: FT "Show Market Price" OFF
     * WHEN: ClaimHandler(CH) created claim
     * AND: CH search for Product from the Catalog
     * THEN: Market Price column not displays on Test Search page
     * THEN: Market Price value not displays on Product Details page
     * THEN: Market Price is unavailable on the Product's Best Fit page
     * THEN: Market Price supplier not displays on Product Details page
     */
    @Test(description = "CHARLIE-588 Show Market Price (off)", dataProvider = "testDataProvider")
    public void charlie_588_1_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);
        updateFT(user, LoginPage.class, FT.disable(FTSetting.SHOW_MARKET_PRICE));
        assertFalse(isMarketPriceVisible(user, claim, claimItem), "Market Price is Visible");
    }

    @Test(description = "CHARLIE-588 Show Market Price (on)", dataProvider = "testDataProvider")
    public void charlie_588_2_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        enableNewSid(user);
        updateFT(user, LoginPage.class, FT.enable(FTSetting.SHOW_MARKET_PRICE));
        assertTrue(isMarketPriceVisible(user, claim, claimItem), "Market Price is Visible");
    }

    private boolean isMarketPriceVisible(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat1()).
                isMarketPriceVisible();
    }
}

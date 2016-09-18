package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.SettingRequired;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.listeners.FuncTemplatesListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Listeners({FuncTemplatesListener.class})
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
    @SettingRequired(type = FTSetting.SHOW_MARKET_PRICE, enabled = false)
    public void charlie_588_1_showMarketPriceDisabled(User user, Claim claim, ClaimItem claimItem) {
        assertFalse(isMarketPriceVisible(user, claim, claimItem), "Market Price is Visible");
    }

    @Test(description = "CHARLIE-588 Show Market Price (on)", dataProvider = "testDataProvider")
    @SettingRequired(type = FTSetting.SHOW_MARKET_PRICE)
    public void charlie_588_2_showMarketPriceEnabled(User user, Claim claim, ClaimItem claimItem) {
        assertTrue(isMarketPriceVisible(user, claim, claimItem), "Market Price is Visible");
    }

    private boolean isMarketPriceVisible(User user, Claim claim, ClaimItem claimItem) {
        return loginAndCreateClaim(user, claim).
                findInCatalogue().
                chooseCategory(claimItem.getExistingCat1_Born()).
                isMarketPriceVisible();
    }
}

package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.RequiredValuationIsNeededDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

/**
 * @author : igu
 */
@Jira("https://jira.scalepoint.com/browse/CHARLIE-625")
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_3RD_VALUATION_FIELD)
@RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
public class MandatoryNewPriceTests extends BaseUITest {

    @Test(groups = {TestGroups.SID, TestGroups.MANDATORY_NEW_PRICE},
            dataProvider = "testDataProvider",
            description = "CHARLIE-625 When required valuation is not set then SID closes without popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "Select valuation type...")
    public void charlie625WhenRequiredValuationIsNotSetThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(Constants.PRICE_100_000)
                            .withNewPrice(Constants.PRICE_2400)
                            .withDepreciation(Constants.DEPRECIATION_10)
                            .withCategory(claimItem.getCategoryBabyItems())
                            .withDiscretionaryPrice(48.00);
                })
                .closeSidWithOk();
    }

    @Test(groups = {TestGroups.SID, TestGroups.MANDATORY_NEW_PRICE},
            dataProvider = "testDataProvider",
            description = "CHARLIE-625 When Required Valuation Is Set To New Price And No New Price Entered Then Popup Is Shown")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndNoNewPriceEnteredThenPopupIsShown(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(Constants.PRICE_500)
                            .withCategory(claimItem.getCategoryBabyItems())
                            .withDiscretionaryPrice(48.00);
                })
                .tryToCloseSidWithOkButExpectDialog(RequiredValuationIsNeededDialog.class);
    }

    @Test(groups = {TestGroups.SID, TestGroups.MANDATORY_NEW_PRICE},
            dataProvider = "testDataProvider",
            description = "CHARLIE-625 When Required Valuation Is Set To New Price And Catalog Item Has Market Price SID Closes Without Popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndCatalogItemHasMarketPriceSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setCustomerDemand(Constants.PRICE_500)
                .closeSidWithOk();
    }

    @Test(groups = {TestGroups.SID, TestGroups.MANDATORY_NEW_PRICE},
            dataProvider = "testDataProvider",
            description = "CHARLIE-625 When Required Valuation Is Set To New Price And New Price Entered Then SID Closes Without Popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndNewPriceEnteredThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(Constants.PRICE_500)
                            .withNewPrice(Constants.PRICE_2400)
                            .withCategory(claimItem.getCategoryBabyItems())
                            .withDiscretionaryPrice(48.00);
                })
                .closeSidWithOk();
    }

}

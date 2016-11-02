package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.RequiredValuationIsNeededDialog;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

/**
 * @author : igu
 */
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class MandatoryNewPriceTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-625 When required valuation is not set then SID closes without popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "Select valuation type...")
    @RequiredSetting(type = FTSetting.ENABLE_3RD_VALUATION_FIELD)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    public void charlie625WhenRequiredValuationIsNotSetThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim).
                addManually().
                fillBaseData(claimItem).
                fillDepreciationValue(48)
                .ok();
    }

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-625 When Required Valuation Is Set To New Price And No New Price Entered Then Popup Is Shown")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @RequiredSetting(type = FTSetting.ENABLE_3RD_VALUATION_FIELD)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndNoNewPriceEnteredThenPopupIsShown(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand_500()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillDepreciationValue(48).
                isDialogShownAfterOk(RequiredValuationIsNeededDialog.class);
    }

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-625 When Required Valuation Is Set To New Price And Catalog Item Has Market Price SID Closes Without Popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @RequiredSetting(type = FTSetting.ENABLE_3RD_VALUATION_FIELD)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndCatalogItemHasMarketPriceSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim).
                toTextSearchPage().
                chooseCategory(claimItem.getExistingCat3_Telefoni()).
                sortOrderableFirst().
                matchFirst().
                fillCustomerDemand(claimItem.getCustomerDemand_500()).
                ok();
    }

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-625 When Required Valuation Is Set To New Price And New Price Entered Then SID Closes Without Popup")
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @RequiredSetting(type = FTSetting.ENABLE_3RD_VALUATION_FIELD)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    public void charlie625WhenRequiredValuationIsSetToNewPriceAndNewPriceEnteredThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim).
                addManually().
                fillDescription(claimItem.getTextFieldSP()).
                fillCustomerDemand(claimItem.getCustomerDemand_500()).
                fillNewPrice(claimItem.getNewPriceSP_2400()).
                fillCategory(claimItem.getExistingCat1_Born()).
                fillSubCategory(claimItem.getExistingSubCat1_Babyudstyr()).
                fillDepreciationValue(48).
                ok();
    }

}

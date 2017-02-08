package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.NotCheapestChoiceDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author : igu
 */
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP)
public class NotCheapestChoiceTests extends BaseTest {

    /*09*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Not Minimal Valuation Is Selected Then Minimal Valuation Is Suggested")
    public void charlie530WhenNotMinimalValuationIsSelectedThenMinimalValuationIsSuggested(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class)
                .assertMinimalValuationIsSuggested("1.00");
    }

    /*10*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Selected reason is stored")
    public void charlie530SelectedReasonIsStored(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        String selectedReason = selectFirstNotCheapestReason(claimItem, settlementPage);
        settlementPage
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine()
                .assertNotCheapestReasonIs(selectedReason);
    }

    /*11*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Minimal Valuation Is Selected Then Sid Closes Without Popup")
    public void charlie530WhenMinimalValuationIsSelectedThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .selectValuation(SettlementDialog.Valuation.CUSTOMER_DEMAND)
                .closeSidWithOk();
    }

    /*12*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Discretionary Depreciated Price")
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    public void charlie530MinimalValuationIsSuggestedInCaseOfDiscretionaryDepreciatedPrice(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .fillDepreciation(50)
                .selectDepreciationType(0)
                .selectDepreciationType(1)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class)
                .assertMinimalValuationIsSuggested("0.50");
    }

    /*13*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Policy Depreciated Price")
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    public void charlie530MinimalValuationIsSuggestedInCaseOfPolicyDepreciatedPrice(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .fillDepreciation(50)
                .selectDepreciationType(1)
                .selectDepreciationType(0)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class)
                .assertMinimalValuationIsSuggested("0.50");
    }

    /*14*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Not Possible To Not Select The Reason")
    public void charlie530NotPossibleToNotSelectTheReason(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class)
                .assertNotPossibleToCloseDialog();
    }

    /*16*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Item From Catalog")
    public void charlie530MinimalValuationIsSuggestedInCaseOfItemFromCatalog(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getExistingCat3_Telefoni())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .fillNewPrice(1)
                .selectValuation(SettlementDialog.Valuation.MARKET_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class);
    }

    /*17*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Generic Item")
    public void charlie530MinimalValuationIsSuggestedInCaseOfGenericItem(User user, Claim claim, GenericItem genericItem) {
        loginAndCreateClaim(user, claim)
                .savePoint(SettlementPage.class)
                .to(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, user.getCompanyName(), true)
                .backToSavePoint(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .findClaimLine(genericItem.getName())
                .editLine()
                .fillNewPrice(1)
                .fillCustomerDemand(48)
                .selectValuation(SettlementDialog.Valuation.CUSTOMER_DEMAND)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class);
    }

    /*21*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When reason is set then edit button appears and reason can be changed using popup")
    public void charlie530WhenReasonIsSetThenEditButtonAppearsAndReasonCanBeChangedUsingPopup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        selectFirstNotCheapestReason(claimItem, settlementPage);

        SettlementDialog settlementDialog = settlementPage
                .findClaimLine(claimItem.getTextFieldSP())
                .editLine();
        NotCheapestChoiceDialog notCheapestChoiceDialog = settlementDialog.editNotCheapestReason();
        String updatedReason = notCheapestChoiceDialog.selectSecondReason();
        notCheapestChoiceDialog.ok();

        assertEquals(updatedReason, settlementDialog.getNotCheapestChoiceReason(), "Reason is not updated");
    }

    private String selectFirstNotCheapestReason(ClaimItem claimItem, SettlementPage settlementPage) {
        NotCheapestChoiceDialog notCheapestChoiceDialog = settlementPage
                .openAddManuallyDialog()
                .fillBaseData(claimItem)
                .fillNewPrice(48)
                .fillCustomerDemand(1)
                .selectValuation(SettlementDialog.Valuation.NEW_PRICE)
                .assertAfterOkWeGet(NotCheapestChoiceDialog.class);

        String selectedReason = notCheapestChoiceDialog.selectAndGetFirstReasonValue();
        notCheapestChoiceDialog.okGoToSettlementPage();

        return selectedReason;
    }

}

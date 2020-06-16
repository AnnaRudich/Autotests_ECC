package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.NotCheapestChoiceDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.DepreciationType;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.*;
import static org.testng.Assert.assertEquals;

/**
 * @author : igu
 */
@Jira("https://jira.scalepoint.com/browse/CHARLIE-530")
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP)
public class NotCheapestChoiceTests extends BaseTest {

    /*09*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Not Minimal Valuation Is Selected Then Minimal Valuation Is Suggested")
    public void charlie530WhenNotMinimalValuationIsSelectedThenMinimalValuationIsSuggested(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> prepareBaseFiller(claimItem, sid).withValuation(NEW_PRICE))
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class)
                .doAssert(notCheapestDialog -> notCheapestDialog.assertMinimalValuationIsSuggested(1.00));
    }

    /*10*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Selected reason is stored")
    public void charlie530SelectedReasonIsStored(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);
        String selectedReason = selectFirstNotCheapestReason(claimItem, settlementPage);
        settlementPage
                .findClaimLine(Constants.TEXT_LINE)
                .editLine()
                .doAssert(sid -> sid.assertNotCheapestReasonIs(selectedReason));
    }

    /*11*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Minimal Valuation Is Selected Then Sid Closes Without Popup")
    public void charlie530WhenMinimalValuationIsSelectedThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> prepareBaseFiller(claimItem, sid).withValuation(CUSTOMER_DEMAND))
                .closeSidWithOk();
    }

    /*12*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Discretionary Depreciated Price")
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled = false, isDefault = true)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    public void charlie530MinimalValuationIsSuggestedInCaseOfDiscretionaryDepreciatedPrice(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    prepareBaseFiller(claimItem, sid)
                            .withDepreciation(50, DepreciationType.DISCRETIONARY)
                            .withValuation(NEW_PRICE);
                })
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class)
                .doAssert(notCheapestDialog -> notCheapestDialog.assertMinimalValuationIsSuggested(0.50));
    }

    /*13*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Policy Depreciated Price")
    @RequiredSetting(type = FTSetting.DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, enabled = false, isDefault = true)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    public void charlie530MinimalValuationIsSuggestedInCaseOfPolicyDepreciatedPrice(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    prepareBaseFiller(claimItem, sid)
                            .withDepreciation(50, DepreciationType.POLICY)
                            .withValuation(NEW_PRICE);
                })
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class)
                .doAssert(notCheapestDialog -> notCheapestDialog.assertMinimalValuationIsSuggested(0.50));
    }

    /*14*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Not Possible To Not Select The Reason")
    public void charlie530NotPossibleToNotSelectTheReason(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    prepareBaseFiller(claimItem, sid)
                            .withValuation(NEW_PRICE);
                })
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class)
                .doAssert(NotCheapestChoiceDialog.Asserts::assertNotPossibleToCloseDialog);
    }

    /*16*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Item From Catalog")
    public void charlie530MinimalValuationIsSuggestedInCaseOfItemFromCatalog(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .chooseCategory(claimItem.getCategoryMobilePhones())
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .setNewPrice(1.00)
                .setValuation(MARKET_PRICE)
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class);
    }

    /*17*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Minimal Valuation Is Suggested In Case Of Generic Item")
    public void charlie530MinimalValuationIsSuggestedInCaseOfGenericItem(User user, Claim claim, GenericItem genericItem) {
        genericItem.setPrice("10");
        loginAndCreateClaim(user, claim)
                .savePoint(SettlementPage.class)
                .to(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, user.getCompanyName(), true)
                .backToSavePoint(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .findClaimLine(genericItem.getName())
                .editLine()
                .setCustomerDemand(48.00)
                .setValuation(CUSTOMER_DEMAND)
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class);
    }

    /*21*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When reason is set then edit button appears and reason can be changed using popup")
    public void charlie530WhenReasonIsSetThenEditButtonAppearsAndReasonCanBeChangedUsingPopup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        selectFirstNotCheapestReason(claimItem, settlementPage);

        SettlementDialog settlementDialog = settlementPage
                .findClaimLine(Constants.TEXT_LINE)
                .editLine();
        NotCheapestChoiceDialog notCheapestChoiceDialog = settlementDialog.editNotCheapestReason();
        String updatedReason = notCheapestChoiceDialog.selectSecondReason();
        notCheapestChoiceDialog.ok();

        assertEquals(updatedReason, settlementDialog.getNotCheapestChoiceReason(), "Reason is not updated");
    }

    private String selectFirstNotCheapestReason(ClaimItem claimItem, SettlementPage settlementPage) {
        NotCheapestChoiceDialog notCheapestChoiceDialog = settlementPage
                .openSidAndFill(sid -> prepareBaseFiller(claimItem, sid).withValuation(NEW_PRICE))
                .tryToCloseSidWithOkButExpectDialog(NotCheapestChoiceDialog.class);

        String selectedReason = notCheapestChoiceDialog.selectAndGetFirstReasonValue();
        notCheapestChoiceDialog.okGoToSettlementPage();

        return selectedReason;
    }

    private SettlementDialog.FormFiller prepareBaseFiller(ClaimItem claimItem, SettlementDialog.FormFiller sid) {
        return sid
                .withCustomerDemandPrice(1.00)
                .withNewPrice(48.00)
                .withCategory(claimItem.getCategoryBabyItems());
    }

}

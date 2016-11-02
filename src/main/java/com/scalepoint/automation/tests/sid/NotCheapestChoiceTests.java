package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.NotCheapestChoiceDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author : igu
 */
public class NotCheapestChoiceTests extends BaseTest {

    /*09*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Not Minimal Valuation Is Selected Then Minimal Valuation Is Suggested")
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP)
    public void charlie530WhenNotMinimalValuationIsSelectedThenMinimalValuationIsSuggested(User user, Claim claim, ClaimItem claimItem) {
        NotCheapestChoiceDialog notCheapestChoiceDialog = loginAndCreateClaim(user, claim).
                addManually().
                fillBaseData(claimItem).
                fillNewPrice(48).
                fillCustomerDemand(1).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                isDialogShownAfterOk(NotCheapestChoiceDialog.class);

        String suggestedAmount = notCheapestChoiceDialog.getAmount();

        boolean minimalValuationIsSuggested = "1.00".equals(suggestedAmount);

        assertTrue(minimalValuationIsSuggested);
    }

    /*10*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 Selected reason is stored")
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP)
    public void charlie530SelectedReasonIsStored(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim);

        NotCheapestChoiceDialog notCheapestChoiceDialog = settlementPage.
                addManually().
                fillBaseData(claimItem).
                fillNewPrice(48).
                fillCustomerDemand(1).
                selectValuation(SettlementDialog.Valuation.NEW_PRICE).
                isDialogShownAfterOk(NotCheapestChoiceDialog.class);

        String selectedReason = notCheapestChoiceDialog.selectFirstReason();
        notCheapestChoiceDialog.okGoToSettlementPage();

        SettlementDialog settlementDialog = settlementPage.editClaimLine(claimItem.getTextFieldSP());

        assertEquals(selectedReason, settlementDialog.getNotCheapestChoiceReason(), "Reason is not stored");
    }

    /*11*/
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-530 When Minimal Valuation Is Selected Then Sid Closes Without Popup")
    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP)
    public void charlie530WhenMinimalValuationIsSelectedThenSidClosesWithoutPopup(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim).
                addManually().
                fillBaseData(claimItem).
                fillNewPrice(48).
                fillCustomerDemand(1).
                selectValuation(SettlementDialog.Valuation.CUSTOMER_DEMAND).
                ok();
    }

}

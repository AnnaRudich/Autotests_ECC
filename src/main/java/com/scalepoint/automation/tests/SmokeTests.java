package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.enable;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class SmokeTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3032 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void ecc3032_reopenSavedClaim(User user, Claim claim) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim).
                saveClaim().
                openRecentClaim().
                reopenClaim();

        assertTrue(settlementPage.isSettlementPagePresent(), "Settlement page is not loaded");
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3032, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void ecc3032_2629_completeClaimWithMail(User user, Claim claim) {

        MyPage myPage = loginAndCreateClaim(user, claim).
                completeClaim().
                fillClaimForm(claim).
                completeWithEmail();

        assertTrue(myPage.isRecentClaimCompleted(claim), "Claim should have completed status");
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3032 It's possible to save claim without completing from Enter base info page. " +
                    "Saved claim is added to the latest claims list with Saved status")
    public void ecc3032_saveClaimFromBaseInfo(User user, Claim claim) {

        MyPage myPage = loginAndCreateClaim(user, claim).
                completeClaim().
                fillClaimForm(claim).
                saveClaim();

        assertTrue(myPage.isRecentClaimSaved(claim), "Claim should have status saved");
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-3256, ECC-3050 It's possible to login to Self Service from email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void ecc3256_3050_loginToSelfService(User user, Claim claim) {
        String password = "12341234";

        loginAndCreateClaim(user, claim).
                requestSelfService(claim, password).
                toMailsPage().
                openWelcomeCustomerMail().
                findSelfServiceLinkAndOpenIt().
                enterPassword(password).
                login();
    }

    @Bug(bug = "CHARLIE-479")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3256, ECC-3050 It's possible add note on Settlement page")
    public void ecc3256_3050_addInternalAndCustomerNotes(User user, Claim claim) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);

        FtOperation[] enableOperations = {
                enable(FTSetting.USE_INTERNAL_NOTES),
                enable(FTSetting.SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON),
                enable(FTSetting.SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON)
        };

        FtOperation[] disableOperations = {
                disable(FTSetting.USE_INTERNAL_NOTES),
                disable(FTSetting.SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON),
                disable(FTSetting.SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON)
        };

        String customerNote = "Customer note!";
        String internalNote = "Internal note!";

        loginAndCreateClaim(user, claim);

        NotesPage notesPage = functionalTemplatesApi.
                updateTemplate(user.getFtId(), SettlementPage.class, enableOperations).
                addCustomerNote(customerNote).
                addInternalNote(internalNote);

        assertTrue(notesPage.isCustomerNotesPresent(customerNote), "Customer Note has not been added");
        assertTrue(notesPage.isInternalNotesPresent(internalNote), "Internal note has not been added");

        functionalTemplatesApi.updateTemplate(user.getFtId(), NotesPage.class, disableOperations);

        assertTrue(notesPage.isEditCustomerNoteButtonPresent(), "Edit Customer Note button is not visible");
        assertFalse(notesPage.isInternalNoteHeaderPresent(), "Internal Note field is visible");
        assertFalse(notesPage.isAddInternalNoteButtonPresent(), "Add Internal Note button is visible");

        functionalTemplatesApi.updateTemplate(user.getFtId(), NotesPage.class, enableOperations);

        assertTrue(notesPage.isEditCustomerNoteButtonPresent(), "Edit Customer Note button is not visible");
        assertTrue(notesPage.isInternalNotePresent(), "Internal Note field is not visible");
        assertTrue(notesPage.isAddInternalNoteButtonDisplayed(), "Add Internal Note button is not visible");
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-2631 It's possible to matchFirst product via Quick matchFirst icon for Excel imported claim lines")
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void ecc2631_quickMatchFromExcel(User user, Claim claim, ClaimItem claimItem) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim).importExcelFile(claimItem.getExcelPath1());

        assertTrue(settlementPage.isItemPresent(claimItem.getXlsDescr1()), "The claim item is not found");

        settlementPage.selectClaimItemByDescription(claimItem.getSetDialogTextMatch()).
                getToolBarMenu().
                productMatch().
                sortSearchResults().
                match(claimItem.getSetDialogTextMatch()).
                cancel();
    }
}

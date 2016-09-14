package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.NotesPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.log4j.MDC;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.scalepoint.automation.services.externalapi.ftemplates.FT.*;
import static org.testng.Assert.assertTrue;

public class SmokeTests extends BaseTest {

    @Test(description = "ECC-3032 It's possible to reopen saved claim. Settlement is displayed for reopened claim", dataProvider = "testDataProvider")
    public void ecc3032_reopenSavedClaim(User user, Claim claim) {

        SettlementPage settlementPage = loginAndCreateClaim(user, claim).
                saveClaim().
                openRecentClient().
                reopenClaim();

        assertTrue(settlementPage.isSettlementPagePresent(), "Settlement page is not loaded");
    }


    @Test(description = "ECC-3032, ECC-2629 It's possible to complete claim with mail. Completed " +
            "claim is added to the latest claims list with Completed status", dataProvider = "testDataProvider")
    public void ecc3032_2629_completeClaimWithMail(User user, Claim claim) {

        MyPage myPage = loginAndCreateClaim(user, claim).
                completeClaim().
                fillClaimForm(claim).
                completeWithEmail();

        assertTrue(myPage.isRecentClaimCompleted(claim), "Claim should have completed status");
    }

    @Test(description = "ECC-3032 It's possible to save claim without completing from Enter base info page. " +
            "Saved claim is added to the latest claims list with Saved status", dataProvider = "testDataProvider")
    public void ecc3032_saveClaimFromBaseInfo(User user, Claim claim) {

        MyPage myPage = loginAndCreateClaim(user, claim).
                completeClaim().
                fillClaimForm(claim).
                saveClaim();

        assertTrue(myPage.isRecentClaimSaved(claim), "Claim should have status saved");
    }

    @Test(description = "ECC-3256, ECC-3050 It's possible to login to Self Service from email", dataProvider = "testDataProvider")
    public void ecc3256_3050_loginToSelfService(User user, Claim claim) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);
        functionalTemplatesApi.updateTemplate(user.getFtId(),
                LoginPage.class,
                disable(FTSetting.USE_SELF_SERVICE2),
                enable(FTSetting.ENABLE_SELF_SERVICE),
                enable(FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
        );


        String password = "12341234";
        loginAndCreateClaim(user, claim).
                requestSelfService(claim, password).
                toMailsPage().
                viewLastMail().
                findSelfServiceLinkAndOpenIt().
                enterPassword(password).
                login();
    }

    @Bug(bug = "CHARLIE-479")
    @Test(description = "ECC-3256, ECC-3050 It's possible add note on Settlement page", dataProvider = "testDataProvider")
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

        Assert.assertTrue(notesPage.isCustomerNotesPresent(customerNote), "Customer Note has not been added");
        Assert.assertTrue(notesPage.isInternalNotesPresent(internalNote), "Internal note has not been added");

        functionalTemplatesApi.updateTemplate(user.getFtId(), NotesPage.class, disableOperations);

        Assert.assertTrue(notesPage.isEditCustomerNoteButtonPresent(), "Edit Customer Note button is not visible");
        Assert.assertFalse(notesPage.isInternalNoteHeaderPresent(), "Internal Note field is visible");
        Assert.assertFalse(notesPage.isAddInternalNoteButtonPresent(), "Add Internal Note button is visible");

        functionalTemplatesApi.updateTemplate(user.getFtId(), NotesPage.class, enableOperations);

        Assert.assertTrue(notesPage.isEditCustomerNoteButtonPresent(), "Edit Customer Note button is not visible");
        Assert.assertTrue(notesPage.isInternalNotePresent(), "Internal Note field is not visible");
        Assert.assertTrue(notesPage.isAddInternalNoteButtonDisplayed(), "Add Internal Note button is not visible");
    }

    @Test(description = "ECC-2631 It's possible to match product via Quick match icon for Excel imported claim lines", dataProvider = "testDataProvider")
    public void ecc2631_quickMatchFromExcel(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim);

        SettlementPage settlementPage = new FunctionalTemplatesApi(user)
                .updateTemplate(user.getFtId(), SettlementPage.class,
                        enable(FTSetting.BEST_FIT_FOR_NONORDERABLE_PRODUCTS),
                        enable(FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT),
                        setValue(FTSetting.NUMBER_BEST_FIT_RESULTS, "5"),
                        select(FTSetting.ALLOW_NONORDERABLE_PRODUCTS, "Yes, Always")
                );

        settlementPage.importExcelFile(claimItem.getExcelPath1());

        assertTrue(settlementPage.isItemPresent(claimItem.getXlsDescr1()), "The claim item is not found");

        settlementPage.
                selectClaimItemByDescription(claimItem.getSetDialogTextMatch()).
                getToolBarMenu().
                productMatch().
                sortSearchResults().
                match(claimItem.getSetDialogTextMatch()).
                cancel();
    }

}

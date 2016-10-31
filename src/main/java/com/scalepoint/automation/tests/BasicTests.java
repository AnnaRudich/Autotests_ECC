package com.scalepoint.automation.tests;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.*;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.Page.to;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.enable;

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
public class BasicTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void charlie544_reopenSavedClaim(User user, Claim claim) {
        loginAndCreateClaim(user, claim).
                saveClaim().
                openRecentClaim().
                reopenClaim().
                assertSettlementPagePresent("Settlement page is not loaded");
    }

    /**
     * GIVEN: SP User, saved claim C1
     * WHEN: User cancels C1
     * THEN: "Cancelled" is the status of C1
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void charlie544_cancelSavedClaim(User user, Claim claim) throws Exception {
        boolean recentClaimCancelled = loginAndCreateClaim(user, claim).
                saveClaim().
                openRecentClaim().
                cancelClaim().
                to(MyPage.class).
                isRecentClaimCancelled();
        Assert.assertTrue(recentClaimCancelled);
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void charlie544_2629_completeClaimWithMail(User user, Claim claim) {
        loginAndCreateClaim(user, claim).
                toCompleteClaimPage().
                fillClaimForm(claim).
                completeWithEmail().
                assertClaimHasStatus(claim.getStatusCompleted());
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void charlie544_2629_completeClaimWithoutMail(User user, Claim claim) {
        loginAndCreateClaim(user, claim).
                toCompleteClaimPage().
                fillClaimForm(claim).
                completeWithoutEmail().
                assertClaimHasStatus(claim.getStatusClosedEx());
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to save claim without completing from Enter base info page. " +
                    "Saved claim is added to the latest claims list with Saved status")
    public void charlie544_saveClaimFromBaseInfo(User user, Claim claim) {

        loginAndCreateClaim(user, claim).
                toCompleteClaimPage().
                fillClaimForm(claim).
                saveClaim().
                assertClaimHasStatus(claim.getStatusSaved());
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


    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void charlie544_not_possible_login_to_cancelled_claim(User user, Claim claim) {
        String password = "12341234";

        CustomerDetailsPage customerDetailsPage = loginAndCreateClaim(user, claim).
                toCompleteClaimPage().
                fillClaimForm(claim).
                completeWithEmail().
                openRecentClaim();
        String loginToShopLink = customerDetailsPage.
                toMailsPage().
                openWelcomeCustomerMail().
                findLoginToShopLink();

        customerDetailsPage.toCustomerDetails().cancelClaim();

        Browser.driver().get(loginToShopLink);
        Page.at(LoginShopPage.class).
                enterPassword(password).
                loginWithFail();
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

        loginAndCreateClaim(user, claim);

        String customerNote = "Customer note!";
        String internalNote = "Internal note!";

        functionalTemplatesApi.
                updateTemplate(user.getFtId(), SettlementPage.class, enableOperations).
                toNotesPage().
                addCustomerNote(customerNote).
                addInternalNote(internalNote).
                assertCustomerNotePresent(customerNote).
                assertInternalNotePresent(internalNote);

        functionalTemplatesApi.
                updateTemplate(user.getFtId(), NotesPage.class, disableOperations).
                assertEditCustomerNoteButtonPresent().
                assertInternalNoteButtonNotPresent();

        functionalTemplatesApi.updateTemplate(user.getFtId(), NotesPage.class, enableOperations).
                assertEditCustomerNoteButtonPresent().
                assertInternalNoteFieldsPresent();
    }

    @Test(dataProvider = "testDataProvider",
            description = "ECC-2631 It's possible to matchFirst product via Quick matchFirst icon for Excel imported claim lines")
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void ecc2631_quickMatchFromExcel(User user, Claim claim, ClaimItem claimItem) {

        String claimLineDescription = claimItem.getSetDialogTextMatch();

        loginAndCreateClaim(user, claim).
                importExcelFile(claimItem.getExcelPath1()).
                assertItemIsPresent(claimItem.getXlsDescr1()).
                selectClaimItemByDescription(claimLineDescription).
                getToolBarMenu().
                toProductMatchPage().
                sortOrderableFirst().
                match(claimLineDescription).
                cancel();
    }


    /**
     * GIVEN: SP User, active claim C1
     * WHEN: User completes claim with wizard
     * THEN: C1 status is "Completed"
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2632 It's possible to complete simple claim with replacement wizard for SP user. " +
                    "Claim status is Completed in the claims list")
    public void charlie544_2632_completeSPSimpleClaimWizard(User user, Claim claim, ClaimItem claimItem) {
        boolean recentClaimCompleted = loginAndCreateClaim(user, claim).
                addManually().
                fillBaseData(claimItem).
                ok().
                toCompleteClaimPage().
                fillClaimForm(claim).
                openReplacementWizard().
                completeClaimUsingCompPayment().
                to(MyPage.class).
                isRecentClaimCompleted();

        Assert.assertTrue(recentClaimCompleted);
    }

    /**
     * GIVEN: SP User, active claim C1
     * WHEN: User completes claim with shop
     * THEN: C1 status is "Completed"
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to complete simple claim with with shop for SP user. " +
                    "Claim status is Completed in the claims list")
    public void charlie544_completeSimpleClaimWithShopExistingData(User user, Claim claim, ClaimItem claimItem) throws Exception {
        boolean recentClaimCompleted = loginAndCreateClaim(user, claim).
                addManually().
                fillBaseData(claimItem).
                ok().
                toCompleteClaimPage().
                fillClaimForm(claim).
                openReplacementWizard().
                goToShop().
                toProductSearchPage().
                addProductToCart(1).
                getAccountBox().
                toShoppingCart().
                toCashPayoutPage().
                keepMoneyOnAccountAndProceed().
                selectAgreeOption().
                selectPlaceMyOrderOption().
                to(MyPage.class).
                isRecentClaimCompleted();
        Assert.assertTrue(recentClaimCompleted);
    }
}

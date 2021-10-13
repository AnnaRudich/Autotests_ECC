package com.scalepoint.automation.tests;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.stubs.MailserviceMock;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Year;
import java.util.Arrays;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.CATALOG_PRICE;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.*;
import static com.scalepoint.automation.pageobjects.pages.Page.to;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.enable;
import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Constants.JANUARY;

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
public class ClaimTests extends BaseTest {

    MailserviceMock.MailserviceStub mailserviceStub;

    @BeforeClass
    public void startWireMock() {

        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        mailserviceStub = new MailserviceMock(wireMock, databaseApi).addStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
    }

    private final String POLICY_TYPE = "testPolicy ÆæØøÅåß";
    private final String EMPTY = "";

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void reopenSavedClaimTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .saveClaim(claim)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .doAssert(settlementPage -> settlementPage.assertSettlementPagePresent("Settlement page is not loaded"));
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID}, dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void reopenSavedClaimScalepointIdTest(User user, Claim claim) {
        reopenSavedClaimTest(user, claim);
    }
    /**
     * GIVEN: SP User, saved claim C1
     * WHEN: User cancels C1
     * THEN: "Cancelled" is the status of C1
     */

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void cancelSavedClaimTest(User user, Claim claim) throws Exception {
        loginAndCreateClaim(user, claim)
                .saveClaim(claim)
                .openRecentClaim()
                .cancelClaim()
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertRecentClaimCancelled);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID}, dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void cancelSavedClaimScalepointIdTest(User user, Claim claim) throws Exception {
        cancelSavedClaimTest(user, claim);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void completeClaimWithMailTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> mail.isMailExist(CUSTOMER_WELCOME));
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID}, dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void completeClaimWithMailScalepointIdTest(User user, Claim claim) {
        completeClaimWithMailTest(user, claim);
    }

    @RequiredSetting(type = FTSetting.INCLUDE_AGENT_DATA)
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Verifies integration with agent info, send email to agent enabled")
    public void includeAgentDataSendEmailEnabledTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimFormWithAgent(claim)
                .sendAgendEmail(true)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> mail.isMailExist(SETTLEMENT_NOTIFICATION_TO_IC))
                .viewMail(SETTLEMENT_NOTIFICATION_TO_IC)
                .doAssert(mailViewDialog -> mailViewDialog.isTextVisible(claim.getAgentEmail()));
    }

    @RequiredSetting(type = FTSetting.INCLUDE_AGENT_DATA)
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Verifies integration with agent info, send email to agent disabled")
    public void includeAgentDataSendEmailDisabledTest(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimFormWithAgent(claim)
                .sendAgendEmail(false)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> mail.isMailExist(SETTLEMENT_NOTIFICATION_TO_IC))
                .viewMail(SETTLEMENT_NOTIFICATION_TO_IC)
                .doAssert(mailViewDialog -> mailViewDialog.isTextInvisible(claim.getAgentEmail()));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim externally. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void charlie544_2629_completeClaimExternally(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeExternally(claim, databaseApi)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusClosedExternally()));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to save claim without completing from Enter base info page. " +
                    "Saved claim is added to the latest claims list with Saved status")
    public void charlie544_saveClaimFromBaseInfo(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .saveClaim(true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusSaved()));
    }

    @RequiredSetting(type = FTSetting.SETTLE_WITHOUT_MAIL)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3332")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CONTENTS-3332 Be able to settle a claim without sending an e-mail to customer. " +
                    "The new close method in history")
    public void charlie544_2629_completeClaimWithoutMail(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)

                .completeWithoutEmail()
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))

                .openRecentClaim().toEmptyMailsPage()
                .doAssert(mail -> {
                    mail.noOtherMailsOnThePage(Arrays.asList(new MailsPage.MailType[]{SETTLEMENT_NOTIFICATION_TO_IC}));
                });
    }

    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "It's possible to login to Self Service 2.0 from email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void loginToSelfService2_0(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD);
    }
    /*
    the claim which will be validated in Audit must have mobile, zipcode, address and city as required fields
    IC Validation code should be = topdanmark always
    Product should not be Iphone to have APPROVED line
     */
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, enabled = false, dataProvider = "testDataProvider",
            description = "It's possible submit product match from Self Service 2.0 and Audit automatically approves claim")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void charlie_1585_auditApprovedClaimAfterSelfServiceSubmit(@UserAttributes(company = CompanyCode.TOPDANMARK) User user, Claim claim) {
        login(getSystemUser());
        new InsCompaniesPage().enableAuditForIc(user.getCompanyName());

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .enterAddress(claim.getAddress(), claim.getAddress2(), claim.getCity(), claim.getZipCode())
                .saveClaim(true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("Sony")
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView);
        new SettlementSummary().ensureAuditInfoPanelVisible()
                .checkStatusFromAudit("Approved");//"APPROVED" does not work. Change later.
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-541")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "ECC-3256, ECC-3050 It's possible add note on Settlement page")
    @RequiredSetting(type = FTSetting.USE_INTERNAL_NOTES)
    @RequiredSetting(type = FTSetting.SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON)
    @RequiredSetting(type = FTSetting.SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON)
    public void ecc3256_3050_addInternalAndCustomerNotes(User user, Claim claim) {

        String customerNote = "Customer note!";
        String internalNote = "Internal note!";

        loginAndCreateClaim(user, claim)
                .toNotesPage()
                .addCustomerNote(customerNote)
                .addInternalNote(internalNote)
                .doAssert(notesPage -> {
                    notesPage.assertCustomerNotePresent(customerNote);
                    notesPage.assertInternalNotePresent(internalNote);
                });


        updateFT(user, SettlementPage.class,
                disable(FTSetting.USE_INTERNAL_NOTES),
                disable(FTSetting.SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON),
                disable(FTSetting.SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON))
                .toNotesPage()
                .doAssert(notesPage -> {
                    notesPage.assertEditCustomerNoteButtonPresent();
                    notesPage.assertInternalNoteButtonNotPresent();
                });

        updateFT(user, SettlementPage.class,
                enable(FTSetting.USE_INTERNAL_NOTES),
                enable(FTSetting.SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON),
                enable(FTSetting.SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON))
                .toNotesPage()
                .doAssert(notesPage -> {
                    notesPage.assertEditCustomerNoteButtonPresent();
                    notesPage.assertInternalNoteFieldsPresent();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "ECC-2631 It's possible to openSidForFirstProduct product via Quick openSidForFirstProduct icon for Excel imported claim lines")
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void ecc2631_quickMatchFromExcel(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = claimItem.getSetDialogTextMatch();

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPath1())
                .doAssert(sid -> sid.assertItemIsPresent(claimItem.getXlsDescr1()))
                .findClaimLine(claimLineDescription)
                .selectLine()
                .getToolBarMenu()
                .toProductMatchPage()
                .sortOrderableFirst()
                .match(claimLineDescription)
                .doAssert(asserts -> asserts.assertIsStatusMatchedNotificationContainsText(claimItem.getMatchedText()));

        String description = settlementDialog.getDescriptionText();
        double price = settlementDialog.valuationGrid().parseValuationRow(CATALOG_PRICE).getTotalPrice();

        settlementDialog.closeSidWithOk(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(price);
                    asserts.assertProductDetailsIconIsDisplayed();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "ECC-2631 It's possible to openSidForFirstProduct product via Quick openSidForFirstProduct icon for SelfService imported claim lines")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void ecc2631_quickMatchFromSS(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = claimItem.getSetDialogTextMatch();
        loginAndCreateClaim(user, claim)
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions(claimLineDescription)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .addNewPrice((double)3000)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem()
                .sendResponseToEcc();

        SettlementDialog settlementDialog = login(user)
                .openActiveRecentClaim()
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView)
                .findClaimLine(claimLineDescription)
                .selectLine()
                .getToolBarMenu()
                .toProductMatchPage()
                .sortOrderableFirst()
                .match(claimLineDescription)
                .doAssert(asserts -> asserts.assertIsStatusMatchedNotificationContainsText(claimItem.getMatchedText()));

        String description = settlementDialog.getDescriptionText();
        double price = settlementDialog.valuationGrid().parseValuationRow(CATALOG_PRICE).getTotalPrice();

        settlementDialog.closeSidWithOk(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(price);
                    asserts.assertProductDetailsIconIsDisplayed();
                });

        to(MyPage.class).openActiveRecentClaim()
                .toMailsPage()

                .doAssert(mail -> {
                    mail.isMailExist(ITEMIZATION_CUSTOMER_MAIL);
                    mail.isMailExist(ITEMIZATION_CONFIRMATION_IC_MAIL);
                });

    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "ECC-2631 It's possible to openSidForFirstProduct product via Quick openSidForFirstProduct icon for Excel imported claim lines")
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void ecc2631_addMatchedProductFromCatalog(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = claimItem.getSetDialogTextMatch();

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimLineDescription)
                .sortOrderableFirst()
                .match(claimLineDescription)
                .doAssert(asserts -> asserts.assertIsStatusMatchedNotificationContainsText(claimItem.getMatchedText()));

        String description = settlementDialog.getDescriptionText();
        double price = settlementDialog.valuationGrid().parseValuationRow(CATALOG_PRICE).getTotalPrice();

        settlementDialog.closeSidWithOk(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(price);
                    asserts.assertProductDetailsIconIsDisplayed();
                });
    }
    /**
     * GIVEN: SP User, active claim C1
     * WHEN: User completes claim with wizard
     * THEN: C1 status is "Completed"
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2632 It's possible to complete claim with replacement wizard for SP user. " +
                    "Claim status is Completed in the claims list")
    public void charlie544_2632_completeClaimUsingReplacementWizard(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .completeClaimUsingCashPayoutToBankAccount("1","12345678890")
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertClaimCompleted);
    }

    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "CONTENTS-173 - after setting description the category and " +
                    "pseudo-category in SID is auto selected")
    public void contents173_autoCategorization(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setDescriptionAndWaitForCategoriesToAutoSelect("iphone")
                .doAssert(claimLine -> {
                    PseudoCategory categoryMobilePhones = claimItem.getCategoryMobilePhones();
                    claimLine.assertCategoryTextIs(categoryMobilePhones.getGroupName());
                    claimLine.assertSubCategoryTextIs(categoryMobilePhones.getCategoryName());
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-1840")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider")
    public void contents1840_copyClaimLineNote(User user, Claim claim, ClaimItem claimItem) {
        String noteText = Long.toString(System.currentTimeMillis());

        loginAndCreateClaim(user, claim)
                .addLines(claimItem, "item1")
                .getToolBarMenu()
                .openClaimLineNotes()
                .toClaimLineNotesPage()
                .clickClaimLine("item1")
                .enterClaimLineNote(noteText)
                .clickCopyNoteTextButton(noteText)
                .pasteClipboardInNoteWindow()
                .doAssert(notesPage -> notesPage.assertNoteIsCopied(noteText));
    }

    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Tests that the deductible warning information doesn't appear if amount is not zero")
    @RequiredSetting(type = FTSetting.WARNING_DEDUCTIBLE)
    public void nonZeroAmountDeductibleWarningDialogTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .getSettlementSummary().editSelfRisk("50")
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()));
    }

    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Tests the warning information that deductible amount is zero - the confirm case.")
    @RequiredSetting(type = FTSetting.WARNING_DEDUCTIBLE)
    public void confirmInDeductibleWarningDialogTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toDeductibleWarning()
                .confirm()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()));
    }

    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Tests the warning information that deductible amount is zero - the cancel case.")
    @RequiredSetting(type = FTSetting.WARNING_DEDUCTIBLE)
    public void cancelInDeductibleWarningDialogTest(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toDeductibleWarning()
                .cancel()
                .toDeductibleWarning()
                .confirm()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(groups = {TestGroups.CLAIM_MISCELLANEOUS}, dataProvider = "testDataProvider",
            description = "Tests the warning information that deductible amount is zero - the text serach case.")
    @RequiredSetting(type = FTSetting.WARNING_DEDUCTIBLE)
    public void searchProductDeductibleWarningDialogTest(User user, Claim claim, ClaimItem claimItem) {
        String claimLineDescription = claimItem.getSetDialogTextMatch();

        loginAndCreateClaim(user, claim)
                .toTextSearchPage()
                .searchByProductName(claimLineDescription)
                .openSidForFirstProduct()
                .closeSidWithOk()
                .toDeductibleWarning()
                .confirm()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()));
    }

    @Test(dataProvider = "testDataProvider",
            description = "Tests canceling of the EditPolicyType dialog")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    public void cancelEditPolicyTypeDialogTest(User user, Claim claim) {

        loginAndCreateClaimToEditPolicyDialog(user, claim)
                .selectPolicyType(POLICY_TYPE)
                .cancel()
                .toCustomerDetails()
                .doAssert(customerDetailsPage ->
                        customerDetailsPage.assertPolicyType(EMPTY));
    }

    @Test(dataProvider = "testDataProvider",
            description = "Tests setting of policy type in the EditPolicyType dialog")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    public void setPolicyTypeInEditPolicyTypeDialogTest(User user, Claim claim) {

        loginAndCreateClaimToEditPolicyDialog(user, claim)
                .selectPolicyType(POLICY_TYPE)
                .confirm()
                .toCustomerDetails()
                .doAssert(customerDetailsPage ->
                        customerDetailsPage.assertPolicyType(POLICY_TYPE));
    }

    @Test(dataProvider = "testDataProvider",
            description = "Tests setting of policy type using createUser endpoint")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    public void setPolicyTypeUsingCreateUserEndpointTest(User user, Claim claim) {

        loginAndCreateClaim(user, claim, POLICY_TYPE)
                .doAssert(settlmentPage ->
                        settlmentPage.assertEditPolicyTypeDialogIsNotPresent())
                .toCustomerDetails()
                .doAssert(customerDetailsPage ->
                        customerDetailsPage.assertPolicyType(POLICY_TYPE));
    }

    @Test(dataProvider = "testDataProvider",
            description = "Verify that EditPolicyType dialog is not displayed when show policy type setting is disabled")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    public void editPolicyTypeDialogWithDisabledShowPolicyTypeTest(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .doAssert(settlmentPage ->
                        settlmentPage.assertEditPolicyTypeDialogIsNotPresent())
                .toCustomerDetails()
                .doAssert(customerDetailsPage ->
                        customerDetailsPage.assertPolicyType(EMPTY));
    }

    @RequiredSetting(type= FTSetting.ENABLE_SHOW_SETTLEMENT_PAGE, isDefault = true)
    @Test(dataProvider = "testDataProvider",
            description = "verify cancel open the claim")
    public void cancelOpenClaim(User user, Claim claim){
        loginAndCreateClaim(user, claim).
                toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .cancelOpenClaim()
                .doAssert(CustomerDetailsPage.Asserts::assertCustomerDetailsPagePresent);
    }

    @RequiredSetting(type= FTSetting.ENABLE_SHOW_SETTLEMENT_PAGE, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "verify the option to show Settlement Page without having to reopen the claim is disabled")
    public void viewClaimIsOff(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaimWhenViewModeIsDisabled()
                .doAssert(settlementPage ->
                        settlementPage.assertSettlementPagePresent("Settlement page is not loaded"));
    }

    @RequiredSetting(type= FTSetting.ENABLE_SHOW_SETTLEMENT_PAGE, isDefault = true)
    @Test(dataProvider = "testDataProvider",
            description = "verify Settlement page in view mode")
    public void claimViewModeE2eTest(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, "lineDescription")
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .viewClaim()
                .getClaimOperationsMenu()
                .doAssert(claimOperationsMenu -> {
                    claimOperationsMenu.assertAddManuallyButtonIsDisabled();
                    claimOperationsMenu.assertImportExcelButtonIsDisabled();
                    claimOperationsMenu.assertRequestSelfServiceButtonIsDisabled();
                });

        new SettlementPage().findClaimLine("lineDescription").editLine()
                .doAssert(SettlementDialog.Asserts::assertOkButtonIsDisabled);
        new SettlementDialog()
                .cancel()
                .getSettlementSummary()
                .reopenClaimFromViewMode()
                .getClaimOperationsMenu()
                .doAssert(claimOperationsMenu -> {
                    claimOperationsMenu.assertAddManuallyButtonIsEnabled();
                    claimOperationsMenu.assertImportExcelButtonIsEnabled();
                    claimOperationsMenu.assertRequestSelfServiceButtonIsEnabled();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-7254")
    @RequiredSetting(type= FTSetting.ENABLE_SHOW_SETTLEMENT_PAGE, isDefault = true)
    @Test(enabled = false, dataProvider = "testDataProvider",
            description = "verify it's possible to close claim from view mode")
    public void closeClaimInViewMode(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, "lineDescription")
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .viewClaim()
                .getSettlementSummary()
                .closeClaimFromViewMode()
                .doAssert(c -> c.assertClaimNumber(claim.getClaimNumber()));
    }
}

package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.LoginShopPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.annotations.Test;

import java.time.Year;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.CUSTOMER_WELCOME;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.ITEMIZATION_CONFIRMATION_IC_MAIL;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.ORDER_CONFIRMATION_BY_IC;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.SETTLEMENT_NOTIFICATION_TO_IC;
import static com.scalepoint.automation.pageobjects.pages.Page.to;
import static com.scalepoint.automation.services.externalapi.SolrApi.findProductWithPriceLowerThan;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.enable;
import static com.scalepoint.automation.utils.Constants.JANUARY;

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
public class ClaimTests extends BaseTest {

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void charlie544_reopenSavedClaim(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .saveClaim()
                .openRecentClaim()

                .reopenClaim()
                .doAssert(settlementPage -> settlementPage.assertSettlementPagePresent("Settlement page is not loaded"));
    }

    /**
     * GIVEN: SP User, saved claim C1
     * WHEN: User cancels C1
     * THEN: "Cancelled" is the status of C1
     */
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void charlie544_cancelSavedClaim(User user, Claim claim) throws Exception {
        loginAndCreateClaim(user, claim)
                .saveClaim()
                .openRecentClaim()
                .cancelClaim()
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertRecentClaimCancelled);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void charlie544_2629_completeClaimWithMail(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail()
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail ->  mail.isMailExist(CUSTOMER_WELCOME));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void charlie544_2629_completeClaimWithoutMail(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithoutEmail()
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusClosedExternally()));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to save claim without completing from Enter base info page. " +
                    "Saved claim is added to the latest claims list with Saved status")
    public void charlie544_saveClaimFromBaseInfo(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .saveClaim()
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusSaved()));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-541")
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3256, ECC-3050 It's possible to login to Self Service from email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void ecc3256_3050_loginToSelfService(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login();
    }

    @Test(dataProvider = "testDataProvider",
            description = "It's possible to login to Self Service 2.0 from email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void loginToSelfService2_0(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login();
    }

    //TODO
    @Test(dataProvider = "testDataProvider",
            description = "It's possible submit product match from Self Service 2.0 and Audit automatically approved claim")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void charlie_1585_auditApprovedClaimAfterFnolSubmit(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .enableAuditForIc(user.getCompanyName())
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescription("Apple")
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView)
                .ensureAuditInfoPanelVisible()
                .checkStatusFromAudit("APPROVED");
    }


    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void charlie544_not_possible_login_to_cancelled_claim(User user, Claim claim) {

        CustomerDetailsPage customerDetailsPage = loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail()
                .openRecentClaim();

        String loginToShopLink = customerDetailsPage
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME)
                .findLoginToShopLink();

        customerDetailsPage.toCustomerDetails().cancelClaim();

        Browser.driver().get(loginToShopLink);

        Page.at(LoginShopPage.class)
                .enterPassword(Constants.PASSWORD)
                .loginWithFail();
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-541")
    @Bug(bug = "CHARLIE-479")
    @Test(dataProvider = "testDataProvider",
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
    @Test(dataProvider = "testDataProvider",
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
        double price = settlementDialog.parseValuationRow(SettlementDialog.Valuation.CATALOG_PRICE).getTotalPrice();

        settlementDialog.closeSidWithOk(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertPurchasePriceIs(price);
                    asserts.assertProductDetailsIconIsDisplayed();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(dataProvider = "testDataProvider",
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
                .enableAuditForIc(user.getCompanyName())
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescriptionWithOutSuggestions(claimLineDescription)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .selectCategory(claimItem.getExistingCat3_Telefoni())
                .selectSubCategory(claimItem.getExistingSubCat3_Mobiltelefoner())
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
        double price = settlementDialog.parseValuationRow(SettlementDialog.Valuation.CATALOG_PRICE).getTotalPrice();

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

    @RunOn(value = DriverType.IE_REMOTE)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-511")
    @Test(dataProvider = "testDataProvider",
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
        double price = settlementDialog.parseValuationRow(SettlementDialog.Valuation.CATALOG_PRICE).getTotalPrice();

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
    @RunOn(value = DriverType.IE_REMOTE)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544, ECC-2632 It's possible to complete simple claim with replacement wizard for SP user. " +
                    "Claim status is Completed in the claims list")
    @RequiredSetting(type = FTSetting.PAYOUT_TO_CHEQUE_CLAIMSHANDLER)
    public void charlie544_2632_completeSPSimpleClaimWizard(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .completeClaimUsingCompPayment()
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertClaimCompleted);
    }

    /**
     * GIVEN: SP User, active claim C1
     * WHEN: User completes claim with shop
     * THEN: C1 status is "Completed"
     */
    @RunOn(value = DriverType.IE_REMOTE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-544 It's possible to complete simple claim with with shop for SP user. " +
                    "Claim status is Completed in the claims list")
    public void charlie544_completeSimpleClaimWithShopExistingData(User user, Claim claim, ClaimItem claimItem) {
        ProductInfo productInfo = findProductWithPriceLowerThan(claimItem.getCustomerDemand().toString());

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .goToShop()
                .toProductSearchPage()
                .searchForProduct(productInfo.getModel())
                .addProductToCart(0)
                .toShoppingCart()
                .toCashPayoutPage()
                .keepMoneyOnAccountAndProceed()
                .selectAgreeOption()
                .selectPlaceMyOrderOption()
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertClaimCompleted);


                new MyPage().openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(SETTLEMENT_NOTIFICATION_TO_IC);
                    mail.isMailExist(ORDER_CONFIRMATION_BY_IC);
                });
    }
}

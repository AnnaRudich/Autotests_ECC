package com.scalepoint.automation.tests.selfservice;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.selfservice.SelfServicePage;
import com.scalepoint.automation.pageobjects.pages.selfservice.SelfServicePage.SelfServiceGrid.SelfServiceGridRow;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

/**
 * Created by aru on 2017-07-10.
 */
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
public class SelfServiceTest extends BaseTest {

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line. Required fields only. Category auto match")
    public void charlie504_addSSLineWithoutDocsAndNotes(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .doAssert(SelfServiceGridRow -> {
                    SelfServiceGridRow.assertDescriptionIsNotEmpty();
                    SelfServiceGridRow.assertPurchaseDateIsNotEmpty();
                    SelfServiceGridRow.assertPurchasePriceIsNotEmpty();
                    SelfServiceGridRow.assertNewPriceIsNotEmpty();
                    SelfServiceGridRow.assertCustomerDemandIsNotEmpty();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Reloaded data saved")
    public void charlie504_reloadedDataSaved(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescriptionSelectFirstSuggestion("Iphone 6")
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .selfServiceGrid()
                .selfServicePage()
                .reloadPage()
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .doAssert(SelfServiceGridRow -> {
                    SelfServiceGridRow.assertDescriptionIsNotEmpty();
                    SelfServiceGridRow.assertCategoryIsNotEmpty();
                    SelfServiceGridRow.assertPurchaseDateIsNotEmpty();
                    SelfServiceGridRow.assertPurchasePriceIsNotEmpty();
                    SelfServiceGridRow.assertNewPriceIsNotEmpty();
                    SelfServiceGridRow.assertCustomerDemandIsNotEmpty();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Delete line")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_deleteLine(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .deleteRow()
                .doAssert(SelfServiceGrid -> SelfServiceGrid.assertRowsSize(2));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Submit SS. " +
                    "Auto import")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_submitLine_autoImport(User user, Claim claim) {

        String description = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false)
                .getDescription();

        new SelfServicePage().selectSubmitOption();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Save SelfService")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_saveSelfService(User user, Claim claim) {

        SelfServiceGridRow selfServiceGridRow = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescriptionSelectFirstSuggestion("Iphone 6")
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addPurchasePrice("1500")
                .addNewPrice("2500")
                .addCustomerDemandPrice("2000")
                .uploadDocumentation(false);

        String lineDescription = selfServiceGridRow.getDescription();

        selfServiceGridRow
                .selfServiceGrid()
                .selfServicePage()
                .selectCloseOption();

        login(user)
                .openActiveRecentClaim();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemNotPresent(lineDescription);
        });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Required fields validation")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_requiredFieldsValidation(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selfServiceGrid()
                .selfServicePage()
                .selectSubmitOption();

        new SelfServicePage()
                .doAssert(SelfServicePage-> SelfServicePage.assertRequiredFieldsAlertIsPresent())
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .doAssert(SelfServiceGridRow -> {
                    SelfServiceGridRow.assertCategoryIsMarkedAsRequired();
                    SelfServiceGridRow.assertDocumentationIsMarkedAsRequired();
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line with Documentation attached")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_addLineWithDocumentsUploaded(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .uploadDocumentation(true)
                .doAssert(SelfServiceGridRow -> SelfServiceGridRow.assertAttachIconIsPresent())
                .selfServiceGrid()
                .selfServicePage()
                .selectSubmitOption();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine()
                .doAssert(SettlementPage.ClaimLine.Asserts::assertAttachmentsIconIsDisplayed);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line with customer comment")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_addLineWithCustomerComment(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescription("test")
                .selectRandomCategory()
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .uploadDocumentation(false)
                .selfServiceGrid()
                .addCustomerComment("test customer comment")
                .selfServicePage()
                .selectSubmitOption();

        login(user)
                .openActiveRecentClaim()
                .toNotesPage()

                .doAssert(NotesPage -> {
                    NotesPage.assertInternalNotePresent("test customer comment");
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(groups = {TestGroups.SELF_SERVICE}, dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line with customer note")
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_addLineWithCustomerLineNote(User user, Claim claim) {

        SelfServiceGridRow selfServiceGridRow = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .getSelfServiceGrid()
                .getRows()
                .get(1)
                .addDescriptionSelectFirstSuggestion("iphone 6")
                .selectRandomAcquired()
                .selectRandomPurchaseDate()
                .addNewPrice("200")
                .uploadDocumentation(false);

        String lineDescription = selfServiceGridRow.getDescription();

        selfServiceGridRow
                .selfServiceGrid()
                .selfServicePage()
                .addCustomerNote("customer note")
                .selectSubmitOption();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(lineDescription);
        });
    }
}


package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Acquired;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

@RunOn(DriverType.CHROME_REMOTE)
@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = true)
public class SelfService2Tests extends BaseTest {
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Category auto match. Auto import")
    public void Charlie735_addLine_categoryAutoMatch_autoImport(User user, Claim claim) {
        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = ssPage.getProductMatchDescription();

        ssPage.selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(asserts -> asserts.assertItemsListSizeIs(1))
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)

    @Test(enabled = false, dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: ageAsSingleValue + notes")
    public void Charlie735_addLine_ageAsSingleValue_notes(@UserCompany(CompanyCode.TOPDANMARK) User user, Claim claim) {
        String claimNote = "Claim Note";

        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("sony");
        String description = ssPage.getProductMatchDescription();


        ssPage.selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addItemCustomerNote("Item Customer Note")
                .saveItem()
                .addClaimNote(claimNote)
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });

        new SettlementPage().toNotesPage()
                .doAssert(asserts -> asserts.assertCustomerNotePresent(claimNote));//FAILS...strange
    }
    /*
     * assertions for the LineNote will be added when corresponding Page Object will be implemented
     */

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Add line with documentation")
    public void Charlie735_addLineWithDocumentation(User user, Claim claim) {
        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = ssPage.getProductMatchDescription();


        ssPage.selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> asserts.assertItemIsPresent(description));

        new SettlementPage().findClaimLine(description)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertAttachmentsIconIsDisplayed);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Edit line. Add acquired")
    public void Charlie735_editLine_addAcquired(User user, Claim claim, Acquired acquired) {

        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)
                .addDescription("iPhone");
        String description = ssPage.getProductMatchDescription();

        ssPage.selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .startEditItem()
                .selectAcquired(acquired.getAcquiredNew())
                .finishEditItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
            /*
             *assert Acquired in not implemented on Settlement page
             */
        });
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Delete line")
    public void Charlie735_deleteLine(User user, Claim claim) {

        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = ssPage.getProductMatchDescription();

        ssPage.selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .deleteItem()
                .doAssert(asserts ->
                        asserts.assertLineIsNotPresent(description));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Undo delete line")
    public void Charlie735_undoDeleteLine(User user, Claim claim) {

        SelfService2Page ssPage = loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = ssPage.getProductMatchDescription();

        ssPage.selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .deleteItem()
                .undoDelete()
                .doAssert(asserts -> asserts.assertLineIsPresent(description));

        ssPage.sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });
    }
}

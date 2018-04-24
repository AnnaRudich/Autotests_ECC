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
    //Self Service 2.0 Defined age by year and month ON
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Category auto match. Auto import")
    public void Charlie735_addSSLine_categoryAutoMatch_autoImport(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = new SelfService2Page().getProductMatchDescription();

        new SelfService2Page().selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
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
    //Self Service 2.0 Defined age by year and month OFF
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: ageAsSingleValue + SelfService notes")
    public void Charlie735_addSSLine_ageAsSingleValue_SsNotes(@UserCompany(CompanyCode.TOPDANMARK) User user, Claim claim) {
        String claimNote = "Claim Note";

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = new SelfService2Page().getProductMatchDescription();


        new SelfService2Page().selectAge("2")
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
                .doAssert(asserts -> asserts.assertCustomerNotePresent(claimNote));
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
    public void Charlie735_addSSLineWithDocumentation(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.PASSWORD)

                .addDescription("iPhone");
        String description = new SelfService2Page().getProductMatchDescription();


        new SelfService2Page().selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addDocumentation()
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });
        /*
         * will be enabled when https://jira.scalepoint.com/browse/CONTENT-3114 will be fixed
         */
        //new SettlementPage().findClaimLine(description).doAssert(SettlementPage.ClaimLine.Asserts::assertAttachmentsIconIsDisplayed);
    }
}

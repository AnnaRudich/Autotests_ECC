package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.selfservice.SelfServicePage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by aru on 2017-07-10.
 */

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
//more FTs? add all possible fields? or basic?
public class SelfServiceTest extends BaseTest {

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add lines. Required fields")
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    public void charlie504_assertRequiredFieldsUI(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()

                .doAssert(SelfServicePage.Asserts::assertDescriptionColumnIsMarkedAsRequired)
                .doAssert(SelfServicePage.Asserts::assertCategoryColumnIsMarkedAsRequired)
                .doAssert(SelfServicePage.Asserts::assertPurchaseDateColumnIsMarkedAsRequired)
                .doAssert(SelfServicePage.Asserts::assertPurchasePriceColumnIsMarkedAsRequired)
                .doAssert(SelfServicePage.Asserts::assertNewPriceColumnIsMarkedAsRequired)
                .doAssert(SelfServicePage.Asserts::assertDocumentationOrNoteIsMarkedAsRequired);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line. Required fields only")
    public void charlie504_addSSLineWithoutDocsAndNotes(User user, Claim claim) {
        loginAndCreateClaim(user, claim)//should the dedicated method logInToSS be created?
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()

                .addDescriptionSelectFirstSuggestion("Iphone 6")
                .addPurchaseDate("1924", 2)
                .addRandomAcquired(1)
                .addPurchasePrice("1500", 1)
                .addNewPrice("2500", 1)
                .addCustomerDemandPrice("2000", 1)
                .addDocumentationSetting(1, false)

                .doAssert(SelfServicePage -> {
                    SelfServicePage.assertDescriptionIsNotEmpty(1);
                    SelfServicePage.assertPurchasePriceIsNotEmpty(1);
                    SelfServicePage.assertPurchasePriceIsNotEmpty(1);
                    SelfServicePage.assertNewPriceIsNotEmpty(1);
                    SelfServicePage.assertCustomerDemandIsNotEmpty(1);
                });
        new SelfServicePage().selectSubmitOption();
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-504")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-504 Self Service sending. Add line. Required fields only")
    public void charlie504_addSSLine(User user, Claim claim) {
        loginAndCreateClaim(user, claim)//should the dedicated method logInToSS be created?
                .requestSelfService(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()

                .addDescription("test", 1);


        new SelfServicePage().selectSubmitOption();
    }
}

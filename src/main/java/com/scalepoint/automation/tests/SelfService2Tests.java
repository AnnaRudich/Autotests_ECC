package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.pageobjects.pages.selfservice.SelfServicePage;
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

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2, enabled = true)
public class SelfService2Tests extends BaseTest {

    @RunOn(value = DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    //Self Service 2.0 Defined age by year and month OFF
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-503 Self Service2. Add line. Category auto match. Auto import")
    public void Charlie2089_addSSLine_categoryAutoMatch_autoImport(User user, Claim claim) {
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


    @RunOn(value = DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    //Self Service 2.0 Defined age by year and month OFF
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-503 Self Service2. Add line. Category auto match. Auto import")
    public void Charlie2089_addSSLine2(@UserCompany(CompanyCode.TOPDANMARK) User user, Claim claim) {
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
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .parseFirstClaimLine();

        new SettlementPage().doAssert(asserts -> {
            asserts.assertItemIsPresent(description);
        });
    }
}

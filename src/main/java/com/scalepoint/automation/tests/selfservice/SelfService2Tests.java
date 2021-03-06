package com.scalepoint.automation.tests.selfservice;

import com.scalepoint.automation.pageobjects.dialogs.SelfServicePasswordDialog;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.selfService2.LoginSelfService2Page;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.LossType;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
public class SelfService2Tests extends BaseUITest {

    private static final String IPHONE = "iPhone";
    private static final String CYKLER = "cykler";
    private static final String CLAIM_NOTE = "Claim Note";
    private static final String ITEM_CUSTOMER_NOTE = "Item Customer Note";

    private String newPasswordToSelfService;

    @BeforeMethod(alwaysRun = true)
    void init() {
        newPasswordToSelfService = null;
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Category auto match. Auto import")
    public void Charlie735_addLineWithDocumentation(User user, Claim claim, Translations translations) {

        String[] description = new String[1];

        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addDocumentation()
                .saveItem()
                .doAssert(asserts -> asserts.assertItemsListSizeIs(1))

                .startEditItem()
//                .selectAcquired(translations.getAcquired().getAcquiredNew())
                .finishEditItem()

                .deleteItem()
//                .doAssert(asserts -> asserts.assertLineIsNotPresent(description))

                .undoDelete()
                .doAssert(asserts -> asserts.assertLineIsPresent(description[0]))

                .sendResponseToEcc()
                //add confirmation page
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]))
                .findClaimLine(description[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertAttachmentsIconIsDisplayed);
        //assert Acquired in not implemented on Settlement page
    }

    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "SelfService2 password reset, login and logout")
    public void selfService2LogInWithNewPassword(User user, Claim claim) {
        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .newSelfServicePassword()
                .apply(SelfServicePasswordDialog.class, p -> newPasswordToSelfService = p.getNewPasswordToSelfService())
                .closeSelfServicePasswordDialog()
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(newPasswordToSelfService)
                .doAssert(SelfService2Page.Asserts::assertLogOutIsDisplayed)
                .logOut()
                .doAssert(LoginSelfService2Page.Asserts::assertLogOutIsSuccessful);
        new SelfService2Page().doAssert(SelfService2Page.Asserts::assertLogOutIsNotDisplayed);
    }

    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)
    @Test(groups = {TestGroups.SELF_SERVICE2, UserCompanyGroups.TOPDANMARK}, dataProvider = "testDataProvider",
            description = "Loss Item Import add case")
    public void addLossItemTest(User user, Claim claim) {

        String[] description = new String[1];

        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .savePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("sony")
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(selfService2Page -> selfService2Page.assertLineIsPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]))
                .backToSavePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectAge("1")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(selfService2Page -> selfService2Page.assertLineIsPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .openImportSelfServiceDialog()
                .selectFirstSelfServiceResponse()
                .confirmSelfServiceImport()
                .updateAll()
                .confirmImportAfterErrorsWereFixed()
                .confirmImportSummary()
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]));
    }

    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)
    @Test(groups = {TestGroups.SELF_SERVICE2, UserCompanyGroups.TOPDANMARK}, dataProvider = "testDataProvider",
            description = "Loss Item Import delete case")
    public void deleteLossItemTest(User user, Claim claim) {

        String[] description = new String[2];

        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .savePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("sony")
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(selfService2Page -> selfService2Page.assertLineIsPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]))
                .backToSavePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .deleteItem()
                .addDescription("samsung")
                .apply(SelfService2Page.class, p -> description[1] = p.getProductMatchDescription())
                .selectAge("1")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(selfService2Page -> selfService2Page
                        .assertLineIsPresent(description[1])
                        .assertLineIsNotPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .openImportSelfServiceDialog()
                .selectFirstSelfServiceResponse()
                .confirmSelfServiceImport()
                .updateAll()
                .confirmImportAfterErrorsWereFixed()
                .confirmImportSummary()
                .doAssert(asserts -> asserts
                        .assertItemIsPresent(description[1])
                        .assertItemNotPresent(description[0]));
    }

    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)
    @Test(groups = {TestGroups.SELF_SERVICE2, UserCompanyGroups.TOPDANMARK}, dataProvider = "testDataProvider",
            description = "Loss Item Import update case")
    public void updateLossItemUpdate(User user, Claim claim) {

        String[] description = new String[1];

        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .savePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("sony")
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .saveItem()
                .doAssert(selfService2Page -> selfService2Page.assertLineIsPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]))
                .backToSavePoint(LoginSelfService2Page.class)
                .login(Constants.DEFAULT_PASSWORD)
                .startEditItem()
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .addNewPrice(Constants.PRICE_100)
                .addCustomerDemandPrice(Constants.PRICE_30)
                .finishEditItem()
                .doAssert(selfService2Page -> selfService2Page
                        .assertLineIsPresent(description[0]))
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .openImportSelfServiceDialog()
                .selectFirstSelfServiceResponse()
                .confirmSelfServiceImportNoErrors()
                .doAssert(asserts -> asserts
                        .assertItemIsPresent(description[0]));
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)
    @Test(groups = {TestGroups.SELF_SERVICE2, UserCompanyGroups.TOPDANMARK}, dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: ageAsSingleValue + notes")
    public void Charlie735_addLine_ageAsSingleValue_notes(@UserAttributes(company = CompanyCode.TOPDANMARK) User user, Claim claim) {

        String[] description = new String[1];

        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("sony")
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addItemCustomerNote(ITEM_CUSTOMER_NOTE)
                .saveItem()
                .addClaimNote(CLAIM_NOTE)
                .saveItem()
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]))
                .doAssert(asserts -> asserts.assertItemNoteIsPresent(ITEM_CUSTOMER_NOTE))
                .toNotesPage()
                .doAssert(asserts -> asserts.assertInternalNotePresent(CLAIM_NOTE));
    }

    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, enabled = false)
    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4975")
    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "IntelligentRepair1_submitRepairLine_checkGUI_in_SelfService", enabled = false)
    public void submitRepairLine(User user, Claim claim) {

        String[] description = new String[1];

        loginFlow.loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .requestSelfServiceWithEnabledNewPassword(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(CYKLER)
                .apply(SelfService2Page.class, p -> description[0] = p.getProductMatchDescription())
                .selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")

                .setLossType(LossType.DAMAGED)
                .isRepaired(true)
                .addRepairPrice(Constants.PRICE_100)
                .addNewPrice(Constants.PRICE_500)
                .saveItem()

                .sendResponseToEcc()

                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description[0]));
    }

    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "Failed Mail service request - Internal Server Error")
    public void sendSMSInternalServerErrorTest(User user, Claim claim) {

        sendSMSandVerifyResponse(user, claim, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "Failed Mail service request - No Found")
    public void sendSMSNotFoundTest(User user, Claim claim) {

        sendSMSandVerifyResponse(user, claim, HttpStatus.SC_NOT_FOUND);
    }

    @Test(groups = {TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "Failed Mail service request - Missing token")
    public void sendSMSMissingTokenTest(User user, Claim claim) {

        sendSMSandVerifyResponse(user, claim, HttpStatus.SC_OK);
    }

    private void sendSMSandVerifyResponse(User user, Claim claim, int httpStatus){

        claim.setCellNumber(mailserviceStub.getTestMobileNumberForStatusCode(httpStatus));
        loginFlow.loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD);

        databaseApi.waitForFailedMailServiceRequest(claim.getClaimId(), httpStatus);
    }
}

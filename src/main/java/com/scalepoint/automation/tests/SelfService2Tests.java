package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SelfServicePasswordDialog;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.selfService2.LoginSelfService2Page;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.LossType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
public class SelfService2Tests extends BaseTest {

    private static final String IPHONE = "iPhone";
    private static final String HERRE = "Herre";
    private static final String CLAIM_NOTE = "Claim Note";
    private static final String ITEM_CUSTOMER_NOTE = "Item Customer Note";

    private String description;
    private String newPasswordToSelfService;

    @BeforeMethod
    void init() {
        description = null;
        newPasswordToSelfService = null;
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-735")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: Category auto match. Auto import")
    public void Charlie735_addLineWithDocumentation(User user, Claim claim, Translations translations) {

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(IPHONE)
                .apply(SelfService2Page.class, p -> description = p.getProductMatchDescription())
                .selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addDocumentation()
                .saveItem()
                .doAssert(asserts -> asserts.assertItemsListSizeIs(1))

                .startEditItem()
                .selectAcquired(translations.getAcquired().getAcquiredNew())
                .finishEditItem()

                .deleteItem()
//                .doAssert(asserts -> asserts.assertLineIsNotPresent(description))

                .undoDelete()
                .doAssert(asserts -> asserts.assertLineIsPresent(description))

                .sendResponseToEcc()
                //add confirmation page
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .findClaimLine(description)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertAttachmentsIconIsDisplayed);
        //assert Acquired in not implemented on Settlement page
    }

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @Test(dataProvider = "testDataProvider", description = "SelfService2 password reset, login and logout")
    public void selfService2LogInWithNewPassword(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .newSelfServicePassword()
                .apply(SelfServicePasswordDialog.class, p -> newPasswordToSelfService = p.getNewPasswordToSelfService())
                .closeSelfServicePasswordDialog()
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(newPasswordToSelfService)
                .doAssert(SelfService2Page.Asserts::assertLogOutIsDisplayed)
                .savePoint(SelfService2Page.class)
                .logOut()
                .doAssert(LoginSelfService2Page.Asserts::assertLogOutIsSuccessful)
                .backToSavePoint(SelfService2Page.class)
                .doAssert(SelfService2Page.Asserts::assertLogOutIsNotDisplayed);
    }

    @Jira("https://jira.scalepoint.com/browse/CHARLIE-503")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-735 SelfService_2.0: ageAsSingleValue + notes")
    public void Charlie735_addLine_ageAsSingleValue_notes(@UserCompany(CompanyCode.TOPDANMARK) User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription("sony")
                .apply(SelfService2Page.class, p -> description = p.getProductMatchDescription())
                .selectAge("2")
                .addNewPrice(Constants.PRICE_500)
                .addCustomerDemandPrice(Constants.PRICE_50)
                .addItemCustomerNote(ITEM_CUSTOMER_NOTE)
                .saveItem()
                .addClaimNote(CLAIM_NOTE)
                .saveItem()
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description))
                .doAssert(asserts -> asserts.assertItemNoteIsPresent(ITEM_CUSTOMER_NOTE))
                .toNotesPage()
                .doAssert(asserts -> asserts.assertInternalNotePresent(CLAIM_NOTE));
    }

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, enabled = false)
    @RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "IntelligentRepair1_submitRepairLine_checkGUI_in_SelfService")
    public void submitRepairLine(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim)
                .openRecentClaim()
                .reopenClaim()
                .requestSelfServiceWithEnabledNewPassword(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescription(HERRE)
                .apply(SelfService2Page.class, p -> description = p.getProductMatchDescription())
                .selectPurchaseYear("2017")
                .selectPurchaseMonth("Jan")

                .setLossType(LossType.DAMAGED)
                .isRepaired(true)
                .addRepairPrice(Constants.PRICE_100)
                .addNewPrice(Constants.PRICE_500)
                .saveItem()

                .sendResponseToEcc()

                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(description));
    }
}

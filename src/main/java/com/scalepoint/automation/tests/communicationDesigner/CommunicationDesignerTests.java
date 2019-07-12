package com.scalepoint.automation.tests.communicationDesigner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.schemaValidation.SchemaValidation;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.stubs.CommunicationDesignerStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Year;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.ITEMIZATION_CONFIRMATION_IC_MAIL;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL;
import static com.scalepoint.automation.pageobjects.pages.Page.to;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.FUTURE60;
import static com.scalepoint.automation.utils.Constants.JANUARY;

public class CommunicationDesignerTests extends BaseTest {

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        CommunicationDesignerStubs.templatesQueryStub();
        CommunicationDesignerStubs.templatesGenerateStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
    }

    @Test(dataProvider = "testDataProvider", description = "Use communication designer to prepare SelfService Customer welcome email")
    public void SelfServiceCustomerWelcomeTest(@UserCompany(FUTURE60) User user, Claim claim) {
        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omSelfServiceCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .doAssert(mailViewDialog -> {
                    mailViewDialog.isTextVisible("[SelfServiceCustomerWelcome]");
                });

        new SchemaValidation(wireMock)
                .validateTemplateGenerateSchemaManual(claim.getClaimNumber());
    }

    @Test(dataProvider = "testDataProvider",
            description = "Use communication designer to prepare Itemization Submit Loss Items email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS)
    @RequiredSetting(type = FTSetting.USE_BRAND_LOYALTY_BY_DEFAULT)
    @RequiredSetting(type = FTSetting.NUMBER_BEST_FIT_RESULTS, value = "5")
    @RequiredSetting(type = FTSetting.ALLOW_NONORDERABLE_PRODUCTS, value = "Yes, Always")
    public void itemizationSubmitLossItemsTest(@UserCompany(FUTURE60) User user, Claim claim, ClaimItem claimItem) {

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omItemizationSubmitLossItems(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

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
                })
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible("[ItemizationSubmitLossItems]")
                );

        new SchemaValidation(wireMock)
                .validateTemplateGenerateSchemaAutomatic(claim.getClaimNumber());
    }
}

package com.scalepoint.automation.tests.communicationDesigner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.schemaValidation.SchemaValidation;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.CommunicationDesignerStubs;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.Translations;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
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
                .validateTemplateGenerateSchema(claim.getClaimNumber());
    }

    @Test(dataProvider = "testDataProvider",
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsTest(@UserCompany(FUTURE60) User user, Claim claim, ClaimItem claimItem) {

        final String ITEMIZATION_SUBMIT_LOSS_ITEMS = "[ItemizationSubmitLossItems]";
        final String ITEMIZATION_SAVE_LOSS_ITEMS = "[ItemizationSaveLossItems]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omItemizationSubmitLossItems(true)
                .omItemizationSaveLossItems(true)
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
                .saveResponse()
                .continueRegistration()
                .login(Constants.DEFAULT_PASSWORD)
                .sendResponseToEcc();

        to(MyPage.class).openActiveRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(ITEMIZATION_CUSTOMER_MAIL);
                    mail.isMailExist(ITEMIZATION_CONFIRMATION_IC_MAIL);
                })
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, ITEMIZATION_SUBMIT_LOSS_ITEMS)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ITEMIZATION_SUBMIT_LOSS_ITEMS)
                )
                .cancel()
                .viewMail(MailsPage.MailType.ITEMIZATION_CUSTOMER_MAIL, ITEMIZATION_SAVE_LOSS_ITEMS)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ITEMIZATION_SAVE_LOSS_ITEMS)
                );

        new SchemaValidation(wireMock)
                .validateTemplateGenerateSchema(claim.getClaimNumber());
    }

    @Test(dataProvider = "testDataProvider",
            description = "Use communication designer to prepare CustomerWelcomeRejectionMail")
    public void customerWelcomeRejectionMail(@UserCompany(FUTURE60) User user, Claim claim) {

        final String CUSTOMER_WELCOME_REJECTION = "[CustomerWelcomeRejectionMail]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcomeRejectionMail(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)

                .completeWithEmail(claim)
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME_REJECTION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME_REJECTION)
                );
    }

    @Test(dataProvider = "testDataProvider",
            description = "Use communication designer to prepare CustomerWelcome mail")
    public void customerWelcomeMail(@UserCompany(FUTURE60) User user, Claim claim, ClaimItem claimItem) {

        final String CUSTOMER_WELCOME = "[CustomerWelcome]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)

                .completeWithEmail(claim)
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME)
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Use communication designer to prepare CustomerWelcomeWithOutstanding mail")
    public void customerWelcomeWithOutstanding(@UserCompany(FUTURE60) User user, Claim claim, ServiceAgreement agreement, Translations translations, ClaimItem claimItem) {
        String lineDescription = RandomUtils.randomName("RnVLine");

        final String CUSTOMER_WELCOME_WITH_OUTSTANDING = "[CustomerWelcomeWithOutstanding]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcomeWithOutstanding(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption();

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnV(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim);

        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .openEvaluateTaskDialog()
                .acceptFeedback();

        new ClaimNavigationMenu()
                .toSettlementPage()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .completeClaimUsingCashPayout()
                .reopenClaim();
        new SettlementSummary()
                .editSelfRisk("2000")
                .toCompleteClaimPage()
                .completeWithEmail(claim)

                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME_WITH_OUTSTANDING)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME_WITH_OUTSTANDING)
                );

    }
}


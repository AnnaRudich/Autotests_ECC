package com.scalepoint.automation.tests.communicationDesigner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.services.ucommerce.CreateOrderService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.stubs.AuditMock;
import com.scalepoint.automation.stubs.CommunicationDesignerMock;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Arrays;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.*;
import static com.scalepoint.automation.pageobjects.pages.Page.to;
import static com.scalepoint.automation.utils.Constants.JANUARY;

public class CommunicationDesignerTests extends BaseTest {

    RnVMock.RnvStub rnvStub;
    AuditMock.AuditStub auditStub;
    CommunicationDesignerMock communicationDesignerMock;

    @BeforeClass
    public void startWireMock() {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        rnvStub = new RnVMock(wireMock)
                .addStub();
        auditStub = new AuditMock(wireMock)
                .addStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
        communicationDesignerMock = new CommunicationDesignerMock(wireMock);
    }

    @DataProvider(name = "stubDataProvider")
    public Object[][] stubDataProvider(Method method) throws IOException {
        Object[][] params = provide(method);

        User user = (User) Arrays
                .stream(params[0])
                .filter(param -> param.getClass()
                        .equals(User.class))
                .findFirst()
                .get();

        communicationDesignerMock.addStub(user.getCompanyName().toLowerCase());

        return params;
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider", description = "Use communication designer to prepare SelfService Customer welcome email")
    public void SelfServiceCustomerWelcomeTest(User user, Claim claim) {
        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omSelfServiceCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .doAssert(mailViewDialog -> {
                    mailViewDialog.isTextVisible("[SelfServiceCustomerWelcome]");
                });

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider",
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsTest(User user, Claim claim, ClaimItem claimItem) {

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
                .selectSaveOption(false);

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
                .addNewPrice((double)3000)
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

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider",
            description = "Use communication designer to prepare CustomerWelcomeRejectionMail")
    public void customerWelcomeRejectionMail(User user, Claim claim) {

        final String CUSTOMER_WELCOME_REJECTION = "[CustomerWelcomeRejectionMail]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcomeRejectionMail(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)

                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME_REJECTION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME_REJECTION)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider",
            description = "Use communication designer to prepare CustomerWelcome")
    public void customerWelcomeMail(User user, Claim claim, ClaimItem claimItem) {

        final String CUSTOMER_WELCOME = "[CustomerWelcome]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)

                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider", description = "Use communication designer to prepare CustomerWelcomeWithOutstanding mail", enabled = true)
    public void customerWelcomeWithOutstanding(User user, Claim claim, ServiceAgreement agreement, Translations translations, ClaimItem claimItem) {
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
                .selectSaveOption(false);

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_100), claim, rnvStub);

        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .openEvaluateTaskDialog()
                .acceptFeedback();

        new ClaimNavigationMenu()
                .toSettlementPage()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .completeClaimUsingCashPayoutToBankAccount("1","12345678890")
                .reopenClaim();
        new SettlementSummary()
                .editSelfRisk("2000")
                .toCompleteClaimPage()
                .completeWithEmail(claim, databaseApi, false)

                .openRecentClaim()
                .toMailsPage()
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME_WITH_OUTSTANDING)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME_WITH_OUTSTANDING)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(dataProvider = "stubDataProvider", description = "Use communication designer to prepare split replacement mails")
    public void splitReplacement(User user, Claim claim, ClaimItem claimItem) {

        final String CUSTOMER_WELCOME = "[CustomerWelcome]";
        final String ORDER_CONFIRMATION = "[OrderConfirmation]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omCustomerWelcome(true)
                .omOrderConfirmation(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        replacement(user, claim, claimItem)
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, CUSTOMER_WELCOME)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(CUSTOMER_WELCOME)
                )
                .cancel()
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL, ORDER_CONFIRMATION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ORDER_CONFIRMATION));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }
    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(dataProvider = "stubDataProvider",
            description = "Use communication designer to prepare order confirmation mails")
    public void orderConfirmation(User user, Claim claim, ClaimItem claimItem) {
        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

        final String ORDER_CONFIRMATION = "[OrderConfirmation]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omOrderConfirmation(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim();

        new CreateOrderService().createOrderForProductExtraPay
                (voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new CustomerDetailsPage().toMailsPage()
                .viewMail(MailsPage.MailType.ORDER_CONFIRMATION, ORDER_CONFIRMATION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ORDER_CONFIRMATION));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(dataProvider = "stubDataProvider", description = "Use communication designer to prepare replacement mail")
    public void replacementMail(User user, Claim claim, ClaimItem claimItem) {

        final String REPLACEMENT_MAIL = "[ReplacementMail]";

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omReplacementMail(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        replacement(user, claim, claimItem)
                .doAssert(mailViewDialog ->
                        mailViewDialog.noOtherMailsOnThePage(
                                Arrays.asList(
                                        REPLACEMENT_WITH_MAIL,
                                        SETTLEMENT_NOTIFICATION_TO_IC)))
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(REPLACEMENT_MAIL)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    private void schemaValidation(String companyName, String clamNumber){

        communicationDesignerMock.getStub(companyName)
                .doValidation(schemaValidation ->
                        schemaValidation.validateTemplateGenerateSchema(clamNumber));
    }

    @CommunicationDesignerCleanUp
    @Test(dataProvider = "stubDataProvider",
            description = "Use communication designer to prepare CustomerWelcome")
    public void automaticCustomerWelcomeMail(User user, ClaimItem claimItem) {

        CommunicationDesigner communicationDesigner = CommunicationDesigner.builder()
                .useOutputManagement(true)
                .omAutomaticCustomerWelcome(true)
                .build();

        login(user)
                .to(InsCompaniesPage.class)
                .editCompany(user.getCompanyName())
                .setCommunicationDesignerSection(communicationDesigner)
                .selectSaveOption(false);

        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();
        ClaimRequest itemizationClaimRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        createClaimRequest.setCompany(user.getCompanyName());
        createClaimRequest.setTenant(user.getCompanyName());
        createClaimRequest.setAllowAutoClose(true);
        itemizationClaimRequest.setCompany(user.getCompanyName());
        itemizationClaimRequest.setTenant(user.getCompanyName());
        itemizationClaimRequest.setAllowAutoClose(true);
        String claimLineDescription = claimItem.getSetDialogTextMatch();

        String token = createFNOLClaimAndGetClaimToken(itemizationClaimRequest, createClaimRequest);

        loginAndOpenUnifiedIntegrationClaimByToken(user, token)
                .requestSelfServiceWithEnabledNewPassword(createClaimRequest, Constants.DEFAULT_PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions(claimLineDescription)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .addNewPrice(3000.00)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem()
                .sendResponseToEcc();

        final String automaticCustomerWelcome = "[AutomaticCustomerWelcome]";

        to(MyPage.class).openRecentClaim()
                .toMailsPage()
                .doAssert(mail ->
                        mail.noOtherMailsOnThePage(
                                Arrays.asList(
                                        ITEMIZATION_CUSTOMER_MAIL,
                                        SELFSERVICE_CUSTOMER_WELCOME,
                                        CUSTOMER_WELCOME))
                )
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, automaticCustomerWelcome)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(automaticCustomerWelcome)
                );
    }

    private MailsPage replacement(User user, Claim claim, ClaimItem claimItem){
        return loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .completeClaimUsingCashPayoutToBankAccount("1","12345678890")
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertClaimCompleted)
                .openRecentClaim()
                .toMailsPage();
    }
}

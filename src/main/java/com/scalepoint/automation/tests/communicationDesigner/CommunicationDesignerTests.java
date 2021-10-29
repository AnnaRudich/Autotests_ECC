package com.scalepoint.automation.tests.communicationDesigner;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage.CommunicationDesigner;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.services.ucommerce.CreateOrderService;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.communicationDesignerEmailTemplates.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.*;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class CommunicationDesignerTests extends CommunicationDesignerBaseTests {

    private static final String SELF_SERVICE_CUSTOMER_WELCOME_DATA_PROVIDER = "selfServiceCustomerWelcomeDataProvider";
    private static final String SELF_SERVICE_CUSTOMER_WELCOME_WITH_ATTACHMENT_DATA_PROVIDER = "selfServiceCustomerWelcomeWithAttachmentsDataProvider";
    private static final String ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEM_DATA_PROVIDER = "itemizationSubmitAndSaveLossItemsDataProvider";
    private static final String ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEMS_WITH_ATTACHMENTS_DATA_PROVIDER = "itemizationSubmitAndSaveLossItemsWithAttachmentsDataProvider";
    private static final String CUSTOMER_WELCOME_REJECTION_MAIL_DATA_PROVIDER = "customerWelcomeRejectionMailDataProvider";
    private static final String CUSTOMER_WELCOME_DATA_PROVIDER = "customerWelcomeDataProvider";
    private static final String CUSTOMER_WELCOME_WITH_OUTSTANDING_DATA_PROVIDER = "customerWelcomeWithOutstandingDataProvider";
    private static final String SPLIT_REPLACEMENT_DATA_PROVIDER = "splitReplacementDataProvider";
    private static final String SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER = "splitReplacementWithAttachmentsDataProvider";
    private static final String ORDER_CONFIRMATION_DATA_PROVIDER = "orderConfirmationDataProvider";
    private static final String REPLACEMENT_MAIL_DATA_PROVIDER = "replacementMailDataProvider";

    @BeforeMethod
    public void toAdminPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        Claim claim = getLisOfObjectByClass(parameters, Claim.class).get(0);

        loginAndCreateClaim(user, claim);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = SELF_SERVICE_CUSTOMER_WELCOME_DATA_PROVIDER,
            description = "Use communication designer to prepare SelfService Customer welcome email")
    public void selfServiceCustomerWelcomeTest(User user, Claim claim,
                                               CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                               String password, CommunicationDesigner communicationDesigner) {

        sendSelfServiceCustomerWelcomeEmail(claim, user.getCompanyCode(), password,
                communicationDesignerEmailTemplates.getEmailTemplateByClass(SelfServiceEmailTemplate.class));
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = SELF_SERVICE_CUSTOMER_WELCOME_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Use communication designer to prepare SelfService Customer welcome email with attachments")
    public void selfServiceCustomerWelcomeWithAttachmentsTest(User user, Claim claim,
                                                              CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                              String password, CommunicationDesigner communicationDesigner) {


        sendSelfServiceCustomerWelcomeEmail(claim, user.getCompanyCode(), password,
                communicationDesignerEmailTemplates.getEmailTemplateByClass(SelfServiceEmailTemplate.class));
    }


    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEM_DATA_PROVIDER,
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsTest(User user, Claim claim, ClaimItem claimItem,
                                                      CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                      String password, String month, Double newPrice,
                                                      CommunicationDesigner communicationDesigner) {

        sendItemizationSubmitAndSaveLossItemsEmails(user, claim, claimItem, communicationDesignerEmailTemplates,
                password, month, newPrice);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEMS_WITH_ATTACHMENTS_DATA_PROVIDER,
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email with attachments")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsWithAttachmentsTest(User user, Claim claim, ClaimItem claimItem,
                                                                     CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                                     String password, String month, Double newPrice,
                                                                     CommunicationDesigner communicationDesigner) {

        sendItemizationSubmitAndSaveLossItemsEmails(user, claim, claimItem, communicationDesignerEmailTemplates,
                password, month, newPrice);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_REJECTION_MAIL_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcomeRejectionMail")
    public void customerWelcomeRejectionMailTest(User user, Claim claim,
                                                 CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                 CommunicationDesigner communicationDesigner) {

        String title = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(CustomerWelcomeRejectionMailEmailTemplate.class)
                .getTitle();

        Page.at(SettlementPage.class)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toMailsPage(mailserviceStub)
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, title)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(title)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcome")
    public void customerWelcomeTest(User user, Claim claim, ClaimItem claimItem ,
                                    CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                    CommunicationDesigner communicationDesigner) {

        String title = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(CustomerWelcomeEmailTemplate.class)
                .getTitle();

        Page.at(SettlementPage.class)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .toMailsPage(mailserviceStub)
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, title)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(title)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_WITH_OUTSTANDING_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcomeWithOutstanding mail")
    public void customerWelcomeWithOutstandingTest(User user, Claim claim, ServiceAgreement agreement,
                                                   Translations translations, ClaimItem claimItem,
                                                   CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                   BankAccount bankAccount, String lineDescription,
                                                   Double newPrice, BigDecimal repairPrice, String selfRisk,
                                                   CommunicationDesigner communicationDesigner) {

        String title = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(CustomerWelcomeWithOutstandingEmailTemplate.class)
                .getTitle();

        Page.at(SettlementPage.class)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), newPrice)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedbackWithoutInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .openEvaluateTaskDialog()
                .acceptFeedback();

        new ClaimNavigationMenu()
                .toSettlementPage()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard(true)
                .completeClaimUsingCashPayoutToBankAccount(bankAccount.getRegNumber(), bankAccount.getAccountNumber())
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim();

        new SettlementSummary()
                .editSelfRisk(selfRisk)
                .toCompleteClaimPage()
                .completeWithEmail(claim, databaseApi, false)
                .openRecentClaim()
                .toMailsPage(mailserviceStub)
                .viewMail(MailsPage.MailType.CUSTOMER_WELCOME, title)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(title)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SPLIT_REPLACEMENT_DATA_PROVIDER,
            description = "Use communication designer to prepare split replacement mails")
    public void splitReplacementTest(User user, Claim claim, ClaimItem claimItem, BankAccount bankAccount,
                                     CommunicationDesignerEmailTemplates communicationDesignerEmailTemplate,
                                     CommunicationDesigner communicationDesigner) {

        sendSplitReplacementEmails(user, claim, claimItem, bankAccount, communicationDesignerEmailTemplate);
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER,
            description = "Use communication designer to prepare split replacement mails with attachments")
    public void splitReplacementWithAttachmentsTest(User user, Claim claim, ClaimItem claimItem,
                                                    CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                                    BankAccount bankAccount, CommunicationDesigner communicationDesigner) {

        sendSplitReplacementEmails(user, claim, claimItem, bankAccount, communicationDesignerEmailTemplates);
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = ORDER_CONFIRMATION_DATA_PROVIDER,
            description = "Use communication designer to prepare order confirmation mails")
    public void orderConfirmationTest(User user, Claim claim, ClaimItem claimItem,
                                      CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                      CommunicationDesigner communicationDesigner) {

        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);
        String orderConfirmationTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(OrderConfirmationEmailTemplate.class)
                .getTitle();


        Page.at(SettlementPage.class)
                .openSid()
                .setBaseData(claimItem)
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimFormWithPassword(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim();

        new CreateOrderService().createOrderForProductExtraPay
                (voucherInfo, claim.getClaimNumber(), claim.getPhoneNumber(), claim.getEmail(), isEvoucher);

        new CustomerDetailsPage().toMailsPage(mailserviceStub)
                .viewMail(MailsPage.MailType.ORDER_CONFIRMATION, orderConfirmationTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(orderConfirmationTitle));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = REPLACEMENT_MAIL_DATA_PROVIDER,
            description = "Use communication designer to prepare replacement mail")
    public void replacementMailTest(User user, Claim claim, ClaimItem claimItem, BankAccount bankAccount,
                                    CommunicationDesignerEmailTemplates communicationDesignerEmailTemplates,
                                    CommunicationDesigner communicationDesigner) {

        String replacementMailTitle = communicationDesignerEmailTemplates
                .getEmailTemplateByClass(ReplacementMailEmailTemplate.class)
                .getTitle();

        replacement(claim, claimItem, bankAccount.getRegNumber(), bankAccount.getAccountNumber())
                .doAssert(mailViewDialog ->
                        mailViewDialog.noOtherMailsOnThePage(
                                Arrays.asList(
                                        MailsPage.MailType.REPLACEMENT_WITH_MAIL,
                                        MailsPage.MailType.SETTLEMENT_NOTIFICATION_TO_IC)))
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL, replacementMailTitle)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(replacementMailTitle)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @DataProvider(name = SELF_SERVICE_CUSTOMER_WELCOME_DATA_PROVIDER)
    public static Object[][] selfServiceCustomerWelcomeDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setSelfServiceCustomerWelcome(false, null);

        String password = DEFAULT_PASSWORD;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, password, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = SELF_SERVICE_CUSTOMER_WELCOME_WITH_ATTACHMENT_DATA_PROVIDER)
    public static Object[][] selfServiceCustomerWelcomeWithAttachmentsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setSelfServiceCustomerWelcome(true, twoAttachments);

        String password = DEFAULT_PASSWORD;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, password, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEM_DATA_PROVIDER)
    public static Object[][] itemizationSubmitAndSaveLossItemsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setItemizationSaveLossItems(false, null)
                .setItemizationSubmitLossItems(false, null);

        String password = DEFAULT_PASSWORD;
        String month = DEFAULT_MONTH;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, password, month, newPrice, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEMS_WITH_ATTACHMENTS_DATA_PROVIDER)
    public static Object[][] itemizationSubmitAndSaveLossItemsWithAttachmentsDataProvider(Method method) {


        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setItemizationSaveLossItems(true, twoAttachments)
                .setItemizationSubmitLossItems(true, oneAttachment);

        String password = DEFAULT_PASSWORD;
        String month = DEFAULT_MONTH;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, password, month, newPrice, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = CUSTOMER_WELCOME_REJECTION_MAIL_DATA_PROVIDER)
    public static Object[][] customerWelcomeRejectionMailDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcomeRejectionMail(false, null);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = CUSTOMER_WELCOME_DATA_PROVIDER)
    public static Object[][] customerWelcomeDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(false, null);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = CUSTOMER_WELCOME_WITH_OUTSTANDING_DATA_PROVIDER)
    public static Object[][] customerWelcomeWithOutstandingDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcomeWithOutstanding(false, null);

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        Double newPrice = RnVMock.OK_PRICE;
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(1000.00);
        String selfRisk = "2000";

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, newPrice, repairPrice, selfRisk, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = SPLIT_REPLACEMENT_DATA_PROVIDER)
    public static Object[][] splitReplacementDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(false, null)
                .setOrderConfirmation(false, null);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER)
    public static Object[][] splitReplacementWithAttachmentsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(true, twoAttachments)
                .setOrderConfirmation(true, oneAttachment);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = ORDER_CONFIRMATION_DATA_PROVIDER)
    public static Object[][] orderConfirmationDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setOrderConfirmation(false, null);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }

    @DataProvider(name = REPLACEMENT_MAIL_DATA_PROVIDER)
    public static Object[][] replacementMailDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setReplacementMail(false, null);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, communicationDesigner).toArray()
        };
    }
}

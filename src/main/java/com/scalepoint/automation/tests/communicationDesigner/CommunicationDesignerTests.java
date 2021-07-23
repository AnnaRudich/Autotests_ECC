package com.scalepoint.automation.tests.communicationDesigner;

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
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
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
    private static final String CUSTOMER_WELCOME_WITH_OUTSTATNDING_DATA_PROVIDER = "customerWelcomeWithOutstandingDataProvider";
    private static final String SPLIT_REPLACEMENT_DATA_PROVIDER = "splitReplacementDataProvider";
    private static final String SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER = "splitReplacementWithAttachmentsDataProvider";
    private static final String ORDER_CONFIRMATION_DATA_PROVIDER = "orderConfirmationDataProvider";
    private static final String REPLACEMENT_MAIL_DATA_PROVIDER = "replacementMailDataProvider";

    @BeforeMethod
    public void toAdminPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        Claim claim = getObjectByClass(parameters, Claim.class).get(0);

        loginAndCreateClaim(user, claim);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SELF_SERVICE_CUSTOMER_WELCOME_DATA_PROVIDER,
            description = "Use communication designer to prepare SelfService Customer welcome email")
    public void selfServiceCustomerWelcomeTest(User user, Claim claim, String password,
                                               CommunicationDesigner communicationDesigner) {

        sendSelfServiceCustomerWelcomeEmail(claim, user.getCompanyCode(), password);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SELF_SERVICE_CUSTOMER_WELCOME_WITH_ATTACHMENT_DATA_PROVIDER,
            description = "Use communication designer to prepare SelfService Customer welcome email with attachments")
    public void selfServiceCustomerWelcomeWithAttachmentsTest(User user, Claim claim, String password,
                                                              CommunicationDesigner communicationDesigner) {

        sendSelfServiceCustomerWelcomeEmail(claim, user.getCompanyCode(), password);
    }


    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEM_DATA_PROVIDER,
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsTest(User user, Claim claim, ClaimItem claimItem, String password,
                                                      String month, Double newPrice,
                                                      CommunicationDesigner communicationDesigner) {

        sendItemizationSubmitAndSaveLossItemsEmails(user, claim, claimItem, password, month, newPrice);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER},
            dataProvider = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEMS_WITH_ATTACHMENTS_DATA_PROVIDER,
            description = "Use communication designer to prepare Itemization Submit And Save Loss Items email with attachments")
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    public void itemizationSubmitAndSaveLossItemsWithAttachmentsTest(User user, Claim claim, ClaimItem claimItem,
                                                                     String password, String month, Double newPrice,
                                                                     CommunicationDesigner communicationDesigner) {

        sendItemizationSubmitAndSaveLossItemsEmails(user, claim, claimItem, password, month, newPrice);
    }

    @CommunicationDesignerCleanUp
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_REJECTION_MAIL_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcomeRejectionMail")
    public void customerWelcomeRejectionMailTest(User user, Claim claim,
                                                 CommunicationDesigner communicationDesigner) {

        Page.at(SettlementPage.class)
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
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcome")
    public void customerWelcomeTest(User user, Claim claim, ClaimItem claimItem,
                                    CommunicationDesigner communicationDesigner) {

        Page.at(SettlementPage.class)
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
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = CUSTOMER_WELCOME_WITH_OUTSTATNDING_DATA_PROVIDER,
            description = "Use communication designer to prepare CustomerWelcomeWithOutstanding mail")
    public void customerWelcomeWithOutstandingTest(User user, Claim claim, ServiceAgreement agreement,
                                                   Translations translations, ClaimItem claimItem, String lineDescription,
                                                   Double newPrice, BigDecimal repairPrice, String regNumber,
                                                   String accountNumber, String selfRisk,
                                                   CommunicationDesigner communicationDesigner) {

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
                .completeClaimUsingCashPayoutToBankAccount(regNumber, accountNumber)
                .reopenClaim();
        new SettlementSummary()
                .editSelfRisk(selfRisk)
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
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SPLIT_REPLACEMENT_DATA_PROVIDER,
            description = "Use communication designer to prepare split replacement mails")
    public void splitReplacementTest(User user, Claim claim, ClaimItem claimItem, String regNumber, String accountNumber,
                                     CommunicationDesigner communicationDesigner) {

        sendSplitReplacementEmails(user, claim, claimItem, regNumber, accountNumber);
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER,
            description = "Use communication designer to prepare split replacement mails with attachments")
    public void splitReplacementWithAttachmentsTest(User user, Claim claim, ClaimItem claimItem, String regNumber,
                                                    String accountNumber,
                                                    CommunicationDesigner communicationDesigner) {

        sendSplitReplacementEmails(user, claim, claimItem, regNumber, accountNumber);
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = ORDER_CONFIRMATION_DATA_PROVIDER,
            description = "Use communication designer to prepare order confirmation mails")
    public void orderConfirmationTest(User user, Claim claim, ClaimItem claimItem,
                                      CommunicationDesigner communicationDesigner) {

        Boolean isEvoucher = false;
        VoucherInfo voucherInfo = getVoucherInfo(isEvoucher);

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

        new CustomerDetailsPage().toMailsPage()
                .viewMail(MailsPage.MailType.ORDER_CONFIRMATION, ORDER_CONFIRMATION)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(ORDER_CONFIRMATION));

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @CommunicationDesignerCleanUp
    @RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
    @RequiredSetting(type = FTSetting.SPLIT_REPLACEMENT_EMAIL)
    @Test(groups = {TestGroups.COMMUNICATION_DESIGNER}, dataProvider = REPLACEMENT_MAIL_DATA_PROVIDER,
            description = "Use communication designer to prepare replacement mail")
    public void replacementMailTest(User user, Claim claim, ClaimItem claimItem, String regNumber, String accountNumber,
                                    CommunicationDesigner communicationDesigner) {

        replacement(claim, claimItem, regNumber, accountNumber)
                .doAssert(mailViewDialog ->
                        mailViewDialog.noOtherMailsOnThePage(
                                Arrays.asList(
                                        MailsPage.MailType.REPLACEMENT_WITH_MAIL,
                                        MailsPage.MailType.SETTLEMENT_NOTIFICATION_TO_IC)))
                .viewMail(MailsPage.MailType.REPLACEMENT_WITH_MAIL)
                .doAssert(mailViewDialog ->
                        mailViewDialog.isTextVisible(REPLACEMENT_MAIL)
                );

        schemaValidation(user.getCompanyName().toLowerCase(), claim.getClaimNumber());
    }

    @DataProvider(name = SELF_SERVICE_CUSTOMER_WELCOME_DATA_PROVIDER)
    public static Object[][] selfServiceCustomerWelcomeDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setSelfServiceCustomerWelcome(false, null);

        String password = Constants.DEFAULT_PASSWORD;

        return addNewParameters(TestDataActions.getTestDataParameters(method), password, communicationDesigner);
    }

    @DataProvider(name = SELF_SERVICE_CUSTOMER_WELCOME_WITH_ATTACHMENT_DATA_PROVIDER)
    public static Object[][] selfServiceCustomerWelcomeWithAttachmentsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setSelfServiceCustomerWelcome(true, TWO_ATTACHMENTS);

        String password = Constants.DEFAULT_PASSWORD;

        return addNewParameters(TestDataActions.getTestDataParameters(method), password, communicationDesigner);
    }

    @DataProvider(name = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEM_DATA_PROVIDER)
    public static Object[][] itemizationSubmitAndSaveLossItemsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setItemizationSaveLossItems(false, null)
                .setItemizationSubmitLossItems(false, null);

        String password = Constants.DEFAULT_PASSWORD;
        String month = Constants.JANUARY;
        Double newPrice = Double.valueOf(3000);

        return addNewParameters(TestDataActions.getTestDataParameters(method), password, month, newPrice, communicationDesigner);
    }

    @DataProvider(name = ITEMIZATION_SUBMIT_AND_SAVE_LOSS_ITEMS_WITH_ATTACHMENTS_DATA_PROVIDER)
    public static Object[][] itemizationSubmitAndSaveLossItemsWithAttachmentsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setItemizationSaveLossItems(true, TWO_ATTACHMENTS)
                .setItemizationSubmitLossItems(true, ONE_ATTACHMENT);

        String password = Constants.DEFAULT_PASSWORD;
        String month = Constants.JANUARY;
        Double newPrice = Double.valueOf(3000);

        return addNewParameters(TestDataActions.getTestDataParameters(method), password, month, newPrice, communicationDesigner);
    }

    @DataProvider(name = CUSTOMER_WELCOME_REJECTION_MAIL_DATA_PROVIDER)
    public static Object[][] customerWelcomeRejectionMailDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcomeRejectionMail(false, null);

        return addNewParameters(TestDataActions.getTestDataParameters(method), communicationDesigner);
    }

    @DataProvider(name = CUSTOMER_WELCOME_DATA_PROVIDER)
    public static Object[][] customerWelcomeDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(false, null);

        return addNewParameters(TestDataActions.getTestDataParameters(method), communicationDesigner);
    }

    @DataProvider(name = CUSTOMER_WELCOME_WITH_OUTSTATNDING_DATA_PROVIDER)
    public static Object[][] customerWelcomeWithOutstandingDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcomeWithOutstanding(false, null);

        String lineDescription = RandomUtils.randomName("RnVLine");
        Double newPrice = RnVMock.OK_PRICE;
        BigDecimal repairPrice = BigDecimal.valueOf(Constants.PRICE_100);
        String regNumber = "1";
        String accountNumber = "12345678890";
        String selfRisk = "2000";

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, newPrice, repairPrice,
                regNumber, accountNumber, selfRisk, communicationDesigner);
    }

    @DataProvider(name = SPLIT_REPLACEMENT_DATA_PROVIDER)
    public static Object[][] splitReplacementDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(false, null)
                .setOrderConfirmation(false, null);

        String regNumber = "1";
        String accountNumber = "12345678890";

        return addNewParameters(TestDataActions.getTestDataParameters(method), regNumber, accountNumber, communicationDesigner);
    }

    @DataProvider(name = SPLIT_REPLACEMENT_WITH_ATTACHMENTS_DATA_PROVIDER)
    public static Object[][] splitReplacementWithAttachmentsDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setCustomerWelcome(true, TWO_ATTACHMENTS)
                .setOrderConfirmation(true, ONE_ATTACHMENT);

        String regNumber = "1";
        String accountNumber = "12345678890";

        return addNewParameters(TestDataActions.getTestDataParameters(method), regNumber, accountNumber, communicationDesigner);
    }

    @DataProvider(name = ORDER_CONFIRMATION_DATA_PROVIDER)
    public static Object[][] orderConfirmationDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setOrderConfirmation(false, null);

        return addNewParameters(TestDataActions.getTestDataParameters(method), communicationDesigner);
    }

    @DataProvider(name = REPLACEMENT_MAIL_DATA_PROVIDER)
    public static Object[][] replacementMailDataProvider(Method method) {

        CommunicationDesigner communicationDesigner = new CommunicationDesigner()
                .setUseOutputManagement(true)
                .setReplacementMail(false, null);

        String regNumber = "1";
        String accountNumber = "12345678890";

        return addNewParameters(TestDataActions.getTestDataParameters(method), regNumber, accountNumber, communicationDesigner);
    }
}

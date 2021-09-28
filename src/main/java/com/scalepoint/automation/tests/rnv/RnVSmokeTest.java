package com.scalepoint.automation.tests.rnv;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class RnVSmokeTest extends RnVBase {

    private static final String SEND_LINE_TO_RNV_SEND_FEEDBACK_IS_SUCCESS_DATA_PROVIDER = "sendLineToRnvSSendFeedbackIsSuccessDataProvider";
    private static final String MESSAGES_DATA_PROVIDER = "messagesDataProvider";
    private static final String SINGLE_INVOICE_CREDIT_NOTES_DATA_PROVIDER = "singleInvoiceCreditNotesDataProvider";
    private static final String MULTIPLE_INVOICED_CREDIT_NOTES_DATA_PROVIDER = "multipleInvoicesCreditNotesDataProvider";
    private static final String CREDIT_NOTE_WITHOUT_INVOICE_NUMBER_DATA_PROVIDER = "creditNoteWithoutInvoiceNumberDataProvider";
    private static final String SELF_RISK_LOWER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER = "selfRiskLowerThanTotalAmountOfCompensationDataProvider";

    private static final String TEST_MESSAGE = "Test message";

    @BeforeMethod
    public void toSettlementPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        Claim claim = getLisOfObjectByClass(parameters, Claim.class).get(0);
        ServiceAgreement agreement = getLisOfObjectByClass(parameters, ServiceAgreement.class).get(0);
        Translations translations = getLisOfObjectByClass(parameters, Translations.class).get(0);
        String lineDescription = getLisOfObjectByClass(parameters, String.class).get(0);

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid()
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);
    }
    /*
     * send line to RnV
     * send feedback with Invoice
     * Assert: task has feedback received status
     * Assert: there are lines in Invoice dialog opened from Invoice tab
     */
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE},
            dataProvider = SEND_LINE_TO_RNV_SEND_FEEDBACK_IS_SUCCESS_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnvSSendFeedbackIsSuccessTest(User user, Claim claim, ServiceAgreement agreement,
                                                        Translations translations, String lineDescription,
                                                        BigDecimal repairPrice, Double lineTotal) {

        completeWithEmailAndSendRnV(lineDescription);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice,claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .findInvoiceLineByIndex(1)
                .assertTotalForTheLineWithIndex(1, lineTotal);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = MESSAGES_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void messagesTest(User user, Claim claim, ServiceAgreement agreement, Translations translations,
                             String lineDescription, String testMessage) {

        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .sendTextMailToSePa(testMessage)
                .assertLatestMessageContains(testMessage);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = SINGLE_INVOICE_CREDIT_NOTES_DATA_PROVIDER,
            description = "Sends multiple credit notes to the single invoice")
    public void singleInvoiceCreditNotesTest(User user, Claim claim, ServiceAgreement agreement,
                                             Translations translations, String lineDescription, BigDecimal repairPrice,
                                             BigDecimal invoicePrice, BigDecimal creditNote1, BigDecimal creditNote2,
                                             BigDecimal creditNote3) {

        completeWithEmailAndSendRnV(lineDescription);

        ServiceTaskImport serviceTaskImport = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        ProjectsPage projectsPage = new ClaimNavigationMenu().toRepairValuationProjectsPage();
        verifyInvoicesDetails(projectsPage, 1, 0, invoicePrice, invoicePrice);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport, creditNote1);

        BigDecimal invoiceTotal = invoicePrice.subtract(creditNote1);

        projectsPage.refresh();
        verifyInvoicesDetails(projectsPage, 2, 1, creditNote1, invoiceTotal);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport, creditNote2);

        invoiceTotal = invoiceTotal.subtract(creditNote2);

        projectsPage.refresh();
        verifyInvoicesDetails(projectsPage, 3, 2, creditNote2, invoiceTotal);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport, creditNote3);

        invoiceTotal = invoiceTotal.subtract(creditNote3);

        projectsPage.refresh();
        verifyInvoicesDetails(projectsPage, 4, 3, creditNote3, invoiceTotal);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport, creditNote3)
                .doAssert(rnvService -> {
                    rnvService.assertCreated();
                    rnvService.assertCreditNoteHigherThanInvoice();
                });

        projectsPage.refresh();
        verifyInvoiceTotalAndGridSize(projectsPage, invoiceTotal, 4);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = MULTIPLE_INVOICED_CREDIT_NOTES_DATA_PROVIDER,
            description = "Sends multiple creditNotes to the multiple invoices")
    public void multipleInvoicesCreditNotesTest(User user, Claim claim, ServiceAgreement agreement,
                                                Translations translations, String lineDescription,
                                                BigDecimal invoicePrice, BigDecimal creditNote, BigDecimal repairPrice) {

        completeWithEmailAndSendRnV(lineDescription);

        ServiceTaskImport serviceTaskImport1 = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        ProjectsPage projectsPage = new ClaimNavigationMenu().toRepairValuationProjectsPage();
        verifyInvoicesDetails(projectsPage, 1, 0, invoicePrice, invoicePrice);

        ServiceTaskImport serviceTaskImport2 = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        BigDecimal invoiceTotal = invoicePrice.add(invoicePrice);

        projectsPage.refresh();
        verifyInvoicesDetails(projectsPage, 2, 1, invoicePrice, invoiceTotal);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport1, creditNote)
                .doAssert(rnvService -> {
                    rnvService.assertCreated();
                    rnvService.assertCreditNoteHigherThanInvoice();
                });

        projectsPage.refresh();
        verifyInvoiceTotalAndGridSize(projectsPage, invoiceTotal, 2);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport2, invoicePrice);

        invoiceTotal = invoiceTotal.subtract(invoicePrice);

        projectsPage.refresh();
        verifyInvoicesDetails(projectsPage, 3, 2, invoicePrice, invoiceTotal);

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport2, invoicePrice)
                .doAssert(rnvService -> {
                    rnvService.assertCreated();
                    rnvService.assertCreditNoteHigherThanInvoice();
                });

        projectsPage.refresh();
        verifyInvoiceTotalAndGridSize(projectsPage, invoiceTotal, 3);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = CREDIT_NOTE_WITHOUT_INVOICE_NUMBER_DATA_PROVIDER,
            description = "Sends credit note without correct invoice number")
    public void creditNoteWithoutInvoiceNumberTest(User user, Claim claim, ServiceAgreement agreement,
                                                   Translations translations, String lineDescription,
                                                   BigDecimal invoicePrice, BigDecimal repairPrice) {

        completeWithEmailAndSendRnV(lineDescription);

        ServiceTaskImport serviceTaskImport = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);
        serviceTaskImport.getInvoice().setInvoiceNumber("");

        new RnvService()
                .sendDefaultFeedbackWithCreditNote(serviceTaskImport, invoicePrice)
                .doAssert(rnvService -> {
                    rnvService.assertCreated();
                    rnvService.assertMissingInvoiceReferenceNumber();
                });

        ProjectsPage projectsPage = new ClaimNavigationMenu().toRepairValuationProjectsPage();
        verifyInvoicesDetails(projectsPage, 1, 0, invoicePrice, invoicePrice);
    }

    @DataProvider(name = SEND_LINE_TO_RNV_SEND_FEEDBACK_IS_SUCCESS_DATA_PROVIDER)
    public static Object[][] sendLineToRnvSSendFeedbackIsSuccessDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        Double lineTotal = 50.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, repairPrice, lineTotal).toArray()
        };
    }

    @DataProvider(name = MESSAGES_DATA_PROVIDER)
    public static Object[][] messagesDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription).toArray()
        };
    }

    @DataProvider(name = SINGLE_INVOICE_CREDIT_NOTES_DATA_PROVIDER)
    public static Object[][] singleInvoiceCreditNotesDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        BigDecimal creditNote1 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(25.00);
        BigDecimal creditNote2 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal creditNote3 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, repairPrice, invoicePrice, creditNote1, creditNote2, creditNote3).toArray()
        };
    }

    @DataProvider(name = MULTIPLE_INVOICED_CREDIT_NOTES_DATA_PROVIDER)
    public static Object[][] multipleInvoicesCreditNotesDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        BigDecimal creditNote = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(75.00);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, repairPrice, invoicePrice, creditNote).toArray()
        };
    }

    @DataProvider(name = CREDIT_NOTE_WITHOUT_INVOICE_NUMBER_DATA_PROVIDER)
    public static Object[][] creditNoteWithoutInvoiceNumberDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, repairPrice, invoicePrice).toArray()
        };
    }

    @DataProvider(name = SELF_RISK_LOWER_THAN_TOTAL_AMOUNT_OF_COMPENSATION_DATA_PROVIDER)
    public static Object[][] selfRiskLowerThanTotalAmountOfCompensationDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal selfRiskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, lineDescription, selfRiskByServicePartner, repairPrice, selfRisk).toArray()
        };
    }
}


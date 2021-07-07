package com.scalepoint.automation.tests.rnv;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.OverviewTab;
import com.scalepoint.automation.pageobjects.pages.suppliers.DefaultSettingsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public class RnVSmokeTest extends BaseTest {

    RnVMock.RnvStub rnvStub;

    /*
     * send line to RnV
     * send feedback with Invoice
     * Assert: task has feedback received status
     * Assert: there are lines in Invoice dialog opened from Invoice tab
     */

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        completeWithEmailAndSendRnV(user, claim, agreement, translations);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_30),claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .findInvoiceLineByIndex(1)
                .assertTotalForTheLineWithIndex(1, Constants.PRICE_50);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void messagesTest(User user, Claim claim, ServiceAgreement agreement) {

        final String testMessage = "Test message";
        final String lineDescription = RandomUtils.randomName("RnVLine");

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
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);


        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .sendTextMailToSePa(testMessage)
                .assertLatestMessageContains(testMessage);
    }

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "Sends multiple credit notes to the single invoice")
    public void singleInvoiceCreditNotesTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        final BigDecimal creditNote1 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(25.00);
        final BigDecimal creditNote2 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal creditNote3 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        completeWithEmailAndSendRnV(user, claim, agreement, translations);

        ServiceTaskImport serviceTaskImport = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00), claim, rnvStub);

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

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "Sends multiple creditNotes to the multiple invoices")
    public void multipleInvoicesCreditNotesTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        final BigDecimal creditNote = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(75.00);

        completeWithEmailAndSendRnV(user, claim, agreement, translations);

        ServiceTaskImport serviceTaskImport1 = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00), claim, rnvStub);

        ProjectsPage projectsPage = new ClaimNavigationMenu().toRepairValuationProjectsPage();
        verifyInvoicesDetails(projectsPage, 1, 0, invoicePrice, invoicePrice);

        ServiceTaskImport serviceTaskImport2 = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00), claim, rnvStub);

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

    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "Sends credit note without correct invoice number")
    public void creditNoteWithoutInvoiceNumberTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);

        completeWithEmailAndSendRnV(user, claim, agreement, translations);

        ServiceTaskImport serviceTaskImport = new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00), claim, rnvStub);
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

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskLowerThanTotalAmounOfCompensation(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        final BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);
        final BigDecimal selfRiskTakenByInsureanceCompany = repairPrice.subtract(selfriskByServicePartner).subtract(selfRisk);

        setSelfRiskCollectedByServicePartner(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), selfriskByServicePartner)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfriskByServicePartner,
                selfRisk,
                zero,
                zero,
                selfRiskTakenByInsureanceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfriskByServicePartner,
                selfRisk,
                zero,
                selfRiskTakenByInsureanceCompany,
                zero);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskEqualToTotalAmountOfCompensation(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        final BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);
        final BigDecimal selfRiskTakenByInsureanceCompany = repairPrice.subtract(selfriskByServicePartner).subtract(selfRisk);

        setSelfRiskCollectedByServicePartner(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), selfriskByServicePartner)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfriskByServicePartner,
                selfRisk,
                zero,
                zero,
                selfRiskTakenByInsureanceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                selfriskByServicePartner,
                selfRisk,
                zero,
                selfRiskTakenByInsureanceCompany,
                zero);
    }
    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback", enabled = false)
    public void selfRiskHigherThanTotalAmountOfCompensation(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(20.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

        setSelfRiskCollectedByServicePartner(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner)
                .doAssert(rnvService -> rnvService.assertTakenSelfRiskNotWithinAllowedRange());

        verifyPanelView(agreement.getWaitingStatus(), zero);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskLowerThanRepairPrice(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(30.00);
        final BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);
        final BigDecimal selfRiskTakenByInsureanceCompany = repairPrice.subtract(selfRisk);

        setSelfRiskCollectedByInsuranceCompany(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), zero)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                selfRisk,
                zero,
                zero,
                selfRiskTakenByInsureanceCompany);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                selfRisk,
                zero,
                selfRiskTakenByInsureanceCompany,
                zero);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskEqualToRepairPrice(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);
        final BigDecimal selfRiskTakenByInsureanceCompany = repairPrice.subtract(selfRisk);

        setSelfRiskCollectedByInsuranceCompany(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), zero)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                selfRiskTakenByInsureanceCompany,
                zero,
                zero,
                repairPrice);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                zero,
                zero,
                selfRisk,
                zero);
    }

    @RequiredSetting(type = FTSetting.ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT)
    @RequiredSetting(type = FTSetting.DEFAULT_AUTOMATIC_INVOICE_PAYMENTS, value = "Insurance company")
    @Test(groups = {TestGroups.RNV, TestGroups.RNV_SMOKE}, dataProvider = "testDataProvider",
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void selfRiskHigherThanRepairPrice(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal selfriskByServicePartner = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal repairPrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);
        final BigDecimal selfRisk = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(20.00);
        final BigDecimal zero = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

        setSelfRiskCollectedByInsuranceCompany(user);

        sendRnVAndFeedbackWithTakenSelfRisk(user, claim, agreement, translations, repairPrice, selfriskByServicePartner);

        SettlementPage settlementPage = verifyPanelView(agreement.getFeedbackReceivedStatus(), zero)
                .clickEvaluateAssignment()
                .acceptFeedback()
                .toSettlementPage();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                zero,
                zero,
                zero,
                repairPrice);

        settlementPage = settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim();

        verifyRepairPanel(settlementPage,
                repairPrice,
                zero,
                zero,
                zero,
                repairPrice,
                zero);
    }

    private void completeWithEmailAndSendRnV(User user, Claim claim, ServiceAgreement agreement, Translations translations){

        String lineDescription = RandomUtils.randomName("RnVLine");

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
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }

    private void setSelfRiskCollectedByServicePartner(User user){

        DefaultSettingsPage defaultSettingsPage = login(user)
                .getMainMenu()
                .toEccAdminPage()
                .toDefaultSettings();
        defaultSettingsPage
                .toDefaultSettingsGrid()
                .getDefaultSettingsRow(0)
                .setSelfRiskCollectedByServicePartner();
        defaultSettingsPage.logout();
    }

    private void setSelfRiskCollectedByInsuranceCompany(User user){

        DefaultSettingsPage defaultSettingsPage = login(user)
                .getMainMenu()
                .toEccAdminPage()
                .toDefaultSettings();
        defaultSettingsPage
                .toDefaultSettingsGrid()
                .getDefaultSettingsRow(0)
                .setSelfRiskCollectedByInsuranceCompany();
        defaultSettingsPage.logout();
    }

    private RnvService sendRnVAndFeedbackWithTakenSelfRisk(User user, Claim claim, ServiceAgreement agreement, Translations translations, BigDecimal repairPrice, BigDecimal selfriskByServicePartner){

        final String lineDescription = RandomUtils.randomName("RnVLine");

        loginAndCreateClaim(user, claim)
                .getSettlementSummary()
                .editSelfRisk("15")
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

        return new RnvService()
                .sendFeedbackWithInvoiceWithRepairPriceAndTakenSelfRisk(repairPrice, selfriskByServicePartner, claim, rnvStub);
    }

    private void verifyInvoicesDetails(ProjectsPage projectsPage,
                                       int gridSize,
                                       int gridIndex,
                                       BigDecimal invoicePrice,
                                       BigDecimal invoiceTotal){

        InvoiceTab invoiceTab = verifyInvoiceTotalAndGridSize(projectsPage, invoiceTotal, gridSize);
        invoiceTab
                .toInvoiceGrid()
                .getGridLine(gridIndex)
                .doAssert(iGrid -> iGrid.assertTotal(invoicePrice));
        invoiceTab
                .openInvoiceDialogForLineWithIndex(gridIndex)
                .doAssert(iDialog -> iDialog.assertTotalIs(invoicePrice));
    }

    private InvoiceTab verifyInvoiceTotalAndGridSize(ProjectsPage projectsPage, BigDecimal invoiceTotal, int gridSize){

        projectsPage
                .toOverviewTab()
                .toPanelViewGrid()
                .getPanelViewGridLine(0)
                .doAssert(panelViewGridLine ->
                        panelViewGridLine.assertInvoicePrice(invoiceTotal)
                );

        InvoiceTab invoiceTab = projectsPage
                .toInvoiceTab();
        return invoiceTab
                .doAssert(iTab ->
                        iTab.assertInvoiceGridSize(gridSize));
    }

    private OverviewTab.PanelViewGrid.PanelViewGridLine verifyPanelView(String status, BigDecimal selfriskByServicePartner){

        return new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toOverviewTab()
                .toPanelViewGrid()
                .getPanelViewGridLine(0)
                .doAssert(panelViewGridLine ->
                        panelViewGridLine
                                .assertTaskStatus(status)
                                .assertSelfriskByServicePartner(selfriskByServicePartner)
                );
    }

    private SettlementSummary.RepairPanel verifyRepairPanel(SettlementPage settlementPage,
                                                            BigDecimal repairPrice,
                                                            BigDecimal selfriskByServicePartner,
                                                            BigDecimal substractedFromStatement,
                                                            BigDecimal payBackOverCollectedDeductible,
                                                            BigDecimal selfRiskTakenByInsureanceCompany,
                                                            BigDecimal outstandingSelfRiskTakenByInsureanceCompany){

        return settlementPage
                .getSettlementSummary()
                .getRepairPanel()
                .doAssert(repairPanel ->
                        repairPanel
                                .assertRepairPrice(repairPrice)
                                .assertSelfRiskTakenByServicePartner(selfriskByServicePartner)
                                .assertSubtractedFromStatement(substractedFromStatement)
                                .assertPayBackOverCollectedDeductible(payBackOverCollectedDeductible)
                                .asserSelfRiskTakenByInsureanceCompany(selfRiskTakenByInsureanceCompany)
                                .assertOutstandingSelfRiskTakenByInsureanceCompany(outstandingSelfRiskTakenByInsureanceCompany)
                );
    }
}


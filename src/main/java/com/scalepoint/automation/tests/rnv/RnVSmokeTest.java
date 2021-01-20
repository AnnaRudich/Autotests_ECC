package com.scalepoint.automation.tests.rnv;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;

public class RnVSmokeTest extends BaseTest {

    RnVMock.RnvStub rnvStub;

    @BeforeClass
    public void startWireMock() throws IOException {
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        rnvStub = new RnVMock(wireMock)
                .addStub();
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));
    }
    /*
     * send line to RnV
     * send feedback with Invoice
     * Assert: task has feedback received status
     * Assert: there are lines in Invoice dialog opened from Invoice tab
     */

    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        sendRnV(user, claim, agreement, translations);

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

    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void messagesTest(User user, Claim claim, ServiceAgreement agreement) {

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
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        final String testMessage = "Test message";
        new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .sendTextMailToSePa(testMessage)
                .assertLatestMessageContains(testMessage);
    }

    @Test(dataProvider = "testDataProvider", description = "Sends multiple credit notes to the single invoice")
    public void singleInvoiceCreditNotesTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        final BigDecimal creditNote1 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(25.00);
        final BigDecimal creditNote2 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(10.00);
        final BigDecimal creditNote3 = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(15.00);

        sendRnV(user, claim, agreement, translations);

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

    @Test(dataProvider = "testDataProvider", description = "Sends multiple creditNotes to the multiple invoices")
    public void multipleInvoicesCreditNotesTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);
        final BigDecimal creditNote = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(75.00);

        sendRnV(user, claim, agreement, translations);

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

    @Test(dataProvider = "testDataProvider", description = "Sends credit note without correct invoice number")
    public void creditNoteWithoutInvoiceNumberTest(User user, Claim claim, ServiceAgreement agreement, Translations translations) {

        final BigDecimal invoicePrice = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(50.00);

        sendRnV(user, claim, agreement, translations);

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

    private void sendRnV(User user, Claim claim, ServiceAgreement agreement, Translations translations){

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
}


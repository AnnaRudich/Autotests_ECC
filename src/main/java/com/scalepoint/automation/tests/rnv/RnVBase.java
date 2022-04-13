package com.scalepoint.automation.tests.rnv;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.modules.SettlementSummary;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.OverviewTab;
import com.scalepoint.automation.pageobjects.pages.suppliers.DefaultSettingsPage;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.NumberFormatUtils;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;

import java.math.BigDecimal;

public class RnVBase extends BaseTest {

    protected static final BigDecimal ZERO = NumberFormatUtils.formatBigDecimalToHaveTwoDigits(0.00);

    protected void completeWithEmailAndSendRnV(String lineDescription){

        Page.at(SettlementPage.class)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }

    protected void verifyInvoicesDetails(ProjectsPage projectsPage,
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

    protected InvoiceTab verifyInvoiceTotalAndGridSize(ProjectsPage projectsPage, BigDecimal invoiceTotal, int gridSize){

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

    protected void setSelfRiskCollectedByServicePartner(User user){

        DefaultSettingsPage defaultSettingsPage = loginFlow.login(user)
                .getMainMenu()
                .toEccAdminPage()
                .toDefaultSettings();

        defaultSettingsPage
                .toDefaultSettingsGrid()
                .getDefaultSettingsRow(0)
                .setSelfRiskCollectedByServicePartner();

        defaultSettingsPage.logout();
    }

    protected void setSelfRiskCollectedByInsuranceCompany(User user){

        DefaultSettingsPage defaultSettingsPage = loginFlow.login(user)
                .getMainMenu()
                .toEccAdminPage()
                .toDefaultSettings();

        defaultSettingsPage
                .toDefaultSettingsGrid()
                .getDefaultSettingsRow(0)
                .setSelfRiskCollectedByInsuranceCompany();

        defaultSettingsPage.logout();
    }

    protected RnvService sendRnVAndFeedbackWithTakenSelfRisk(User user, Claim claim, ServiceAgreement agreement,
                                                             Translations translations, String lineDescription,
                                                             String selfRisk, BigDecimal repairPrice,
                                                             BigDecimal selfriskByServicePartner){

        loginFlow.loginAndCreateClaim(user, claim)
                .getSettlementSummary()
                .editSelfRisk()
                .setSelfRisk(selfRisk)
                .clickOkButton()
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

    protected OverviewTab.PanelViewGrid.PanelViewGridLine verifyPanelView(String status, BigDecimal selfRiskByServicePartner){

        return new ClaimNavigationMenu()
                .toRepairValuationProjectsPage()
                .toOverviewTab()
                .toPanelViewGrid()
                .getPanelViewGridLine(0)
                .doAssert(panelViewGridLine ->
                        panelViewGridLine
                                .assertTaskStatus(status)
                                .assertSelfriskByServicePartner(selfRiskByServicePartner)
                );
    }

    protected SettlementSummary.RepairPanel verifyRepairPanel(SettlementPage settlementPage,
                                                              BigDecimal repairPrice,
                                                              BigDecimal selfRiskByServicePartner,
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
                                .assertSelfRiskTakenByServicePartner(selfRiskByServicePartner)
                                .assertSubtractedFromStatement(substractedFromStatement)
                                .assertPayBackOverCollectedDeductible(payBackOverCollectedDeductible)
                                .asserSelfRiskTakenByInsureanceCompany(selfRiskTakenByInsureanceCompany)
                                .assertOutstandingSelfRiskTakenByInsureanceCompany(outstandingSelfRiskTakenByInsureanceCompany)
                );
    }
}

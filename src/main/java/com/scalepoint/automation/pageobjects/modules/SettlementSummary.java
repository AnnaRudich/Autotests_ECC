package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SelfRiskDialog;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.OperationalUtils;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Table;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.NumberFormatUtils.formatDoubleToHaveTwoDigits;
import static com.scalepoint.automation.utils.OperationalUtils.toNumber;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SettlementSummary extends Module {

    private static final String FRAUDULENT_TEXT = "CentralScore ej ok";
    private static final String NOT_FRAUDULENT_TEXT = "CentralScore ok";
    private static final int FRAUD_ALERT_WAIT_TIMEOUT_MS = 30000;

    @FindBy(xpath = "//div[@id='settlementSummaryTotalTable-targetEl']//table")
    private Table claimsResult;

    @FindBy(id = "cancelCaseBtn")
    private WebElement cancel;

    @FindBy(id = "saveCaseBtn")
    private WebElement saveClaim;

    @FindBy(id = "finishCaseBtn")
    private WebElement completeClaim;

    @FindBy(id = "settleExternallyBtn")
    private WebElement completeClaimExternally;

    @FindBy(id="sendToAuditBtn-btnInnerEl")
    private WebElement sentToAudit;

    @FindBy(css = "#appContainer-body .x-region-collapsed-placeholder .x-tool-expand-top")
    private WebElement expand;

    @FindBy(id = "total_indemnity_replacement_amount-inputEl")
    private WebElement claimSumValue;

    @FindBy(id = "subtotal_cash_payout-inputEl")
    private WebElement subtotalValue;

    @FindBy(id = "draft-status-inputEl")
    private WebElement auditStatus;

    @FindBy(id = "auditInfoPanel")
    private WebElement auditInfoPanel;

    @FindBy(css = "[id^=fraudStatus] [role=textbox]")
    private WebElement fraudStatus;

    @FindBy(id = "settlementSummaryTotalsPanel")
    private WebElement settlementSummaryTotalsPanel;

    @FindBy(id="reopenClaimBtn")
    private WebElement reopenClaimViewMode;

    @FindBy(id = "closeClaimPreviewBtn")
    private WebElement closeClaimViewMode;

    public void cancel() {

        hoverAndClick($(cancel));
    }

    public void saveClaim() {

        expand();
        hoverAndClick($(saveClaim));
    }

    private boolean isExpanded(){

        return $("[aria-label='Collapse panel']")
                .has(visible);
    }

    public SettlementPage reopenClaimFromViewMode(){

        expand();
        $(reopenClaimViewMode).click();
        getAlertTextAndAccept();
        return Page.at(SettlementPage.class);
    }

    public CustomerDetailsPage closeClaimFromViewMode(){

        expand();
        hoverAndClick($(closeClaimViewMode));
        return Page.at(CustomerDetailsPage.class);

    }

    public void completeClaim() {

        expand();
        jsIfClickDoesNotWork($(completeClaim));
    }

    public void completeClaimWithoutMail() {

        expand();
        hoverAndClick($(completeClaimExternally));
    }

    private void expand() {

        if (!isExpanded()) {

            $(expand)
                    .should(Condition.visible)
                    .click();
        }
    }

    private String getClaimSumValue() {

        expand();
        $(claimSumValue).should(not(exactText("")));
        return claimSumValue.getText();
    }

    private String getSubtotalSumValue() {

        expand();
        $(subtotalValue).should(not(exactText("")));
        return subtotalValue.getText();
    }

    private boolean isCompleteClaimEnabled() {

        expand();
        return completeClaim.isEnabled();
    }

    public RepairPanel getRepairPanel(){

        expand();
        return new RepairPanel();
    }

    private boolean isFraudulent(){

        return $(fraudStatus)
                .should(Condition.or("fraudStatus", Condition.text(FRAUDULENT_TEXT), Condition.text(NOT_FRAUDULENT_TEXT)),
                        Duration.ofMillis(FRAUD_ALERT_WAIT_TIMEOUT_MS))
                .has(Condition.exactText(FRAUDULENT_TEXT));
    }

    public SettlementSummary ensureAuditInfoPanelVisible() {

        expand();
        verifyElementVisible($(auditInfoPanel));
        return this;
    }

    public SettlementSummary checkStatusFromAudit(String status) {

        ExpectedConditions.textToBePresentInElement(auditStatus, status);
        return this;
    }

    public SelfRiskDialog editSelfRisk(){

        expand();
        hoverAndClick($(By.xpath("//a[contains(text(), 'Selvrisiko:')]")));
        return BaseDialog.at(SelfRiskDialog.class);
    }

    @Getter
    static public class RepairPanel{

        private static final By repairPanelPath = By.cssSelector("#settlementSummaryTotalsRepairPanel");
        private static final By repairPanelItemsPath = By.cssSelector("[role=textbox]");

        private BigDecimal repairPrice;
        private BigDecimal selfRiskTakenByServicePartner;
        private BigDecimal subtractedFromStatement;
        private BigDecimal payBackOverCollectedDeductible;
        private BigDecimal selfRiskTakenByInsureanceCompany;
        private BigDecimal outstandingSelfRiskTakenByInsureanceCompany;

        RepairPanel(){
            ElementsCollection  repairPanelItems = $(repairPanelPath).findAll(repairPanelItemsPath);
            repairPrice = OperationalUtils.toBigDecimal(repairPanelItems.get(0).getText());
            selfRiskTakenByServicePartner = OperationalUtils.toBigDecimal(repairPanelItems.get(1).getText());
            subtractedFromStatement = OperationalUtils.toBigDecimal(repairPanelItems.get(2).getText());
            payBackOverCollectedDeductible = OperationalUtils.toBigDecimal(repairPanelItems.get(3).getText());
            selfRiskTakenByInsureanceCompany = OperationalUtils.toBigDecimal(repairPanelItems.get(4).getText());
            outstandingSelfRiskTakenByInsureanceCompany = OperationalUtils.toBigDecimal(repairPanelItems.get(5).getText());
        }

        static boolean isDisplayed(){

            return !$(repairPanelPath).has(visible);
        }

        public RepairPanel doAssert(Consumer<Asserts> assertFunc) {
            assertFunc.accept(new Asserts());
            return RepairPanel.this;
        }

        public class Asserts {
            public Asserts assertRepairPrice(BigDecimal expectedValue) {

                assertThat(getRepairPrice())
                        .as(String.format("Repair price should be : %s", expectedValue))
                        .isEqualTo(repairPrice);
                return this;
            }

            public Asserts assertSelfRiskTakenByServicePartner(BigDecimal expectedValue) {

                assertThat(getSelfRiskTakenByServicePartner())
                        .as(String.format("Deductible charged by repairer should be : %s", expectedValue))
                        .isEqualTo(expectedValue);
                return this;
            }

            public Asserts assertSubtractedFromStatement(BigDecimal expectedValue) {

                assertThat(getSubtractedFromStatement())
                        .as(String.format("Subtracted from statement should be : %s", expectedValue))
                        .isEqualTo(expectedValue);
                return this;
            }

            public Asserts assertPayBackOverCollectedDeductible(BigDecimal expectedValue) {

                assertThat(getPayBackOverCollectedDeductible())
                        .as(String.format("Pay back over collected deductible should be : %s", expectedValue))
                        .isEqualTo(expectedValue);
                return this;
            }

            public Asserts asserSelfRiskTakenByInsureanceCompany(BigDecimal expectedValue) {

                assertThat(getSelfRiskTakenByInsureanceCompany())
                        .as(String.format("Deductible charged by company should be : %s", expectedValue))
                        .isEqualTo(expectedValue);
                return this;
            }

            public Asserts assertOutstandingSelfRiskTakenByInsureanceCompany(BigDecimal expectedValue) {

                assertThat(getOutstandingSelfRiskTakenByInsureanceCompany())
                        .as(String.format("Outstanding deductible charged by company should be : %s", expectedValue))
                        .isEqualTo(expectedValue);
                return this;
            }
        }
    }

    public SettlementSummary doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return SettlementSummary.this;
    }

    public class Asserts {
        public Asserts assertClaimSumValueIs(double value) {

            assertThat(toNumber(getClaimSumValue()))
                    .as("Claim sum must be: " + value)
                    .isEqualTo(formatDoubleToHaveTwoDigits(value));
            return this;
        }

        public Asserts assertSubtotalSumValueIs(double value) {

            assertThat(toNumber(getSubtotalSumValue()))
                    .as("Subtotal sum must be: " + value)
                    .isEqualTo(formatDoubleToHaveTwoDigits(value));
            return this;
        }

        public Asserts assertCompleteClaimEnabled() {

            assertThat(isCompleteClaimEnabled())
                    .as("Complete Claim button is disabled")
                    .isTrue();
            return this;
        }

        public Asserts assertFraudulent(){

            assertThat(isFraudulent())
                    .as("Claim is not fraudulent")
                    .isTrue();
            return this;
        }

        public Asserts assertNotFraudulent(){

            assertThat(isFraudulent())
                    .as("Claim is fraudulent")
                    .isFalse();
            return this;
        }

    }
}

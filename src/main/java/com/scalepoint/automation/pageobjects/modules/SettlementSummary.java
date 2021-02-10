package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.OperationalUtils;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Table;

import java.math.BigDecimal;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.utils.NumberFormatUtils.formatDoubleToHaveTwoDigits;
import static com.scalepoint.automation.utils.OperationalUtils.toNumber;
import static com.scalepoint.automation.utils.Wait.waitForLoaded;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SettlementSummary extends Module {

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

    @FindBy(xpath = "//div[contains(@class, 'x-tool-expand-top')]")
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

    public void cancel() {
        hoverAndClick($(cancel));
    }

    public void saveClaim() {
        SelenideElement element = $(saveClaim);
        if (!element.isDisplayed()) {
            expand();
        }
        element.click();
    }

    public void completeClaim() {
        if (!completeClaim.isDisplayed() & !sentToAudit.isDisplayed()) {
            expand();
        }
        jsIfClickDoesNotWork($(completeClaim));
    }

    public void completeClaimWithoutMail() {
        if (!completeClaimExternally.isDisplayed()) {
            expand();
        }
        hoverAndClick($(completeClaimExternally));
    }

    private void expand() {
        $(expand).waitUntil(Condition.visible, Constants.WAIT_UNTIL_MS).click();
    }

    private String getClaimSumValue() {
        if (!claimSumValue.isDisplayed()) {
            expand();
        }
        $(claimSumValue).waitUntil(not(exactText("")), Constants.WAIT_UNTIL_MS);
        return claimSumValue.getText();
    }

    private String getSubtotalSumValue() {
        if (!subtotalValue.isDisplayed()) {
            expand();
        }
        $(subtotalValue).waitUntil(not(exactText("")), Constants.WAIT_UNTIL_MS);
        return subtotalValue.getText();
    }

    private boolean isCompleteClaimEnabled() {
        if (!completeClaim.isDisplayed()) {
            expand();
        }
        return completeClaim.isEnabled();
    }

    public RepairPanel getRepairPanel(){
        if (RepairPanel.isDisplayed()) {
            expand();
        }
        return new RepairPanel();
    }

    private boolean isFraudulent(){

        String text = "CentralScore ej ok";
        if($(settlementSummaryTotalsPanel).is(not(Condition.visible))){
            expand();
        }
        return $(fraudStatus)
                .waitUntil(Condition.text(text), FRAUD_ALERT_WAIT_TIMEOUT_MS).getText().equals(text);
    }

    private boolean isNotFraudulent(){

        String text = "CentralScore ok";
        if($(settlementSummaryTotalsPanel).is(not(Condition.visible))){
            expand();
        }

        return $(fraudStatus)
                .waitUntil(Condition.text(text), FRAUD_ALERT_WAIT_TIMEOUT_MS).getText().equals(text);
    }

    public SettlementSummary ensureAuditInfoPanelVisible() {
        expand();
        waitForVisible(auditInfoPanel);
        return this;
    }

    public SettlementSummary checkStatusFromAudit(String status) {
        ExpectedConditions.textToBePresentInElement(auditStatus, status);
        return this;
    }

    public SettlementPage editSelfRisk(String newValue){
        if($(By.xpath("//a[contains(text(), 'Selvrisiko:')]")).is(not(visible))){
            expand();
        }

        $(By.xpath("//a[contains(text(), 'Selvrisiko:')]"))
                .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                .click();

        $(By.xpath("//input[@role='textbox']"))
                .waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS)
                .setValue(newValue);

        $(By.xpath("//span[contains(text(), 'OK')]/parent::span")).click();
        waitForLoaded();
        return at(SettlementPage.class);
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

            Assert.assertEquals(toNumber(getClaimSumValue()), formatDoubleToHaveTwoDigits(value), "Claim sum must be: " + value);
            return this;
        }

        public Asserts assertSubtotalSumValueIs(double value) {
            Assert.assertEquals(toNumber(getSubtotalSumValue()), formatDoubleToHaveTwoDigits(value), "Subtotal sum must be: " + value);
            return this;
        }

        public Asserts assertCompleteClaimEnabled() {
            Assert.assertTrue(isCompleteClaimEnabled(), "Complete Claim button is disabled");
            return this;
        }

        public Asserts assertFraudulent(){
            Assert.assertTrue(isFraudulent(), "Claim is not fraudulent");
            return this;
        }

        public Asserts assertNotFraudulent(){
            Assert.assertTrue(isNotFraudulent(), "Claim is fraudulent");
            return this;
        }

    }
}

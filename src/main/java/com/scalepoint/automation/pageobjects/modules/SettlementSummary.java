package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.utils.OperationalUtils.toNumber;
import static com.scalepoint.automation.utils.Wait.waitForLoaded;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SettlementSummary extends Module {

    private static final int WAIT_TIMEOUT_MS = 6000;

    @FindBy(xpath = "//div[@id='settlementSummaryTotalTable-targetEl']//table")
    private Table claimsResult;

    @FindBy(id = "cancelCaseBtn")
    private Button cancel;

    @FindBy(id = "saveCaseBtn")
    private WebElement saveClaim;

    @FindBy(id = "finishCaseBtn")
    private Button completeClaim;

    @FindBy(id = "settleExternallyBtn")
    private Button completeClaimExternally;

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
        clickUsingJsIfSeleniumClickReturnError(cancel);
    }

    public void saveClaim() {
        SelenideElement element = $(saveClaim);
        if (!element.isDisplayed()) {
            expand();
        }
        element.click();
    }

    public void completeClaim() {
        if (!completeClaim.isDisplayed()) {
            expand();
        }
        clickUsingJsIfSeleniumClickReturnError(completeClaim);
    }

    public void completeClaimWithoutMail() {
        if (!completeClaimExternally.isDisplayed()) {
            expand();
        }
        clickUsingJsIfSeleniumClickReturnError(completeClaimExternally);
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

    private boolean isFraudulent(){
        
        String text = "CentralScore ej ok";
        if($(settlementSummaryTotalsPanel).is(not(Condition.visible))){
            expand();
        }
        return $(fraudStatus)
                .waitUntil(Condition.text(text), WAIT_TIMEOUT_MS).getText().equals(text);
    }

    private boolean isNotFraudulent(){

        String text = "CentralScore ok";
        if($(settlementSummaryTotalsPanel).is(not(Condition.visible))){
            expand();
        }

        return $(fraudStatus)
                .waitUntil(Condition.text(text), WAIT_TIMEOUT_MS).getText().equals(text);
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
        if(!$(By.xpath("//a[contains(text(), 'Selvrisiko:')]")).isDisplayed()){
            expand();
        }

        $(By.xpath("//a[contains(text(), 'Selvrisiko:')]"))
                .waitUntil(Condition.visible, WAIT_TIMEOUT_MS)
                .click();

        $(By.xpath("//input[@role='textbox']"))
                .waitUntil(Condition.visible, WAIT_TIMEOUT_MS)
                .setValue(newValue);

        $(By.xpath("//span[contains(text(), 'OK')]/parent::span")).click();
        waitForLoaded();
        return at(SettlementPage.class);
    }

    public SettlementSummary doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return SettlementSummary.this;
    }

    public class Asserts {
        public Asserts assertClaimSumValueIs(double value) {
            Assert.assertEquals(toNumber(getClaimSumValue()), value, "Claim sum must be: " + value);
            return this;
        }

        public Asserts assertSubtotalSumValueIs(double value) {
            Assert.assertEquals(toNumber(getSubtotalSumValue()), value, "Subtotal sum must be: " + value);
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

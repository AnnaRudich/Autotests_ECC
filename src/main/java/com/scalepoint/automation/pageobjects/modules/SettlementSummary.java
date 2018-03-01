package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.OperationalUtils.toNumber;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SettlementSummary extends Module {

    @FindBy(xpath = "//div[@id='settlementSummaryTotalTable-targetEl']//table")
    private Table claimsResult;

    @FindBy(id = "cancelCaseBtn")
    private Button cancel;

    @FindBy(id = "saveCaseBtn")
    private Button saveClaim;

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

    public void cancel() {
        clickUsingJsIfSeleniumClickReturnError(cancel);
    }

    public void saveClaim() {
        if (!saveClaim.isDisplayed()) {
            expand();
        }
        clickUsingJsIfSeleniumClickReturnError(saveClaim);
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
        clickUsingJsIfSeleniumClickReturnError(expand);
        Wait.waitForDisplayed(By.id("finishCaseBtn"));
    }

    private String getClaimSumValue() {
        if (!claimSumValue.isDisplayed()) {
            expand();
        }
        return claimSumValue.getText();
    }

    private String getSubtotalSumValue() {
        if (!subtotalValue.isDisplayed()) {
            expand();
        }
        return subtotalValue.getText();
    }

    private boolean isCompleteClaimEnabled() {
        if (!completeClaim.isDisplayed()) {
            expand();
        }
        return completeClaim.isEnabled();
    }


    public SettlementSummary ensureAuditInfoPanelVisible() {
        expand();
        waitForVisible(auditInfoPanel);
        return this;
    }

    public SettlementSummary checkStatusFromAudit(String status){
        ExpectedConditions.textToBePresentInElement(auditStatus, status);
        return this;
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

    }
}

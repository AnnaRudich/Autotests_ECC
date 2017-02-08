package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import static com.scalepoint.automation.utils.OperationalUtils.toNumber;

public class BottomMenu extends Module {

    @FindBy(xpath = "//div[@id='settlementSummaryTotalTable-targetEl']//table")
    private Table claimsResult;

    @FindBy(id = "cancelCaseBtn")
    private Button cancel;

    @FindBy(id = "saveCaseBtn")
    private Button saveClaim;

    @FindBy(id = "finishCaseBtn")
    private Button completeClaim;

    @FindBy(xpath = "//div[contains(@class, 'x-tool-expand-top')]")
    private WebElement expand;

    @FindBy(id = "total_indemnity_replacement_amount-inputEl")
    private WebElement claimSumValue;

    @FindBy(id = "subtotal_cash_payout-inputEl")
    private WebElement subtotalValue;

    public void cancel() {
        cancel.click();
    }

    public void saveClaim() {
        if (!saveClaim.isDisplayed()) {
            expand();
        }
        saveClaim.click();
    }

    public void completeClaim() {
        if (!completeClaim.isDisplayed()) {
            expand();
        }
        completeClaim.click();
    }

    private void expand() {
        expand.click();
        Wait.waitForDisplayed(By.id("finishCaseBtn"));
    }

    private String getClaimSumValue() {
        if (!claimSumValue.isDisplayed()) {
            expand();
        }
        return claimSumValue.getText();
    }

    public BottomMenu assertClaimSumValueIs(double value) {
        Assert.assertEquals(toNumber(getClaimSumValue()), value, "Claim sum must be: "+value);
        return this;
    }

    public BottomMenu assertSubtotalSumValueIs(double value) {
        Assert.assertEquals(toNumber(getSubtotalSumValue()), value, "Subtotal sum must be: "+value);
        return this;
    }

    private String getSubtotalSumValue() {
        if (!subtotalValue.isDisplayed()) {
            expand();
        }
        return subtotalValue.getText();
    }

    public boolean isCompleteClaimEnabled() {
        if (!completeClaim.isDisplayed()) {
            expand();
        }
        return completeClaim.isEnabled();
    }

    public BottomMenu assertCompleteClaimEnabled() {
        Assert.assertTrue(isCompleteClaimEnabled(), "Complete Claim button is disabled");
        return this;
    }

}

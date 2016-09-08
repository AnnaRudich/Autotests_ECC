package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

public class BottomMenu extends Module {

    @FindBy(xpath = "//div[@id='settlementSummaryTotalTable-targetEl']//table")
    private Table claimsResult;

    @FindBy(id = "cancelCaseBtn")
    private Button cancel;

    @FindBy(id = "saveCaseBtn")
    private Button saveClaim;

    @FindBy(id = "finishCaseBtn")
    private Button completeClaim;

    @FindBy(xpath = "//img[contains(@class, 'x-tool-expand-top')]")
    private WebElement expand;

    @FindBy(id = "total_indemnity_replacement_amount-inputEl")
    private WebElement claimSumValue;

    @FindBy(id = "subtotal_cash_payout-inputEl")
    private WebElement subtotalValue;


    public Table getClaimsResult() {
        return claimsResult;
    }

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
        Wait.waitForElementDisplaying(By.id("finishCaseBtn"));
    }

    public String getClaimSumValue() {
        if (!claimSumValue.isDisplayed()) {
            expand();
        }
        return claimSumValue.getText();
    }

    public String getSubtotalSumValue() {
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

}

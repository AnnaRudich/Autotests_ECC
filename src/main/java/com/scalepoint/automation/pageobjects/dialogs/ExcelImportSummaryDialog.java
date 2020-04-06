package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class ExcelImportSummaryDialog extends BaseDialog {

    @FindBy(id = "import-summary-close-button")
    private WebElement confirmButton;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(confirmButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public SettlementPage confirmImportSummary() {
        confirmButton.click();
        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }
}

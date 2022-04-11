package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class ExcelImportSummaryDialog extends BaseDialog {

    @FindBy(id = "import-summary-close-button")
    private SelenideElement confirmButton;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        confirmButton.should(Condition.visible);
    }

    public SettlementPage confirmImportSummary() {

        confirmButton.click();
        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }
}

package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@EccPage
public class ExcelImportSummaryDialog extends BaseDialog {

    private SelenideElement confirmButton = $(By.id("import-summary-close-button"));

    @Override
    protected BaseDialog ensureWeAreAt() {
        confirmButton.shouldBe(Condition.visible);
        return null;
    }

    public SettlementPage confirmImportSummary() {
        confirmButton.click();
        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }
}

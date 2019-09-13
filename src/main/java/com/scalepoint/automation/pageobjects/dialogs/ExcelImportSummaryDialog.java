package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class ExcelImportSummaryDialog extends BaseDialog {

    @FindBy(id = "import-summary-close-button")
    private WebElement confirmButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(confirmButton);
        return null;
    }

    public SettlementPage confirmImportSummary() {
        confirmButton.click();
        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }
}

package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@EccPage
public class ExcelImportSummaryDialog extends BaseDialog {

    private static final String CLOSE_IMPORT = "import-summary-close-button";

    @FindBy(id = CLOSE_IMPORT)
    private WebElement confirmButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForDisplayed(By.id(CLOSE_IMPORT));
        return null;
    }

    public SettlementPage confirmImport() {
        confirmButton.click();
        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }
}

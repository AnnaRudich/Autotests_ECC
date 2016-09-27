package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;

@EccPage
public class ExcelImportDialog extends Page {

    @Override
    protected String getRelativeUrl() {
        return "ImportLossLines";
    }

    @Override
    public ExcelImportDialog ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    public SettlementPage cancel() {
        switchToLast();
        String buttonId = driver.getCurrentUrl().contains("ExcelImport") ? "Cancel_button" : "_Cancel_button";
        cancelDialogWindow(buttonId);
        return at(SettlementPage.class);
    }

    private void cancelDialogWindow(String buttonId) {
        By cancelButton = By.id(buttonId);
        Wait.waitForElement(cancelButton);
        closeDialog(driver.findElement(cancelButton));
        Wait.waitForLoaded();
    }
}

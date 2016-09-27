package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class ImportLossInformationDialog extends Page {

    @FindBy(id = "upfile")
    private TextInput browseControl;

    @FindBy(id = "_OK_button")
    private Button importButton;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/dialog/upload_excel_import.jsp";
    }

    @Override
    public ImportLossInformationDialog ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        waitForVisible(importButton);
        return this;
    }

    public SettlementPage uploadExcel(String path) {
        Wait.waitForElement(By.id("upfile"));
        browseControl.clear();
        browseControl.sendKeys(path);
        Wait.waitForElement(By.id("_OK_button"));
        importButton.click();
        ExcelImportDialog excelImportDialog = at(ExcelImportDialog.class);
        excelImportDialog.cancel();
        return at(SettlementPage.class);
    }

}

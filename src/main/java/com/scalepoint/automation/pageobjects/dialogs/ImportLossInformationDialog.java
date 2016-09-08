package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class ImportLossInformationDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/dialog/upload_excel_import.jsp";

    @FindBy(id = "upfile")
    private TextInput browseControl;

    @FindBy(id = "_OK_button")
    private Button importButton;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public ImportLossInformationDialog ensureWeAreOnPage() {
        Window.get().switchToLast();
        waitForUrl(URL);
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

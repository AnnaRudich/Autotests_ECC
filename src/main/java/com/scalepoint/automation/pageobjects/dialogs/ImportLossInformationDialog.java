package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

@EccPage
public class ImportLossInformationDialog extends BaseDialog {

    @FindBy(name = "upfile")
    private TextInput browseControl;

    @FindBy(id = "excel-import-button")
    private Button importButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(importButton);
        return this;
    }

    public SettlementPage uploadExcel(String path) {
        startUploadExcel(path);
        return finishUploadExcel();
    }

    public ImportLossInformationDialog startUploadExcel(String path){
        Wait.waitForInvisible(browseControl);
        browseControl.sendKeys(path);
        Wait.waitForDisplayed(By.id("excel-import-button"));
        importButton.click();
        Wait.waitForLoaded();
        return this;
    }

    public SettlementPage finishUploadExcel(){
        new ExcelImportSummaryDialog().confirmImport();
        return Page.at(SettlementPage.class);
    }
}

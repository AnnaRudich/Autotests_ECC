package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class LossImportDialog extends BaseDialog {

    @FindBy(id = "self-service-import-button")
    private WebElement selfServiceImportButton;

    @FindBy(id = "excel-import-button")
    private WebElement excelImportButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(selfServiceImportButton);
        return this;
    }
/*
 * SelfService
 */
    public LossImportDialog selectFirstSelfServiceResponse() {
        SelenideElement selfServiceResponsesCombo = $(By.id("loss-import-combo"));
        selfServiceResponsesCombo.selectOption(1);
        Wait.waitForAjaxCompleted();
        Wait.wait(3);
        return this;
    }

    public SettlementPage confirmSelfServiceImport() {
        selfServiceImportButton.click();
        return Page.at(SettlementPage.class);
    }
    /*
     * ExcelImportCategoriesAndValuationsSelectionTest
     */

    private void startUploadExcel(String path){
        $(By.name("upfile")).uploadFile(new File(path));
        Wait.waitForVisible(excelImportButton);
        excelImportButton.click();
        Wait.waitForLoaded();
    }

    public SettlementPage uploadExcelNoErrors(String path){
        startUploadExcel(path);
        return new ExcelImportSummaryDialog().confirmImportSummary();
    }

    public LossLineImportDialog uploadExcelWithErrors(String path){
        startUploadExcel(path);
        return BaseDialog.at(LossLineImportDialog.class);
    }
}

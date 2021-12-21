package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxDivBoundList;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class LossImportDialog extends BaseDialog {

    @FindBy(id = "self-service-import-button")
    private WebElement selfServiceImportButton;

    @FindBy(id = "excel-import-button")
    private WebElement excelImportButton;

    @FindBy(id = "loss-import-combo")
    private ExtComboBoxDivBoundList lossImportCombobox;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(selfServiceImportButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }
    /*
     * SelfService
     */
    public LossImportDialog selectFirstSelfServiceResponse() {
        lossImportCombobox.select(0);
        return this;
    }

    public LossLineImportDialog confirmSelfServiceImport() {
        selfServiceImportButton.click();
        return BaseDialog.at(LossLineImportDialog.class);
    }

    public SettlementPage confirmSelfServiceImportNoErrors() {
        selfServiceImportButton.click();
        return new ExcelImportSummaryDialog().confirmImportSummary();
    }
    /*
     * ExcelImportCategoriesAndValuationsSelectionTest
     */

    private void startUploadExcel(String path){
        $(By.name("upfile")).uploadFile(new File(path));
        waitElementVisible($(excelImportButton));
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

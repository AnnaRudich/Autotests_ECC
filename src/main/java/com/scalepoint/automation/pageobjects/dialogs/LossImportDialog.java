package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxDivBoundList;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class LossImportDialog extends BaseDialogSelenide {

    @FindBy(id = "self-service-import-button")
    private SelenideElement selfServiceImportButton;

    @FindBy(id = "excel-import-button")
    private SelenideElement excelImportButton;

    private ExtComboBoxDivBoundList getLossImportCombobox(){

        return new ExtComboBoxDivBoundList($(By.id("loss-import-combo")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        selfServiceImportButton.should(Condition.visible);
    }
    /*
     * SelfService
     */
    public LossImportDialog selectFirstSelfServiceResponse() {

        getLossImportCombobox().select(0);
        return this;
    }

    public LossLineImportDialog confirmSelfServiceImport() {

        selfServiceImportButton.click();
        return BaseDialogSelenide.at(LossLineImportDialog.class);
    }

    public SettlementPage confirmSelfServiceImportNoErrors() {

        selfServiceImportButton.click();
        return BaseDialogSelenide.at(ExcelImportSummaryDialog.class).confirmImportSummary();
    }
    /*
     * ExcelImportCategoriesAndValuationsSelectionTest
     */
    private void startUploadExcel(String path){

        $(By.name("upfile")).uploadFile(new File(path));
        waitElementVisible(excelImportButton);
        excelImportButton.click();
        Wait.waitForLoaded();
    }

    public SettlementPage uploadExcelNoErrors(String path){

        startUploadExcel(path);
        return BaseDialogSelenide.at(ExcelImportSummaryDialog.class).confirmImportSummary();
    }

    public LossLineImportDialog uploadExcelWithErrors(String path){

        startUploadExcel(path);
        return BaseDialogSelenide.at(LossLineImportDialog.class);
    }
}

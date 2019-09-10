package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class LossImportDialog extends BaseDialog {

    @FindBy(name = "upfile")
    private TextInput browseControl;

    private SelenideElement selfServiceImportButton = $(By.id("self-service-import-button"));
    private SelenideElement excelImportButton = $(By.id("excel-import-button"));

    @FindBy(id = "self-service-import-button")
    private Button importSelfService;

    @FindBy(id = "loss-import-confirmImportSummary-button")
    private Button cancel;

    @Override
    protected BaseDialog ensureWeAreAt() {
        excelImportButton.shouldBe(Condition.visible);
        selfServiceImportButton.shouldBe(Condition.visible);
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
     * ExcelImport
     */

    private void startUploadExcel(String path){
        $(By.name("upfile")).uploadFile(new File(path));
        excelImportButton.shouldBe(Condition.visible);
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

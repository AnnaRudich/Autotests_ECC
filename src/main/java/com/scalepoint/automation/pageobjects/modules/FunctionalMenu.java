package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ImportLossInformationDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class FunctionalMenu extends Module {

    @FindBy(xpath = "//span[contains(@style,'findInCatalogIcon.png')]/ancestor::a")
    private Link findInCatalogue;

    @FindBy(id = "addByHandBtn")
    private Button addManually;

    @FindBy(xpath = "//span[contains(@style,'selfServiceRequestIcon.png')]/ancestor::a")
    private Link requestSelfService;

    @FindBy(id = "excelImportBtn")
    private Button importExcel;

    public void findInCatalogue() {
        findInCatalogue.click();
    }

    public SettlementDialog addManually() {
        addManually.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public void RequestSelfService() {
        requestSelfService.click();
    }

    public void ClickImportClaimSheet() {
        importExcel.click();
    }

    public ImportLossInformationDialog openImportExcelDialog() {
        logger.info("Main: {}", driver.getWindowHandle());
        driver.findElement(By.id("excelImportBtn")).click();
        return BaseDialog.at(ImportLossInformationDialog.class);
    }
}


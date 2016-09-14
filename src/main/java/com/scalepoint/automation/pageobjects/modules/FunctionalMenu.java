package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.ImportLossInformationDialog;
import com.scalepoint.automation.utils.Window;
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

    public void addManually() {
        addManually.click();
    }

    public void RequestSelfService() {
        requestSelfService.click();
    }

    public void ClickImportClaimSheet() {
        importExcel.click();
    }

    public ImportLossInformationDialog openImportExcelDialog() {
        Window.get().openDialog(driver.findElement(By.id("excelImportBtn")));
        return at(ImportLossInformationDialog.class);
    }
}


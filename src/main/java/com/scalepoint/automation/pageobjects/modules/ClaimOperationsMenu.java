package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.AddGenericItemDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SendSelfServiceRequestDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Button;

public class ClaimOperationsMenu extends Module {

    @FindBy(id = "selfServiceBtn")
    private Button selfService;

    @FindBy(id = "addGenericItemBtn")
    private Button addGenericItemBtn;

    @FindBy(id = "excelImportBtn")
    private Button excelImportBtn;

    public SendSelfServiceRequestDialog requestSelfService() {
        selfService.click();
        return BaseDialog.at(SendSelfServiceRequestDialog.class);
    }

    public AddGenericItemDialog addGenericItem() {
        Wait.forCondition(ExpectedConditions.elementToBeClickable(addGenericItemBtn));
        clickUsingJsIfSeleniumClickReturnError(addGenericItemBtn);
        return BaseDialog.at(AddGenericItemDialog.class);
    }

    public ImportDialog openImportDialog() {
        excelImportBtn.click();
        return BaseDialog.at(ImportDialog.class);
    }
}

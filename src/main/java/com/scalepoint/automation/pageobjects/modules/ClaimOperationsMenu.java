package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.AddGenericItemDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SendSelfServiceRequestDialog;
import org.openqa.selenium.support.FindBy;
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
        addGenericItemBtn.click();
        return BaseDialog.at(AddGenericItemDialog.class);
    }

    public ImportDialog openImportDialog() {
        excelImportBtn.click();
        return BaseDialog.at(ImportDialog.class);
    }
}

package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.AddGenericItemDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.LossImportDialog;
import com.scalepoint.automation.pageobjects.dialogs.SendSelfServiceRequestDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;

public class ClaimOperationsMenu extends Module {

    @FindBy(id = "selfServiceBtn")
    private WebElement selfService;

    @FindBy(id = "addGenericItemBtn")
    private Button addGenericItemBtn;

    @FindBy(id = "excelImportBtn")
    private Button excelImportBtn;

    public SendSelfServiceRequestDialog requestSelfService() {
        hoverAndClick($(selfService));
        return BaseDialog.at(SendSelfServiceRequestDialog.class);
    }

    public AddGenericItemDialog addGenericItem() {
        Wait.forCondition(ExpectedConditions.elementToBeClickable(addGenericItemBtn));
        clickUsingJavaScriptIfClickDoesNotWork(addGenericItemBtn);
        return BaseDialog.at(AddGenericItemDialog.class);
    }

    public LossImportDialog openImportDialog() {
        excelImportBtn.click();
        return BaseDialog.at(LossImportDialog.class);
    }
}

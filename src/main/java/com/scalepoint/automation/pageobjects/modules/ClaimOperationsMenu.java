package com.scalepoint.automation.pageobjects.modules;

import com.scalepoint.automation.pageobjects.dialogs.SendSelfServiceRequestDialog;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class ClaimOperationsMenu extends Module {

    @FindBy(id = "selfServiceBtn")
    private Button selfService;

    public SendSelfServiceRequestDialog requestSelfService() {
        selfService.click();
        return at(SendSelfServiceRequestDialog.class);
    }
}

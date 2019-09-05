package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class ImportDialog extends BaseDialog {

    @FindBy(id = "loss-import-combo")
    private ExtComboBox selfServiceResponsesCombo;

    @FindBy(id = "self-service-import-button")
    private Button importSelfService;

    @FindBy(id = "loss-import-confirmImport-button")
    private Button cancel;

    @Override
    protected BaseDialog ensureWeAreAt() {
        waitForVisible(selfServiceResponsesCombo);
        waitForVisible(importSelfService);
        return this;
    }

    public ImportDialog selectFirstSelfServiceResponse() {
        this.selfServiceResponsesCombo.select(1);
        Wait.waitForAjaxCompleted();
        Wait.wait(3);
        return this;
    }

    public SettlementPage importSelfServiceClick() {
        cancel.click();
        return Page.at(SettlementPage.class);
    }
}

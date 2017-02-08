package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditPolicyDialog extends BaseDialog {

    @FindBy(id = "edit-policy-ok-button")
    private WebElement ok;

    @FindBy(id = "edit-policy-cancel-button")
    private WebElement cancel;

    @FindBy(id = "edit-policy-combo")
    private ExtComboBox policiesCombo;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        Wait.waitForVisible(policiesCombo);
        return this;
    }

    public SettlementPage chooseAny() {
        policiesCombo.select(1);
        ok.click();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage choose(String policyTypeValue) {
        policiesCombo.select(policyTypeValue);
        ok.click();
        return Page.at(SettlementPage.class);
    }
}

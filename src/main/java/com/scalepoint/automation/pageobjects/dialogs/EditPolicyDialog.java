package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditPolicyDialog extends Page {

    @FindBy(id = "_OK_button")
    private Button ok;

    @FindBy(id = "_Cancel_button")
    private Button cancel;

    @FindBy(id = "policy_type")
    private Select policyType;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/dialog/edit_policy_dialog.jsp";
    }

    @Override
    public EditPolicyDialog ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        waitForVisible(policyType);
        return this;
    }

    public void chooseAny() {
        policyType.selectByIndex(1);
        closeDialog(ok);
    }

    public void choose(String policyTypeValue) {
        policyType.selectByValue(policyTypeValue);
        closeDialog(ok);
    }
}

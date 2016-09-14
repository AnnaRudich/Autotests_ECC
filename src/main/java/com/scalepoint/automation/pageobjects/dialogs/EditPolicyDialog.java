package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class EditPolicyDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/dialog/edit_policy_dialog.jsp";

    @FindBy(id = "_OK_button")
    private Button ok;

    @FindBy(id = "_Cancel_button")
    private Button cancel;

    @FindBy(id = "policy_type")
    private Select policyType;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public EditPolicyDialog ensureWeAreOnPage() {
        Window.get().switchToLast();
         waitForUrl(URL);
        waitForVisible(policyType);
        return this;
    }

    public void chooseAny(){
        policyType.selectByIndex(1);
        Window.get().closeDialog(ok);
    }
}

package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class OpenSettlementOptionDialog extends Page {

    @FindBy(id = "btn_reopen")
    private Button reopen;

    @FindBy(id = "btn_cancel")
    private Button cancel;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/dialog/open_claim_option_dialog.jsp";
    }

    @Override
    public OpenSettlementOptionDialog ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(reopen);
        waitForVisible(cancel);
        return null;
    }
}

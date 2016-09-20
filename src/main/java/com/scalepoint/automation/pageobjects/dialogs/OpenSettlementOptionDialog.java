package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class OpenSettlementOptionDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/dialog/open_claim_option_dialog.jsp";

    @FindBy(id = "btn_reopen")
    private Button reopen;

    @FindBy(id = "btn_view")
    private Button view;

    @FindBy(id = "btn_cancel")
    private Button cancel;

    @Override
    protected String getRelativeUrl() {
        return URL;
    }

    @Override
    public OpenSettlementOptionDialog ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(reopen);
        waitForVisible(cancel);
        return null;
    }

    public void reopen() {
        reopen.click();
    }
}

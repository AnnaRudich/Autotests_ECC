package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.MailViewDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import static com.scalepoint.automation.utils.Wait.waitForAjaxComplete;

@EccPage
public class MailsPage extends Page {

    @FindBy(css = ".x-grid-item tbody")
    private Table sentMails;

    @FindBy(xpath = "//table[last()]//span[contains(@class, 'x-btn-inner-grid-cell-small') and text() = 'Vis mail']")
    private Button viewLastMail;

    @FindBy(xpath = "//tr[..//div[text()[contains(.,'Kundemail')]]]//span[contains(@class, 'x-btn-inner-grid-cell-small') and text() = 'Vis mail']")
    private Button viewLastWelcomeMail;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_mails.jsp";
    }

    @Override
    public MailsPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxComplete();
        return this;
    }

    public MailViewDialog openWelcomeCustomerMail() {
        Wait.waitForVisible(viewLastWelcomeMail);

        viewLastWelcomeMail.click();
        return BaseDialog.at(MailViewDialog.class);
    }
}
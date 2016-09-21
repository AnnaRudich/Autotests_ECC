package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.MailViewDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForAjaxComplete;

@EccPage
public class MailsPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/customer_mails.jsp";

    @FindBy(css = ".x-grid-item tbody")
    private Table sentMails;

    @FindBy(xpath = "//table[last()]//span[contains(@class, 'x-btn-inner-grid-cell-small') and text() = 'Vis mail']")
    private Button viewLastMail;

    @FindBy(xpath = "//tr[..//div[text()[contains(.,'Kundemail')]]]//span[contains(@class, 'x-btn-inner-grid-cell-small') and text() = 'Vis mail']")
    private Button viewLastWelcomeMail;

    @Override
    protected String getRelativeUrl() {
        return URL;
    }

    @Override
    public MailsPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForAjaxComplete();
        return this;
    }

    public MailViewDialog openWelcomeCustomerMail() {
        Wait.waitForVisible(viewLastWelcomeMail);
        viewLastWelcomeMail.click();
        return at(MailViewDialog.class);
    }
}
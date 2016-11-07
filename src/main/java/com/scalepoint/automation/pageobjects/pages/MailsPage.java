package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.MailViewDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Table;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;

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
        waitForAjaxCompleted();
        return this;
    }

    public MailViewDialog openWelcomeCustomerMail() {
        Wait.waitForVisible(viewLastWelcomeMail);

        viewLastWelcomeMail.click();
        return BaseDialog.at(MailViewDialog.class);
    }

    public boolean isRequiredMailSent(String subj) {
        String mailSubjectXpath = "//div[contains(.,'$1')]".replace("$1", subj);
        boolean result = isElementPresent(By.xpath(mailSubjectXpath));
        if(!result){
            logger.info("Mail with subject {}  was not found", subj.toUpperCase());
            return false;
        }
        return true;
    }
}
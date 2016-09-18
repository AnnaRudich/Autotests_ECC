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

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public MailsPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForAjaxComplete();
        return this;
    }

    public MailViewDialog viewLastMail() {
        try {
            //old one
            By oldViewLastMail = By.xpath("(.//*[text()='Vis mail'])[last()]");
            Wait.waitForElementDisplaying(oldViewLastMail);
            driver.findElement(oldViewLastMail).click();
        } catch (Exception e) {
            //new one
            Wait.waitForVisible(viewLastMail);
            viewLastMail.click();
        }
        return at(MailViewDialog.class);
    }

    private int findRowNumber(String localeName) {
        List<WebElement> rows = driver.findElements(By.cssSelector(".x-grid-item tr"));
        for (int i = 0; i < rows.size(); i++) {
             if (rows.get(i).getText().contains(localeName)) {
                 return i;
             }
        }
        return -1;
    }

    public MailViewDialog clickVisMail(String localeName) {
        int numberOfRow = findRowNumber(localeName) + 1;
        driver.findElement(By.xpath("(//tr//span[contains(@class, 'x-btn-inner-grid-cell-small') and text() = 'Vis mail'])["+numberOfRow+"]")).click();
        return at(MailViewDialog.class);
    }
}
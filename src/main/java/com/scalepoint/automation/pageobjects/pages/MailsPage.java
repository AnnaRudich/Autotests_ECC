package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.SelfServiceMailViewDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.EccPage;
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

    @FindBy(css = ".fit-last-column tbody")
    private Table sentMails;

    @FindBy(xpath = "(//a[contains(@class, 'viewMailButtonCls')])[last()]")
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

    public SelfServiceMailViewDialog viewLastMail() {
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
        return at(SelfServiceMailViewDialog.class);
    }

    private int findRowNumber(String localeName) {
        List<List<WebElement>> rowsNames = sentMails.getRows();
        for (int i = 0; i < rowsNames.size(); i++) {
            String typeName = rowsNames.get(i).get(1).getText();
            if (typeName.contains(localeName)) {
                return i;
            }
        }

        return -1;
    }

    public void ClickVisMail(String localeName) {
        int numberOfRow = findRowNumber(localeName) + 2;
        driver.findElement(By.xpath("//tr[" + numberOfRow + "]//button[contains(@onclick, 'openEmailDetails')][last()]")).click();
    }
}
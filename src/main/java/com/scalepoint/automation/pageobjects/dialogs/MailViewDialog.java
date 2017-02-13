package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.LoginShopPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.selfservice.LoginSelfServicePage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class MailViewDialog extends BaseDialog {

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?selfService')]")
    private Link selfServiceLink;

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?login')]")
    private Link loginToShopLink;

    @Override
    public MailViewDialog ensureWeAreAt() {
        return this;
    }

    public LoginSelfServicePage findSelfServiceLinkAndOpenIt() {
        waitForVisible(selfServiceLink);
        String link = selfServiceLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return Page.at(LoginSelfServicePage.class);
    }

    public LoginShopPage findLoginToShopLinkAndOpenIt() {
        String link = findLoginToShopLink();
        Browser.open(link);
        return Page.at(LoginShopPage.class);
    }

    public String findLoginToShopLink() {
        waitForVisible(loginToShopLink);
        return loginToShopLink.getWrappedElement().getAttribute("href");
    }

}

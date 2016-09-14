package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.LoginSelfServicePage;
import com.scalepoint.automation.pageobjects.pages.LoginShopPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class MailViewDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/customer_mails.jsp";

    private MainMenu mainMenu = new MainMenu();

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?selfService')]")
    private Link selfServiceLink;

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?login')]")
    private Link loginToShopLink;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public MailViewDialog ensureWeAreOnPage() {
        return this;
    }

    public LoginSelfServicePage findSelfServiceLinkAndOpenIt() {
        waitForVisible(selfServiceLink);
        String link = selfServiceLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return at(LoginSelfServicePage.class);
    }

    public LoginShopPage findLoginToShopLinkAndOpenIt() {
        waitForVisible(loginToShopLink);
        String link = loginToShopLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return at(LoginShopPage.class);
    }

}

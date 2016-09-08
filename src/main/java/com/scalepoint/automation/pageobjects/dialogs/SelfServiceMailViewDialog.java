package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.modules.MainMenu;
import com.scalepoint.automation.pageobjects.pages.LoginSelfServicePage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.annotations.EccPage;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class SelfServiceMailViewDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/customer_mails.jsp";

    private MainMenu mainMenu = new MainMenu();

    //element from awaited SelfServiceMailViewDialog
    @FindBy(id = "_cancel_button")
    private Button cancel;

    @FindBy(xpath = "//a[contains(@href,'LoginToShop?selfService')]")
    private Link selfServiceLink;

    //element from main menu
    @FindBy(id = "signOutButton")
    private Link signOut;

    //element from awaited SelfService
    @FindBy(id = "password")
    private ExtInput password;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public SelfServiceMailViewDialog ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(selfServiceLink);
        return this;
    }

    public void close() {
        waitForVisible(cancel);
        cancel.click();
    }

    public LoginSelfServicePage findSelfServiceLinkAndOpenIt() {
        waitForVisible(selfServiceLink);
        String link = selfServiceLink.getWrappedElement().getAttribute("href");
        Browser.open(link);
        return at(LoginSelfServicePage.class);
    }

}

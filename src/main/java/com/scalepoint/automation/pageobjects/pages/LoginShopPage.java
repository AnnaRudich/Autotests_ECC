package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class LoginShopPage extends Page {

    private static final String URL = "shop/LoginToShop?login";

    @FindBy(id = "password")
    private ExtInput password;

    @FindBy(css = ".LoginBox_button .button")
    private Button login;

    //element from awaited Shop page
    @FindBy(css = ".save_button_table span")
    private Button save;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public LoginShopPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(password);
        waitForVisible(login);
        return this;
    }

    public LoginShopPage enterPassword(String _password) {
        Wait.waitForVisible(password);
        password.enter(_password);
        return this;
    }

    public void login() {
        login.click();
        Wait.waitForLoaded();
    }
}

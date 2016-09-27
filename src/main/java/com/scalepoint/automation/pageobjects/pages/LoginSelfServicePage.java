package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class LoginSelfServicePage extends Page {

    @FindBy(id = "password")
    private ExtInput passwordField;

    @FindBy(css = ".LoginBox_button .button")
    private Button login;

    //element from awaited Shop page
    @FindBy(css = ".save_button_table span")
    private Button save;

    @Override
    protected String getRelativeUrl() {
        return "shop/LoginToShop?selfService";
    }

    @Override
    public LoginSelfServicePage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(passwordField);
        waitForVisible(login);
        return this;
    }

    public LoginSelfServicePage enterPassword(String password) {
        this.passwordField.enter(password);
        return this;
    }

    public void login() {
        login.click();
        Wait.waitForLoaded();
    }
}

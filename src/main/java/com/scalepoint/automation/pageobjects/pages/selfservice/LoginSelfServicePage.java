package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class LoginSelfServicePage extends Page {

    @FindBy(id = "password")
    private WebElement passwordField;

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
    public void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(passwordField);
        waitForVisible(login);
    }

    public LoginSelfServicePage enterPassword(String password) {
        $(this.passwordField).setValue(password);
        return this;
    }

    public SelfServicePage login(String password) {
        $(this.passwordField).setValue(password);
        login.click();
        Wait.waitForLoaded();
        return at(SelfServicePage.class);
    }
}

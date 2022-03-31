package com.scalepoint.automation.pageobjects.pages.selfservice;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;

@EccPage
public class LoginSelfServicePage extends Page {

    @FindBy(id = "password")
    private SelenideElement passwordField;

    private Button getLogin(){

        return new Button($(By.cssSelector(".LoginBox_button .button")));
    }

    //element from awaited Shop page
    private Button getSave(){

        return new Button($(By.cssSelector(".save_button_table span")));
    }

    @Override
    protected String getRelativeUrl() {

        return "shop/LoginToShop?selfService";
    }

    @Override
    public void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        verifyElementVisible(passwordField);
        verifyElementVisible($(getLogin()));
    }

    public LoginSelfServicePage enterPassword(String password) {

        $(this.passwordField).setValue(password);
        return this;
    }

    public SelfServicePage login(String password) {

        this.passwordField.setValue(password);
        getLogin().click();
        Wait.waitForLoaded();
        return at(SelfServicePage.class);
    }
}

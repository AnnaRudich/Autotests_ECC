package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class LoginSelfService2Page extends Page {

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement login;

    @Override
    protected String getRelativeUrl() {
        return "self-service/dk/login";
    }

    @Override
    public LoginSelfService2Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(passwordField);
        waitForVisible(login);
        return this;
    }

    public SelfService2Page login(String password){
        sendKeys(this.passwordField, password);
        this.login.click();
        Wait.waitForLoaded();
        return at(SelfService2Page.class);
    }
}

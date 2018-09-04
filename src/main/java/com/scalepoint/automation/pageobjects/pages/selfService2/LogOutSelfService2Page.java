package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.selfservice.SelfServicePage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

/**
 * Created by aru on 2018-09-04.
 */
public class LogOutSelfService2Page extends Page {

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement login;

    @Override
    protected LogOutSelfService2Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(passwordField);
        waitForVisible(login);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "self-service/dk/login?token=";
    }

    public LoginSelfService2Page logOut() {
       $(By.xpath(".//div[@class='log-out']//input[@value='Log ud']")).click();
        Wait.waitForLoaded();
        return at(LoginSelfService2Page.class);
    }
}

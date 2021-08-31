package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.TimeoutException;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class ScalepointIdLoginPage extends Page {

    private static final By USERNAME_PATH = By.cssSelector("#username");
    private static final By PASSWORD_PATH = By.cssSelector("#password");
    private static final By LOGIN_BUTTON_PATH = By.cssSelector("button[translate='login.Login']");

    @Override
    protected String getRelativeUrl() {
        return "login";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        Wait.waitForVisibleAndEnabled(getPasswordField());
        Wait.waitForVisibleAndEnabled(getUsernameField());
        Wait.waitForVisibleAndEnabled(getLoginButton());
    }

    @Override
    public Boolean areWeAt() {

        try {

            return $(LOGIN_BUTTON_PATH).isDisplayed();
        } catch (Exception ex) {

            return false;
        }
    }

    public MyPage login(User user) {
        return login(user.getLogin(), user.getPassword());
    }

    public MyPage login(String userLogin, String userPassword) {
        return login(userLogin, userPassword, MyPage.class);
    }

    public <T extends Page> T login(String userLogin, String userPassword, Class<T> pageClass) {
        loginWithoutExpectedPage(userLogin, userPassword);
        return at(pageClass);
    }

    public void loginWithoutExpectedPage(String userLogin, String userPassword) {
        getUsernameField().setValue(userLogin);
        getPasswordField().setValue(userPassword);
        SelenideElement loginButton  = getLoginButton();

        loginButton
                .click();
        try {

            at(MyPage.class);
        }catch (TimeoutException e){

            loginButton.click();
        }
    }

    private SelenideElement getUsernameField(){

        return $(USERNAME_PATH);
    }

    private SelenideElement getPasswordField(){

        return $(PASSWORD_PATH);
    }

    private SelenideElement getLoginButton(){

        return $(LOGIN_BUTTON_PATH);
    }
}

package com.scalepoint.automation.pageobjects.pages;

import com.google.common.base.Function;
import com.scalepoint.automation.exceptions.LoginInvalidException;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccAdminPage
public class LoginPage extends Page {

    @FindBy(css = "#loginFormBlock .login-button")
    private Button loginButton;

    @FindBy(id = "j_username")
    private WebElement username;

    @FindBy(id = "j_password")
    private WebElement password;

    /**
     * element from the awaited page
     */
    @FindBy(id = "signOutButton")
    private Link signOut;

    @Override
    protected String getRelativeUrl() {
        return "";
    }

    @Override
    public LoginPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(username);
        waitForVisible(password);
        waitForVisible(loginButton);
        return this;
    }

    @Override
    public Boolean areWeAt() {
        try {
            return username.isDisplayed() && password.isDisplayed() && loginButton.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    public SettlementPage login(User user) {
        return login(user.getLogin(), user.getPassword());
    }

    public SettlementPage login(String userLogin, String userPassword) {
        return login(userLogin, userPassword, SettlementPage.class);
    }

    public <T extends Page> T login(String userLogin, String userPassword, Class<T> pageClass) {
        loginWithoutExpectedPage(userLogin, userPassword);
        return at(pageClass);
    }

    public void loginWithoutExpectedPage(String userLogin, String userPassword) {
        Wait.waitForDisplayed(By.id("j_username"));
        username.sendKeys(userLogin);
        password.sendKeys(userPassword);
        loginButton.click();

        boolean loginError = isLoginErrorPresent();
        if (loginError) {
            throw new LoginInvalidException(userLogin + ":" + userPassword + " are invalid!");
        }
    }

    private boolean isLoginErrorPresent() {
        try {
            Wait.forConditionShort((Function<WebDriver, Object>) webDriver -> {
                try {
                    driver.findElement(By.id("loginError"));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }, 5, 1000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

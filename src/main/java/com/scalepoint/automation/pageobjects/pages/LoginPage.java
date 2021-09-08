package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
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

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccAdminPage
public class LoginPage extends Page {

    private static final By LOGIN_VIA_SCALEPOINT_ID_LINK_PATH = By.cssSelector("#loginform > a");

    @FindBy(css = "[value=Login]")
    private Button loginButton;

    @FindBy(css =  "j_username")
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
        return "login.action";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(username).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(password).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(loginButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    public Boolean areWeAt() {
        try {
            return loginButton.isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    public ScalepointIdLoginPage loginViaScalepointId(){

        $(LOGIN_VIA_SCALEPOINT_ID_LINK_PATH).click();
        return at(ScalepointIdLoginPage.class);
    }

    public SettlementPage login(User user) {
        return login(user, SettlementPage.class);
    }

    public <T extends Page> T login(User user, Class<T> pageClass) {
        if(user.getType().equals(User.UserType.SCALEPOINT_ID)) {

            loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), pageClass);
        }else {

            loginWithoutExpectedPage(user.getLogin(), user.getPassword());
        }

        return at(pageClass);
    }

    public void loginWithoutExpectedPage(String userLogin, String userPassword) {
        verifyElementVisible($(By.id("j_username")));
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
            Wait.forCondition((Function<WebDriver, Object>) webDriver -> {
                try {
                    driver.findElement(By.id("loginError"));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }, 5);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

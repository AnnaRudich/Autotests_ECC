package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccAdminPage;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccAdminPage
public class LoginPage extends Page {

    private static final String URL = "";

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
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public LoginPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(username);
        waitForVisible(password);
        waitForVisible(loginButton);
        return this;
    }

    public SettlementPage login(User user) {
        Wait.waitForElement(By.id("j_username"));
        username.sendKeys(user.getLogin());
        password.sendKeys(user.getPassword());
        loginButton.click();
        Wait.waitForElement(By.id("signOutButton"));
        return at(SettlementPage.class);
    }
}

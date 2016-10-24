package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class LoginShopPage extends Page {

    @FindBy(id = "password")
    private ExtInput password;

    @FindBy(css = ".LoginBox_button .button")
    private Button login;

    @FindBy(css = ".LoginBox .label_text")
    private TextBlock errorMessage;

    //element from awaited Shop page
    @FindBy(css = ".save_button_table span")
    private Button save;

    @Override
    protected String getRelativeUrl() {
        return "shop/LoginToShop?login";
    }

    @Override
    public LoginShopPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(password);
        waitForVisible(login);
        return this;
    }

    public LoginShopPage enterPassword(String _password) {
        Wait.waitForVisible(password);
        password.enter(_password);
        return this;
    }

    public ShopWelcomePage login() {
        login.click();
        return at(ShopWelcomePage.class);
    }

    public LoginShopPage loginWithFail() {
        login.click();
        Wait.waitForVisible(errorMessage);
        return this;
    }
}

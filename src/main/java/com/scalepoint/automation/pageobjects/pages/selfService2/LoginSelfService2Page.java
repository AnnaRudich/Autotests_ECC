package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginSelfService2Page extends Page {

    @FindBy(name = "password")
    private SelenideElement passwordField;
    @FindBy(xpath = "//button[@type='submit']")
    private SelenideElement login;

    @Override
    protected String getRelativeUrl() {
        return "self-service/dk/login";
    }

    @Override
    public void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        login.should(Condition.visible);
    }

    public SelfService2Page login(String password) {

        passwordField.sendKeys(password);
        this.login.click();
        Wait.waitForLoaded();
        return Page.at(SelfService2Page.class);
    }

    public LoginSelfService2Page doAssert(Consumer<LoginSelfService2Page.Asserts> assertFunc) {

        assertFunc.accept(new LoginSelfService2Page.Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertLogOutIsSuccessful() {

            assertThat(isDisplayed(login))
                    .as("login form should be displayed")
                    .isTrue();
            return this;
        }
    }
}

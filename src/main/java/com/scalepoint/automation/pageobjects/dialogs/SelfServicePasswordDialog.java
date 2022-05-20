package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

public class SelfServicePasswordDialog extends BaseDialog {

    @FindBy(css = "#new-password-dialog-ok-button")
    private SelenideElement ok;
    @FindBy(css = "#new-password-dialog-password-textfield-inputEl")
    private SelenideElement passwordInput;

    @Override
    protected void ensureWeAreAt() {

        ok.should(Condition.visible);
    }

    public String getNewPasswordToSelfService() {

        return passwordInput.getAttribute("value");
    }

    public CustomerDetailsPage closeSelfServicePasswordDialog() {

        ok.click();
        $(By.cssSelector("#new-password-dialogs-ok-button-btnIconEl")).shouldNot(Condition.visible);
        return Page.at(CustomerDetailsPage.class);
    }
}

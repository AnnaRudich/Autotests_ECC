package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SelfServicePasswordDialog extends BaseDialog {
    @FindBy(css = "#new-password-dialogs-ok-button-btnIconEl")
    private WebElement ok;

    @FindBy(css = "#new-password-dialogs-password-textfield-inputEl")
    private WebElement passwordInput;

    @Override
    protected SelfServicePasswordDialog ensureWeAreAt() {
        waitForVisible(ok);
        return this;
    }

    public String getNewPasswordToSelfService() {
        return passwordInput.getAttribute("value");
    }

    public CustomerDetailsPage closeSelfServicePasswordDialog() {
        ok.click();
        Wait.waitElementDisappeared(By.cssSelector("#new-password-dialogs-ok-button-btnIconEl"));
        return Page.at(CustomerDetailsPage.class);
    }
}

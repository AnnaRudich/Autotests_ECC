package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

public class SaveConfirmationPage extends Page {


    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
    }

    @Override
    protected String getRelativeUrl() {
        return "/self-service/dk/save-confirmation";
    }

    public LoginSelfService2Page continueRegistration(){
        $(".btn").click();
        return Page.at(LoginSelfService2Page.class);
    }
}

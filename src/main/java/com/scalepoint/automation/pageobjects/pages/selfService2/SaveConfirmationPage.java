package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;

import static com.codeborne.selenide.Selenide.$;

public class SaveConfirmationPage extends Page {


    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
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

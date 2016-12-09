package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;

public abstract class AdminBasePage extends Page {

    public LoginPage logout() {
        return to(AdminPage.class).toMatchingEngine().getClaimMenu().logout();
    }

}

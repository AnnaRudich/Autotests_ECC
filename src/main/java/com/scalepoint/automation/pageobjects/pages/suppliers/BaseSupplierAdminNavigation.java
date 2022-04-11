package com.scalepoint.automation.pageobjects.pages.suppliers;


import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public abstract class BaseSupplierAdminNavigation extends Page {

    private final static By LOSS_SHEET_TEMPLATES_PATH = By.cssSelector("[data-recordid=mainMenuLossSheetTemplatesId]");
    private final static By SUPPLIERS_PATH = By.cssSelector("[data-recordid=mainMenuSuppliersId]");
    private final static By VOUCHERS_PATH = By.cssSelector("[data-recordid=mainMenuVouchersId]");
    private final static By LOGOUT_PATH = By.cssSelector("a[href$='logout'");
    private final static By DEFAULT_SETTINGS = By.cssSelector("[data-recordid=mainDefaultSettingsId]");

    public SuppliersPage toSuppliersPage() {

        $(SUPPLIERS_PATH).click();
        return at(SuppliersPage.class);
    }

    public DefaultSettingsPage toDefaultSettings() {

        $(DEFAULT_SETTINGS).click();
        return at(DefaultSettingsPage.class);
    }

    public VouchersPage toVouchersPage() {

        $(VOUCHERS_PATH).click();
        return at(VouchersPage.class);
    }

    public LossSheetTemplatesPage toLossSheetTemplates(){

        $(LOSS_SHEET_TEMPLATES_PATH).click();
        return at(LossSheetTemplatesPage.class);
    }

    public LoginPage logout() {

        $(LOGOUT_PATH).click();
        return at(LoginPage.class);
    }
}

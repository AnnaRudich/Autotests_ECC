package com.scalepoint.automation.pageobjects.pages.suppliers;


import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class BaseEccAdminNavigation extends Page {

    @FindBy(className = "mainMenuSuppliersClass")
    private WebElement suppliersLink;

    @FindBy(xpath = ".//a[contains(@href, 'logout')]")
    private WebElement logoutLink;

    public SuppliersPage toSuppliersPage() {
        suppliersLink.click();
        return at(SuppliersPage.class);
    }

    public LoginPage logout() {
        logoutLink.click();
        return at(LoginPage.class);
    }
}

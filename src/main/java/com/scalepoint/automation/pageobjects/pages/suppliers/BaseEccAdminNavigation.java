package com.scalepoint.automation.pageobjects.pages.suppliers;


import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class BaseEccAdminNavigation extends Page {

    @FindBy(className = "mainMenuSuppliersClass")
    private WebElement suppliersLink;

    public SuppliersPage toSuppliersPage() {
        suppliersLink.click();
        return at(SuppliersPage.class);
    }
}

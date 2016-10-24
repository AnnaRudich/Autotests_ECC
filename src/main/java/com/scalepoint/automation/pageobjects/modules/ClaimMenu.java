package com.scalepoint.automation.pageobjects.modules;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.driver.Browser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.pages.Page.at;

public class ClaimMenu extends Module {
    /**
     * The method selects Suppliers menu items and waits for Create supplier control is displayed
     */
    public void selectSuppliersItem() {
        $(By.xpath("//a[contains(@href, 'RedirectToSupplyManagement')]")).click();
    }

    /**
     * This method selects Admin menu item and waits for Matching Engine link in Admin part
     */
    public AdminPage selectAdminItem() {
        $(By.xpath("//a[contains(@href, 'webshop/jsp/Admin')]")).click();
        return at(AdminPage.class);
    }

    public LoginPage logout() {
        $(By.id("signOutButton")).click();
        try {
            Alert alert = Browser.driver().switchTo().alert();
            alert.accept();
            Wait.waitForModalWindowDisappear();
        } catch (Exception e) {
            SelenideElement gemaktive = $(By.id("gemaktive"));
            if (gemaktive.isDisplayed()) {
                gemaktive.click();
            }
        }
        return at(LoginPage.class);
    }

    public boolean isAdminLinkDisplayed() {
        return $(By.id("topMenuAdminButton")).isDisplayed();
    }

    public boolean isLogoutLinkDisplayed() {
        return $(By.xpath("//a[@id='signOutButton']")).isDisplayed();
    }

    public void selectCalculationItem() {
        clickAndWaitForDisplaying(By.xpath("//button[@onclick='backToSettlement()']"), By.id("finishCaseBtn-btnInnerEl"));
    }

    public void selectMyPageItem() {
        if ($(By.xpath("//button[@onclick='onEditPreferencesClick()']")).isDisplayed()) return;
        $(By.xpath("//a[@id='myPageButton']")).click();
        Wait.waitForStableElement(By.xpath("//button[@onclick='onEditPreferencesClick()']"));
    }

    public void selectTextSearchItem() {
        $(By.xpath("//span[contains(@style, 'findInCatalogIcon')]/ancestor::a")).click();
        Wait.waitForAjaxComplete();
    }

    /**
     * This method navigates to Vouchers List
     */
    public void openVouchersListPage() {
        $(By.id("produkterLabel")).click();
        clickAndWaitForDisplaying(By.id("submenugavekort"), By.id("btn_voucher_terms"));
    }

    /**
     * selects Search option in Claims menu item
     */
    public void selectSearchClaimItem() {
        clickAndWaitForDisplaying(By.id("sager"), By.id("soegsager"));
        clickAndWaitForDisplaying(By.id("soegsager"), By.id("soeg"));
    }
}

package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

interface SupplierTabs {

    default SupplierDialog.GeneralTab selectGeneralTab() {
        return selectTab(SupplierDialog.GeneralTab.class, "General");
    }

    default SupplierDialog.ShopsTab selectShopsTab() {
        return selectTab(SupplierDialog.ShopsTab.class, "Shops");
    }

    default SupplierDialog.AgreementsTab selectAgreementsTab() {
        return selectTab(SupplierDialog.AgreementsTab.class, "Agreements");
    }

    default SupplierDialog.BannerTab selectBannerTab() {
        return selectTab(SupplierDialog.BannerTab.class, "Banner");
    }

    default SupplierDialog.OrdersTab selectOrdersTab() {
        return selectTab(SupplierDialog.OrdersTab.class, "Orders");
    }

    default <T extends BaseDialog & SupplierTabs> T selectTab(Class<T> tabClass, String tabName) {
        $(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'" + tabName + "')]//..")).click();
        return BaseDialog.at(tabClass);
    }

    default SuppliersPage saveSupplier() {
        $(By.className("edit-supplier-save-btn"))
                .waitUntil(Condition.visible, 30000)
                .click();
        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        return Page.at(SuppliersPage.class);
    }

    default SuppliersPage closeSupplier() {
        $(By.className("edit-supplier-close-btn")).click();
        return Page.at(SuppliersPage.class);
    }

    default SuppliersPage cancelSupplier() {
        $(By.className("edit-supplier-cancel-btn")).click();
        return Page.at(SuppliersPage.class);
    }
}

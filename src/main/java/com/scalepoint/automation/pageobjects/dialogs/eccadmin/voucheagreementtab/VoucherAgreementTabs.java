package com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab;

import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.Actions;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.AgreementsTab;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.pageobjects.dialogs.BaseDialog.at;

public interface VoucherAgreementTabs extends Actions {

    int TIMEOUT_MS = 10000;

    default VoucherAgreementLegalTab selectLegalTab() {
        return selectTab(VoucherAgreementLegalTab.class, "Legal");
    }

    default VoucherAgreementCoverageTab selectCoverageTab() {
        return selectTab(VoucherAgreementCoverageTab.class, "Coverage");
    }

    default VoucherAgreementInfoTab selectInfoTab() {
        return selectTab(VoucherAgreementInfoTab.class, "Info");
    }

    default VoucherAgreementGeneralTab selectGeneralTab() {
        return selectTab(VoucherAgreementGeneralTab.class, "General");
    }

    default VoucherAgreementCategoriesTab selectCategoriesTab() {
        return selectTab(VoucherAgreementCategoriesTab.class, "Categories");
    }

    default VoucherAgreementAdvancedTab selectAdvancedTab() {
        return selectTab(VoucherAgreementAdvancedTab.class, "Advanced");
    }

    default VoucherAgreementDiscountDistributionTab selectDiscountDistributionTab() {
        return selectTab(VoucherAgreementDiscountDistributionTab.class, "Discount Distribution");
    }

    default <T extends BaseDialog & VoucherAgreementTabs> T selectTab(Class<T> tabClass, String tabName) {
        SelenideElement element = $(By.xpath("//div[contains(@class,'editSupplierVoucherWindow')]//span[contains(normalize-space(text()),'" + tabName + "')]"));
        ((JavascriptExecutor) Browser.driver()).executeScript("arguments[0].click();", element);
        return at(tabClass);
    }

    default AgreementsTab saveVoucherAgreement() {

        SelenideElement element = $(".supplier-save-voucher-btn");
        zoomIfClickDoesNotWork(element);
        return at(AgreementsTab.class);
    }
}

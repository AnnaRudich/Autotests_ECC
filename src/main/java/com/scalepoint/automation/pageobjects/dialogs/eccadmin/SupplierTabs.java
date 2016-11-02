package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

interface SupplierTabs {

    default SupplierDialog.ShopsTab selectShopsTab() {
        $(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Shops')]")).click();
        return BaseDialog.at(SupplierDialog.ShopsTab.class);
    }


    /*public void selectOrdersTab() {
        switchToTab(SupplierDialog.Tab.ORDERS);
    }

    public void selectSPShopsTabByIC() {
        switchToTab(SupplierDialog.Tab.SHOPS);
    }

    public void selectAgreementsTab() {
        switchToTab(SupplierDialog.Tab.AGREEMENTS);
    }

    public void selectBannerTab() {
        switchToTab(SupplierDialog.Tab.BANNER);
    }

    public void selectAdvancedTab() {
        switchToTab(SupplierDialog.Tab.ADVANCED);
    }

    public void selectLegalTab() {
        switchToTab(SupplierDialog.Tab.LEGAL);
    }

    public void selectCategoriesTab() {
        switchToTab(SupplierDialog.Tab.CATEGORIES);
    }

    public void selectDistributionTab() {
        switchToTab(SupplierDialog.Tab.DISTRIBUTION);
    }

    public void selectGeneralTab() {
        switchToTab(SupplierDialog.Tab.GENERAL);
    }

    public void selectAgreementCategoriesTab() {
        switchToTab(SupplierDialog.Tab.AGREEMENT_CATEGORIES);
    }

    public void selectAgreementLocationsTab() {
        switchToTab(SupplierDialog.Tab.AGREEMENT_LOCATIONS);
    }

    public void selectAgreementContractsTab() {
        switchToTab(SupplierDialog.Tab.AGREEMENT_CONTRACTS);
    }

    public void selectPricesTab() {
        switchToTab(SupplierDialog.Tab.PRICES);
    }

    public void selectBankInfoTab() {
        switchToTab(SupplierDialog.Tab.BANKINFO);
    }

    public void selectCoverageTab() {
        switchToTab(SupplierDialog.Tab.COVERAGE);
    }

    public SupplierTabs switchToTab(SupplierDialog.Tab tab) {
        switch (tab) {
            case ORDERS:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Orders')]"),
                        By.xpath("//input[contains(@name,'orderEmail')]"));
                break;
            case GENERAL:
                $(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'General')]")).click();
                break;
            case SHOPS:
                $(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Shops')]")).click();
                return BaseDialog.at(SupplierDialog.ShopsTab.class);
            case BANNER:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Banner')]"),
                        By.xpath("//div[contains(@id,'bannerTabPnlId-body')]"));
                break;

            case ADVANCED:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierVoucherWindow')]//span[contains(text(),'Advanced')]"),
                        By.xpath("//span[contains(text(),'New voucher agreement')]"));
                break;
            case LEGAL:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierVoucherWindow ')]//span[contains(text(),'Legal')]"),
                        By.xpath("//textarea[contains(@name, 'conditions')]"));
                break;
            case CATEGORIES:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierVoucherWindow ')]//span[contains(text(),'Categories')]"),
                        By.id("categoriesGridId"));
                break;
            case DISTRIBUTION:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierVoucherWindow')]//span[contains(text(),'Discount Distribution')]"),
                        By.xpath("//span[contains(text(),'New voucher agreement')]"));
                break;
            case AGREEMENTS:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Agreements')]"),
                        By.xpath("//span[contains(text(),'New voucher agreement')]"));
                break;
            case AGREEMENT_CATEGORIES:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'ServiceAgreementWindow')]//span[contains(text(),'Categories')]"),
                        By.cssSelector("div#agreementCategoriesGridId"));
                break;
            case AGREEMENT_LOCATIONS:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'ServiceAgreementWindow')]//span[contains(text(),'Locations')]"),
                        By.xpath("//a[contains(@class,'supplier-new-location-btn')]"));
                break;
            case AGREEMENT_CONTRACTS:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'ServiceAgreementWindow')]//span[contains(text(),'Contracts')]"),
                        By.xpath("//textarea[contains(@name,'notes')]"));
                break;
            case PRICES:
                clickAndWaitForDisplaying(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Prices')]"),
                        By.xpath("//label[contains(text(),'Default price source:')]"));
                break;
            case BANKINFO:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierWindow')]//span[contains(text(),'Bank information')]"),
                        By.xpath("//label[contains(text(),'Unknown')]"));
                break;
            case COVERAGE:
                clickAndWaitForStable(By.xpath("//div[contains(@class,'SupplierVoucherWindow ')]//span[contains(text(),'Coverage')]"),
                        By.xpath("//label[contains(text(),'Brands')]"));
                break;

        }
        return null;
    }
*/

}
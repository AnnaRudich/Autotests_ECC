package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.*;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.utils.Wait;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog.SuppliersTab.TabType.findTabTypeByName;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class SupplierDialog extends BaseDialog {

    protected By tabsPath = By.cssSelector("[id$=SupplierTabPanelId] .x-header a");
    private static final By SAVE_BUTTON_PATH = By.className("edit-supplier-save-btn");
    private static final By CLOSE_BUTTON_PATH = By.className("edit-supplier-close-btn");
    private static final By CANCEL_BUTTON_PATH = By.className("edit-supplier-cancel-btn");

    private List<SuppliersTab> tabs = getTabs();

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
    }

    public GeneralTab selectGeneralTab() {
        return selectTab(SuppliersTab.TabType.GENERAL);
    }

    public ShopsTab selectShopsTab() {
        return selectTab(SuppliersTab.TabType.SHOPS);
    }

    public AgreementsTab selectAgreementsTab() {
        return selectTab(SuppliersTab.TabType.AGREEMENTS);
    }

    public BannerTab selectBannerTab() {
        return selectTab(SuppliersTab.TabType.BANNER);
    }

    public OrdersTab selectOrdersTab() {
        return selectTab(SuppliersTab.TabType.ORDERS);
    }

    public <T extends SupplierDialog> T selectTab(SuppliersTab.TabType tabType) {

        tabs.stream()
                .filter(tab -> tab.getTabType().equals(tabType))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .click();
        return (T) BaseDialog.at(tabType.getTabClass());
    }

    private List<SuppliersTab> getTabs(){

        return $$(tabsPath)
                .filterBy(Condition.visible)
                .stream()
                .map(element -> new SuppliersTab(element, findTabTypeByName(element.getText())))
                .collect(Collectors.toList());
    }

    public SuppliersPage saveSupplier() {
        hoverAndClick($(SAVE_BUTTON_PATH));
        return Page.at(SuppliersPage.class);
    }

    public SuppliersPage closeSupplier() {
        hoverAndClick($(CLOSE_BUTTON_PATH));
        return Page.at(SuppliersPage.class);
    }

    public SuppliersPage cancelSupplier() {
        hoverAndClick($(CANCEL_BUTTON_PATH));
        return Page.at(SuppliersPage.class);
    }

    static class SuppliersTab{

        @Getter
        private TabType tabType;
        @Getter
        private SelenideElement tabElement;

        SuppliersTab(SelenideElement tab, TabType tabType) {
            this.tabElement = tab;
            this.tabType = findTabTypeByName(tab.getText());
        }

        public SupplierDialog click(){
            tabElement.click();
            return (SupplierDialog) BaseDialog.at(tabType.getTabClass());
        }

        enum TabType{

            GENERAL("General", GeneralTab.class),
            SHOPS("Shops", ShopsTab.class),
            AGREEMENTS("Agreements", AgreementsTab.class),
            BANNER("Banner", BannerTab.class),
            ORDERS("Orders", OrdersTab.class),
            UNKNOWN("Unknown", null);

            @Getter
            private String name;
            @Getter
            private Class tabClass;

            TabType(String name, Class tabClass){
                this.name = name;
                this.tabClass = tabClass;
            }
            public static TabType findTabTypeByName(String name) {

                return Arrays.stream(TabType.values())
                        .filter(tabType -> tabType.getName().equals(name))
                        .findFirst()
                        .orElse(UNKNOWN);
            }
        }
    }

    public enum OrderMailFormat {
        PLAIN("Plain text", "PLAIN_TEXT_MAIL"),
        XML_ATTACHMENT("XML attachment", "NAVISION_XML_MAIL_ATTACHMENT"),
        XML_MAIL_BODY("XML mail body", "XML_MAIL_BODY"),
        VOUCHERS_ONLY_PLAIN("Voucher orders only Plain text", "VOUCHER_ORDER_ONLY_PLAIN_TEXT_MAIL"),
        VOUCHERS_ONLY_XML_ATTACHMENT("Voucher orders only XML attachment", "VOUCHER_ORDER_ONLY_XML_ATTACHMENT"),
        VOUCHERS_ONLY_XML_MAIL_BODY("Voucher orders only XML mail body", "VOUCHER_ORDER_ONLY_XML");

        @Getter
        private String option;
        @Getter
        private String value;

        OrderMailFormat(String optionText, String value) {
            this.option = optionText;
            this.value = value;
        }
    }

    public static class GeneralTabReadMode extends BaseDialogSelenide {

        @FindBy(xpath = "//label[contains(text(),'Supplier name:')]")
        private SelenideElement name;

        @Override
        protected boolean areWeAt() {

            Wait.waitForAjaxCompleted();
            try {

                return this.name.isDisplayed() && driver.findElements(By.name("name")).isEmpty();
            } catch (Exception e) {

                logger.error(e.getMessage());
                return false;
            }
        }

        public GeneralTabReadMode setName(String name) {

            this.name.clear();
            this.name.sendKeys(name);
            return this;
        }

        @Override
        protected void ensureWeAreAt() {
            waitForAjaxCompletedAndJsRecalculation();
        }
    }
}

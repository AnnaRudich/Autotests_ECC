package com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.AddShopDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.AddShopDialogViewMode;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.entity.input.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ShopsTab extends SupplierDialog {

    @FindBy(xpath = "//a[contains(@class,'supplier-add-shop-btn')]")
    private WebElement addShopButton;

    @FindBy(xpath = "//a[contains(@class,'supplier-delete-shop-btn')]")
    private WebElement deleteShopButton;

    @FindBy(xpath = "//a[contains(@class,'supplier-import-shop-btn')]")
    private WebElement importShopButton;

    @FindBy(xpath = "//div[contains(@class,'supplier-delete-shop-confirm-window')]//span[contains(text(),'Yes')]/ancestor::a")
    private WebElement deleteShopYesButton;

    @FindBy(id = "supplierShopsGridId")
    private WebElement shopsGridId;

    private String byShopNameXpath = " //div[@id='supplierShopsGridId']//div[contains(text(),'%s')]";

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(shopsGridId).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public AddShopDialog openAddShopDialog() {
        addShopButton.click();
        return BaseDialog.at(AddShopDialog.class);
    }

    boolean isNewShopExists(Shop shop) {
        try {
            SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shop.getShopName())));
            element.scrollTo();
            return true;
        } catch (Error e) {
            return false;
        }
    }

    ShopsTab selectShop(Shop shop) {
        SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shop.getShopName())));
        element.scrollTo();
        hoverAndClick(element);
        $(By.xpath("//div[contains(@class,'SupplierWindow ')]//span[contains(text(),'Delete shop')]")).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    public AddShopDialogViewMode openShopViewModel(String shopName) {
        SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shopName)));
        element
                .scrollTo()
                .doubleClick();
        return at(AddShopDialogViewMode.class);
    }

    public AddShopDialog openEditShopDialog(String shopName) {
        SelenideElement element = $(By.xpath(String.format(byShopNameXpath, shopName)));
        element
                .scrollTo()
                .doubleClick();
        Wait.waitForVisibleAndEnabled(By.name("shopName"));
        return at(AddShopDialog.class);
    }

    public ShopsTab deleteShop(Shop shop) {
        selectShop(shop);
        deleteShopButton.click();
        hoverAndClick($(deleteShopYesButton));
        waitForAjaxCompleted();
        return this;
    }

    public ShopsTab doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ShopsTab.this;
    }

    public class Asserts {
        public Asserts assertNewShopExists(Shop shop) {
            assertTrue(isNewShopExists(shop));
            return this;
        }

        public Asserts assertNewShopNotExists(Shop shop) {
            assertFalse(isNewShopExists(shop));
            return this;
        }
    }
}

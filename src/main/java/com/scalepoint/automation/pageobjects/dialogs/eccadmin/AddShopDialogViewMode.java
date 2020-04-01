package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class AddShopDialogViewMode extends BaseDialog {

    private static final String SUPPLIER_CANCEL_VIEW_SHOP_BTN = "//a[contains(@class,'supplier-cancel-view-shop-btn')]";

    @FindBy(xpath = "//label[contains(text(),'Name:')]")
    private WebElement nameLabel;

    @Override
    protected AddShopDialogViewMode ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(nameLabel).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    public SupplierDialog.ShopsTab cancelViewShopDialog() {
        find(By.xpath(SUPPLIER_CANCEL_VIEW_SHOP_BTN)).click();
        return at(SupplierDialog.ShopsTab.class);
    }

    public AddShopDialogViewMode doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return AddShopDialogViewMode.this;
    }

    public class Asserts {
        public Asserts assertIsShopDialogNotEditable() {
            Assert.assertTrue(Wait.invisibleOfElement(By.id("editSupplierShopTabFormId")));
            return this;
        }
    }
}

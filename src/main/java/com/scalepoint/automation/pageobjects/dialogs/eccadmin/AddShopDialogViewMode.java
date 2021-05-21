package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.ShopsTab;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;

public class AddShopDialogViewMode extends BaseDialog {

    private static final String SUPPLIER_CANCEL_VIEW_SHOP_BTN = "//a[contains(@class,'supplier-cancel-view-shop-btn')]";

    @FindBy(xpath = "//label[contains(text(),'Name:')]")
    private WebElement nameLabel;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(nameLabel).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public ShopsTab cancelViewShopDialog() {
        hoverAndClick($(By.xpath(SUPPLIER_CANCEL_VIEW_SHOP_BTN)));
        return at(ShopsTab.class);
    }

    public AddShopDialogViewMode doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return AddShopDialogViewMode.this;
    }

    public class Asserts {
        public Asserts assertIsShopDialogNotEditable() {
            Assert.assertFalse(verifyElementVisible($(By.id("editSupplierShopTabFormId"))));
            return this;
        }
    }
}

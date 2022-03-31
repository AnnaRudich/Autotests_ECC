package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.ShopsTab;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class AddShopDialogViewMode extends BaseDialog {

    private static final String SUPPLIER_CANCEL_VIEW_SHOP_BTN = "//a[contains(@class,'supplier-cancel-view-shop-btn')]";

    @FindBy(xpath = "//label[contains(text(),'Name:')]")
    private SelenideElement nameLabel;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        nameLabel.should(Condition.visible);
    }

    public ShopsTab cancelViewShopDialog() {

        hoverAndClick($(By.xpath(SUPPLIER_CANCEL_VIEW_SHOP_BTN)));
        return BaseDialog.at(ShopsTab.class);
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

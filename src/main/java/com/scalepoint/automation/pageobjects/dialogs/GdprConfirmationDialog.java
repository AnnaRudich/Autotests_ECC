package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GdprConfirmationDialog extends BaseDialog {
    @FindBy(css = "#messagebox-1001")
    private WebElement gdprDialog;

    @Override
    protected GdprConfirmationDialog ensureWeAreAt() {
        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        gdprDialog.isDisplayed();
        return this;
    }

    public SupplierDialog.GeneralTab closeDialog(){
        $$(".x-tool-close").get(1).click();
        return at(SupplierDialog.GeneralTab.class);
    }

    public SupplierDialog.GeneralTab confirmUpdate(){
        $(By.xpath("//span[text() = 'Confirm']/following-sibling::span")).click();
        return at(SupplierDialog.GeneralTab.class);
    }
}

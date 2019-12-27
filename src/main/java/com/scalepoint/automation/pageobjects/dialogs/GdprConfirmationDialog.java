package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

public class GdprConfirmationDialog extends BaseDialog {
    @FindBy(css = "#messagebox-1001")
    private WebElement gdprDialog;

    @Override
    protected GdprConfirmationDialog ensureWeAreAt() {
        gdprDialog.isDisplayed();
        return this;
    }

    public SupplierDialog.ShopsTab closeDialog(){
        $(".x-tool-close").click();
        return BaseDialog.at(SupplierDialog.ShopsTab.class);
    }

    public SupplierDialog.ShopsTab confirmUpdate(){
        $(By.xpath("//span[text() = 'Confirm']/following-sibling::span")).click();
        return BaseDialog.at(SupplierDialog.ShopsTab.class);
    }
}

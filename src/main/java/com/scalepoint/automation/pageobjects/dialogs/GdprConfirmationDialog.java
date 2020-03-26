package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GdprConfirmationDialog extends BaseDialog {
    @FindBy(css = ".x-message-box")
    private WebElement gdprDialog;

    @Override
    protected GdprConfirmationDialog ensureWeAreAt() {
        Wait.waitForJavascriptRecalculation();
        Wait.waitForAjaxCompleted();
        $(gdprDialog).waitUntil(Condition.visible, 5000);
        return this;
    }

    public void confirm(){
        $$(".x-message-box a[role=button]").get(0).click();
    }
}

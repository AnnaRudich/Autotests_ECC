package com.scalepoint.automation.pageobjects.pages.rnv;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class InvoiceDialog extends BaseDialog {

    @Override
    public InvoiceDialog ensureWeAreAt() {
        Wait.waitForAjaxCompleted();
        $(By.xpath("//div[contains(text(), 'Faktura')]")).shouldBe(Condition.visible);
        return this;
    }
}

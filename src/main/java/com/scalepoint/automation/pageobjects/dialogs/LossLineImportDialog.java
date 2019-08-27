package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class LossLineImportDialog extends BaseDialog{

SelenideElement dialog = $(By.xpath("//div[@data-componentid='loss-line-import-window']"));

    @Override
    protected BaseDialog ensureWeAreAt() {
        dialog.shouldBe(Condition.visible);
        return null;
    }
}

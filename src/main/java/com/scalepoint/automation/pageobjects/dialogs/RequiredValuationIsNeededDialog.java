package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;

/**
 * @author : igu
 */
public class RequiredValuationIsNeededDialog extends BaseDialog {

    @FindBy(xpath = "//div[contains(@class,'required-valuation-is-needed-dialogs')]")
    private WebElement dialog;

    @Override
    protected BaseDialog ensureWeAreAt() {
        $(dialog).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

}

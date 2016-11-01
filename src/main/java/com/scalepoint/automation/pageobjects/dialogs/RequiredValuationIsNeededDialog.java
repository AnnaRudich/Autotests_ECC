package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author : igu
 */
public class RequiredValuationIsNeededDialog extends BaseDialog {

    @FindBy(xpath = "//div[contains(@class,'required-valuation-is-needed-dialog')]")
    private WebElement dialog;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(dialog);

        return this;
    }

}

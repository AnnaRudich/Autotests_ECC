package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;

public class VoucherTermsAndConditionsDialog extends BaseDialog {

    @FindBy(id = "terms-conditions-condidtions")
    private WebElement termsAndConditions;

    @FindBy(id = "terms-conditions-close-button")
    private Button close;

    @Override
    protected VoucherTermsAndConditionsDialog ensureWeAreAt() {
        Wait.waitForVisible(termsAndConditions);
        return this;
    }

    public String getTermsAndConditions(){
        return termsAndConditions.getText();
    }

    public void close(){
        close.click();
    }

    public VoucherTermsAndConditionsDialog assertTermsAndConditionsTextIs(String expectedText) {
        Assert.assertEquals(getTermsAndConditions(), expectedText, "The Terms And Conditions are different");
        return this;
    }
}

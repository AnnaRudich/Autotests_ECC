package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class ReplacementDialog extends BaseDialog {

    @FindBy(id = "btn_cancel")
    private Button cancelButton;

    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[3]")
    private WebElement voucherFaceValue;

    @FindBy(xpath = "//table[@class='valuationTable']//tr[2]/td[4]")
    private WebElement itemPrice;

    @Override
    public ReplacementDialog ensureWeAreAt() {
        Wait.waitForAjaxComplete();
        Window.get().switchToLast();
        waitForVisible(cancelButton);
        return this;
    }

    public void closeReplacementDialog() {
        cancelButton.click();
    }

    public Double getVoucherFaceValue() {
        if (Configuration.isDK()) {
            return OperationalUtils.toNumber(voucherFaceValue.getText().split("rdi")[1].replaceAll("[^\\.,0123456789]", ""));
        } else {
            return OperationalUtils.toNumber(voucherFaceValue.getText().split("£")[1].replaceAll("[^\\.,0123456789]", ""));
        }
    }

    public Double getItemPriceValue() {
        return OperationalUtils.toNumber(itemPrice.getText().replaceAll("[^\\.,0123456789]", ""));
    }
}

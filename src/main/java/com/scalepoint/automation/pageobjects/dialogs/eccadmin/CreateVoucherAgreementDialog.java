package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

public class CreateVoucherAgreementDialog extends BaseDialog {

    @FindBy(name = "voucherName")
    private WebElement voucherName;
    @FindBy(name = "agreementDiscount")
    private WebElement agreementDiscount;
    @FindBy(xpath = ".//div[contains(@class, 'addSupplierVoucherWindow')]//*[contains(@class,'x-window-header-text')]")
    private WebElement windowHeader;
    @FindBy(xpath = ".//*[contains(@class,'supplier-create-voucher-btn')]")
    private WebElement createVoucherButton;

    @Override
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(windowHeader);
        Wait.forCondition(webDriver -> "Add voucher agreement".equals(windowHeader.getText()));
        return this;
    }

    public CreateVoucherAgreementDialog fill(Consumer<CreateVoucherAgreementDialog> fillFunc) {
        fillFunc.accept(this);
        return this;
    }

    public VoucherAgreementDialog.GeneralTab createVoucherAgreement() {
        clickElementUsingJS(createVoucherButton);
        return at(VoucherAgreementDialog.GeneralTab.class);
    }

    public static class FormFiller {

        private CreateVoucherAgreementDialog dialog;

        public FormFiller(CreateVoucherAgreementDialog dialog) {
            this.dialog = dialog;
        }

        public FormFiller withVoucherName(String voucherName) {
            dialog.voucherName.sendKeys(voucherName);
            return this;
        }

        public FormFiller withAgreementDiscount(Integer discount) {
            dialog.agreementDiscount.sendKeys(discount.toString());
            return this;
        }
    }

    
}

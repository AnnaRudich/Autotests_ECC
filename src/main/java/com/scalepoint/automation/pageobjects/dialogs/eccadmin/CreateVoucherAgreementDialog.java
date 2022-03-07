package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementGeneralTab;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class CreateVoucherAgreementDialog extends BaseDialogSelenide {

    @FindBy(name = "voucherName")
    private SelenideElement voucherName;
    @FindBy(name = "agreementDiscount")
    private SelenideElement agreementDiscount;
    @FindBy(xpath = ".//div[contains(@class, 'addSupplierVoucherWindow')]//*[contains(@class,'x-window-header-text')]/span")
    private SelenideElement windowHeader;
    @FindBy(xpath = ".//*[contains(@class,'supplier-create-voucher-btn')]")
    private SelenideElement createVoucherButton;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        createVoucherButton.should(Condition.visible);
    }

    public CreateVoucherAgreementDialog fill(Consumer<CreateVoucherAgreementDialog> fillFunc) {

        fillFunc.accept(this);
        return this;
    }

    public VoucherAgreementGeneralTab createVoucherAgreement() {

        hoverAndClick(createVoucherButton);
        return BaseDialog.at(VoucherAgreementGeneralTab.class);
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

package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class CreateSupplierDialog extends BaseDialog {

    @FindBy(name = "name")
    private SelenideElement supplierName;
    @FindBy(name = "cvr")
    private SelenideElement cvr;
    @FindBy(name = "address")
    private SelenideElement address1;
    @FindBy(name = "address2")
    private SelenideElement address2;
    @FindBy(name = "city")
    private SelenideElement city;
    @FindBy(name = "postalCode")
    private SelenideElement postalCode;
    @FindBy(name = "phone")
    private SelenideElement phone;
    @FindBy(name = "orderEmail")
    private SelenideElement orderEmail;
    @FindBy(xpath = ".//*[contains(@class,'x-window-header-text')]")
    private SelenideElement windowHeader;
    @FindBy(xpath = ".//*[contains(@class,'add-supplier-create-btn')]")
    private SelenideElement createSupplierButton;

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        windowHeader.should(Condition.exactText("Add supplier"));
    }

    public CreateSupplierDialog fill(Consumer<CreateSupplierDialog> fillFunc) {

        fillFunc.accept(this);
        return this;
    }

    public <T extends BaseDialog> T createSupplier(Class<T> dialogClass) {

        $(createSupplierButton).click();
        BaseDialog
                .at(GdprConfirmationDialog.class)
                .confirm();
        return BaseDialog.at(dialogClass);
    }

    public static class FormFiller {

        private CreateSupplierDialog dialog;

        public FormFiller(CreateSupplierDialog dialog) {
            this.dialog = dialog;
        }

        public FormFiller withSupplierName(String supplierName) {

            dialog.supplierName.sendKeys(supplierName);
            return this;
        }

        public FormFiller withCvr(String cvr) {

            dialog.cvr.sendKeys(cvr);
            return this;
        }

        public FormFiller withAddress1(String address1) {

            dialog.address1.sendKeys(address1);
            return this;
        }

        public FormFiller withAddress2(String address2) {

            dialog.address2.sendKeys(address2);
            return this;
        }

        public FormFiller withCity(String city) {

            dialog.city.sendKeys(city);
            return this;
        }

        public FormFiller withPostalCode(String postalCode) {

            dialog.postalCode.sendKeys(postalCode);
            return this;
        }

        public FormFiller withPhone(String phone) {

            dialog.phone.sendKeys(phone);
            return this;
        }

        public FormFiller withOrderEmail(String orderEmail) {

            dialog.orderEmail.sendKeys(orderEmail);
            return this;
        }
    }
}

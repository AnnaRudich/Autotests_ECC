package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class CreateSupplierDialog extends BaseDialog {

    @FindBy(name = "name")
    private WebElement supplierName;
    @FindBy(name = "cvr")
    private WebElement cvr;
    @FindBy(name = "address")
    private WebElement address1;
    @FindBy(name = "address2")
    private WebElement address2;
    @FindBy(name = "city")
    private WebElement city;
    @FindBy(name = "postalCode")
    private WebElement postalCode;
    @FindBy(name = "phone")
    private WebElement phone;
    @FindBy(name = "orderEmail")
    private WebElement orderEmail;
    @FindBy(xpath = ".//*[contains(@class,'x-window-header-text')]")
    private WebElement windowHeader;
    @FindBy(xpath = ".//*[contains(@class,'add-supplier-create-btn')]")
    private WebElement createSupplierButton;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(windowHeader).waitUntil(Condition.exactText("Add supplier"), TIME_OUT_IN_MILISECONDS);
    }

    public CreateSupplierDialog fill(Consumer<CreateSupplierDialog> fillFunc) {
        fillFunc.accept(this);
        return this;
    }

    public <T extends BaseDialog> T createSupplier(Class<T> dialogClass) {
        $(createSupplierButton).click();
        at(GdprConfirmationDialog.class)
                .confirm();
        return at(dialogClass);
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

package com.scalepoint.automation.pageobjects.dialogs.eccadmin;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.function.Consumer;

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
    protected BaseDialog ensureWeAreAt() {
        Wait.waitForVisible(windowHeader);
        Wait.forCondition(webDriver -> "Add supplier".equals(windowHeader.getText()));
        return this;
    }

    public CreateSupplierDialog fill(Consumer<CreateSupplierDialog> fillFunc) {
        fillFunc.accept(this);
        return this;
    }

    public SupplierDialog.GeneralTab createSupplier(boolean gdprCheckIsEnabled) {
        createSupplierButton.click();
        if(gdprCheckIsEnabled){
            BaseDialog.at(GdprConfirmationDialog.class).confirmUpdate();
        }
        return at(SupplierDialog.GeneralTab.class);
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

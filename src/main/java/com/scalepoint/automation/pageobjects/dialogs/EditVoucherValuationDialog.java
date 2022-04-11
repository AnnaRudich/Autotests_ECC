package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtText;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.TextInput;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertEquals;

public class EditVoucherValuationDialog extends BaseDialog {

    @FindBy(id = "edit-voucher-tags-text-inputEl")
    private SelenideElement tags;
    @FindBy(id = "edit-voucher-ok-button")
    private SelenideElement ok;
    @FindBy(id = "edit-voucher-rebate-display-inputEl")
    private SelenideElement voucherRebate;
    @FindBy(id = "edit-voucher-distribution-button")
    private SelenideElement editDistributionButton;

    private TextInput getBrandsText(){

        return new TextInput($(By.name("edit-voucher-brands-text-inputEl")));
    }

    private Button getTermsAndConditions(){

        return new Button($(By.id("edit-voucher-terms-button")));
    }

    private ExtText getDiscountDistribution(){

        return new ExtText($(By.id("edit-voucher-to-customer-input")));
    }

    private ExtText getVoucherFace(){

        return new ExtText($(By.id("edit-voucher-face-value-input-inputEl")));
    }

    private ExtText getVoucherCash(){

        return new ExtText($(By.id("edit-voucher-cash-value-input-inputEl")));
    }

    private ExtText getCompanyPercentageInput(){

        return new ExtText($(By.id("edit-voucher-to-company-input-inputEl")));
    }

    private ExtText getCustomerPercentageInput(){

        return new ExtText($(By.id("edit-voucher-to-customer-input-inputEl")));
    }

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        ok.should(Condition.visible);
        getVoucherFace().should(Condition.visible);
    }

    public EditVoucherValuationDialog updatePercentage(DistributeTo distributeTo, Integer percentage) {

        if (DistributeTo.COMPANY.equals(distributeTo)) {

            getCompanyPercentageInput().sendKeys(Keys.HOME, Keys.chord(Keys.SHIFT, Keys.END), percentage.toString());
        } else {

            getCompanyPercentageInput().enter(percentage.toString());
        }
        return this;
    }

    public EditDiscountDistributionDialog updatePercentageFromDialog(DistributeTo distributeTo, Integer percentage) {

        editDistributionButton.click();
        return BaseDialog.at(EditDiscountDistributionDialog.class).updatePercentage(distributeTo, percentage);
    }

    public String getBrands() {
        return $(By.name("edit-voucher-brands-text-inputEl")).getValue();
    }

    public String getTags() {

        return tags.getValue();
    }

    public SettlementDialog closeDialogWithOk() {

        ok.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public VoucherTermsAndConditionsDialog openTermsAndConditions() {

        getTermsAndConditions().click();
        return BaseDialog.at(VoucherTermsAndConditionsDialog.class);
    }

    public EditVoucherValuationDialog discountDistribution(String discountDistributionValue) {

        getDiscountDistribution().enter(discountDistributionValue);
        return this;
    }

    public SettlementDialog saveVoucherValuation() {

        ok.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public String getVoucherCashValue() {
        return getVoucherCash().getText();
    }


    public Integer getVoucherPercentage() {

        Wait.waitForAjaxCompleted();
        String text = voucherRebate.getText();
        return Integer.valueOf(text.replaceAll("([0-9]+),.*", "$1"));
    }

    public enum DistributeTo {

        CUSTOMER,
        COMPANY
    }

    public EditVoucherValuationDialog doAssert(Consumer<Asserts> assertFunc) {

        assertFunc.accept(new Asserts());
        return EditVoucherValuationDialog.this;
    }

    public class Asserts {

        public Asserts assertBrandsTextIs(String expectedBrandsText) {

            assertEquals(getBrands(), expectedBrandsText, "Wrong Brand is displayed.");
            return this;
        }

        public Asserts assertTagsTextIs(String expectedTagsText) {

            assertEquals(getTags(), expectedTagsText, "Wrong Tags are displayed");
            return this;
        }
    }

}


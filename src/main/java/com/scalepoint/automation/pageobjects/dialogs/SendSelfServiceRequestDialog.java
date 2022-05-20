package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeDiv;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxDivBoundList;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.Customer;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class SendSelfServiceRequestDialog extends BaseDialog {

    private static final String OK_BUTTON_PATH = "//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]";

    @Override
    protected void ensureWeAreAt() {

        waitForAjaxCompletedAndJsRecalculation();
        email.should(Condition.visible);
        getOkButton().should(Condition.visible);
    }

    @FindBy(name = "email")
    private SelenideElement email;

    @FindBy(name = "mobileNumber")
    private SelenideElement mobileNumber;

    @FindBy(name = "password")
    private SelenideElement password;

    @FindBy(name = "password")
    private SelenideElement newPasswordCheckbox;

    private ExtCheckboxTypeDiv getSendSms(){

        return new ExtCheckboxTypeDiv($(By.id("sendPasswordSmsCheckbox-bodyEl")));
    }

    private ExtCheckboxTypeDiv getCloseAutomatically(){

        return new ExtCheckboxTypeDiv($(By.id("closeAutomatically-bodyEl")));
    }

    private SelenideElement getOkButton(){

        return $(By.xpath(OK_BUTTON_PATH));
    }

    public SendSelfServiceRequestDialog fill(Claim claim, String password) {

        return enterEmail(claim.getEmail())
                .enterMobileNumber(claim.getCellNumber())
                .enterPassword(password)
                .disableSendSms();
    }

    public SendSelfServiceRequestDialog fill(ClaimRequest claimRequest, String password) {

        Customer customer = claimRequest.getCustomer();

        return enterEmail(customer.getEmail())
                .enterMobileNumber(customer.getMobile())
                .enterPassword(password)
                .disableSendSms();
    }

    public SendSelfServiceRequestDialog fill(String password) {

        return enterPassword(password)
                .disableSendSms();
    }

    public SendSelfServiceRequestDialog enterEmail(String email) {

        this.email.setValue(email);
        return this;
    }

    private SendSelfServiceRequestDialog enterPassword(String password) {

        this.password.setValue(password);
        return this;
    }

    private SendSelfServiceRequestDialog enterMobileNumber(String mobileNumber) {

        this.mobileNumber.setValue(mobileNumber);
        return this;
    }

    public SendSelfServiceRequestDialog setSendLossAdjusterSheet(String name){

        $("#excel-send-type-radio").click();
        new ExtComboBoxDivBoundList($("#self-service-template-combo")).select(name);
        return this;
    }

    private SendSelfServiceRequestDialog disableSendSms() {

        ExtCheckboxTypeDiv sendSms = getSendSms();

        if (sendSms.isChecked()) {

            sendSms.set(false);
        }

        return this;
    }

    public SendSelfServiceRequestDialog enableAutoClose() {

        getCloseAutomatically().set(true);
        return this;
    }

    public SendSelfServiceRequestDialog enableNewPassword(){

        newPasswordCheckbox.click();
        return this;
    }

    public SettlementPage send() {

        getOkButton().click();
        BaseDialog.at(GdprConfirmationDialog.class)
                .confirm();
        return Page.at(SettlementPage.class);
    }

    public SettlementPage sendWithoutGdpr() {

        SelenideElement element = getOkButton();
        element.click();
        element.shouldNot(Condition.visible);
        return Page.at(SettlementPage.class);
    }
}

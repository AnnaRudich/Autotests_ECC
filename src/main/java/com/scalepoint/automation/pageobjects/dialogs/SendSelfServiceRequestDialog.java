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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitElementInvisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class SendSelfServiceRequestDialog extends BaseDialog {

    private static final String OK_BUTTON_PATH = "//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]";

    @FindBy(name = "email")
    private WebElement email;

    @FindBy(name = "mobileNumber")
    private WebElement mobileNumber;

    @FindBy(name = "password")
    private WebElement password;

    @FindBy(id = "sendPasswordSmsCheckbox-bodyEl")
    private ExtCheckboxTypeDiv sendSms;

    @FindBy(id = "closeAutomatically-bodyEl")
    private ExtCheckboxTypeDiv closeAutomatically;

    @FindBy(xpath = OK_BUTTON_PATH)
    private Button ok;

    @FindBy(name = "password")
    private WebElement newPasswordCheckbox;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(email).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(ok).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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
        $(this.email).setValue(email);
        return this;
    }

    private SendSelfServiceRequestDialog enterPassword(String password) {
        $(this.password).setValue(password);
        return this;
    }

    private SendSelfServiceRequestDialog enterMobileNumber(String mobileNumber) {
        $(this.mobileNumber).setValue(mobileNumber);
        return this;
    }

    public SendSelfServiceRequestDialog setSendLossAdjusterSheet(String name){

        $("#excel-send-type-radio").click();
        new ExtComboBoxDivBoundList($("#self-service-template-combo")).select(name);
        return this;
    }

    private SendSelfServiceRequestDialog disableSendSms() {
        if (sendSms.isChecked()) {
            sendSms.set(false);
        }
        return this;
    }

    public SendSelfServiceRequestDialog enableAutoClose() {
        closeAutomatically.set(true);
        return this;
    }

    public SendSelfServiceRequestDialog enableNewPassword(){
        newPasswordCheckbox.click();
        return this;
    }

    public SettlementPage send() {
        SelenideElement element = $(By.xpath(OK_BUTTON_PATH));
        element.click();
        at(GdprConfirmationDialog.class)
                .confirm();
        waitElementInvisible(element);
        return Page.at(SettlementPage.class);
    }

    public SettlementPage sendWithoutGdpr() {
        SelenideElement element = $(ok);
        element.click();
        waitElementInvisible(element);
        return Page.at(SettlementPage.class);
    }
}

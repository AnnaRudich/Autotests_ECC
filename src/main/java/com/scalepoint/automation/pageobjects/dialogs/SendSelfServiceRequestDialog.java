package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForInvisible;

public class SendSelfServiceRequestDialog extends BaseDialog {

    @FindBy(name = "email")
    private ExtInput email;

    @FindBy(name = "mobileNumber")
    private ExtInput mobileNumber;

    @FindBy(name = "password")
    private ExtInput password;

    @FindBy(id = "sendPasswordSmsCheckbox-bodyEl")
    private ExtCheckbox sendSms;

    @FindBy(id = "closeAutomatically-bodyEl")
    private ExtCheckbox closeAutomatically;

    @FindBy(xpath = "//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]")
    private Button ok;

    @FindBy(id = "newPasswordCheckbox-displayEl")
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

    public SendSelfServiceRequestDialog fill(String password) {
        return enterPassword(password)
                .disableSendSms();
    }

    private SendSelfServiceRequestDialog enterEmail(String email) {
        this.email.enter(email);
        return this;
    }

    private SendSelfServiceRequestDialog enterPassword(String password) {
        this.password.enter(password);
        return this;
    }

    private SendSelfServiceRequestDialog enterMobileNumber(String mobileNumber) {
        this.mobileNumber.enter(mobileNumber);
        return this;
    }

    private SendSelfServiceRequestDialog disableSendSms() {
        if (sendSms.isSelected()) {
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
        ok.click();
        at(GdprConfirmationDialog.class)
                .confirm();
        waitForInvisible(ok);
        return Page.at(SettlementPage.class);
    }
}

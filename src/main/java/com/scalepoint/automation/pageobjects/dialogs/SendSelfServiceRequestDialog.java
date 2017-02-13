package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SendSelfServiceRequestDialog extends BaseDialog {

    @FindBy(name = "email")
    private ExtInput email;

    @FindBy(name = "mobileNumber")
    private ExtInput mobileNumber;

    @FindBy(name = "password")
    private ExtInput password;

    @FindBy(id = "sendPasswordSmsCheckbox-bodyEl")
    private ExtCheckbox sendSms;

    @FindBy(xpath = "//span[contains(@class,'x-btn-inner-default-small')][contains(text(),'Ok')]")
    private Button ok;

    @Override
    public SendSelfServiceRequestDialog ensureWeAreAt() {
        waitForVisible(email);
        waitForVisible(ok);
        return this;
    }

    public SendSelfServiceRequestDialog fill(Claim claim, String password) {
        return enterEmail(claim.getEmail())
                .enterMobileNumber(claim.getCellNumber())
                .enterPassword(password)
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

    public SettlementPage send() {
        ok.click();
        return Page.at(SettlementPage.class);
    }
}

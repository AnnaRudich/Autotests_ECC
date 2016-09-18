package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class SendSelfServiceRequestDialog extends Page {

    private static final String URL = "webshop/jsp/matching_engine/settlement.jsp";

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

    @FindBy(xpath = "//span[contains(@class,'x-btn-inner-center')][contains(text(),'Annuller')]")
    private Button cancel;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public SendSelfServiceRequestDialog ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(email);
        waitForVisible(ok);
        return this;
    }

    public SendSelfServiceRequestDialog fill(Claim claim, String password) {
        return EnterEmail(claim.getEmail())
                .EnterMobileNumber(claim.getCellNumber())
                .EnterPassword(password)
                .UncheckSendSms();
    }

    public SendSelfServiceRequestDialog EnterEmail(String _email) {
        email.enter(_email);
        return this;
    }

    public SendSelfServiceRequestDialog EnterPassword(String _password) {
        password.enter(_password);
        return this;
    }

    public SendSelfServiceRequestDialog EnterMobileNumber(String _mobileNumber) {
        mobileNumber.enter(_mobileNumber);
        return this;
    }

    public SendSelfServiceRequestDialog UncheckSendSms() {
        if (sendSms.isSelected()) {
            sendSms.set(false);
        }
        return this;
    }

    public SettlementPage send() {
        ok.click();
        return at(SettlementPage.class);
    }
}

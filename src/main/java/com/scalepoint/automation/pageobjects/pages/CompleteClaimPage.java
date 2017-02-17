package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class CompleteClaimPage extends Page {

    @FindBy(name = "policy_number")
    private ExtInput policyNumber;

    @FindBy(name = "claim_number")
    private ExtInput claimNumber;

    @FindBy(name = "phone")
    private ExtInput phoneField;

    @FindBy(name = "cellPhoneNumber")
    private ExtInput cellPhoneField;

    @FindBy(name = "adr1")
    private ExtInput addressField;

    @FindBy(name = "adr2")
    private ExtInput address2Field;

    @FindBy(name = "city")
    private ExtInput cityField;

    @FindBy(name = "zipcode")
    private ExtInput zipcodeField;

    @FindBy(name = "email")
    private ExtInput emailField;

    @FindBy(name = "password")
    private ExtInput customerPasswordField;

    @FindBy(id = "sendPasswordSMSspan")
    private ExtCheckbox spSMSCheckBOX;

    @FindBy(xpath = "//button[@onclick='sendThroughScalePoint()']")
    private Button compWthMailButton;

    @FindBy(id = "nosend")
    private Button compWithoutMailButton;

    @FindBy(xpath = "//button[@onclick='endThroughScalePoint()']")
    private Button compWthMailButtonWithMistakeInLocator;

    @FindBy(id = "gem")
    private Button saveClaim;

    @FindBy(id = "genlever")
    private Button replace;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/enter_base_info.jsp";
    }

    @Override
    public CompleteClaimPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(emailField);
        waitForVisible(saveClaim);
        return this;
    }

    public CompleteClaimPage fillClaimForm(Claim claim) {
        enterPhone(claim.getPhoneNumber()).
                enterCellPhone(claim.getCellNumber()).
                enterAddress(claim.getAddress(), claim.getAddress2(), claim.getCity(), claim.getZipCode()).
                enterEmail(claim.getEmail()).
                sendSMS(false);
        return this;
    }

    public CompleteClaimPage fillClaimFormWithPassword(Claim claim) {
        return fillClaimForm(claim).enterPassword(Constants.PASSWORD);
    }

    public CompleteClaimPage enterPhone(String phone) {
        phoneField.setValue(phone);
        return this;
    }

    public CompleteClaimPage enterCellPhone(String phone) {
        cellPhoneField.setValue(phone);
        return this;
    }

    public CompleteClaimPage enterAddress(String addr, String addr2, String city, String zip) {
        addressField.setValue(addr);
        address2Field.setValue(addr2);
        cityField.setValue(city);
        zipcodeField.setValue(zip);
        return this;
    }

    public CompleteClaimPage enterEmail(String email) {
        emailField.setValue(email);
        return this;
    }

    public CompleteClaimPage enterPassword(String pass) {
        customerPasswordField.setValue(pass);
        return this;
    }

    public CompleteClaimPage sendSMS(boolean state) {
        spSMSCheckBOX.set(state);
        return this;
    }

    public CompleteClaimPage enterPolicyNumber(String policyNumber) {
        this.policyNumber.enter(policyNumber);
        return this;
    }

    public CompleteClaimPage enterClaimNumber(String claimNumber) {
        this.claimNumber.enter(claimNumber);
        return this;
    }

    public MyPage completeWithEmail() {
        compWthMailButton.click();
        return at(MyPage.class);
    }

    public MyPage completeWithoutEmail() {
        compWithoutMailButton.click();
        return at(MyPage.class);
    }

    public ShopWelcomePage completeWithEmailAndLoginToShop() {
        return completeWithEmail().
                openRecentClaim().
                toMailsPage().
                viewMail(MailsPage.MailType.CUSTOMER_WELCOME).
                findLoginToShopLinkAndOpenIt().
                enterPassword(Constants.PASSWORD).
                login();
    }

    public MyPage saveClaim() {
        saveClaim.click();
        return at(MyPage.class);
    }

    public ReplacementDialog openReplacementWizard() {
        replace.click();
        return BaseDialog.at(ReplacementDialog.class);
    }
}

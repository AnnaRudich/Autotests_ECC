package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class CompleteClaimPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/enter_base_info.jsp";

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

    @FindBy(name = "sendPasswordSMS")
    private ExtCheckbox spSMSCheckBOX;

    @FindBy(xpath = "//button[@onclick='sendThroughScalePoint()']")
    private WebElement compWthMailButton;

    @FindBy(xpath = "//button[@onclick='endThroughScalePoint()']")
    private WebElement compWthMailButtonWithMistakeInLocator;

    @FindBy(id = "gem")
    private Button saveClaim;

    @FindBy(id = "genlever")
    private Button replace;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public CompleteClaimPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(emailField);
        waitForVisible(saveClaim);
        return this;
    }

    public CompleteClaimPage fillClaimForm(Claim claim) {
        enterPhone(claim.getPhoneNumber()).
                enterCellPhone(claim.getCellNumber()).
                enterAddress(claim.getAddress(), claim.getAddress2(), claim.getCity(), claim.getZipCode()).
                enterEmail(claim.getEmail());
        return this;
    }

    public CompleteClaimPage enterPhone(String phone) {
        Wait.waitForElementDisplaying(By.name("phone"));
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

    public CompleteClaimPage EnterPassword(String pass) {
        customerPasswordField.setValue(pass);
        return this;
    }

    public CompleteClaimPage SetSendSMS(boolean state) {
        spSMSCheckBOX.set(state);
        return this;
    }

    public CompleteClaimPage EnterPolicyNumber(String _policyNumber) {
        policyNumber.enter(_policyNumber);
        return this;
    }

    public CompleteClaimPage enterClaimNumber(String _claimNumber) {
        claimNumber.enter(_claimNumber);
        return this;
    }

    public MyPage completeWithEmail() {
        compWthMailButton.click();
        return at(MyPage.class);
    }

    public MyPage saveClaim() {
        saveClaim.click();
        return at(MyPage.class);
    }

    public void ReplaceClaim() {
        replace.click();
        Wait.waitForPageLoaded();
    }
}

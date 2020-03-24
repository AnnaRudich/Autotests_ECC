package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckbox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.pages.oldshop.ShopWelcomePage;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;


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

    @FindBy(id = "send")
    private Button compWthMailButton;

    @FindBy(id = "nosend")
    private Button compWithoutMailButton;

    @FindBy(id = "externally")
    private Button compExternallyButton;

    @FindBy(id = "gem")
    private Button saveClaim;

    @FindBy(id = "genlever")
    private Button replace;

    @FindBy(xpath = "//*[contains(@id, 'replacement-button-shop')][contains(@class, 'x-btn-icon')]")
    private Button goToShop;

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

    By replacementButtonByXpath = By.id("genlever");

    public CompleteClaimPage fillClaimForm(Claim claim) {
        enterPhone(claim.getPhoneNumber()).
                enterCellPhone(claim.getCellNumber()).
                enterAddress(claim.getAddress(), claim.getAddress2(), claim.getCity(), claim.getZipCode()).
                enterEmail(claim.getEmail()).
                sendSMS(false);
        return this;
    }

    public CompleteClaimPage fillClaimFormWithPassword(Claim claim) {
        return fillClaimForm(claim).enterPassword(Constants.DEFAULT_PASSWORD);
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

    public CompleteClaimPage enterZipCode(String zip) {
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

    public MyPage completeWithEmail(Claim claim) {
        compWthMailButton.click();
        SolrApi.waitForClaimStatusChangedTo(claim, ClaimStatus.COMPLETED);
        return at(MyPage.class);
    }

    public MyPage completeWithEmail(Claim claim, DatabaseApi databaseApi) {
        compWthMailButton.click();
        new GdprConfirmationDialog().confirmUpdateOnBaseInfo();
        databaseApi.waitForClaimStatusChangedTo(claim, ClaimStatus.COMPLETED);
        return at(MyPage.class);
    }

    public MyPage completeWithoutEmail() {
        compWithoutMailButton.click();
        new GdprConfirmationDialog().confirmUpdateOnBaseInfo();
        return at(MyPage.class);
    }

    public MyPage completeExternally(Claim claim, DatabaseApi databaseApi) {
        compExternallyButton.click();
        new GdprConfirmationDialog().confirmUpdateOnBaseInfo();
        databaseApi.waitForClaimStatusChangedTo(claim, ClaimStatus.CLOSED_EXTERNALLY);
        return at(MyPage.class);
    }

    public ShopWelcomePage completeWithEmailAndLoginToShop(Claim claim, DatabaseApi databaseApi) {
        return completeWithEmail(claim, databaseApi).
                openRecentClaim().
                toMailsPage().
                viewMail(MailsPage.MailType.CUSTOMER_WELCOME).
                findLoginToShopLinkAndOpenIt().
                enterPassword(Constants.DEFAULT_PASSWORD).
                login();
    }

    public MyPage saveClaim() {
        saveClaim.click();
        new GdprConfirmationDialog().confirmUpdateOnBaseInfo();
        return at(MyPage.class);
    }

    public ReplacementDialog openReplacementWizard() {
        Wait.waitForAjaxCompleted();
        replace.click();
        new GdprConfirmationDialog().confirmUpdateOnBaseInfo();
        return BaseDialog.at(ReplacementDialog.class);
    }

    public CompleteClaimPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return CompleteClaimPage.this;
    }

    public class Asserts {
        public CompleteClaimPage.Asserts assertReplacementButtonIsNotVisible() {
            assertThat(Wait.isElementNotPresent(replacementButtonByXpath)).as("replacement button should should not be present").isTrue();
            return this;
        }

    }
}

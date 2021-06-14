package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.GdprConfirmationDialog;
import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxTypeDiv;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.shared.ClaimStatus;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;


@EccPage
public class CompleteClaimPage extends Page {

    @FindBy(name = "policy_number")
    private WebElement policyNumber;

    @FindBy(name = "claim_number")
    private WebElement claimNumber;

    @FindBy(name = "phone")
    private WebElement phoneField;

    @FindBy(name = "cellPhoneNumber")
    private WebElement cellPhoneField;

    @FindBy(name = "adr1")
    private WebElement addressField;

    @FindBy(name = "adr2")
    private WebElement address2Field;

    @FindBy(name = "city")
    private WebElement cityField;

    @FindBy(name = "zipcode")
    private WebElement zipcodeField;

    @FindBy(name = "email")
    private WebElement emailField;

    @FindBy(name = "password")
    private WebElement customerPasswordField;

    @FindBy(id = "sendPasswordSMSspan")
    private ExtCheckboxTypeDiv spSMSCheckBOX;

    @FindBy(id = "send")
    private Button compWthMailButton;

    @FindBy(id = "nosend")
    private Button compWithoutMailButton;

    @FindBy(id = "externally")
    private Button compExternallyButton;

    @FindBy(id = "gem")
    private Button saveClaim;

    @FindBy(id = "genlever")
    private WebElement replace;

    @FindBy(xpath = "//*[contains(@id, 'replacement-button-shop')][contains(@class, 'x-btn-icon')]")
    private Button goToShop;

    @FindBy(name = "agent_name")
    private WebElement agentName;

    @FindBy(name = "agent_email")
    private WebElement agentEmail;

    @FindBy(name = "send_agent_email")
    private WebElement sendAgentEmail;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/enter_base_info.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(saveClaim).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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

    public CompleteClaimPage fillClaimFormWithAgent(Claim claim) {
        fillClaimForm(claim)
                .enterAgentName(claim.getAgentName())
                .enterAgentEmail(claim.getAgentEmail());
        return this;
    }

    public CompleteClaimPage fillClaimFormWithPassword(Claim claim) {
        return fillClaimForm(claim).enterPassword(Constants.DEFAULT_PASSWORD);
    }

    public CompleteClaimPage enterPhone(String phone) {
        $(phoneField).setValue(phone);
        return this;
    }

    public CompleteClaimPage enterCellPhone(String phone) {
        $(cellPhoneField).setValue(phone);
        return this;
    }

    public CompleteClaimPage enterAddress(String addr, String addr2, String city, String zip) {
        $(addressField).setValue(addr);
        $(address2Field).setValue(addr2);
        $(cityField).setValue(city);
        $(zipcodeField).setValue(zip);
        return this;
    }

    public CompleteClaimPage enterZipCode(String zip) {
        $(zipcodeField).setValue(zip);
        return this;
    }

    public CompleteClaimPage enterEmail(String email) {
        $(emailField).setValue(email);
        return this;
    }

    public CompleteClaimPage enterPassword(String pass) {
        $(customerPasswordField).setValue(pass);
        return this;
    }

    public CompleteClaimPage enterAgentName(String name) {
        $(agentName).setValue(name);
        return this;
    }

    public CompleteClaimPage enterAgentEmail(String email) {
        $(agentEmail).setValue(email);
        return this;
    }

    public CompleteClaimPage sendSMS(boolean state) {
        spSMSCheckBOX.set(state);
        return this;
    }

    public CompleteClaimPage sendAgendEmail(boolean state) {
        $(sendAgentEmail).setSelected(state);
        return this;
    }

    public CompleteClaimPage enterPolicyNumber(String policyNumber) {
        $(this.policyNumber).setValue(policyNumber);
        return this;
    }

    public CompleteClaimPage enterClaimNumber(String claimNumber) {
        $(this.claimNumber).setValue(claimNumber);
        return this;
    }

    public MyPage completeWithEmail(Claim claim, DatabaseApi databaseApi, boolean gdpr) {
        compWthMailButton.click();
        if(gdpr) {
            BaseDialog.
                    at(GdprConfirmationDialog.class)
                    .confirm();
        }
        databaseApi.waitForClaimStatusChangedTo(claim, ClaimStatus.COMPLETED);
        return at(MyPage.class);
    }

    public MyPage completeWithoutEmail() {
        compWithoutMailButton.click();
        BaseDialog.
                at(GdprConfirmationDialog.class)
                .confirm();
        return at(MyPage.class);
    }

    public MyPage completeExternally(Claim claim, DatabaseApi databaseApi) {
        compExternallyButton.click();
        BaseDialog
                .at(GdprConfirmationDialog.class)
                .confirm();
        databaseApi.waitForClaimStatusChangedTo(claim, ClaimStatus.CLOSED_EXTERNALLY);
        return at(MyPage.class);
    }

    public MyPage saveClaim(boolean gdpr) {
        saveClaim.click();
        if(gdpr) {
            BaseDialog
                    .at(GdprConfirmationDialog.class)
                    .confirm();
        }
        return at(MyPage.class);
    }

    public ReplacementDialog openReplacementWizard(boolean gdpr) {
        Wait.waitForAjaxCompleted();
        Wait.waitForJavascriptRecalculation();
        hoverAndClick($(replace));
        if(gdpr) {
            BaseDialog
                    .at(GdprConfirmationDialog.class)
                    .confirm();
        }
        return BaseDialog.at(ReplacementDialog.class);
    }

    public CompleteClaimPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return CompleteClaimPage.this;
    }

    public class Asserts {
        public CompleteClaimPage.Asserts assertReplacementButtonIsNotVisible() {
            assertThat(verifyElementVisible($(replacementButtonByXpath))).as("replacement button should should not be present").isFalse();
            return this;
        }

    }
}

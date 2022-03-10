package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialogSelenide;
import com.scalepoint.automation.pageobjects.dialogs.SelfServicePasswordDialog;
import com.scalepoint.automation.pageobjects.modules.CustomerDetails;
import com.scalepoint.automation.utils.DateUtils;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.OperationalUtils.assertEqualsDouble;
import static com.scalepoint.automation.utils.Wait.verifyElementVisible;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
@RequiredParameters("shnbr=%s")
public class CustomerDetailsPage extends BaseClaimPage {

    @FindBy(id = "genoptag")
    private Button reopenClaim;

    @FindBy(id = "annuller_sag")
    private WebElement cancelClaimButton;

    @FindBy(id = "damage_date_value")
    private WebElement damageDate;

    @FindBy(id = "claim_no")
    private WebElement claimNumber;

    @FindBy(xpath = "//a[@href='javascript:EditDamageDate()']")
    private WebElement damageDateEdit;

    @FindBy(xpath = "//button[contains(@onclick, 'newPassword')]")
    private WebElement newPasswordButton;

    @FindBy(id = "firstname")
    private WebElement firstName;

    @FindBy(id = "lastname")
    private WebElement lastName;

    @FindBy(id = "address1")
    private WebElement address1;

    @FindBy(id = "zipcode")
    private WebElement zipCode;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "policy_type")
    private WebElement policyType;

    private CustomerDetails customerDetails = new CustomerDetails();

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/customer_details.jsp";
    }

    protected String getAlternativeUrl() {
        return "webshop/jsp/matching_engine/details.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl(), getAlternativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        if (driver.getCurrentUrl().contains(getRelativeUrl())) {
            $(reopenClaim).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
            $(cancelClaimButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        } else {
            $(claimNumber).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        }
    }

    public String getClaimNumber() {
        return claimNumber.getText();
    }

    public CustomerDetailsPage cancelClaim() {
        cancelClaimButton.click();
        By alertMessageBy = By.xpath(".//div[contains(@id, 'messagebox')]//span[text()='Yes']//ancestor::a");
        verifyElementVisible($(alertMessageBy));
        $(alertMessageBy).click();
        return at(CustomerDetailsPage.class);
    }

    public ReopenClaimDialog startReopenClaimWhenViewModeIsEnabled() {
        $(By.id("genoptag")).click();
        return BaseDialog.at(ReopenClaimDialog.class);
    }

    public SettlementPage reopenClaimWhenViewModeIsDisabled(){
        $(By.id("genoptag")).click();
        getAlertTextAndAccept();
        return at(SettlementPage.class);
    }

    public SelfServicePasswordDialog newSelfServicePassword() {
        newPasswordButton.click();
        return BaseDialogSelenide.at(SelfServicePasswordDialog.class);
    }

    public CustomerDetailsPage selectDamageDate(LocalDate date) {
        $(By.xpath("//a[@href='javascript:EditDamageDate()']")).click();
        $(By.id("damageDate-inputEl")).click();
        verifyElementVisible($(By.xpath("//div[contains(@id, 'datepicker') and contains(@class, 'x-datepicker-default')]")));
        $(By.xpath("//a[contains(@id, 'splitbutton')]")).click();
        verifyElementVisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-month']/a[text()='" + date.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("da-DK")).substring(0, 3).toLowerCase() + "']")).click();
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-year']/a[text()='" + date.getYear() + "']")).click();
        hoverAndClick($(By.xpath("//div[@class='x-monthpicker-buttons']//span[contains(text(),'OK')]")));
        verifyElementVisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//table//td[contains(@class, 'x-datepicker-active')]/div[text()='" + date.getDayOfMonth() + "']")).click();
        verifyElementVisible($(By.xpath("//div[contains(@id, 'datepicker') and contains(@class, 'x-datepicker-default')]")));
        $(By.xpath("//div[contains(@id, 'toolbar')]//div[@class='x-box-inner']//a")).click();
        verifyElementVisible($(By.id("damageDate-inputEl")));
        return this;
    }

    public CustomerDetailsPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return CustomerDetailsPage.this;
    }

    public class Asserts {

        private static final String PAGE_FORMAT = "dd-MM-yyyy";

        private String toPageFormat(LocalDate date) {
            return DateUtils.format(date, PAGE_FORMAT);
        }

        public Asserts assertCustomerCashValueIs(Double expectedPrice) {
            assertEqualsDouble(customerDetails.getCashValue(), expectedPrice, "Voucher cash value %s should be assertEqualsDouble to not depreciated voucher cash value %s");
            return this;
        }

        public Asserts assertClaimNumber(String expectedClaimNumber) {
            assertThat($(claimNumber).getText()).isEqualTo(expectedClaimNumber);
            return this;
        }

        public Asserts assertFirstName(String expectedFirstName) {
            assertThat($(firstName).getText()).isEqualTo(expectedFirstName);
            return this;
        }

        public Asserts assertLastName(String expectedLastName) {
            assertThat($(lastName).getText()).isEqualTo(expectedLastName);
            return this;
        }

        public Asserts assertAddress1(String expectedAddress) {
            assertThat($(address1).getText()).isEqualTo(expectedAddress);
            return this;
        }

        public Asserts assertZipCode(String expectedZipCode) {
            assertThat($(zipCode).getText()).isEqualTo(expectedZipCode);
            return this;
        }

        public Asserts assertCity(String expectedCity) {
            assertThat($(city).getText()).isEqualTo(expectedCity);
            return this;
        }

        public Asserts assertPolicyType(String expectedPolicyType) {
            assertThat($(policyType).getText()).isEqualTo(expectedPolicyType);
            return this;
        }

        public Asserts assertCustomerFaceValueTooltipIs(Double expectedPrice) {
            assertEqualsDouble(customerDetails.getFaceTooltipValue(), expectedPrice, "Voucher face value %s should be assertEqualsDouble to not depreciated new price %s");
            return this;
        }

        public Asserts assertDamageDateIs(LocalDate expectedDamageData) {
            assertThat(damageDate.getText()).isEqualTo(toPageFormat(expectedDamageData));
            return this;
        }

        public Asserts assertDamageDateIsEmpty() {
            assertThat(damageDate.getText().equals(""));
            return this;
        }

        public Asserts assertIsDamageDateEditAvailable() {
            assertThat(verifyElementVisible($(damageDateEdit))).isTrue();
            return this;
        }

        public Asserts assertIsDamageDateEditNotAvailable() {
            Boolean isDisplayed;
            try {
                isDisplayed = damageDateEdit.isDisplayed();
            } catch (NoSuchElementException ex) {
                logger.info(ex.getMessage());
                isDisplayed = false;
            }
            assertThat(isDisplayed).isFalse();
            return this;
        }

        public Asserts assertCustomerDetailsPagePresent(){
            assertThat($(reopenClaim).is(Condition.visible))
                    .as("CustomerDetails page is not loaded")
                    .isTrue();
            return this;
        }

    }

}

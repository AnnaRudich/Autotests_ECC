package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.PastedData;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import lombok.Builder;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.*;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class NewCustomerPage extends Page {

    @FindBy(id = "damageDate-inputEl")
    private WebElement damageDate;

    @FindBy(name = "salutation")
    private WebElement title;

    @FindBy(name = "last_name")
    private WebElement surname;

    @FindBy(name = "first_name")
    private WebElement firstNames;

    @FindBy(name = "postal_code")
    private WebElement postalCode;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "address")
    private WebElement address;

    @FindBy(name = "policy_number")
    private WebElement policyNumber;

    @FindBy(name = "claim_number")
    private WebElement claimsNumber;

    @FindBy(id = "policy_type")
    private Select policyType;

    @FindBy(css = ".selectfield")
    private Select selectCompany;

    @FindBy(id = "continue")
    private Button continueButton;

    @FindBy(className = "copyPasteTextArea")
    private WebElement copyPasteTextArea;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/indtast_kunde.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        waitForPageLoaded();
        $(claimsNumber).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(surname).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public WebElement getContinueButton() {
        return continueButton.getWrappedElement();
    }

    public NewCustomerPage enterTitle(String titleName) {
        $(title).setValue(titleName);
        return this;
    }

    public NewCustomerPage enterSurname(String surnameText) {
        $(surname).setValue(surnameText);
        return this;
    }

    public NewCustomerPage enterFirstName(String firstNameText) {
        $(firstNames).setValue(firstNameText);
        return this;
    }

    public NewCustomerPage enterPolicyNumber(String policyNumberText) {
        $(policyNumber).setValue(policyNumberText);
        return this;
    }

    public NewCustomerPage enterClaimNumber(String claimNumberText) {
        $(claimsNumber).setValue(claimNumberText);
        return this;
    }

    public NewCustomerPage selectPolicyType(String policyType) {
        this.policyType.selectByVisibleText(policyType);
        return this;
    }

    public NewCustomerPage enterCopyPasteTextArea(String text) {
        $(copyPasteTextArea).setValue(text);
        return this;
    }

    public NewCustomerPage selectPolicyType(int index) {
        waitForVisible(policyType);
        policyType.selectByIndex(index);
        return this;
    }

    public SettlementPage create() {
        continueButton.click();
        Wait.waitForPageLoaded();
        return Page.at(SettlementPage.class);
    }

    public NewCustomerPage selectCompany(String company) {
        selectCompany.selectByVisibleText(company);
        return this;
    }

    public NewCustomerPage selectDamageDate(LocalDate date) {
        damageDate.click();
        waitForVisible($(By.xpath("//div[contains(@id, 'datepicker') and contains(@class, 'x-datepicker-default')]")));
        $(By.xpath("//a[contains(@id, 'splitbutton')]")).click();
        waitForVisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-month']/a[text()='" + date.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("da-DK")).substring(0, 3).toLowerCase() + "']")).click();
        $(By.xpath("//div[@class='x-monthpicker-item x-monthpicker-year']/a[text()='" + date.getYear() + "']")).click();
        hoverAndClick($(By.xpath("//div[@class='x-monthpicker-buttons']//span[text()='OK']/parent::span")));
        waitForInvisible($(By.xpath("//div[contains(@class, 'x-monthpicker-body')]")));
        $(By.xpath("//table[@class='x-datepicker-inner']//td[@class='x-datepicker-active x-datepicker-cell']/a[text()='" + date.getDayOfMonth() + "']")).click();
        return this;
    }

    public NewCustomerPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertThatDamageDateIsDisplayed() {
            assertThat(damageDate.isDisplayed()).isTrue();
            return this;
        }

        public Asserts assertDamageDate(String expectedDamageDate) {
            assertThat($(damageDate).getValue()).isEqualTo(expectedDamageDate);
            return this;
        }

        public Asserts assertLastName(String expectedLastName) {
            assertThat($(surname).getValue()).isEqualTo(expectedLastName);
            return this;
        }

        public Asserts assertFirstName(String expectedFirstName) {
            assertThat($(firstNames).getValue()).isEqualTo(expectedFirstName);
            return this;
        }

        public Asserts assertCity(String expectedCity) {
            assertThat($(city).getValue()).isEqualTo(expectedCity);
            return this;
        }

        public Asserts assertAddress(String expectedAdress) {
            assertThat($(address).getValue()).isEqualTo(expectedAdress);
            return this;
        }

        public Asserts assertClaimNumber(String expectedClaimNumber) {
            assertThat($(claimsNumber).getValue()).isEqualTo(expectedClaimNumber);
            return this;
        }

        public Asserts assertPostalCode(String expectedPostalCode) {
            assertThat($(postalCode).getValue()).isEqualTo(expectedPostalCode);
            return this;
        }

        public Asserts assertPolicyNumber(String expectedPolicyNumber) {
            SelenideElement policyNumberElement = $(policyNumber);
            if(policyNumberElement.has(not(attribute("type", "hidden")))) {
                assertThat($(policyNumber).getValue()).isEqualTo(expectedPolicyNumber);
            }
            return this;
        }

        public Asserts assertPolicyType(String expectedPolicyType) {
            assertThat($(policyType).getText()).isEqualTo(expectedPolicyType);
            return this;
        }

        public Asserts assertCopyPasteMechanism() {
            PastedData pastedData = PastedData.parsePastedData($(copyPasteTextArea).getValue());
            assertFirstName(pastedData.getFirstName());
            assertLastName(pastedData.getLastName());
            assertPostalCode(pastedData.getZipCode());
            assertClaimNumber(pastedData.getClaimNumber());
            assertCity(pastedData.getCity());
            assertAddress(pastedData.getAddress());
            assertPolicyNumber(pastedData.getPolicyNumber());
            assertDamageDate(pastedData.getDamageDate());
            assertPolicyType(pastedData.getPolicyType());
            return this;
        }
    }
}

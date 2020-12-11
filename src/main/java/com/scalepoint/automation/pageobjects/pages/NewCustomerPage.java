package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

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
    }
}

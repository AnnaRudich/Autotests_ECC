package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class NewCustomerPage extends Page {

    @FindBy(id = "damageDate-inputEl")
    private WebElement damageDate;

    @FindBy(name = "salutation")
    private ExtInput title;

    @FindBy(name = "last_name")
    private ExtInput surname;

    @FindBy(name = "first_name")
    private ExtInput firstNames;

    @FindBy(name = "policy_number")
    private ExtInput policyNumber;

    @FindBy(name = "claim_number")
    private ExtInput claimsNumber;

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
    public NewCustomerPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(claimsNumber);
        waitForVisible(surname);
        return this;
    }

    public WebElement getContinueButton() {
        return continueButton.getWrappedElement();
    }

    public NewCustomerPage enterTitle(String titleName) {
        title.enter(titleName);
        return this;
    }

    public NewCustomerPage enterSurname(String surnameText) {
        surname.enter(surnameText);
        return this;
    }

    public NewCustomerPage enterFirstName(String firstNameText) {
        firstNames.enter(firstNameText);
        return this;
    }

    public NewCustomerPage enterPolicyNumber(String policyNumberText) {
        policyNumber.enter(policyNumberText);
        return this;
    }

    public NewCustomerPage enterClaimNumber(String claimNumberText) {
        claimsNumber.enter(claimNumberText);
        return this;
    }

    public NewCustomerPage selectPolicyType(String policyType) {
        this.policyType.selectByVisibleText(policyType);
        return this;
    }

    public NewCustomerPage selectPolicyType(int index) {
        policyType.selectByIndex(index);
        return this;
    }

    public void create() {
        continueButton.click();
        Wait.waitForPageLoaded();
    }

    public NewCustomerPage selectCompany(String company) {
        selectCompany.selectByVisibleText(company);
        return this;
    }

    public NewCustomerPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        public Asserts assertThatDamgeDateIsDisplayed() {
            assertThat(damageDate.isDisplayed()).isTrue();
            return this;
        }
    }
}

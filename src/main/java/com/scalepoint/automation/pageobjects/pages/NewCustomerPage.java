package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class NewCustomerPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/indtast_kunde.jsp";

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
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public NewCustomerPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForVisible(title);
        waitForVisible(surname);
        return this;
    }

    public WebElement getContinueButton() {
        return continueButton.getWrappedElement();
    }

    public NewCustomerPage EnterTitle(String titleName) {
        Wait.waitForPageLoaded();
        title.enter(titleName);
        return this;
    }

    public NewCustomerPage EnterSurname(String surnameText) {
        surname.enter(surnameText);
        return this;
    }

    public NewCustomerPage EnterFirstName(String firstNameText) {
        firstNames.enter(firstNameText);
        return this;
    }

    public NewCustomerPage EnterPolicyNumber(String policyNumberText) {
        policyNumber.enter(policyNumberText);
        return this;
    }

    public NewCustomerPage EnterClaimNumber(String claimNumberText) {
        claimsNumber.enter(claimNumberText);
        return this;
    }

    public NewCustomerPage SelectPolicyType(String _policyType) {
        policyType.selectByVisibleText(_policyType);
        return this;
    }

    public NewCustomerPage SelectPolicyType(int index) {
        policyType.selectByIndex(index);
        return this;
    }

    public void Continue() {
        continueButton.click();
        Wait.waitForPageLoaded();
    }

    public NewCustomerPage SelectCompany(String _company) {
        selectCompany.selectByVisibleText(_company);
        return this;
    }
}

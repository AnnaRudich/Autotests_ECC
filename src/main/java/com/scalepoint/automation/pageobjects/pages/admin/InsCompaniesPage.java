package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class InsCompaniesPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(name = "icList")
    private Select companies;

    @Override
    protected InsCompaniesPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(addButton).waitUntil(Condition.visible, STANDARD_WAIT_UNTIL_TIMEOUT);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/insurance_companies.jsp";
    }

    public InsCompAddEditPage toAddNewCompanyPage() {
        clickAndWaitForDisplaying(addButton, By.name("icname"));
        return at(InsCompAddEditPage.class);
    }

    public boolean isCompanyDisplayed(InsuranceCompany insuranceCompany) {
        try {
            companies.selectByVisibleText(insuranceCompany.getIcName());
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private void selectEditOption() {
        clickAndWaitForDisplaying(editButton, By.name("icname"));
    }

    public InsCompAddEditPage editCompany(String icName) {
        companies.selectByVisibleText(icName);
        selectEditOption();
        return at(InsCompAddEditPage.class);
    }

    public InsCompaniesPage enableAuditForIc(String icName) {
        to(InsCompaniesPage.class)
                .editCompany(icName)
                .enableAuditOptionAndSave();
        return to(InsCompaniesPage.class);
    }

    public InsCompaniesPage assertCompanyDisplayed(InsuranceCompany insuranceCompany) {
        Assert.assertTrue(isCompanyDisplayed(insuranceCompany));
        return this;
    }
}

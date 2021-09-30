package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class InsCompaniesPage extends AdminBasePage {

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(addButton).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/insurance_companies.jsp";
    }

    public InsCompAddEditPage toAddNewCompanyPage() {
        hoverAndClick($(addButton));
        return at(InsCompAddEditPage.class);
    }

    private ElementsCollection getCompanies(){

        return $$("[name=icList] > option");
    }

    public boolean isCompanyDisplayed(InsuranceCompany insuranceCompany) {
        try {

            getCompanies().findBy(Condition.text(insuranceCompany.getIcName()));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    private void selectEditOption() {
        hoverAndClick($(editButton));
    }

    public InsCompAddEditPage editCompany(String icName) {
        getCompanies().findBy(Condition.text(icName)).click();
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

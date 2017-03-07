package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-509")
public class InsCompanyTest extends BaseTest {

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Insurance company IC1
     * THEN: IC1 is displayed in company's list
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-509 It's possible to update new simple parent IC. IC is displayed in IC list")
    public void charlie509_updateNewSimpleParentIC(InsuranceCompany insuranceCompany) {
        InsuranceCompany anotherCompany = TestData.getInsuranceCompany();
        login(getSystemUser())
                .to(InsCompaniesPage.class)
                .toAddNewCompanyPage()
                .createCompany(insuranceCompany)
                .editCompany(insuranceCompany.getIcName())
                .updateNameAndSave(anotherCompany)
                .assertCompanyDisplayed(anotherCompany);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Insurance company IC1
     * THEN: IC1 is displayed in company's list
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-509 It's possible to create new simple parent IC. IC is displayed in IC list")
    public void charlie509_createNewSimpleParentIC(InsuranceCompany insuranceCompany) {
        login(getSystemUser())
                .to(InsCompaniesPage.class)
                .toAddNewCompanyPage()
                .createCompany(insuranceCompany)
                .assertCompanyDisplayed(insuranceCompany);
    }

}

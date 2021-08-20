package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.admin.InsCompaniesPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-509")
public class InsCompanyTest extends BaseTest {


    @BeforeMethod
    public void toInsCompaniesPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        InsuranceCompany insuranceCompany = getLisOfObjectByClass(parameters, InsuranceCompany.class).get(0);

        login(getSystemUser())
                .to(InsCompaniesPage.class)
                .toAddNewCompanyPage()
                .createCompany(insuranceCompany);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Insurance company IC1
     * THEN: IC1 is displayed in company's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.INS_COMPANY}, dataProvider = TEST_DATA_PROVIDER,
            description = "CHARLIE-509 It's possible to update new simple parent IC. IC is displayed in IC list")
    public void charlie509_updateNewSimpleParentIC(InsuranceCompany insuranceCompany, InsuranceCompany secondInsuranceCompany) {

        at(InsCompaniesPage.class)
                .editCompany(insuranceCompany.getIcName())
                .updateNameAndSave(secondInsuranceCompany)
                .assertCompanyDisplayed(secondInsuranceCompany);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Insurance company IC1
     * THEN: IC1 is displayed in company's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.INS_COMPANY}, dataProvider = TEST_DATA_PROVIDER,
            description = "CHARLIE-509 It's possible to create new simple parent IC. IC is displayed in IC list")
    public void charlie509_createNewSimpleParentIC(InsuranceCompany insuranceCompany) {

        at(InsCompaniesPage.class)
                .assertCompanyDisplayed(insuranceCompany);
    }

}

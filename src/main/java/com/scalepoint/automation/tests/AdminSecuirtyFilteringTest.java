package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.restService.AdminService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class AdminSecuirtyFilteringTest extends BaseApiTest {

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void outputManagementAuthorized(User user){

        new AdminService()
                .login(user)
                .reqouestOutputManagement()
                .doAssert(adminServicc -> adminServicc.assertAuthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void outputManagementUnauthorized(){

        new AdminService()
                .reqouestOutputManagement()
                .doAssert(adminServicc -> adminServicc.assertUnauthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void insuranceCompaniesAuthorized(User user){

        new AdminService()
                .login(user)
                .requestInsuranceCompanies()
                .doAssert(adminServicc -> adminServicc.assertAuthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void insuranceCompaniesUnauthorized(){

        new AdminService()
                .requestInsuranceCompanies()
                .doAssert(adminServicc -> adminServicc.assertUnauthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void requestOutstandingAmountsAuthorized(User user){

        new AdminService()
                .login(user)
                .requestOutstandingAmounts()
                .doAssert(adminServicc -> adminServicc.assertAuthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void requestOutstandingAmountsUnauthorized(){

        new AdminService()
                .requestOutstandingAmounts()
                .doAssert(adminServicc -> adminServicc.assertUnauthorized());
    }
    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void getListOfInsuranceCompaniesAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .getListOfInsuranceCompaniesTest()
                .doAssert(adminServicc -> adminServicc.assertAuthorized());
    }

    @Test(dataProviderClass = BaseTest.class, dataProvider = "testDataProvider")
    public void getListOfInsuranceCompaniesUnauthorizedTest(){

        new AdminService()
                .getListOfInsuranceCompaniesTest()
                .doAssert(adminServicc -> adminServicc.assertUnauthorized());
    }
}

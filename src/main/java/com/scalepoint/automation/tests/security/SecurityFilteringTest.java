package com.scalepoint.automation.tests.security;

import com.scalepoint.automation.services.restService.SecurityService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class SecurityFilteringTest extends BaseApiTest {

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void outputManagementStatusAuthorizedTest(User user){

        new SecurityService()
                .login(user)
                .requestOutputManagementStatus()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void outputManagementStatusUnauthorizedTest(){

        new SecurityService()
                .requestOutputManagementStatus()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getInsuranceCompaniesAuthorizedTest(User user){

        new SecurityService()
                .login(user)
                .requestInsuranceCompanies()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void insuranceCompaniesUnauthorized(){

        new SecurityService()
                .requestInsuranceCompanies()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void requestOutstandingAmountsAuthorizedTest(User user){

        new SecurityService()
                .login(user)
                .requestOutstandingAmounts()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void requestOutstandingAmountsUnauthorizedTest(){

        new SecurityService()
                .requestOutstandingAmounts()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }
    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getListOfInsuranceCompaniesAuthorizedTest(User user){

        new SecurityService()
                .login(user)
                .requestListOfInsuranceCompanies()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getListOfInsuranceCompaniesUnauthorizedTest(){

        new SecurityService()
                .requestListOfInsuranceCompanies()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getVouchersDataAuthorizedTest(User user){

        new SecurityService()
                .login(user)
                .requestVouchersData()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getVouchersDataUnauthorizedTest(){

        new SecurityService()
                .requestVouchersData()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }
}

package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.restService.AdminService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class AdminSecurityFilteringTest extends BaseApiTest {

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void outputManagementStatusAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .requestOutputManagementStatus()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void outputManagementStatusUnauthorizedTest(){

        new AdminService()
                .requestOutputManagementStatus()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getInsuranceCompaniesAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .requestInsuranceCompanies()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void insuranceCompaniesUnauthorized(){

        new AdminService()
                .requestInsuranceCompanies()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void requestOutstandingAmountsAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .requestOutstandingAmounts()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void requestOutstandingAmountsUnauthorizedTest(){

        new AdminService()
                .requestOutstandingAmounts()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }
    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getListOfInsuranceCompaniesAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .requestListOfInsuranceCompanies()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getListOfInsuranceCompaniesUnauthorizedTest(){

        new AdminService()
                .requestListOfInsuranceCompanies()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getVouchersDataAuthorizedTest(User user){

        new AdminService()
                .login(user)
                .requestVouchersData()
                .doAssert(adminService -> adminService.assertAuthorized());
    }

    @Test(groups = {TestGroups.SECURITY}, dataProviderClass = BaseTest.class, dataProvider = BaseTest.TEST_DATA_PROVIDER)
    public void getVouchersDataUnauthorizedTest(){

        new AdminService()
                .requestVouchersData()
                .doAssert(adminService -> adminService.assertUnauthorized());
    }
}

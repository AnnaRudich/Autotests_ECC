package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.RolesPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.SystemUser;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.CLAIMSHANDLER;
import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.SUPPLYMANAGER;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-537")
public class UserRolesTest extends BaseUITest {

    private static final String LOGIN_AS_NEW_SP_ADMIN_DATA_PROVIDER = "loginAsNewSPAdminDataProvider";
    private static final String UPDATE_NEW_SP_ADMIN_USER_DATA_PROVIDER = "updateNewSPAdminUserDataProvider";
    private static final String CREATE_SP_ADMIN_USER_DATA_PROVIDER = "createSPAdminUserDataProvider";
    private static final String CREATE_ICCHSM_USER_DATA_PROVIDER = "createICCHSMUserDataProvider";
    private static final String IC_SM_ONLY_LOGIN_SUP_MAN_ONLY_DATA_PROVIDER = "icSMOnlyLoginSupManOnlyDataProvider";
    private static final String UPDATE_ICSN_ONLY_TO_CH_TYPE_DATA_PROVIDER = "updateICSMOnlyToCHTypeDataProvider";

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * THEN: U2 can sign in to the application
     * THEN: Admin link is available for U2
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = LOGIN_AS_NEW_SP_ADMIN_DATA_PROVIDER,
            description = "CHARLIE-537 It's possible to sign in as new SP Admin user")
    public void loginAsNewSPAdminTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .toMatchingEngine()
                .getClaimMenu()
                .logout()
                .login(User.builder()
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .type(User.UserType.BASIC)
                        .build(), MyPage.class)
                .doAssert(MyPage.Asserts::assertAdminLinkDisplayed);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * WHEN: U1 updates all U2 details
     * THEN: U2 details are stored correctly
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = UPDATE_NEW_SP_ADMIN_USER_DATA_PROVIDER,
            description = "CHARLIE-537 It's possible to update new SP admin user")
    public void updateNewSPAdminUserTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        SystemUser newUser = TestData.getSystemUser();
        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .filterByIC(user.getCompany())
                .openUserForEditing(user.getLogin())
                .clearFields()
                .update(newUser)
                .doAssert(usersPage -> usersPage.assertUserExists(newUser));
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * THEN: U2 is displayed in Users list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = CREATE_SP_ADMIN_USER_DATA_PROVIDER,
            description = "CHARLIE-537 It's possible to create SP admin user. Created user is displayed in Users list")
    public void createSPAdminUserTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .doAssert(usersPage -> usersPage.assertUserExists(user));
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager and claim handler permissions
     * THEN: U2 is displayed in Users list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = CREATE_ICCHSM_USER_DATA_PROVIDER,
            description = "CHARLIE-537 It's possible to create IC Supply manager and claim handler user. Created user is displayed in Users list")
    public void createICCHSMUserTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .filterByIC(user.getCompany())
                .doAssert(usersPage -> usersPage.assertUserExists(user));
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager permissions
     * WHEN: U2 signs in to the application
     * THEN: U2 has an access to Supply Management part only
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = IC_SM_ONLY_LOGIN_SUP_MAN_ONLY_DATA_PROVIDER,
            description = "CHARLIE-537 IC SM only can sign in to Supply Management only")
    public void icSMOnlyLoginSupManOnlyTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .toMatchingEngine()
                .getClaimMenu()
                .logout()
                .login(User.builder()
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .type(User.UserType.BASIC)
                        .build(), SuppliersPage.class)
                .doAssert(SuppliersPage.Asserts::assertsIsToMatchingEngineLinkNotDisplayed);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager permissions
     * WHEN: U2 signs in to the application
     * THEN: U2 has an access to Supply Management part only
     * WHEN: U1 enables claim handler role for U2
     * THEN: U2 has an access to Supply Management and Matching Engine parts
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, dataProvider = UPDATE_ICSN_ONLY_TO_CH_TYPE_DATA_PROVIDER,
            description = "CHARLIE-537 It's possible to update role for IC SM only. " +
                    "CH role can be enabled. In this case user logins to ME")
    public void updateICSMOnlyToCHTypeTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        loginFlow.login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, userTypes)
                .toMatchingEngine()
                .getClaimMenu()
                .logout()
                .login(User.builder()
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .type(User.UserType.BASIC)
                        .build(), SuppliersPage.class)
                .signOut();

        loginFlow.login(getSystemUser(), UsersPage.class)
                .filterByIC(user.getCompany())
                .openUserForEditing(user.getLogin())
                .enableCHType()
                .selectSaveOption()
                .toMatchingEngine()
                .getClaimMenu()
                .logout()
                .login(User.builder()
                        .login(user.getLogin())
                        .password(user.getPassword())
                        .type(User.UserType.BASIC)
                        .build(), MyPage.class);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates role Role1
     * THEN: Role1 is displayed in roles list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, enabled = false, dataProvider = TEST_DATA_PROVIDER,
            description = "CHARLIE-551 It's possible to create new role with all enabled options. The RR is displayed in Roles list")
    public void charlie551_createNewRoleAllOptionsEnabled(Translations translations) {

        String roleName = translations.getRoles().getRoleName();

        loginFlow.login(getSystemUser(), RolesPage.class)
                .toAddRolePage()
                .createNewRoleAllRolesEnabled(roleName)
                .assertRoleDisplayed(roleName);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates role Role1
     * WHEN: U1 creates new user
     * THEN: It's possible to select Role1
     * THEN: User1 is created successfully
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_ROLES}, enabled = false, dataProvider = TEST_DATA_PROVIDER,
            description = "CHARLIE-551 It's possible to select new role for user creation. The new role is displayed on Add user page")
    public void charlie551_newRoleIsAvailableUserCreation(SystemUser user, Translations translations) {

        String roleName = translations.getRoles().getRoleName();

        loginFlow.login(getSystemUser(), RolesPage.class)
                .toAddRolePage()
                .createNewRoleAllRolesEnabled(roleName)
                .to(AdminPage.class)
                .toUsersPage()
                .toUserCreatePage()
                .createNewSPAdminNewRole(user, roleName)
                .doAssert(usersPage -> usersPage.assertUserExists(user));
    }

    @DataProvider(name = LOGIN_AS_NEW_SP_ADMIN_DATA_PROVIDER)
    public static Object[][] loginAsNewSPAdminDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = UPDATE_NEW_SP_ADMIN_USER_DATA_PROVIDER)
    public static Object[][] updateNewSPAdminUserDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = CREATE_SP_ADMIN_USER_DATA_PROVIDER)
    public static Object[][] createSPAdminUserDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = CREATE_ICCHSM_USER_DATA_PROVIDER)
    public static Object[][] createICCHSMUserDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, new UserAddEditPage.UserType[] {CLAIMSHANDLER, SUPPLYMANAGER}}
        };
    }

    @DataProvider(name = IC_SM_ONLY_LOGIN_SUP_MAN_ONLY_DATA_PROVIDER)
    public static Object[][] icSMOnlyLoginSupManOnlyDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, new UserAddEditPage.UserType[] {SUPPLYMANAGER}}
        };
    }

    @DataProvider(name = UPDATE_ICSN_ONLY_TO_CH_TYPE_DATA_PROVIDER)
    public static Object[][] updateICSMOnlyToCHTypeDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, new UserAddEditPage.UserType[] {SUPPLYMANAGER}}
        };
    }
}

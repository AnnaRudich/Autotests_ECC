package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.RolesPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.VouchersPage;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Roles;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.*;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
public class UserRolesTest extends BaseTest {

    public static final UserType[] ALL_ROLES = {ADMIN, CLAIMSHANDLER, SUPPLYMANAGER};

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * THEN: U2 is displayed in Users list
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-537 It's possible to create SP admin user. Created user is displayed in Users list")
    public void charlie537_createSPAdminUser(SystemUser user) {
//        UsersPage usersPage = login(getSystemUser(), UsersPage.class).
//                toUserCreatePage().
//                createUser(user, ALL_ROLES);
//        Assert.assertTrue(usersPage.isUserExists(user));
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * THEN: U2 can sign in to the application
     * THEN: Admin link is available for U2
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-537 It's possible to sign in as new SP Admin user")
    public void charlie537_loginAsNewSPAdmin(SystemUser user) throws Exception {
        MyPage myPage = login(getSystemUser(), UsersPage.class).
                toUserCreatePage().
                createUser(user, ALL_ROLES).
                toMatchingEngine().
                getClaimMenu().
                logout().
                login(user.getLogin(), user.getPassword(), MyPage.class);
        Assert.assertTrue(myPage.getClaimMenu().isAdminLinkDisplayed());
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates user U2 with Admin permissions
     * WHEN: U1 updates all U2 details
     * THEN: U2 details are stored correctly
     */
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-537 It's possible to update new SP admin user")
    public void charlie537_updateNewSPAdminUser(SystemUser user) throws Exception {
        UserAddEditPage editPage = login(getSystemUser(), UsersPage.class).
                toUserCreatePage().
                createUser(user, ALL_ROLES).
                filterByIC(user.getCompany()).
                openUserForEditing(user.getLogin());

        SystemUser newUser = TestData.getSystemUser();

        boolean userExists = editPage.clearFields().
                update(newUser).
                isDisplayed(newUser);
        Assert.assertTrue(userExists, "User is not found");
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager and claim handler permissions
     * THEN: U2 is displayed in Users list
     */
    @Test(dataProvider="testDataProvider",
            description = "CHARLIE-537 It's possible to create IC Supply manager and claim handler user. Created user is displayed in Users list")
    public void charlie537_createICCHSMUser(SystemUser user) throws Exception {
        UsersPage usersPage = login(getSystemUser(), UsersPage.class).
                toUserCreatePage().
                createUser(user, CLAIMSHANDLER, SUPPLYMANAGER).
                filterByIC(user.getCompany());
        Assert.assertTrue(usersPage.isDisplayed(user));
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager permissions
     * WHEN: U2 signs in to the application
     * THEN: U2 has an access to Supply Management part only
     */
    @Test(dataProvider="testDataProvider", description = "CHARLIE-537 IC SM only can sign in to Supply Management only")
    public void charlie537_icSMOnlyLoginSupManOnly(SystemUser user) {
        LoginPage loginPage = login(getSystemUser(), UsersPage.class).
                toUserCreatePage().
                createUser(user, SUPPLYMANAGER).
                toMatchingEngine().
                getClaimMenu().
                logout();

        VouchersPage vouchersPage = loginPage.login(user.getLogin(), user.getPassword(), VouchersPage.class);
        Assert.assertFalse(vouchersPage.isToMeLinkDisplayed());
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates IC user U2 with Supply manager permissions
     * WHEN: U2 signs in to the application
     * THEN: U2 has an access to Supply Management part only
     * WHEN: U1 enables claim handler role for U2
     * THEN: U2 has an access to Supply Management and Matching Engine parts
     */
    @Test(dataProvider="testDataProvider", description = "CHARLIE-537 It's possible to update role for IC SM only. " +
            "CH role can be enabled. In this case user logins to ME")
    public void charlie537_updateICSMOnlyToCHType(SystemUser user) {
        login(getSystemUser(), UsersPage.class).
                toUserCreatePage().
                createUser(user, SUPPLYMANAGER).
                toMatchingEngine().
                getClaimMenu().
                logout().
                login(user.getLogin(), user.getPassword(), VouchersPage.class).
                signOut();
        login(getSystemUser(), UsersPage.class).
                filterByIC(user.getCompany()).
                openUserForEditing(user.getLogin()).
                enableCHType().
                selectSaveOption().
                toMatchingEngine().
                getClaimMenu().
                logout().
                login(user.getLogin(), user.getPassword(), MyPage.class);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates role Role1
     * THEN: Role1 is displayed in roles list
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-551 It's possible to create new role with all enabled options. The RR is displayed in Roles list")
    public void charlie551_createNewRoleAllOptionsEnabled(Roles role) {
        String roleName = role.getRoleName();
        boolean roleDisplayed = login(getSystemUser(), RolesPage.class).
                toAddRolePage().
                createNewRoleAllRolesEnabled(roleName).
                isRoleDisplayed(roleName);
        Assert.assertTrue(roleDisplayed);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates role Role1
     * WHEN: U1 creates new user
     * THEN: It's possible to select Role1
     * THEN: User1 is created successfully
     */
    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-551 It's possible to select new role for user creation. The new role is displayed on Add user page")
    public void charlie551_newRoleIsAvailableUserCreation(SystemUser user, Roles role) throws Exception {
        String roleName = role.getRoleName();
        boolean userDisplayed = login(getSystemUser(), RolesPage.class).
                toAddRolePage().
                createNewRoleAllRolesEnabled(roleName).
                to(AdminPage.class).
                toUsersPage().
                toUserCreatePage().
                createNewSPAdminNewRole(user, role).
                isUserDisplayed(user);
        Assert.assertTrue(userDisplayed);
    }
}

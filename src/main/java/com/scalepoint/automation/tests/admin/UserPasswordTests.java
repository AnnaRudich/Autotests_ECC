package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.EditPreferencesPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.SystemUser;
import com.scalepoint.automation.utils.data.entity.input.UserPasswordRules;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

public class UserPasswordTests extends BaseTest {

    private static final String DEFAULT_PASSWORD_RULE_DATA_PROVIDER = "defaultPasswordRuleDataProvider";
    private static final String PASSWORD_RULE_DATA_PROVIDER = "passwordRuleDataProvider";
    private static final String GENERATE_PASSWORD_FOR_NEW_USER_DATA_PROVIDER = "generatePasswordForNewUserDataProvider";
    private static final String GENERATE_PASSWORD_FOR_EXISTING_USER_DATA_PROVIDER = "generatePasswordForExistingUserDataProvider";
    private static final String GENERATE_PASSWORD_FROM_PREFERENCES_DATA_PROVIDER = "generatePasswordFromPreferencesDataProvider";

    @BeforeMethod
    public void toEditReasonPage(Object[] objects) {

        login(getSystemUser(), UsersPage.class)
                .toUserCreatePage();
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_PASSWORD},
            dataProvider = GENERATE_PASSWORD_FOR_NEW_USER_DATA_PROVIDER,
            description = "CHARLIE-534 generate password works for new user")
    public void generatePasswordForNewUserTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        UserAddEditPage userAddEditPage = at(UserAddEditPage.class)
                .createUserWithoutSaving(user, userTypes);

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_PASSWORD},
            dataProvider = GENERATE_PASSWORD_FOR_EXISTING_USER_DATA_PROVIDER,
            description = "CHARLIE-534 generate password works for existing")
    public void generatePasswordForExistingUserTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        UserAddEditPage userAddEditPage = at(UserAddEditPage.class)
                .createUser(user, userTypes)
                .filterByIC(user.getCompany())
                .openUserForEditing(user.getLogin());

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_PASSWORD},
            dataProvider = GENERATE_PASSWORD_FROM_PREFERENCES_DATA_PROVIDER,
            description = "CHARLIE-534 generate password from prefs")
    public void generatePasswordFromPreferencesTest(SystemUser user, UserAddEditPage.UserType[] userTypes) {

        EditPreferencesPage editPreferencesPage = at(UserAddEditPage.class)
                .createUser(user, userTypes)
                .toMatchingEngine()
                .openEditPreferences();

        editPreferencesPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertsIsGeneratedPasswordCorrect(editPreferencesPage.generateNewPassword());
        });
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_PASSWORD, UserCompanyGroups.SCALEPOINT},
            dataProvider = PASSWORD_RULE_DATA_PROVIDER,
            description = "Check basic password rule")
    public void passwordRuleTest(SystemUser systemUser, String wrongFormatPassword, UserAddEditPage.UserType[] userTypes) {

        trySetPassword(systemUser, wrongFormatPassword, userTypes);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.USER_PASSWORD, UserCompanyGroups.SCALEPOINT},
            dataProvider = DEFAULT_PASSWORD_RULE_DATA_PROVIDER,
            description = "Check basic password rule")
    public void defaultPasswordRuleTest(SystemUser systemUser, String password, UserAddEditPage.UserType[] userTypes) {

        systemUser.setPassword(password);

        at(UserAddEditPage.class).createUser(systemUser, userTypes)
                .doAssert(usersPage -> usersPage.assertUserExists(systemUser))
                .logout()
                .login(User.builder()
                        .login(systemUser.getLogin())
                        .password(systemUser.getPassword())
                        .type(User.UserType.BASIC)
                        .build(), MyPage.class);
    }

    @DataProvider(name = GENERATE_PASSWORD_FOR_NEW_USER_DATA_PROVIDER)
    public static Object[][] generatePasswordForNewUserDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = GENERATE_PASSWORD_FOR_EXISTING_USER_DATA_PROVIDER)
    public static Object[][] generatePasswordForExistingUserDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = GENERATE_PASSWORD_FROM_PREFERENCES_DATA_PROVIDER)
    public static Object[][] generatePasswordFromPreferencesDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, USER_ALL_ROLES}
        };
    }

    @DataProvider(name = PASSWORD_RULE_DATA_PROVIDER)
    public static Object[][] passwordRuleDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);
        UserPasswordRules userPasswordRules = TestData.getUserPasswordRules();

        String loginAsPartOf = systemUser.getLogin() + userPasswordRules.getLoginAsPartOf();

        return new Object[][]{
                {systemUser, userPasswordRules.getOnlySmallLetters(), USER_ALL_ROLES},
                {systemUser, loginAsPartOf, USER_ALL_ROLES},
                {systemUser, userPasswordRules.getMissingSpecialSymbol(), USER_ALL_ROLES}
        };
    }

    @DataProvider(name = DEFAULT_PASSWORD_RULE_DATA_PROVIDER)
    public static Object[][] defaultPasswordRuleDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        SystemUser systemUser = getLisOfObjectByClass(parameters, SystemUser.class).get(0);

        return new Object[][]{
                {systemUser, DEFAULT_USER_PASSWORD, USER_ALL_ROLES}
        };
    }


    private void trySetPassword(SystemUser systemUser, String text, UserAddEditPage.UserType[] userTypes) {

        systemUser.setPassword(text);

        at(UserAddEditPage.class).createUserWithoutSaving(systemUser, userTypes)
                .selectSaveOption(UserAddEditPage.class)
                .doAssert(UserAddEditPage.Asserts::assertIsAlertPresent);
    }
}

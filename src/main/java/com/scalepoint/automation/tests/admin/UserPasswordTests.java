package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.EditPreferencesPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Constants.ALL_ROLES;

public class UserPasswordTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password works for new user")
    public void charlie534_generatePasswordForNewUser(SystemUser user) {
        UserAddEditPage userAddEditPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUserWithoutSaving(user, ALL_ROLES);

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password works for existing")
    public void charlie534_generatePasswordForExistingUser(SystemUser user) {
        UserAddEditPage userAddEditPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, ALL_ROLES)
                .filterByIC(user.getCompany())
                .openUserForEditing(user.getLogin());

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password from prefs")
    public void charlie534_generatePasswordFromPreferences(SystemUser user) {
        EditPreferencesPage editPreferencesPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, ALL_ROLES)
                .toMatchingEngine()
                .openEditPreferences();

        editPreferencesPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertsIsGeneratedPasswordCorrect(editPreferencesPage.generateNewPassword());
        });
    }

    @Test(dataProvider = "testDataProvider", description = "Check basic password rule")
    public void charlie528_defaultPasswordRule(@UserCompany(CompanyCode.SCALEPOINT) User user, SystemUser systemUser) {
        UserAddEditPage userAddEditPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage();

        trySetPassword(systemUser, userAddEditPage, "qwertyuio");
        trySetPassword(systemUser, userAddEditPage, systemUser.getLogin() + "333");
        trySetPassword(systemUser, userAddEditPage, "DuapDuap321");

        systemUser.setPassword("dupaDupa(312");
        userAddEditPage.createUser(systemUser, ALL_ROLES)
                .doAssert(usersPage -> usersPage.assertUserExists(systemUser))
                .logout()
                .login(systemUser.getLogin(), systemUser.getPassword(), MyPage.class);
    }

    private void trySetPassword(SystemUser systemUser, UserAddEditPage userAddEditPage, String text) {
        systemUser.setPassword(text);
        userAddEditPage.createUserWithoutSaving(systemUser, ALL_ROLES)
                .selectSaveOption(UserAddEditPage.class)
                .doAssert(UserAddEditPage.Asserts::assertIsAlertPresent);
    }
}

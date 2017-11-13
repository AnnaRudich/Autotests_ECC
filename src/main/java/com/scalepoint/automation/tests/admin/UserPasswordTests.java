package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.EditPreferencesPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UsersPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.SystemUser;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Constants.ALL_ROLES;

/**
 * Created by bza on 11/13/2017.
 */
public class UserPasswordTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password works for new user")
    public void charlie534_generatePasswordForNewUser(SystemUser user){
        UserAddEditPage userAddEditPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUserWithoutSaving(user, ALL_ROLES);

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertsIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password works for existing")
    public void charlie534_generatePasswordForExistingUser(SystemUser user){
        UserAddEditPage userAddEditPage = login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(user, ALL_ROLES)
                .filterByIC(user.getCompany())
                .openUserForEditing(user.getLogin());

        userAddEditPage.doAssert(asserts -> {
            asserts.assertIsGenerateButtonVisible();
            asserts.assertsIsGeneratedPasswordCorrect(userAddEditPage.generateAndGetNewPassword());
        });
    }

    @Test(dataProvider = "testDataProvider",
            description = "CHARLIE-534 generate password from prefs")
    public void charlie534_generatePasswordFromPreferences(SystemUser user){
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

    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.USER_PASSWORD_VALIDATION_STRATEGY, value = "Basic")
    @Test(dataProvider = "testDataProvider", description = "Check basic password rule")
    public void charlie528_basicPasswordRule(@UserCompany(CompanyCode.SCALEPOINT) User user, SystemUser systemUser){
        systemUser.setPassword("qwertyuiop");

        login(getSystemUser(), UsersPage.class)
                .toUserCreatePage()
                .createUser(systemUser, ALL_ROLES)
                .doAssert(usersPage->usersPage.assertUserExists(systemUser))
                .logout()
                .login(systemUser.getLogin(), systemUser.getPassword(), MyPage.class);
    }
}

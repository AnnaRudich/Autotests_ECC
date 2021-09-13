package com.scalepoint.automation.tests;

import com.scalepoint.automation.exceptions.LoginInvalidException;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Collections;

public class LoginTests extends BaseTest {

    private static final String INVALID_DATA_CREDENTIALS_DATA_PROVIDER = "invalidCredentialsDataProvider";
    private static final String USERS_CREDENTIAL_DATA_PROVIDER = "usersCredentialDataProvider";

    @Test(groups = {TestGroups.LOGIN}, description = "It is not possible to sign in with blank credentials",
            expectedExceptions = {LoginInvalidException.class},
            dataProvider = INVALID_DATA_CREDENTIALS_DATA_PROVIDER)
    public void invalidCredentialsLoginTest(User user) {
        Page.to(LoginPage.class)
                .login(user);
    }

    @Test(groups = {TestGroups.LOGIN}, description = "Login different user types",
            dataProvider = USERS_CREDENTIAL_DATA_PROVIDER)
    public void differentUserTypesLoginTest(User user){

        Page.to(LoginPage.class)
                .login(user, MyPage.class);
    }

    @DataProvider(name = INVALID_DATA_CREDENTIALS_DATA_PROVIDER)
    public Object[][] invalidCredentialsDataProvider() {
        return new Object[][]{

                {new User("", "")},
                {new User("wrong", "wrong")}
        };
    }

    @DataProvider(name = USERS_CREDENTIAL_DATA_PROVIDER)
    public Object[][] usersCredentialDataProvider(Method method) {

        Object[][] objects = new Object[][]{

                {UsersManager.fetchUsersWhenAvailable(Collections.singletonList(new UsersManager.RequestedUserAttributes(CompanyCode.TOPDANMARK, User.UserType.SCALEPOINT_ID))).iterator().next()},
                TestDataActions.getTestDataParameters(method).toArray()
        };

        return objects;
    }
}

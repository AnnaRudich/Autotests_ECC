package com.scalepoint.automation.tests;

import com.scalepoint.automation.exceptions.LoginInvalidException;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(groups = {TestGroups.LOGIN}, description = "It is not possible to sign in with blank credentials",
            expectedExceptions = {LoginInvalidException.class},
            dataProvider = "credentials")
    public void charlie553_invalidLogin(User user) {
        Page.to(LoginPage.class).login(user);
    }

    @DataProvider(name = "credentials")
    public Object[][] credentials() {
        return new Object[][]{
                {new User("", "")},
                {new User("wrong", "wrong")}
        };
    }

}

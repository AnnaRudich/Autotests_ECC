package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.FunctionalTemplatesPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-555")
public class FuncTemplatesScalepointIdTests extends BaseTest {

    private static final String CREATE_NEW_FT_DATA_PROVIDER = "createNewFtDataProvider";
    private static final String DELETE_NEW_FT_DATA_PROVIDER = "deleteNewFtDataProvider";
    private static final String EDIT_FT_DATA_PROVIDER = "editFtDataProvider";

    @BeforeMethod(alwaysRun = true)
    public void toFunctionalTemplatesPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        String ftName = getLisOfObjectByClass(parameters, String.class).get(0);
        User user = getLisOfObjectByClass(parameters, User.class).get(0);

        createNewTemplate(user, ftName);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES, TestGroups.SCALEPOINT_ID}, dataProvider = CREATE_NEW_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to create new ME_FT. New ME_FT is displayed in ME_FT list")
    public void createNewFtTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, String ftName) {

        Page.at(FunctionalTemplatesPage.class)
                .assertTemplateExists(ftName);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES, TestGroups.SCALEPOINT_ID}, dataProvider = DELETE_NEW_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to delete new ME_FT. New ME_FT is not displayed in ME_FT list")
    public void deleteNewFtTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, String ftName) {

        boolean deleted = Page.at(FunctionalTemplatesPage.class)
                .delete(ftName);
        Assert.assertTrue(deleted, "Template can't be deleted");
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES, TestGroups.SCALEPOINT_ID}, dataProvider = EDIT_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to edit new ME_FT. Edited ME_FT is displayed in ME_FT list")
    public void editFtTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, String ftName, String ftNameUpdated) {

        Page.at(FunctionalTemplatesPage.class)
                .editTemplate(ftName)
                .setName(ftNameUpdated)
                .saveTemplate()
                .assertTemplateExists(ftNameUpdated);
    }

    @DataProvider(name = CREATE_NEW_FT_DATA_PROVIDER)
    public static Object[][] createNewFtDataProvider(Method method) {

        String ftName = FT_TEMPLATE_NAME + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, ftName).toArray()
        };
    }

    @DataProvider(name = DELETE_NEW_FT_DATA_PROVIDER)
    public static Object[][] deleteNewFtDataProvider(Method method) {

        String ftName = FT_TEMPLATE_NAME + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, ftName).toArray()
        };
    }

    @DataProvider(name = EDIT_FT_DATA_PROVIDER)
    public static Object[][] editFtDataProvider(Method method) {

        String ftName = FT_TEMPLATE_NAME + System.currentTimeMillis();
        String ftNameUpdated = UPDATED_FT_TEMPLATE_NAME;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, ftName, ftNameUpdated).toArray()
        };
    }

    private FunctionalTemplatesPage createNewTemplate(User user, String ftName) {
        return loginFlow.login(user).
                to(FunctionalTemplatesPage.class).
                copyTemplate(DEFAULT_COPY_TEMPLATE).
                setName(ftName).
                saveTemplate();
    }
}

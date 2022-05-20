package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.FunctionalTemplatesPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.Jira;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-555")
public class FuncTemplatesTests extends BaseUITest {

    private static final String CREATE_NEW_FT_DATA_PROVIDER = "createNewFtDataProvider";
    private static final String DELETE_NEW_FT_DATA_PROVIDER = "deleteNewFtDataProvider";
    private static final String EDIT_FT_DATA_PROVIDER = "editFtDataProvider";

    @BeforeMethod(alwaysRun = true)
    public void toFunctionalTemplatesPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        String ftName = getLisOfObjectByClass(parameters, String.class).get(0);

        createNewTemplate(ftName);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, dataProvider = CREATE_NEW_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to create new ME_FT. New ME_FT is displayed in ME_FT list")
    public void createNewFtTest(String ftName) {

        Page.at(FunctionalTemplatesPage.class)
                .assertTemplateExists(ftName);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, dataProvider = DELETE_NEW_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to delete new ME_FT. New ME_FT is not displayed in ME_FT list")
    public void deleteNewFtTest(String ftName) {

        boolean deleted = Page.at(FunctionalTemplatesPage.class)
                .delete(ftName);
        Assert.assertTrue(deleted, "Template can't be deleted");
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.FUNC_TEMPLATES}, dataProvider = EDIT_FT_DATA_PROVIDER,
            description = "CHARLIE-555 It's possible to edit new ME_FT. Edited ME_FT is displayed in ME_FT list")
    public void editFtTest(String ftName, String ftNameUpdated) {

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
                new Object[]{ftName}
        };
    }

    @DataProvider(name = DELETE_NEW_FT_DATA_PROVIDER)
    public static Object[][] deleteNewFtDataProvider(Method method) {

        String ftName = FT_TEMPLATE_NAME + System.currentTimeMillis();

        return new Object[][]{
                new Object[]{ftName}
        };
    }

    @DataProvider(name = EDIT_FT_DATA_PROVIDER)
    public static Object[][] editFtDataProvider(Method method) {

        String ftName = FT_TEMPLATE_NAME + System.currentTimeMillis();
        String ftNameUpdated = UPDATED_FT_TEMPLATE_NAME;

        return new Object[][]{
                new Object[]{ftName, ftNameUpdated}
        };
    }

    private FunctionalTemplatesPage createNewTemplate(String ftName) {
        return loginFlow.login(getSystemUser()).
                to(FunctionalTemplatesPage.class).
                copyTemplate(DEFAULT_COPY_TEMPLATE).
                setName(ftName).
                saveTemplate();
    }
}

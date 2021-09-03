package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoriesPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoryGroupPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Category;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-545")
public class CategoryGroupsTests extends BaseTest {

    public static final String CREATE_NEW_PS_CAT_GROUP_DATA_PROVIDER = "createNewPsCatGroupDataProvider";
    public static final String CREATE_NEW_PS_CAT_WITH_NEW_GROUP_DATA_PROVIDER = "createNewPsCatWithNewGroupDataProvider";
    public static final String UPDATE_NEW_PS_CAT_DATA_PROVIDER = "updateNewPsCatDataProvider";
    public static final String UPDATE_NEW_PS_CAT_GROUP_DATA_PROVIDER = "updateNewPsCatGroupDataProvider";
    public static final String MOVE_CAT_TO_ANOTHER_GROUP_DATA_PROVIDER = "moveCatToAnotherGroupDataProvider";
    public static final String CREATE_NEW_PS_CAT_MODE_DATA_PROVIDER = "createNewPsCatModelDataProvider";
    public static final String UPDATE_NEW_PS_CAT_MODE_DATA_PROVIDER = "updateNewPsCatModelDataProvider";
    public static final String REMOVE_NEW_PS_CAT_MODEL_DATA_PROVIDER = "removeNewPsCatModelDataProvider";

    @BeforeMethod
    public void toAdminPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);

        login(user, AdminPage.class);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Pseudo Category Group G1
     * THEN: G1 is displayed in group's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = CREATE_NEW_PS_CAT_GROUP_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to create new Pseudo Category Group. New Group is displayed in Group list")
    public void createNewPsCatGroupTest(User user, Category category) {

        createPseudoCategoryGroup(category)
                .assertGroupDisplayed(category.getGroupName());
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Pseudo Category Group G1
     * THEN: G1 can be used for new pseudo category
     * WHEN: U1 creates Pseudo Category C1
     * THEN: C1 is displayed in category's list
     */
    @Test(groups ={TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = CREATE_NEW_PS_CAT_WITH_NEW_GROUP_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to create new Pseudo Category with new pseudo category group. New Category is displayed in Category list")
    public void createNewPsCatWithNewGroupTest(User user, Category category) {

        createPseudoCategoryGroup(category)
                .to(PseudoCategoriesPage.class)
                .toAddCategoryPage()
                .addCategory(category)
                .assertCategoryDisplayed(category.getCategoryName());
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category C1
     * WHEN: U1 updates C1 data
     * THEN: New C1 data is stored correctly
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = UPDATE_NEW_PS_CAT_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to update new Pseudo Category")
    public void updateNewPsCatTest(User user, Category category, String updatedName) {

        createPseudoCategoryGroup(category)
                .to(PseudoCategoriesPage.class)
                .toAddCategoryPage()
                .addCategory(category)
                .editCategory(category.getCategoryName())
                .updateNameAndSave(updatedName)
                .assertCategoryDisplayed(updatedName);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Group G1
     * WHEN: U1 updates G1 data
     * THEN: New G1 data is stored correctly
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = UPDATE_NEW_PS_CAT_GROUP_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to update new Pseudo Category Group")
    public void updateNewPsCatGroupTest(User user, Category category, String updatedName) {

        createPseudoCategoryGroup(category)
                .editGroup(category.getGroupName())
                .updateNameAndSave(updatedName)
                .assertGroupDisplayed(updatedName);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Groups G1, G2
     * GIVEN: Pseudo Category C1 mapped with G1
     * WHEN: U1 moves C1 to G2
     * THEN: C1 mapped with G2
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = MOVE_CAT_TO_ANOTHER_GROUP_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to move category from one group to another")
    public void moveCatToAnotherGroupTest(User user, Category categoryToMove, Category category2) {

        String sourceGroup = categoryToMove.getGroupName();
        String targetGroup = category2.getGroupName();

        Page.at(AdminPage.class).
                toPseudoCategoryGroupPage().toAddGroupPage()
                .addGroup(sourceGroup)
                .toAddGroupPage()
                .addGroup(targetGroup)
                .to(PseudoCategoriesPage.class)
                .toAddCategoryPage()
                .addCategory(categoryToMove)
                .to(PseudoCategoryGroupPage.class)
                .editGroup(sourceGroup)
                .toMoveToGroupPage(categoryToMove.getCategoryName())
                .moveToGroup(targetGroup)
                .to(PseudoCategoryGroupPage.class)
                .editGroup(targetGroup)
                .assertGroupDisplayed(categoryToMove.getCategoryName());
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Pseudo Category Model M1
     * THEN: M1 is displayed in model's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = CREATE_NEW_PS_CAT_MODE_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to create new Pseudo Category Model. New Model is displayed in Models list")
    public void createNewPsCatModelTest(User user, Category category) {

        String modelName = category.getModelName();

        Page.at(AdminPage.class)
                .toPseudoCategoryModelPage()
                .toAddModelPage()
                .updateNameAndSave(modelName)
                .assertModelDisplayed(modelName);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Model M1
     * WHEN: U1 updates M1
     * THEN: M1 data is stored correctly
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = UPDATE_NEW_PS_CAT_MODE_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to update new Pseudo Category Model. Updated Model is displayed in Models list")
    public void updateNewPsCatModelTest(User user, Category category, String updatedName) {

        String initialModelName = category.getModelName();

        Page.at(AdminPage.class)
                .toPseudoCategoryModelPage()
                .toAddModelPage()
                .updateNameAndSave(initialModelName)
                .toEditPage(initialModelName)
                .updateNewModelNameAndSave(updatedName)
                .assertModelDisplayed(updatedName);
    }
    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Model M1
     * WHEN: U1 removes M1
     * THEN: M1 is not displayed in model's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS},
            dataProvider = REMOVE_NEW_PS_CAT_MODEL_DATA_PROVIDER,
            description = "CHARLIE-545 It's possible to remove new Pseudo Category Model. Removed Model is not displayed in Models list")
    public void removeNewPsCatModelTest(User user, Category category) {

        String modelName = category.getModelName();

        Page.at(AdminPage.class)
                .toPseudoCategoryModelPage()
                .toAddModelPage()
                .updateNameAndSave(modelName)
                .selectModelToRemoveAndRemove(modelName)
                .assertModelNotDisplayed(modelName);
    }

    @DataProvider(name = CREATE_NEW_PS_CAT_GROUP_DATA_PROVIDER)
    public static Object[][] createNewPsCatGroupDataProvider(Method method) {

        return provide(method);
    }

    @DataProvider(name = CREATE_NEW_PS_CAT_WITH_NEW_GROUP_DATA_PROVIDER)
    public static Object[][] createNewPsCatWithNewGroupDataProvider(Method method) {

        return provide(method);
    }

    @DataProvider(name = UPDATE_NEW_PS_CAT_DATA_PROVIDER)
    public static Object[][] updateNewPsCatDataProvider(Method method) {

        String updatedName = UPDATED_LINE_DESCRIPTION + System.currentTimeMillis();

        return addNewParameters(TestDataActions.getTestDataParameters(method), updatedName);
    }

    @DataProvider(name = UPDATE_NEW_PS_CAT_GROUP_DATA_PROVIDER)
    public static Object[][] updateNewPsCatGroupDataProvider(Method method) {

        String updatedName = UPDATED_LINE_DESCRIPTION + System.currentTimeMillis();

        return addNewParameters(TestDataActions.getTestDataParameters(method), updatedName);
    }

    @DataProvider(name = MOVE_CAT_TO_ANOTHER_GROUP_DATA_PROVIDER)
    public static Object[][] moveCatToAnotherGroupDataProvider(Method method) {

        return provide(method);
    }

    @DataProvider(name = CREATE_NEW_PS_CAT_MODE_DATA_PROVIDER)
    public static Object[][] createNewPsCatModelDataProvider(Method method) {

        return provide(method);
    }

    @DataProvider(name = UPDATE_NEW_PS_CAT_MODE_DATA_PROVIDER)
    public static Object[][] updateNewPsCatModelDataProvider(Method method) {

        String updatedName = UPDATED_LINE_DESCRIPTION + System.currentTimeMillis();

        return addNewParameters(TestDataActions.getTestDataParameters(method), updatedName);
    }

    @DataProvider(name = REMOVE_NEW_PS_CAT_MODEL_DATA_PROVIDER)
    public static Object[][] removeNewPsCatModelDataProvider(Method method) {

        return provide(method);
    }

    private PseudoCategoryGroupPage createPseudoCategoryGroup(Category category) {

        return Page.at(AdminPage.class)
                .toPseudoCategoryGroupPage()
                .toAddGroupPage()
                .addGroup(category.getGroupName());
    }
}

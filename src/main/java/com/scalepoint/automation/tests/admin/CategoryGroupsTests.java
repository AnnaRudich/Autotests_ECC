package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoriesPage;
import com.scalepoint.automation.pageobjects.pages.admin.PseudoCategoryGroupPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.entity.input.Category;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-545")
public class CategoryGroupsTests extends BaseTest {
    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Pseudo Category Group G1
     * THEN: G1 is displayed in group's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to create new Pseudo Category Group. New Group is displayed in Group list")
    public void charlie545_createNewPsCatGroup(User user, Category category) {
        createPseudoCategoryGroup(user, category)
                .assertGroupDisplayed(category.getGroupName());
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * WHEN: U1 creates Pseudo Category Group G1
     * THEN: G1 can be used for new pseudo category
     * WHEN: U1 creates Pseudo Category C1
     * THEN: C1 is displayed in category's list
     */
    @Test(groups ={TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to create new Pseudo Category with new pseudo category group. New Category is displayed in Category list")
    public void charlie545_createNewPsCatWithNewGroup(User user, Category category) {
        createPseudoCategoryGroup(user, category)
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
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider", description = "CHARLIE-545 It's possible to update new Pseudo Category")
    public void charlie545_updateNewPsCat(User user, Category category) {
        String newCategoryName = "Updated-" + System.currentTimeMillis();
        createPseudoCategoryGroup(user, category)
                .to(PseudoCategoriesPage.class)
                .toAddCategoryPage()
                .addCategory(category)
                .editCategory(category.getCategoryName())
                .updateNameAndSave(newCategoryName)
                .assertCategoryDisplayed(newCategoryName);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Group G1
     * WHEN: U1 updates G1 data
     * THEN: New G1 data is stored correctly
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to update new Pseudo Category Group")
    public void charlie545_updateNewPsCatGroup(User user, Category category) {
        String newGroupName = "Updated-" + System.currentTimeMillis();
        createPseudoCategoryGroup(user, category)
                .editGroup(category.getGroupName())
                .updateNameAndSave(newGroupName)
                .assertGroupDisplayed(newGroupName);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Groups G1, G2
     * GIVEN: Pseudo Category C1 mapped with G1
     * WHEN: U1 moves C1 to G2
     * THEN: C1 mapped with G2
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to move category from one group to another")
    public void charlie545_moveCatToAnotherGroup(User user, Category categoryToMove, Category category2) {
        PseudoCategoryGroupPage groupPage = login(user, AdminPage.class).
                toPseudoCategoryGroupPage();

        String sourceGroup = categoryToMove.getGroupName();
        String targetGroup = category2.getGroupName();
        groupPage.toAddGroupPage()
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
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to create new Pseudo Category Model. New Model is displayed in Models list")
    public void charlie545_createNewPsCatModel(User user, Category category) {
        String modelName = category.getModelName();
        login(user, AdminPage.class)
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
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to update new Pseudo Category Model. Updated Model is displayed in Models list")
    public void charlie545_updateNewPsCatModel(User user, Category category) {
        String initialModelName = category.getModelName();
        String newModelName = "Updated-" + System.currentTimeMillis();
        login(user, AdminPage.class)
                .toPseudoCategoryModelPage()
                .toAddModelPage()
                .updateNameAndSave(initialModelName)
                .toEditPage(initialModelName)
                .updateNewModelNameAndSave(newModelName)
                .assertModelDisplayed(newModelName);
    }

    /**
     * GIVEN: SP user U1 with Admin permissions
     * GIVEN: Pseudo Category Model M1
     * WHEN: U1 removes M1
     * THEN: M1 is not displayed in model's list
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.CATEGORY_GROUPS}, dataProvider = "testDataProvider",
            description = "CHARLIE-545 It's possible to remove new Pseudo Category Model. Removed Model is not displayed in Models list")
    public void charlie545_removeNewPsCatModel(User user, Category category) {
        String modelName = category.getModelName();
        login(user, AdminPage.class)
                .toPseudoCategoryModelPage()
                .toAddModelPage()
                .updateNameAndSave(modelName)
                .selectModelToRemoveAndRemove(modelName)
                .assertModelNotDisplayed(modelName);
    }

    private PseudoCategoryGroupPage createPseudoCategoryGroup(User user, Category category) {
        return login(user, AdminPage.class)
                .toPseudoCategoryGroupPage()
                .toAddGroupPage()
                .addGroup(category.getGroupName());
    }


}

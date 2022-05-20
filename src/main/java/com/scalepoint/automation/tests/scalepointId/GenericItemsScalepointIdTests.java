package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.pageobjects.pages.Page.to;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-535")
public class GenericItemsScalepointIdTests extends BaseUITest {

    @BeforeMethod(alwaysRun = true)
    public void toGenericItemsAdminPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        Claim claim = getLisOfObjectByClass(parameters, Claim.class).get(0);

        loginFlow.loginAndCreateClaim(user, claim)
                .to(GenericItemsAdminPage.class);
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.GENERIC_ITEMS, TestGroups.SCALEPOINT_ID},
            dataProvider = TEST_DATA_PROVIDER, description = "CHARLIE-535 Insert/Update generic item")
    public void charlie535_testWeCanManageGenericItems(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, Claim claim, GenericItem genericItem, GenericItem newGenericItem) {
        String companyName = user.getCompanyName();
        String newDescription = newGenericItem.getName();

        at(GenericItemsAdminPage.class)
                .refreshList()
                .doAssert(GenericItemsAdminPage.Asserts::assertItemsListIsNotEmpty)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, companyName, true)
                .to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .doAssert(spage -> spage.assertItemIsPresent(genericItem.getName()));

        to(GenericItemsAdminPage.class)
                .editItem(genericItem, companyName)
                .setDescription(newDescription)
                .save();

        genericItem.setName(newDescription);

        to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .doAssert(spage -> spage.assertItemIsPresent(genericItem.getName()));
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.GENERIC_ITEMS, TestGroups.SCALEPOINT_ID},
            dataProvider = TEST_DATA_PROVIDER, description = "CHARLIE-535 Publish/Unpublish generic item")
    public void charlie535_testWeCanPublishGenericItems(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, Claim claim, GenericItem genericItem) {
        String companyName = user.getCompanyName();

        at(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, companyName, false)
                .to(SettlementPage.class)
                .doAssert(spage -> spage.assertGenericItemIsNotPresent(genericItem))
                .to(GenericItemsAdminPage.class)
                .editItem(genericItem, companyName)
                .publish(true)
                .save();

        to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .doAssert(spage -> spage.assertItemIsPresent(genericItem.getName()));
    }

    @Test(groups = {TestGroups.ADMIN, TestGroups.GENERIC_ITEMS, TestGroups.SCALEPOINT_ID},
            dataProvider = TEST_DATA_PROVIDER, description = "CHARLIE-535 Delete generic item")
    public void charlie535_testWeCanDeleteGenericItem(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID)User user, Claim claim,
                                                      GenericItem genericItem, GenericItem genericItemToDelete) {
        String companyName = user.getCompanyName();

        at(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, companyName, true)
                .to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .to(GenericItemsAdminPage.class)
                .deleteItem(genericItem, companyName)
                .doAssert(genericItemPage -> genericItemPage.assertGenericItemInList(genericItem.getName()))
                .clickCreateNewItem()
                .addNewGenericItem(genericItemToDelete, companyName, true)
                .deleteItem(genericItemToDelete, companyName)
                .doAssert(genericItemPage -> genericItemPage.assertGenericItemNotInList(genericItemToDelete.getName()));
    }
}

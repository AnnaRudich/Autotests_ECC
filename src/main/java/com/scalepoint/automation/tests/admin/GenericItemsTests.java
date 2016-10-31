package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.AddGenericItemDialog;
import com.scalepoint.automation.pageobjects.modules.ClaimOperationsMenu;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.testng.Retrier;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

@SuppressWarnings("AccessStaticViaInstance")
public class GenericItemsTests extends BaseTest {

  /*  @Test(dataProvider = "testDataProvider", description = "CHARLIE-535 Insert/Update generic item")
    public void test(User user, Claim claim, GenericItem genericItem) throws InterruptedException {
        ClaimOperationsMenu claimOperationsMenu = loginAndCreateClaim(user, claim).
                getClaimOperationsMenu();

        for (int i = 0; i < 10; i++) {
            AddGenericItemDialog  dialog = claimOperationsMenu.
                    addGenericItem().
                    selectCategory(genericItem.getGroup(), genericItem.getCategory());
            Thread.sleep(3000);
            dialog.close();
        }
    }*/


    @Test(dataProvider = "testDataProvider", description = "CHARLIE-535 Insert/Update generic item", retryAnalyzer = Retrier.class)
    public void charlie535_testWeCanManageGenericItems(User user, Claim claim, GenericItem genericItem) {
        String companyName = user.getCompanyName();

        GenericItemsAdminPage genericItemsAdminPage = loginAndCreateClaim(user, claim).
                to(GenericItemsAdminPage.class).
                refreshList().
                assertItemsListIsNotEmpty();

        //Create new item and check we are able to add it to settlement
        SettlementPage settlementPage = genericItemsAdminPage.
                clickCreateNewItem().
                addNewGenericItem(genericItem, companyName, true).
                to(SettlementPage.class).
                addGenericItemToClaim(genericItem).
                assertItemIsPresent(genericItem.getName());

        //Update generic item name and make sure the update is visible in settlement
        String newDescription = genericItem.getName() + "-UPDATED";

        settlementPage.
                to(GenericItemsAdminPage.class).
                editItem(genericItem, companyName).
                setDescription(newDescription).
                save();

        genericItem.setName(newDescription);

        to(SettlementPage.class).
                addGenericItemToClaim(genericItem).
                assertItemIsPresent(genericItem.getName());

    }

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-535 Publish/Unpublish generic item")
    public void charlie535_testWeCanPublishGenericItems(User user, Claim claim, GenericItem genericItem) {
        String companyName = user.getCompanyName();

        loginAndCreateClaim(user, claim).
                to(GenericItemsAdminPage.class).
                clickCreateNewItem().
                addNewGenericItem(genericItem, companyName, false).
                to(SettlementPage.class).
                assertGenericItemIsNotPresent(genericItem).
                to(GenericItemsAdminPage.class).
                editItem(genericItem, companyName).
                publish(true).
                save();

        Page.to(SettlementPage.class).
                addGenericItemToClaim(genericItem).
                assertItemIsPresent(genericItem.getName());
    }

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-535 Delete generic item")
    public void charlie535_testWeCanDeleteGenericItem(User user, Claim claim, GenericItem genericItem) {
        String companyName = user.getCompanyName();

        GenericItem genericItemToDelete = TestData.getGenericItem();

        loginAndCreateClaim(user, claim).
                to(GenericItemsAdminPage.class).
                clickCreateNewItem().
                addNewGenericItem(genericItem, companyName, true).
                to(SettlementPage.class).
                addGenericItemToClaim(genericItem).
                to(GenericItemsAdminPage.class).
                deleteItem(genericItem, companyName).
                assertGenericItemInList(genericItem.getName()).
                clickCreateNewItem().
                addNewGenericItem(genericItemToDelete, companyName, true).
                deleteItem(genericItemToDelete, companyName).
                assertGenericItemNotInList(genericItemToDelete.getName());
    }
}

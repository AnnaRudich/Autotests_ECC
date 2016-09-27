package com.scalepoint.automation.tests.genericitems;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.GenericItemsAdminPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class GenericItemsTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Admin tab opens without errors")
    public void charlie535_testWeCanManageGenericItems(User user, Claim claim, GenericItem genericItem) {
        String genericItemName = genericItem.getItemDescription();
        String categoryGroup = genericItem.getGroup1();
        String category = genericItem.getCategory1();
        String companyName = user.getCompanyName();
        String price = "100";

        GenericItemsAdminPage genericItemsAdminPage = loginAndCreateClaim(user, claim).
                getMainMenu().
                toAdminPage().
                toGenericItemsPage().
                selectRefreshOption();
        assertTrue(genericItemsAdminPage.genericItemsListSize() > 0, "List of generic items is empty");

        //Create new item and check we are able to add it to settlement
        genericItemsAdminPage.
                selectNewOption().
                addNewGenericItem(categoryGroup, category, companyName, genericItemName, price);

        SettlementPage settlementPage = Page.to(SettlementPage.class).
                addGenericItem(genericItemName, categoryGroup, category);
        assertTrue(settlementPage.isItemPresent(genericItemName), "Generic item is not found");

        //Update generic item name and make sure the update is visible in settlement
        String description = genericItemName + "-UPDATED";
        settlementPage.
                toAdminPage().
                toGenericItemsPage().
                filterItems(companyName, categoryGroup, category).
                editItem(genericItemName, companyName).
                addDescription(description).
                selectSaveOption();

        settlementPage = Page.to(SettlementPage.class).
                addGenericItem(description, categoryGroup, category);
        assertTrue(settlementPage.isItemPresent(description), "Generic item is not found");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String test() {
        return "aaaaa";
    }
}

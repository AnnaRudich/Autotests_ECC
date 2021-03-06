package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.ShopsTab;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.data.entity.input.Shop;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.AddShopDialog.ShopType.RETAIL;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-507")
public class ShopTests extends BaseUITest {

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new shop Shop1 for random supplier
     * THEN: Shop1 is displayed in the list for random supplier
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SHOP}, dataProvider = "testDataProvider",
            description = "ECC-3036 It's possible to create new shop for existing supplier")
    public void ecc3036_createNewShop(Shop shop) {
        createRetailShop(shop)
                .doAssert(shopTab -> shopTab.assertNewShopExists(shop))
                .deleteShop(shop);
    }

    /**
     * GIVEN: Existing shop Shop1
     * WHEN: User updates all data for Shop1
     * THEN: Shop1 data is stored correctly
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SHOP}, dataProvider = "testDataProvider",
            description = "ECC-3036 It's possible to update all new shop data for existing supplier")
    public void ecc3036_updateNewShop(Shop shop, Shop updatedWithNewData) {
        createRetailShop(shop)
                .openEditShopDialog(shop.getShopName())
                .updateShop(updatedWithNewData)
                .openEditShopDialog(updatedWithNewData.getShopName())
                .assertShopDataIsEqualTo(updatedWithNewData)
                .cancel()
                .deleteShop(updatedWithNewData);
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates shop Shop1 for random supplier
     * WHEN: User removes Shop1
     * THEN: Shop1 is not displayed in the shops list for random supplier
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SHOP}, dataProvider = "testDataProvider",
            description = "ECC-3036 It's possible to delete new shop for existing supplier")
    public void ecc3036_deleteNewShop(Shop shop) {
        createRetailShop(shop)
                .deleteShop(shop)
                .doAssert(shopTab -> shopTab.assertNewShopNotExists(shop));
    }

    private ShopsTab createRetailShop(Shop shop) {
        return loginFlow
                .login(getSystemUser())
                .getMainMenu()
                .toEccAdminPage()
                .openFirstSupplier()
                .selectShopsTab()
                .openAddShopDialog()
                .createShop(shop, RETAIL, true);
    }
}

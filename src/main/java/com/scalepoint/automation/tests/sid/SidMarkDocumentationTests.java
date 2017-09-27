package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.SUFFICIENT_DOCUMENTATION_CHECKBOX;

/**
 * Created by bza on 9/26/2017.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class SidMarkDocumentationTests extends BaseTest {

    @RequiredSetting(type = SUFFICIENT_DOCUMENTATION_CHECKBOX)
    @Test(dataProvider = "testDataProvider", description = "Is sufficient documentation checkbox checked")
    public void test(User user, Claim claim, GenericItem genericItem){
        loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsCheecked
                )
                .cancel()
                .toTextSearchPage()
                .searchByProductName("samsung")
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsCheecked
                ).cancel(TextSearchPage.class)
                .toSettlementPage()
                .to(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, user.getCompanyName(), true)
                .to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .editFirstClaimLine()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsCheecked
                );
    }
}

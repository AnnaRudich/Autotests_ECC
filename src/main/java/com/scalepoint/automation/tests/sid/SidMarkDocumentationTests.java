package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.pageobjects.pages.admin.GenericItemsAdminPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.SUFFICIENT_DOCUMENTATION_CHECKBOX;

@SuppressWarnings("AccessStaticViaInstance")
public class SidMarkDocumentationTests extends BaseUITest {

    @RequiredSetting(type = SUFFICIENT_DOCUMENTATION_CHECKBOX)
    @Test(groups = {TestGroups.SID, TestGroups.SID_MARK_DOCUMENTATION},
            dataProvider = "testDataProvider",
            description = "Is sufficient documentation checkbox checked")
    public void charlie_547_sufficientDocumentationCheckboxShouldBeChecked(User user, Claim claim, GenericItem genericItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked
                )
                .cancel()
                .toTextSearchPage()
                .searchByProductName("samsung galaxy s7")
                .sortOrderableFirst()
                .openSidForFirstProduct()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked
                ).cancel(TextSearchPage.class)
                .toSettlementPage()
                .to(GenericItemsAdminPage.class)
                .clickCreateNewItem()
                .addNewGenericItem(genericItem, user.getCompanyName(), true)
                .to(SettlementPage.class)
                .addGenericItemToClaim(genericItem)
                .editFirstClaimLine()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked
                );
    }
    @RequiredSetting(type = SUFFICIENT_DOCUMENTATION_CHECKBOX)
    @Test(groups = {TestGroups.SID, TestGroups.SID_MARK_DOCUMENTATION},
            dataProvider = "testDataProvider",
            description = "Is sufficient documentation checkbox checked")
    public void charlie_547_sufficientDocumentationCheckboxShouldBeUnchecked(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsChecked
                )
                .uncheckedDocumentation()
                .setDescription(claimItem.getTextFieldSP())
                .fill(fill ->
                        fill.withCategory(claimItem.getCategoryBabyItems())
                                .withNewPrice(claimItem.getNewPriceSP())
                )
                .closeSidWithOk()
                .editFirstClaimLine()
                .doAssert(
                        SettlementDialog.Asserts::assertIsSufficientDocumentationCheckboxDisplayedAndItIsUnchecked
                );
    }
}

package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.PseudoCategory;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class BulkUpdateCategoryTests extends BaseTest {


    @Jira("https://jira.scalepoint.com/browse/CLAIMSHOP-4524")
    @Test(dataProvider = "testDataProvider")
    public void testBulkUpdateCategories(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        final String itemDescriptions[] = {"item1", "item2"};
        PseudoCategory mobilePhonesCategory = claimItem.getCategoryMobilePhones();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, itemDescriptions[0], itemDescriptions[1])
                .getToolBarMenu()
                .selectAll()
                .openUpdateCategoriesDialog()
                .toUpdateCategoriesDialog()
                .selectCategory(mobilePhonesCategory.getGroupName())
                .selectSubcategory(mobilePhonesCategory.getCategoryName())
                .closeUpdateCategoriesDialog();

        List<SettlementPage.ClaimLine> claimLines = settlementPage.getLinesByDescription(itemDescriptions);
        Assert.assertEquals(claimLines.get(0).getCategory(), mobilePhonesCategory.getGroupName() + " - " + mobilePhonesCategory.getCategoryName());
        Assert.assertEquals(claimLines.get(1).getCategory(), mobilePhonesCategory.getGroupName() + " - " + mobilePhonesCategory.getCategoryName());
    }
}

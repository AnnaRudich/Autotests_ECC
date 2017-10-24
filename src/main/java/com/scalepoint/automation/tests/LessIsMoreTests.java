package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Constants.PRICE_2400;

public class LessIsMoreTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Claim should have flat structure")
    public void charlie550_claimHaveFlatStructure(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView);
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroups(User user, Claim claim, ClaimItem claimItem){
        String groupName = "GroupName" + System.currentTimeMillis();
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByIndex(0,1)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });
    }

}

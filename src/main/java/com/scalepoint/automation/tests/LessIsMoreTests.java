package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.pages.SettlementGroupDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
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

    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroups(User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String[] descriptions = {"item1", "item2"};
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(descriptions[0])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText(descriptions[1])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByDescriptions(descriptions)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(groupName)
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(settlementPage.getLinesByDescription(descriptions).stream()
                            .mapToDouble(SettlementPage.ClaimLine::getReplacementPrice).sum());
                    asserts.assertQuantityIs(2);
                    asserts.assertAgeIs("-");
                    asserts.assertPurchasePriceIs(0.0);
                    asserts.assertDepreciationIs(-1);
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroupsValidateAgeAndQuantity(User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String[] descriptions = {"item1", "item2", "item3"};
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(descriptions[0])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withAge(1,1))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText(descriptions[1])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withAge(2,2))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText(descriptions[2])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByDescriptions(descriptions)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(groupName)
                .doAssert(asserts -> {
                    asserts.assertQuantityIs(3);
                    asserts.assertAgeIs("1/7");
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Create valuation group")
    public void charlie550_createValuationGroup(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String[] descriptions = {"item1"};
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(descriptions[0])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByDescriptions(descriptions)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .selectFirstReason()
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(groupName)
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(123456);
                });
        settlementPage.findClaimLine(descriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

}

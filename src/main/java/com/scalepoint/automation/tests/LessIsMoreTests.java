package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.pages.SettlementGroupDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
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

        settlementPage.findClaimLine(groupName)
                .editGroup()
                .doAssert(asserts -> {
                    asserts.assertAverageAgeIs("1/7");
                    asserts.assertIsOverviewChecked();
                    asserts.assertIsCustomerDemandFiledDisabled();
                    asserts.assertIsNewPriceFiledDisabled();
                    asserts.assertIsIncludeInClaimChecked();
                    asserts.assertIsReasonFiledDisabled();
                    asserts.assertIsShowLineAmountInMailChecked();
                    asserts.assertIsValuationFiledDisabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @Test(dataProvider = "testDataProvider", description = "Create valuation group")
    public void charlie550_createValuationGroup(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String[] descriptions = {"item1", "item2"};
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(descriptions[0])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withAge(2,2))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText(descriptions[1])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withAge(1,1))
                .closeSidWithOk()
                .selectLinesByDescriptions(descriptions)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .clickSave()
                .doAssert(asserts -> {
                    asserts.assertIsValuationRequired();
                    asserts.assertIsReasonRequired();
                })
                .enterValuation(1234.56)
                .selectFirstReason()
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(groupName)
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(1234.56);
                });

        settlementPage.findClaimLine(descriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
        settlementPage.findClaimLine(descriptions[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);

        settlementPage.findClaimLine(groupName)
                .editGroup()
                .doAssert(asserts -> {
                    asserts.assertAverageAgeIs("1/7");
                    asserts.assertIsValuationChecked();
                    asserts.assertIsCustomerDemandFiledEnabled();
                    asserts.assertIsNewPriceFiledEnabled();
                    asserts.assertIsIncludeInClaimChecked();
                    asserts.assertIsReasonFiledEnabled();
                    asserts.assertIsShowLineAmountInMailChecked();
                    asserts.assertIsValuationFiledEnabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "Check reason is not visible")
    public void charlie550_createValuationGroupWithoutReason(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String description = "item1";
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(description)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withAge(2,2))
                .closeSidWithOk()
                .selectLinesByDescriptions(description)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .doAssert(SettlementGroupDialog.Asserts::assertReasonIsNotVisible)
                .enterValuation(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(description)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryNewPrice(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String description = "item1";
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(description)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByDescriptions(description)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearNewPriceField()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertNewPriceIsRequired)
                .enterNewPrice(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                })
                .findClaimLine(description)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "CUSTOMER_DEMAND")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryCustomerDemand(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        String groupName = "GroupName" + System.currentTimeMillis();
        String description = "item1";
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(description)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .selectLinesByDescriptions(description)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearCustomerDemand()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertCustomerDemandIsRequired)
                .enterCustomerDemand(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                })
                .findClaimLine(description)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclude group from claim")
    public void charlie550_excludeGroupFromClaim(User user, Claim claim, ClaimItem claimItem){
        String groupName = "GroupName" + System.currentTimeMillis();
        String[] description = {"item1","item2"};
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(description[0])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText(description[1])
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr()))
                .closeSidWithOk()
                .openSidAndFill(sid -> sid
                        .withText("itemNotInGroup")
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getExistingCatWithoutVoucherAndSubCategory()))
                .closeSidWithOk()
                .selectLinesByDescriptions(description)
                .openGroupCreationDialog()
                .enterGroupName(groupName)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupName, claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.selectLinesByDescriptions(groupName)
                .rejectLines()
                .getSettlementSummary()
                .doAssert(asserts -> asserts.assertSubtotalSumValueIs(PRICE_2400));

        settlementPage.findClaimLine(groupName)
                .doAssert(asserts -> asserts.assertReplacementPriceIs(0.0));

        settlementPage.findClaimLine(description[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);

        settlementPage.findClaimLine(description[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);
    }
}

package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.dialogs.SettlementGroupDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.ClaimLineGroup;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.io.File;

import static com.scalepoint.automation.utils.Constants.PRICE_2400;


public class LessIsMoreTests extends BaseTest {

    private String groupDescription = "GroupName" + System.currentTimeMillis();
    private String[] lineDescriptions = new String[]{"item1", "item2", "item3"};

    @Test(dataProvider = "testDataProvider", description = "Claim should have flat structure")
    public void charlie550_claimHaveFlatStructure(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0])
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView);
    }

    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroups(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .selectLinesByDescriptions(lineDescriptions[0], lineDescriptions[1])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.findClaimLine(groupDescription)
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(settlementPage.getLinesByDescription(lineDescriptions[0], lineDescriptions[1]).stream()
                            .mapToDouble(SettlementPage.ClaimLine::getReplacementPrice).sum());
                    asserts.assertQuantityIs(2);
                    asserts.assertAgeIs("1/1");
                    asserts.assertPurchasePriceIs(0.0);
                    asserts.assertDepreciationIs(-1);
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroupsValidateAgeAndQuantity(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)

                .addLines(claimItem, lineDescriptions)
                .selectLinesByDescriptions(lineDescriptions)
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.findClaimLine(groupDescription)
                .doAssert(asserts -> {
                    asserts.assertQuantityIs(3);
                    asserts.assertAgeIs("1/1");
                });

        settlementPage.findClaimLine(groupDescription)
                .editGroup()
                .doAssert(asserts -> {
                    asserts.assertAverageAgeIs("1/1");
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
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .selectLinesByDescriptions(lineDescriptions[0], lineDescriptions[1])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
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
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.findClaimLine(groupDescription)
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(1234.56);
                });

        settlementPage.findClaimLine(lineDescriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
        settlementPage.findClaimLine(lineDescriptions[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);

        settlementPage.findClaimLine(groupDescription)
                .editGroup()
                .doAssert(asserts -> {
                    asserts.assertAverageAgeIs("1/1");
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
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0])
                .selectLinesByDescriptions(lineDescriptions[0])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .doAssert(SettlementGroupDialog.Asserts::assertReasonIsNotVisible)
                .enterValuation(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.findClaimLine(lineDescriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryNewPrice(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0])
                .selectLinesByDescriptions(lineDescriptions[0])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearNewPriceField()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertNewPriceIsRequired)
                .enterNewPrice(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                })
                .findClaimLine(lineDescriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "CUSTOMER_DEMAND")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryCustomerDemand(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0])
                .selectLinesByDescriptions(lineDescriptions[0])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearCustomerDemand()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertCustomerDemandIsRequired)
                .enterCustomerDemand(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                })
                .findClaimLine(lineDescriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclude group from claim")
    public void charlie550_excludeGroupFromClaim(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .addLines(claimItem, "itemNotInGroup")
                .selectLinesByDescriptions(lineDescriptions[0], lineDescriptions[1])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(lineDescriptions[0], lineDescriptions[1], claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.selectLinesByDescriptions(groupDescription)
                .rejectLines()
                .getSettlementSummary()
                .doAssert(asserts -> asserts.assertSubtotalSumValueIs(PRICE_2400 - PRICE_2400 * 0.1));//minus voucher depreciation, which is applied by default

        settlementPage.findClaimLine(groupDescription)
                .doAssert(asserts -> asserts.assertReplacementPriceIs(0.0));

        settlementPage.findClaimLine(lineDescriptions[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);

        settlementPage.findClaimLine(lineDescriptions[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);
    }
    @Test(dataProvider = "testDataProvider", description = "Move line from group to group")
    public void charlie550_dragAndDropFromGroupToGroup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])

                .selectLinesByDescriptions(lineDescriptions[0])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription + "#1")
                .saveGroup()
                .selectLinesByDescriptions(lineDescriptions[1])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription + "#2")
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription + "#1", groupDescription + "#2", claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.moveLineFromGroupToGroup(lineDescriptions[0], groupDescription + "#2")
                .findClaimLine(groupDescription + "#2")
                .doAssert(asserts -> {
                    asserts.assertQuantityIs(2);
                    asserts.assertReplacementPriceIs(settlementPage.getLinesByDescription(lineDescriptions[0], lineDescriptions[1]).stream()
                            .mapToDouble(SettlementPage.ClaimLine::getReplacementPrice).sum());
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Edit default group")
    public void charlie550_editDefaultGroup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .selectLinesByDescriptions(lineDescriptions[0])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(groupDescription, claimItem.getCategoryOther().getGroupName());
                });

        settlementPage.findClaimLine(claimItem.getCategoryOther().getGroupName())
                .editGroup()
                .enterGroupName(groupDescription + "#newDefault")
                .saveGroup()
                .doAssert(asserts -> asserts.assertSettlementContainsLinesWithDescriptions(groupDescription + "#newDefault"));
    }


    @Test(dataProvider = "testDataProvider", description = "Delete valuation group")
    public void charlie_550_deleteGroup(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, lineDescriptions[0], lineDescriptions[1])
                .selectLinesByDescriptions(lineDescriptions[0], lineDescriptions[1])
                .openGroupCreationDialog()
                .enterGroupName(groupDescription)
                .saveGroup()
                .selectLinesByDescriptions(groupDescription)
                .deleteGroup()

                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(lineDescriptions[0], lineDescriptions[1]);
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Excel import with grouping")
    public void charlie_550_excelImportWithGrouping(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimLineGroup claimLineGroup) {

        loginAndCreateClaim(user, claim)
                .importExcelFile(new File(claimLineGroup.getExcelWithGroupsFilePath()).getAbsolutePath())
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(claimLineGroup.getExcelLineGroups());
                });
    }
}

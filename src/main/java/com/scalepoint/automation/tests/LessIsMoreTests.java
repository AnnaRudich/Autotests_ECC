package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.pages.SettlementGroupDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.ClaimLineGroup;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Constants.*;


@RunOn(DriverType.CHROME)
public class LessIsMoreTests extends BaseTest {

    private String groupDescription;
    private String[] lineDescriptions;

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupDescription() {
        return this.groupDescription;
    }

    public void setLineDescriptions(String... lineDescriptions) {
        this.lineDescriptions = lineDescriptions;
    }

    public String[] getLineDescriptions() {
        return this.lineDescriptions;
    }


    @BeforeTest
    public void generateLineAndGroupDescriptions() {
        setGroupDescription("GroupName" + System.currentTimeMillis());
        setLineDescriptions("item1", "item2", "item3");
    }


    @Test(dataProvider = "testDataProvider", description = "Claim should have flat structure")
    public void charlie550_claimHaveFlatStructure(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0])
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView);
    }

    @Test(dataProvider = "testDataProvider", description = "Claim should have group view")
    public void charlie550_createGroups(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])
                .selectLinesByDescriptions(getLineDescriptions()[0], getLineDescriptions()[1])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(getGroupDescription())
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(settlementPage.getLinesByDescription(getLineDescriptions()[0], getLineDescriptions()[1]).stream()
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

                .addLines(claimItem, getLineDescriptions())
                .selectLinesByDescriptions(getLineDescriptions())
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(getGroupDescription())
                .doAssert(asserts -> {
                    asserts.assertQuantityIs(3);
                    asserts.assertAgeIs("1/1");
                });

        settlementPage.findClaimLine(getGroupDescription())
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
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])
                .selectLinesByDescriptions(getLineDescriptions()[0], getLineDescriptions()[1])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
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
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(getGroupDescription())
                .doAssert(asserts -> {
                    asserts.assertReplacementPriceIs(1234.56);
                });

        settlementPage.findClaimLine(getLineDescriptions()[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
        settlementPage.findClaimLine(getLineDescriptions()[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);

        settlementPage.findClaimLine(getGroupDescription())
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
               .addLines(claimItem, getLineDescriptions()[0])
                .selectLinesByDescriptions(getLineDescriptions()[0])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .doAssert(SettlementGroupDialog.Asserts::assertReasonIsNotVisible)
                .enterValuation(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(getLineDescriptions()[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "NEW_PRICE")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryNewPrice(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0])
                .selectLinesByDescriptions(getLineDescriptions()[0])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearNewPriceField()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertNewPriceIsRequired)
                .enterNewPrice(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                })
                .findClaimLine(getLineDescriptions()[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON, enabled = false)
    @RequiredSetting(type = FTSetting.REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, value = "CUSTOMER_DEMAND")
    @Test(dataProvider = "testDataProvider", description = "Check if new price is mandatory")
    public void charlie550_createValuationGroupWithMandatoryCustomerDemand(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0])
                .selectLinesByDescriptions(getLineDescriptions()[0])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .chooseType(SettlementGroupDialog.GroupTypes.VALUATION)
                .enterValuation(1234.56)
                .clearCustomerDemand()
                .clickSave()
                .doAssert(SettlementGroupDialog.Asserts::assertCustomerDemandIsRequired)
                .enterCustomerDemand(1234.56)
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                })
                .findClaimLine(getLineDescriptions()[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsCrossedOut);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclude group from claim")
    public void charlie550_excludeGroupFromClaim(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])
                .addLines(claimItem, "itemNotInGroup")
                .selectLinesByDescriptions(getLineDescriptions()[0], getLineDescriptions()[1])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getLineDescriptions()[0], getLineDescriptions()[1], claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.selectLinesByDescriptions(getGroupDescription())
                .rejectLines()
                .getSettlementSummary()
                .doAssert(asserts -> asserts.assertSubtotalSumValueIs(PRICE_2400-PRICE_2400*0.2));//minus voucher depreciation, which is applied by default

        settlementPage.findClaimLine(getGroupDescription())
                .doAssert(asserts -> asserts.assertReplacementPriceIs(0.0));

        settlementPage.findClaimLine(getLineDescriptions()[0])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);

        settlementPage.findClaimLine(getLineDescriptions()[1])
                .doAssert(SettlementPage.ClaimLine.Asserts::assertClaimLineIsRejected);
    }

    @Test(dataProvider = "testDataProvider", description = "Move line from group to group")
    public void charlie550_dragAndDropFromGroupToGroup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])

                .selectLinesByDescriptions(getLineDescriptions()[0])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription() + "#1")
                .saveGroup()
                .selectLinesByDescriptions(getLineDescriptions()[1])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription() + "#2")
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription() + "#1", getGroupDescription() + "#2", claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.moveLineFromGroupToGroup(getLineDescriptions()[0], getGroupDescription() + "#2")
                .findClaimLine(getGroupDescription() + "#2")
                .doAssert(asserts -> {
                    asserts.assertQuantityIs(2);
                    asserts.assertReplacementPriceIs(settlementPage.getLinesByDescription(getLineDescriptions()[0], getLineDescriptions()[1]).stream()
                            .mapToDouble(SettlementPage.ClaimLine::getReplacementPrice).sum());
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Edit default group")
    public void charlie550_editDefaultGroup(User user, Claim claim, ClaimItem claimItem) {
        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])
                .selectLinesByDescriptions(getLineDescriptions()[0])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .saveGroup()
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription(), claimItem.getExistingCatWithoutVoucherAndSubCategory());
                });

        settlementPage.findClaimLine(claimItem.getExistingCatWithoutVoucherAndSubCategory())
                .editGroup()
                .enterGroupName(getGroupDescription() + "#newDefault")
                .saveGroup()
                .doAssert(asserts -> asserts.assertSettlementContainsLinesWithDescriptions(getGroupDescription() + "#newDefault"));
    }


    @Test(dataProvider = "testDataProvider", description = "Delete valuation group")
    public void charlie_550_deleteGroup(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .addLines(claimItem, getLineDescriptions()[0], getLineDescriptions()[1])
                .selectLinesByDescriptions(getLineDescriptions()[0], getLineDescriptions()[1])
                .openGroupCreationDialog()
                .enterGroupName(getGroupDescription())
                .saveGroup()
                .selectLinesByDescriptions(getGroupDescription())
                .deleteGroup()

                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(getLineDescriptions()[0], getLineDescriptions()[1]);
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Excel import with grouping")
    public void charlie_550_excelImportWithGrouping(@UserCompany(value = CompanyCode.SCALEPOINT) User user, Claim claim, ClaimLineGroup claimLineGroup) {

        loginAndCreateClaim(user, claim)
                .importExcelFile(claimLineGroup.getExcelWithGroupsFilePath())
                .doAssert(asserts -> {
                    asserts.assertSettlementPageIsNotInFlatView();
                    asserts.assertSettlementContainsLinesWithDescriptions(claimLineGroup.getExcelLineGroups());
                });
    }
}

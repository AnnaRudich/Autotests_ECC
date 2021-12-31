package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.sharedTests.SidManualItemsSharedTests;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM;
import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class SidManualItemsForScalepointIdTests extends SidManualItemsSharedTests {

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify Include in claim option is ON")
    public void setIncludeInClaimCheckboxScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        setIncludeInClaimCheckboxSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it is possible to input Customer demand")
    public void inputCustomDemandScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        inputCustomDemandSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-536")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it is possible to input New price")
    public void inputNewPriceScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        inputNewPriceSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3953 Verify depreciation is not updated if type of depreciation is changed")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION)
    public void depreciationFromSuggestionShouldBeNotUpdatedAfterChangingScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        depreciationFromSuggestionShouldBeNotUpdatedAfterChangingSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-532")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3953 .doAssert(sid -> sid.assertDepreciationPercentageIs(\"10\"))")
    @RequiredSetting(type = FTSetting.SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED)
    @RequiredSetting(type = FTSetting.SHOW_SUGGESTED_DEPRECIATION_SECTION, enabled = false)
    public void depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSidScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        depreciationEnteredManuallyShouldBeNotUpdatedAfterActionsInSidSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it is possible to Save all results entered")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void saveAllEnteredResultsScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {

        saveAllEnteredResultsSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify clicking Cancel doesn't save entered info")
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void cancelEnteredResultsScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        cancelEnteredResultsSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it is possible to add new valuation")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    public void addNewValuationScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        addNewValuationSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify Claim line description is displayed in blue if the options \"Include in claim\" disabled" +
                    "- Claim line value is not added to Total claims sum")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void disableIncludeInClaimScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        disableIncludeInClaimSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify that second claim line value is not added to Total claims sum if the options " +
                    "'Include in claim' and 'Reviewed' enabled")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void enableIncludeInClaimSecondClaimScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        enableIncludeInClaimSecondClaimSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify Claim line description is displayed in pink if the options 'Include in claim'  " +
                    "and 'Reviewed' disabled")
    public void disableIncludeInClaimAndReviewedScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        disableIncludeInClaimAndReviewedSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify 'Complete claim' is enable if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    public void completeClaimIsEnabledScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        completeClaimIsEnabledSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Bug(bug = "CHARLIE-391")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify 'Reviewed' box is not displayed")
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, enabled = false)
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, enabled = false)
    public void reviewedBoxNotDisplayedScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim) {
        reviewedBoxNotDisplayedSharedTest(user, claim);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify 'Complete claim' is enabled if 'Reviewed' is disabled in SID")
    @RequiredSetting(type = ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED)
    @RequiredSetting(type = REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM)
    public void completeClaimIsEnabled2ScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        completeClaimIsEnabled2SharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify cancelled claim line is not added to the claim")
    public void cancelledClaimNotAddedScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        cancelledClaimNotAddedSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify Cash compensation CC is equal to V1")
    public void cashCompensationEqualV1ScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        cashCompensationEqualV1SharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to open Add Valuation dialogs in SID")
    public void openAddValuationDialogInSIDScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        openAddValuationDialogInSidSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to add new valuation price in add " +
                    "valuation dialogs (user selects 3d type)")
    public void addNewValuationPriceInAddValuationDialogScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialogSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to add new valuation price in add valuation dialogs (user selects 4th type)")
    public void addNewValuationPriceInAddValuationDialog2ScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialog2SharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to add new valuation price in " +
                    "add valuation dialogs (user selects 5th type)")
    public void addNewValuationPriceInAddValuationDialog3ScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        addNewValuationPriceInAddValuationDialog3SharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to enable age option")
    public void enableAgeOptionScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        enableAgeOptionSharedTest(user, claim, claimItem);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.SCALEPOINT_ID},
            description = "ECC-3144 Verify it's possible to add years & month and save set")
    public void addYearsAndMonthAndSaveScalepointIdTest(@UserAttributes(company = CompanyCode.FUTURE, type = User.UserType.SCALEPOINT_ID) User user, Claim claim, ClaimItem claimItem) {
        addYearsAndMonthAndSaveSharedTest(user, claim, claimItem);
    }
}
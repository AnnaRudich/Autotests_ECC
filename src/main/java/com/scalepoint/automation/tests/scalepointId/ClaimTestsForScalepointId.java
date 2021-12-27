package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.sharedTests.ClaimSharedTests;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

@SuppressWarnings("AccessStaticViaInstance")

@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
public class ClaimTestsForScalepointId extends ClaimSharedTests {

    private final String POLICY_TYPE = "testPolicy ÆæØøÅåß";
    private final String EMPTY = "";

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID},
            description = "CHARLIE-544 It's possible to reopen saved claim. Settlement is displayed for reopened claim")
    public void reopenSavedClaimScalepointIdTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = CompanyCode.FUTURE) User user, Claim claim) {
        reopenSavedClaimSharedTest(user, claim);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID},
            description = "CHARLIE-544 It's possible to cancel saved claim. Cancelled claim  has status Cancelled")
    public void cancelSavedClaimScalepointIdTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = CompanyCode.FUTURE) User user, Claim claim) throws Exception {
        cancelSavedClaimSharedTest(user, claim);
    }

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-544")
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.CLAIM_MISCELLANEOUS, TestGroups.SCALEPOINT_ID},
            description = "CHARLIE-544, ECC-2629 It's possible to complete claim with mail. " +
                    "Completed claim is added to the latest claims list with Completed status")
    public void completeClaimWithMailScalepointIdTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = CompanyCode.FUTURE) User user, Claim claim) {
        completeClaimWithMailSharedTest(user, claim);
    }
}
package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.sharedTests.CompleteClaimExternallySharedTests;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

public class CompleteClaimExternallyForScalepointIdTests extends CompleteClaimExternallySharedTests {

    @FeatureToggleSetting(type = SCALEPOINTID_LOGIN_ENABLED)
    @RequiredSetting(type = FTSetting.ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE)
    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Test(dataProvider = "testDataProvider",
            groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY, TestGroups.SCALEPOINT_ID},
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Settlement page")
    public void completeClaimExternallyFromSettlementPageScalepointIdTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = CompanyCode.FUTURE) User user, Claim claim) {
        completeClaimExternallyFromSettlementPageTest(user, claim);
    }
}





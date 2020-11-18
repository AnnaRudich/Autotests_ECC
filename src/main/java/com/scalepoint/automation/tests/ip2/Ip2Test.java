package com.scalepoint.automation.tests.ip2;

import com.scalepoint.automation.services.externalapi.IP2Api;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import org.testng.annotations.Test;

@RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
@RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)
public class Ip2Test extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_SETTLED docId SettlementDoc")
    public void claimSettledEventSettlementDoc(@UserCompany(CompanyCode.FUTURE70) User user,
                                               Claim claim,
                                               ClaimItem claimItem) {
       loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
               .toCompleteClaimPage()
               .fillClaimForm(claim)
               .completeWithEmail(claim, databaseApi, true)
               .openRecentClaim();

       new IP2Api()
               .assertTransactionWasProcessed(databaseApi.getUserIdByClaimNumber(claim.getClaimNumber()));


    }

    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_SETTLED docId SettlementInclusiveRepairDoc")
    public void claimSettledEventSettlementInclusiveRepairDoc(@UserCompany(CompanyCode.FUTURE71) User user,
                                                              Claim claim,
                                                              ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true);

        new IP2Api()
                .assertTransactionWasProcessed(databaseApi.getUserIdByClaimNumber(claim.getClaimNumber()));
    }
}

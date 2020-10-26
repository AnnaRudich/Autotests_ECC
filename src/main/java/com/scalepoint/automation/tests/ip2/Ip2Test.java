package com.scalepoint.automation.tests.ip2;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.stubs.Ip2Mock;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Ip2Test extends BaseTest {
    Ip2Mock.Ip2Stubs ip2Stubs;

    @BeforeClass
    public void startWireMock() {
        wireMock.allStubMappings()
                .getMappings()
                .stream()
                .forEach(m -> log.info(String.format("Registered stubs: %s",m.getRequest())));

    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_SETTLED docId SettlementDoc")
    @RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
    @RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)

    public void claimSettledEventSettlementDoc(@UserCompany(CompanyCode.FUTURE50) User user,
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
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_SETTLED docId SettlementInclusiveRepairDoc")
    @RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
    @RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)

    public void claimSettledEventSettlementInclusiveRepairDoc(@UserCompany(CompanyCode.FUTURE51) User user,
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
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_CANCELED, docId SettlementDoc")
    @RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
    @RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)

    public void claimCanceledEventSettlementDoc(@UserCompany(CompanyCode.FUTURE50) User user,
                                                Claim claim,
                                                ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .cancelClaim();
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Event type CLAIM_CANCELED, docId SettlementInclusiveRepairDoc")
    @RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
    @RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)

    public void claimCanceledEventSettlementInclusiveRepairDoc(@UserCompany(CompanyCode.FUTURE51) User user,
                                                               Claim claim,
                                                               ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .cancelClaim();
    }
}

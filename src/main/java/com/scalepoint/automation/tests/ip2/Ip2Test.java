package com.scalepoint.automation.tests.ip2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
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
        WireMock.configureFor(wireMock);
        wireMock.resetMappings();
        ip2Stubs = new Ip2Mock(wireMock)
                .add();
    }

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = " ")
    @RequiredSetting(type = FTSetting.CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS)
    @RequiredSetting(type = FTSetting.ENABLE_ALL_PAYMENT_INTEGRATION)

    public void ecc3031_3_reductionRulePolicyTypeDiscretionary(@UserCompany(CompanyCode.TOPDANMARK) User user,
                                                               Claim claim,
                                                               ClaimItem claimItem) {
       loginAndCreateClaim(user, claim)
                .openSid()
                .setBaseData(claimItem)
                .disableAge()
                .closeSidWithOk()
               .cancelClaim();

       new SettlementPage();


    }
}

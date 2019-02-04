package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;

public class SendingCaseSettledEventV3Case4 extends BaseUnifiedPaymentsApiTest {

    @BeforeMethod
    void setUp(Object[] testArgs){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");


        User user = (User)testArgs[0];
        InsertSettlementItem item1 = (InsertSettlementItem) testArgs[1];
        InsertSettlementItem item2 = (InsertSettlementItem) testArgs[2];

        setPrice(item1, 3000, 20);
        setPrice(item2, 2000, 0);

        createClaim(user, 1000, 0, item1, item2);
    }
}

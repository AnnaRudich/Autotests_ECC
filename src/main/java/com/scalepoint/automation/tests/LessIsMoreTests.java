package com.scalepoint.automation.tests;


import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class LessIsMoreTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Claim should have flat structure")
    public void charlie550_claimHaveFlatStructure(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .doAssert(SettlementPage.Asserts::assertSettlementPageIsInFlatView);
    }

}

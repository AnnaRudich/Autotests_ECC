package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.ClaimSearchPage.ClaimState.IN_USE;

public class ClaimSearchTest extends BaseTest {

    @RunOn(DriverType.CHROME)
    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillClaimNumber(claim.getClaimNumber())
                .search()
                .doAssert(claim, asserts -> {
                    asserts.isOnlyOnList();
                    asserts.isClaimState(IN_USE);
                    asserts.isClaimCompany(user.getCompanyName());
                });
    }
}

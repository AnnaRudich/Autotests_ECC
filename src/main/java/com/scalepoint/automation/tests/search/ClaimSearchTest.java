package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

public class ClaimSearchTest extends BaseTest {


  @Test(dataProvider = "testDataProvider", description = "Search for claim")
  public void searchClaim(User user, Claim claim) {
    loginAndCreateClaim(user, claim)
            .getMainMenu()
            .search();
  }
}

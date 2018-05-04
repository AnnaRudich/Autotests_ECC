package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.pages.ClaimSearchPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.ClaimSearchPage.ClaimState.CLOSED_EXTERNALLY;
import static com.scalepoint.automation.pageobjects.pages.ClaimSearchPage.ClaimState.COMPLETED;
import static com.scalepoint.automation.pageobjects.pages.ClaimSearchPage.ClaimState.IN_USE;
import static com.scalepoint.automation.pageobjects.pages.ClaimSearchPage.ClaimState.OPEN;

@RunOn(DriverType.CHROME)
public class ClaimSearchTest extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_claimHandler(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillClaimHandler("FirstName LastName")
                .search()
                .doAssert(asserts -> asserts.areClaimsMatchingClaimHandler("FirstName LastName"));
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_byName(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillCustomerName(claim.getFirstName())
                .search()
                .doAssert(claim, ClaimSearchPage.Asserts::areClaimsMatchingName)
                .fillCustomerName(claim.getLastName())
                .search()
                .doAssert(claim, ClaimSearchPage.Asserts::areClaimsMatchingName);
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_byCompany(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillCompany(user.getCompanyName())
                .search()
                .doAssert(asserts -> {
                    asserts.areClaimsMatchingCompany(user.getCompanyName());
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_inUse(User user, Claim claim) {
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

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_open(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .saveClaim()
                .getMainMenu()
                .openClaimSearch()
                .fillClaimNumber(claim.getClaimNumber())
                .search()
                .doAssert(claim, asserts -> {
                    asserts.isOnlyOnList();
                    asserts.isClaimState(OPEN);
                    asserts.isClaimCompany(user.getCompanyName());
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_completed(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail()
                .getMainMenu()
                .openClaimSearch()
                .fillClaimNumber(claim.getClaimNumber())
                .search()
                .doAssert(claim, asserts -> {
                    asserts.isOnlyOnList();
                    asserts.isClaimState(COMPLETED);
                    asserts.isClaimCompany(user.getCompanyName());
                });
    }

    @RequiredSetting(type = FTSetting.ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE)
    @RequiredSetting(type = FTSetting.SETTLE_WITHOUT_MAIL)
    @Test(dataProvider = "testDataProvider", description = "Search for claim")
    public void searchClaim_closedExternally(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .completeClaimWithoutMail()
                .getMainMenu()
                .openClaimSearch()
                .fillClaimNumber(claim.getClaimNumber())
                .search()
                .doAssert(claim, asserts -> {
                    asserts.isOnlyOnList();
                    asserts.isClaimState(CLOSED_EXTERNALLY);
                    asserts.isClaimCompany(user.getCompanyName());
                });
    }
}

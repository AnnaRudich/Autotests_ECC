package com.scalepoint.automation.tests.search;

import com.scalepoint.automation.pageobjects.pages.ClaimSearchPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.shared.ClaimStatus.CLOSED_EXTERNALLY;
import static com.scalepoint.automation.shared.ClaimStatus.COMPLETED;
import static com.scalepoint.automation.shared.ClaimStatus.IN_USE;
import static com.scalepoint.automation.shared.ClaimStatus.OPEN;


public class ClaimSearchTest extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Search for claim by multiple fields")
    public void searchClaim_byMultipleConditions(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillCustomerName(claim.getFirstName())
                .fillCompany(user.getCompanyName())
                .fillClaimNumber(claim.getClaimNumber())
                .search()
                .doAssert(claim, asserts -> {
                    asserts.areClaimsMatchingName();
                    asserts.areClaimsMatchingCompany(user.getCompanyName());
                    asserts.isOnlyOnList();
                    asserts.isClaimState(IN_USE);
                });
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim by claim handler")
    public void searchClaim_claimHandler(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .getMainMenu()
                .openClaimSearch()
                .fillClaimHandler("FirstName LastName")
                .search()
                .doAssert(asserts -> asserts.areClaimsMatchingClaimHandler("FirstName LastName"));
    }

    @Test(dataProvider = "testDataProvider", description = "Search for claim by name and last name")
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

    @Test(dataProvider = "testDataProvider", description = "Search for claim by company")
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

    @Test(dataProvider = "testDataProvider", description = "Search for claim in use")
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

    @Test(dataProvider = "testDataProvider", description = "Search for open claim")
    public void searchClaim_open(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .saveClaim(claim)
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

    @Test(dataProvider = "testDataProvider", description = "Search for completed claim")
    public void searchClaim_completed(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
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
    @Test(dataProvider = "testDataProvider", description = "Search for claim closed externally")
    public void searchClaim_closedExternally(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .completeClaimWithoutMail(claim)
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

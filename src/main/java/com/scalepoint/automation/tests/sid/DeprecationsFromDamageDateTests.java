package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.NewCustomerPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import java.time.LocalDate;

import static com.scalepoint.automation.utils.DateUtils.localDateToString;
import static org.assertj.core.api.Assertions.assertThat;

public class DeprecationsFromDamageDateTests extends BaseTest {

    //4
    @Test(dataProvider = "testDataProvider", description = "Check if damage date is displayed while creating new claim")
    public void charlie_554_verifyDamageDateIsDisplayed(User user, Claim claim) {
        login(user)
                .clickCreateNewCase()
                .enterClaimNumber(claim.getClaimNumber())
                .enterFirstName(claim.getFirstName())
                .enterSurname(claim.getLastName())
                .doAssert(
                        NewCustomerPage.Asserts::assertThatDamgeDateIsDisplayed
                );
    }

    //6
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1WithoutDamageDate(User user, Claim claim) {
        claim.setDamageDate("");
        loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );
    }

    //17
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1ReintegrateClaimWithUpdatedNotAllowed(User user, Claim claim) {
        claim.setDamageDate(localDateToString(LocalDate.now()));

        CustomerDetailsPage detailsPage = loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );

        claim.setClaimNumber(detailsPage.getClaimNumber());
        claim.setDamageDate(localDateToString(LocalDate.now().minusDays(1L)));

        createClaimIgnoringExceptions(user, claim);

        login(user)
                .openActiveRecentClaim()
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );
    }

    //18
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1ReintegrateClaimWithUpdateAllowed(@UserCompany(CompanyCode.ALKA) User user, Claim claim) {
        claim.setDamageDate(localDateToString(LocalDate.now()));

        CustomerDetailsPage detailsPage = loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );

        claim.setClaimNumber(detailsPage.getClaimNumber());
        claim.setDamageDate(localDateToString(LocalDate.now().minusDays(1L)));

        loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now().minusDays(1L))
                );
    }


    //21
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1ReintegrateClaimWithLineExisting(User user, Claim claim, ClaimItem claimItem) {
        claim.setDamageDate(localDateToString(LocalDate.now()));

        CustomerDetailsPage detailsPage = loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );

        claim.setClaimNumber(detailsPage.getClaimNumber());
        claim.setDamageDate(localDateToString(LocalDate.now().minusDays(1L)));

        detailsPage.toSettlementPageUsingNavigationMenu()
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(Constants.PRICE_2400)
                .closeSidWithOk();

        createClaimIgnoringExceptions(user, claim);

        login(user)
                .openActiveRecentClaim()
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );
    }

    //22
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1ReintegrateClaimWithLineExistingUpdateNotAllowed(User user, Claim claim, ClaimItem claimItem) {
        claim.setDamageDate(localDateToString(LocalDate.now()));

        CustomerDetailsPage detailsPage = loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );

        claim.setClaimNumber(detailsPage.getClaimNumber());
        claim.setDamageDate(localDateToString(LocalDate.now().minusDays(1L)));

        detailsPage.toSettlementPageUsingNavigationMenu()
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(Constants.PRICE_2400)
                .closeSidWithOk();

        loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIsEqual(LocalDate.now())
                );
    }

    //7
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimUsingIP1WithFutureDamageDate(User user, Claim claim) {
        claim.setDamageDate(localDateToString(LocalDate.now().plusDays(1L)));
        loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        CustomerDetailsPage.Asserts::assertDamageDateIsEmpty
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Check if damage date is not editable after adding claim line")
    public void charlie_554_damageDateOnCustomerDetailsShouldBeNotEditable(User user, Claim claim, ClaimItem claimItem){
        loginAndCreateClaim(user,claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(Constants.PRICE_2400)
                .closeSidWithOk()
                .toCustomerDetails()
                .doAssert(
                        CustomerDetailsPage.Asserts::assertIsDamageDateEditNotAvailable
                );
    }

    @Test(dataProvider = "testDataProvider", description = "Check if damage date is not editable after adding claim line")
    public void charlie_554_damageDateOnCustomerDetailsShouldBeEditable(User user, Claim claim){
        loginAndCreateClaim(user,claim)
                .toCustomerDetails()
                .doAssert(
                        CustomerDetailsPage.Asserts::assertIsDamageDateEditAvailable
                );
    }








   //not working as expected 23
    @Test(dataProvider = "testDataProvider", description = "create claim with wrong damage date")
    public void charlie_554_createClaimWithWrongDataFormat(User user, EccIntegration eccIntegration){
        eccIntegration.getClaim().getDamage().setDamageDate("2017-19-01");
        String response = createClaimUsingEccIntegration(user, eccIntegration).getResponse().extract().response().getBody().asString();
        assertThat(response).contains("The entered Damage Date is not valid.");
    }

    //14
    @RunOn(DriverType.CHROME) // TO DO
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimCurrentDamageDateAndCheckIsRRuleApplied(User user, Claim claim, ClaimItem claimItem) {
        claim.setDamageDate(localDateToString(LocalDate.now().minusMonths(6L)));
        loginAndCreateClaim(user, claim)
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getExistingGroupWithPolicyDepreciationTypeAndReductionRule())
                .enableAge();
    }

    //15
    @RunOn(DriverType.CHROME) // TO DO
    @Test(dataProvider = "testDataProvider", description = "Check if damage is today after creating claim using ip1 without damage date")
    public void charlie_554_createClaimCurrentDamageDateAndCheckAreRRuleApplied(User user, Claim claim, ClaimItem claimItem) {
        claim.setDamageDate(localDateToString(LocalDate.now().minusMonths(24L)));
        loginAndCreateClaim(user, claim)
                .openSid()
                .automaticDepreciation(false)
                .setDescription(claimItem.getTextFieldSP())
                .setCustomerDemand(Constants.PRICE_100_000)
                .setNewPrice(Constants.PRICE_2400)
                .setCategory(claimItem.getAlkaCategory())
                .setSubCategory(claimItem.getAlkaSubCategory())
                .enableAge();
    }



}

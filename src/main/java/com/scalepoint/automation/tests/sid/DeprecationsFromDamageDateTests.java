package com.scalepoint.automation.tests.sid;

import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.services.externalapi.IP1Api;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static org.assertj.core.api.Assertions.assertThat;

public class DeprecationsFromDamageDateTests extends BaseUITest {

    private static final String API_FORMAT = "yyyy-MM-dd";

    private String toDamageFormat(LocalDate initialDamageDate) {
        return format(initialDamageDate, API_FORMAT);
    }

    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Check if damage date is displayed and can be setup on creating new claim and on customer details page")
    public void charlie_554_verifyDamageDateIsDisplayedAndCanBeSetOnCreatingAndDetails(User user, Claim claim) {
        LocalDate threeDaysBefore = LocalDate.now().minusDays(3);
        LocalDate fiveDaysBefore = LocalDate.now().minusDays(5);

        loginFlow.login(user)
                .clickCreateNewCase()
                .enterClaimNumber(claim.getClaimNumber())
                .enterFirstName(claim.getFirstName())
                .enterSurname(claim.getLastName())
                .selectDamageDate(threeDaysBefore)
                .selectPolicyType(1)
                .create()
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIs(threeDaysBefore)
                )
                .selectDamageDate(fiveDaysBefore)
                .doAssert(
                        asserts -> asserts.assertDamageDateIs(fiveDaysBefore)
                );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Damage date should be set to current date if it's not passed in IP1")
    public void charlie_554_ip1_shouldSetDamageDateToCurrentDate(User user, Claim claim) {
        LocalDate initialDamageDate = LocalDate.now();
        claim.setDamageDate("");
        IP1Api.doGetIntegration(user, claim, false).toCustomerDetails().doAssert(
                asserts -> asserts.assertDamageDateIs(initialDamageDate)
        );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE, UserCompanyGroups.ALKA},
            dataProvider = "testDataProvider",
            description = "Check if damage date created/updated for empty claims")
    public void charlie_554_ip1_shouldCreateAndUpdateClaimWithDamageDate(@UserAttributes(company = CompanyCode.ALKA) User user, Claim claim) {
        LocalDate initialDamageDate = LocalDate.now();
        claim.setDamageDate(toDamageFormat(initialDamageDate));

        /* claim is created with damage date*/
        IP1Api.doGetIntegration(user, claim, false).toCustomerDetails().doAssert(
                asserts -> asserts.assertDamageDateIs(initialDamageDate)
        ).toSettlementPage().saveClaim(claim);

        LocalDate updatedDamageData = LocalDate.now().minusDays(1L);
        claim.setDamageDate(toDamageFormat(updatedDamageData));

        /* claim is updated with damage date*/
        IP1Api.doGetIntegration(user, claim, false).toCustomerDetails().doAssert(
                asserts -> asserts.assertDamageDateIs(updatedDamageData)
        ).toSettlementPage().saveClaim(claim);

        /* damage data shouldn't be updated when damage date is absent*/
        claim.setDamageDate("");
        IP1Api.doGetIntegration(user, claim, false).toCustomerDetails().doAssert(
                asserts -> asserts.assertDamageDateIs(updatedDamageData)
        );
    }


    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Check ip1 rejects wrong damage dates")
    public void charlie_554_ip1_shouldRejectNotValidDamageDates(User user, Claim claim) {
        /* damage date from future */
        claim.setDamageDate(toDamageFormat(LocalDate.now().plusDays(1L)));
        IP1Api.assertGetIntegrationHasError(user, claim, false, "The entered Damage Date must not be later than today");

        /* damage date with wrong format */
        //claim.setDamageDate("15-02-2018");
        Lists.newArrayList("19-01-2008", "2008-19-01", "01-2008-19", "19-2008-01").forEach(date -> {
            claim.setDamageDate(date);
            IP1Api.assertGetIntegrationHasError(user, claim, false, "The entered Damage Date is not valid");
        });
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Check ip1 doesn't update damage date if claims lines present")
    public void charlie_554_ip1_shouldNotUpdateDamageDateIfClaimsLinesPresent(User user, Claim claim, ClaimItem claimItem) {
        LocalDate initialDamageDate = LocalDate.now();
        claim.setDamageDate(toDamageFormat(initialDamageDate));

        IP1Api
                .doGetIntegration(user, claim, false)
                .toCustomerDetails()
                .toSettlementPage()
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(Constants.PRICE_2400)
                .closeSidWithOk()
                .saveClaim(claim);

        LocalDate updatedDamageData = LocalDate.now().minusDays(1L);
        claim.setDamageDate(toDamageFormat(updatedDamageData));

        IP1Api.doGetIntegration(user, claim, false).toCustomerDetails().doAssert(
                asserts -> asserts.assertDamageDateIs(initialDamageDate)
        );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Check if damage date is not editable after adding claim line")
    public void charlie_554_damageDateOnCustomerDetailsShouldBeNotEditable(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSid()
                .setDescription(claimItem.getTextFieldSP())
                .setCategory(claimItem.getCategoryBabyItems())
                .setNewPrice(Constants.PRICE_2400)
                .closeSidWithOk()
                .toCustomerDetails()
                .doAssert(
                        CustomerDetailsPage.Asserts::assertIsDamageDateEditNotAvailable
                );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Check if damage date is not editable after adding claim line")
    public void charlie_554_damageDateOnCustomerDetailsShouldBeEditable(User user, Claim claim) {
        loginFlow.loginAndCreateClaim(user, claim)
                .toCustomerDetails()
                .doAssert(
                        CustomerDetailsPage.Asserts::assertIsDamageDateEditAvailable
                );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "create claim with wrong damage date")
    public void charlie_554_createClaimWithWrongDataFormat(User user, EccIntegration eccIntegration) {
        eccIntegration.getClaim().getDamage().setDamageDate("2017-19-01");
        String response = createClaimUsingEccIntegration(user, eccIntegration).getResponse().extract().response().getBody().asString();
        assertThat(response).contains("The entered Damage Date is not valid.");
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Create claim using unified integration")
    public void charlie_554_createClaimUsingUnifiedIntegration(User user, ClaimRequest claimRequest) {
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, loginFlow.createCwaClaimAndGetClaimToken(claimRequest))
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIs(LocalDate.now().minusDays(2L))
                );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Creating claim without damageDate should set it to now")
    public void charlie_554_createClaimUsingUnifiedIntegrationWithNoDamageDate(User user, ClaimRequest claimRequest) {
        claimRequest.setAccidentDate(null);
        loginFlow.loginAndOpenUnifiedIntegrationClaimByToken(user, loginFlow.createCwaClaimAndGetClaimToken(claimRequest))
                .toCustomerDetails()
                .doAssert(
                        asserts -> asserts.assertDamageDateIs(LocalDate.now())
                );
    }

    @Test(groups = {TestGroups.SID, TestGroups.DEPRECATIONS_FROM_DAMAGE_DATE},
            dataProvider = "testDataProvider",
            description = "Creating claim without damageDate should set it to now")
    public void charlie_554_createClaimUsingUnifiedIntegrationWithWrongDamageDate(ClaimRequest claimRequest) {
        claimRequest.setAccidentDate("2017-19-01");
        String response = loginFlow.createCwaClaim(claimRequest).getResponse().body().asString();
        assertThat(response).contains("Failure: Invalid damageDate");
    }

}

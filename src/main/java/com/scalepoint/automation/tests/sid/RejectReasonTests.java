package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Constants.DEPRECIATION_10;
import static com.scalepoint.automation.utils.Constants.PRICE_100_000;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;

public class RejectReasonTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "Check if reject reason dropdown is disabled if there is 0 or 1 reason available for IC")
    public void charlie_549_checkIsRejectReasonDropdownDisabled(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(PRICE_100_000)
                            .withNewPrice(PRICE_2400)
                            .withDepreciation(DEPRECIATION_10)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr());
                })
                .rejectClaim()
                .doAssert(SettlementDialog.Asserts::assertRejectReasonDisabled)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }


    //2 need eccintegration create claim in 2057
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie_549_checkIfCanAddNewRejectReasonToClaimCreatedBefore(User user, Claim claim, ClaimItem claimItem) {
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(PRICE_100_000)
                            .withNewPrice(PRICE_2400)
                            .withDepreciation(DEPRECIATION_10)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr());
                }).closeSidWithOk();
    }


    @RunOn(DriverType.IE)  //there is no reason after disabling
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie_549_disableReason(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reason = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(PRICE_100_000)
                            .withNewPrice(PRICE_2400)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                            .withAge(2, 2);
                })
                .rejectClaim()
                .selectRejectReason(reason)
                .doAssert(sid -> sid.assertRejectReasonEqualTo(reason))
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected)
                .getMainMenu()
                .logOut();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .findReason(reason)
                .disable()
                .assertReasonDisabled(reason)
                .logout();

        login(user)
                .openRecentClaim()
                .reopenClaim()
                .editFirstClaimLine()
                .doAssert(SettlementDialog.Asserts::assertRejectReasonDisabled);
    }

    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie_549_makeRejectReasonMandatory(@UserCompany(CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(PRICE_100_000)
                            .withNewPrice(PRICE_2400)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                            .withAge(0, 6);
                })
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk();
        settlementDialog.closeSidWithOk();
        settlementDialog.doAssert(asserts -> {
                    asserts.assertDiscretionaryReasonHasRedBorder();
                    asserts.assertDiscretionaryReasonEnabled();
                });


    }


}

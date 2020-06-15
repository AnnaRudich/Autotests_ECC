package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-548")
public class VoucherAgreementSharedExclusiveTests extends BaseTest {
    /**
     * GIVEN: IC1 parent user U1, IC1 child user U2, SP admin user U3, IC2 user U4
     * WHEN: U1 creates supplier S1 and it's voucher V1
     * THEN: V1 is displayed in Voucher's list in ME
     * THEN: V1 discount is correct
     * WHEN: U2 navigates to Vouchers list in ME
     * THEN: V1 is displayed in Voucher's list in ME
     * THEN: V1 discount is correct
     * WHEN: U3 navigates to Vouchers list in ME
     * THEN: V1 is not displayed in Voucher's list in ME
     * WHEN: U4 navigates to Vouchers list in ME
     * THEN: V1 is not displayed in Voucher's list in ME
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3030 IC1 voucher is displayed in vouchers list for IC user and not displayed for SP user")
    public void ecc3030_exclusiveVoucherInVouchersList(@UserCompany(CompanyCode.TRYGFORSIKRING) User trygChildUser,
                                                       @UserCompany(CompanyCode.TRYGHOLDING) User trygParentUser,
                                                       @UserCompany(CompanyCode.SCALEPOINT) User scalepointUser,
                                                       User futureCompanyUser,
                                                       Claim claim,
                                                       Supplier supplier,
                                                       Voucher voucher,
                                                       ClaimItem claimItem) {

        String voucherName = voucher.getVoucherNameSP();
        createVoucherAgreement(trygParentUser, supplier, voucher, claimItem.getCategoryBabyItems());

        loginAndCheckVoucherPresence(trygChildUser, claim, claimItem, claim.getPolicyTypeTrygUser(), voucherName, true);
        loginAndCheckVoucherPresence(trygParentUser, TestData.getClaim(), claimItem, claim.getPolicyTypeTrygUser(), voucherName, true);
        loginAndCheckVoucherPresence(scalepointUser, TestData.getClaim(), claimItem, null, voucherName, false);
        loginAndCheckVoucherPresence(futureCompanyUser, TestData.getClaim(), claimItem, null, voucherName, false);
    }

    /**
     * GIVEN: IC1 parent user U1, SP admin user U2, IC2 user U3
     * WHEN: U2 creates supplier S1 and it's voucher V1
     * THEN: V1 is displayed in Voucher's list in ME
     * THEN: V1 discount is correct
     * WHEN: U1 navigates to Vouchers list in ME
     * THEN: V1 is displayed in Voucher's list in ME
     * THEN: V1 discount is correct
     * WHEN: U1 navigates to SM, leaves V1 and navigates to Vouchers list in ME
     * THEN: V1 is not displayed in Voucher's list in ME
     * WHEN: U3 navigates to Vouchers list in ME
     * THEN: V1 is displayed in Voucher's list in ME
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3030 Joined Shared voucher is available for IC1 and not available if it's left")
    public void ecc3030_sharedVoucherInVouchersListForIC(@UserCompany(CompanyCode.TRYGHOLDING) User trygUser,
                                                         @UserCompany(CompanyCode.SCALEPOINT) User scalepointUser,
                                                         User futureCompanyUser,
                                                         Claim claim,
                                                         Supplier supplier,
                                                         Voucher voucher,
                                                         ClaimItem claimItem) {

        String voucherName = voucher.getVoucherNameSP();
        createVoucherAgreement(scalepointUser, supplier, voucher, claimItem.getCategoryBabyItems());

        loginAndCheckVoucherPresence(trygUser, claim, claimItem, claim.getPolicyTypeTrygUser(), voucherName, true);

        loginToEccAdmin(trygUser)
                .editSupplier(supplier.getSupplierName())
                .selectAgreementsTab()
                .doWithAgreement(voucherName, SupplierDialog.AgreementsTab.ActionType.LEAVE)
                .closeSupplier()
                .logout();

        loginAndCheckVoucherPresence(trygUser, TestData.getClaim(), claimItem, claim.getPolicyTypeTrygUser(), voucherName, false);

        loginAndCheckVoucherPresence(futureCompanyUser, TestData.getClaim(), claimItem, null, voucherName, true);


    }

    private void createVoucherAgreement(User user, Supplier supplier, Voucher voucher, PseudoCategory pseudoCategory) {
        SuppliersPage suppliersPage = loginToEccAdmin(user);

        SharedEccAdminFlows.createVoucherAgreement(SharedEccAdminFlows.createSupplier(suppliersPage, supplier),
                SharedEccAdminFlows.VoucherAgreementData
                        .newBuilder(voucher, 10)
                        .mapToCategory(pseudoCategory)
                        .build())
                .saveSupplier()
                .logout();
    }

    private void loginAndCheckVoucherPresence(User userToLogin, Claim claim, ClaimItem claimItem, String policy, String voucherName, boolean mustBePresent) {
        loginAndCreateClaim(userToLogin, claim, policy)
                .openSidAndFill(sid -> sid.withCategory(claimItem.getCategoryBabyItems()))
                .doAssert(sid -> {
                    if (mustBePresent) {
                        sid.assertVoucherListed(voucherName);
                    } else {
                        sid.assertVoucherNotListed(voucherName);
                    }
                });
    }


}

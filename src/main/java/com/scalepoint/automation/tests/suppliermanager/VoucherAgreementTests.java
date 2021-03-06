package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateVoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.AgreementsTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.GeneralTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementAdvancedTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementGeneralTab;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.VouchersPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.Objects;

import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementAdvancedTab.EVoucherOptions.*;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-499")
public class VoucherAgreementTests extends BaseUITest {

    private static final String AUTOTEST_SUPPLIER_VA_TESTS = "Autotest-Supplier-VA-Tests";

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "Create voucher with brands and tags and later use it in sid")
    public void charlie550_createVoucherWithBrandsAndTags(User user, Claim claim, ClaimItem claimItem, Voucher voucher) {
        String brand = "brand_test";
        String tag = "tag_test";
        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectCategoriesTab()
                .mapToCategory(claimItem.getCategoryBabyItems())
                .selectCoverageTab()
                .setBrands(brand)
                .setTags(tag)
                .saveVoucherAgreement()
                .saveSupplier()
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucher.getVoucherNameSP()))
                .signOut();

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(),
                        sid -> sid.withNewPrice(PRICE_2400)
                                .withVoucher(voucher.getVoucherNameSP()))
                .openEditDiscountDistributionForVoucher()
                .doAssert(asserts -> {
                    asserts.assertBrandsTextIs(brand);
                    asserts.assertTagsTextIs(tag);
                }).closeDialogWithOk()
                .closeSidWithOk()
                .parseFirstClaimLine()
                .doAssert(asserts -> {
                    asserts.assertVoucherIconIsDisplayed();
                    asserts.assertVoucherTooltipContains(brand);
                    asserts.assertVoucherTooltipContains(tag);
                });
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new voucher V1 for random supplier
     * THEN: V1 is displayed in the list of Vouchers
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 Vouchers list on Vouchers page contains new voucher")
    public void ecc3038_newVoucherInVouchersList(User user, Voucher voucher) {
        loginFlow.loginToEccAdmin(user).editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .saveVoucherAgreement()
                .saveSupplier()
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucher.getVoucherNameSP()));

    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new voucher V1 for random supplier
     * WHEN: User updates all general data for V1
     * THEN: V1 general data stored correctly
     * <p>
     * ecc3038_activeStatusNo
     * ecc3038_activeStatusYes
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to update all General tab data for new default voucher")
    public void ecc3038_newVoucherGeneralUpdate(User user, Voucher voucher, AttachmentFiles attachmentFiles) {
        int discount = 27;
        int faceValue = 15;
        int faceValueStep = 3;

        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .fill(tab -> {
                    new VoucherAgreementGeneralTab.FormFiller(tab)
                            .withActive(false)
                            .withDiscount(discount)
                            .withFaceValue(faceValue)
                            .withFaceValueStep(faceValueStep)
                            .withImage(attachmentFiles.getJpgFile2Loc())
                            .withLogo(attachmentFiles.getJpgFile2Loc());
                })
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .doAssert(dialog -> {
                    dialog.assertDiscount(discount);
                    dialog.assertFaceValue(faceValue);
                    dialog.assertFaceValueStep(faceValueStep);
                    dialog.assertStatus(false);
                    dialog.assertLogoPresent();
                    dialog.assertImagePresent();
                });
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates voucher V1 for random supplier
     * WHEN: User fills Advanced data for V1
     * WHEN: User saves V1 data
     * THEN: Advanced data for V1 is stored correctly
     *
     * https://jira.scalepoint.com/browse/CLAIMSHOP-4869
     * Feature toggle was added to hide delivery type -
     *         FREIGHT_PRICE,
     *         DONT_SEND_ORDER_EMAIL,
     *         SUPPLIER_NOTES,
     *         DELIVERY_TYPE
     * now to apply this toggle deploy should be done, it's a bug which should be fixed
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            enabled = false,
            dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to add Advanced data for new voucher")
    public void ecc3038_advancedDataAdding(User user, Voucher voucher) {
        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectAdvancedTab()
                .fill(tab -> {
                    new VoucherAgreementAdvancedTab.FormFiller(tab)
                            .useOrderEmail(VoucherAgreementAdvancedTab.OrderMailType.OTHER, "ipo@scalepoint.com")
                            .withDeliveryCost(10)
                            .withDeliveryType("Delivery type Text")
                            .withPopularity(5)
                            .useAsEVoucher(EMAIL_REQUIRED, PERSONAL_CODE_REQUIRED, PHONE_REQUIRED, USE_PORTAL_REQUIRED);
                })
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .selectAdvancedTab()
                .doAssert(tab -> {
                    tab.assertOrderType(VoucherAgreementAdvancedTab.OrderMailType.OTHER);
                    tab.assertOtherEmail("ipo@scalepoint.com");
                    tab.assertDeliveryType("Delivery type Text");
                    tab.assertPopularity(5);
                    tab.assertUsedAsEVoucher(EMAIL_REQUIRED, PERSONAL_CODE_REQUIRED, PHONE_REQUIRED, USE_PORTAL_REQUIRED);
                });
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new voucher V1
     * WHEN: User adds Legal Data for V1
     * THEN: Legal Data is stored correctly
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to add Conditions and Limitations for new voucher")
    public void ecc3038_initialLegalAdding(User user, Voucher voucher) {
        String conditions = RandomStringUtils.randomAlphabetic(100);
        String limitations = RandomStringUtils.randomAlphabetic(140);

        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectLegalTab()
                .setConditions(conditions)
                .setLimitations(limitations)
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .selectLegalTab()
                .doAssert(tab -> {
                    tab.assertConditions(conditions);
                    tab.assertLimitations(limitations);
                });
    }

    /**
     * GIVEN: IC User with Supply Manager credentials
     * WHEN: User opens supplier S1
     * WHEN: User creates voucher V1 for S1
     * WHEN: User sets custom discount distribution DD1 and saves V1 changes
     * WHEN: User sets new V1 discount
     * THEN: IC has complete discount on Discount Distribution page
     * <p>
     * ecc3038_settingDistribution
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 All discount sent to IC if discount was changed and DD was customized before")
    public void ecc3038_discountUpdateClearDistribution(User user, Voucher voucher) {
        int discount = 25;
        int discountToIc = 10;
        int discountToCustomer = 15;

        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(discount);
                })
                .createVoucherAgreement()
                .selectDiscountDistributionTab()
                .doAssert(tab -> tab.assertDiscountToIc(discount))
                .setDiscountToIc(discountToIc)
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .selectDiscountDistributionTab()
                .doAssert(tab -> {
                    tab.assertDiscountToClaimant(discountToCustomer);
                    tab.assertDiscountToIc(discountToIc);
                });
    }

    /**
     * GIVEN: Voucher V1 with assigned category C1
     * THEN: C1 is displayed in V1 categories list
     * WHEN: User unassigns C1 for V1
     * THEN: C1 is not displayed in V1 categories list
     * <p>
     * ecc3038_initialCategoryAdding
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to remove assigned category for new voucher")
    public void ecc3038_removeAssignedCategory(User user, Voucher voucher, ClaimItem claimItem) {
        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectCategoriesTab()
                .mapToCategory(claimItem.getCategoryBabyItems())
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherNameSP())
                .selectCategoriesTab()
                .doAssert(tab -> tab.assertCategoryMapped(claimItem.getCategoryBabyItems()))
                .removeMapping(claimItem.getCategoryBabyItems())
                .doAssert(tab -> tab.assertCategoryNotMapped(claimItem.getCategoryBabyItems()));
    }


    /**
     * GIVEN: SP and IC Users with Supply Manager credentials, SP supplier S1
     * WHEN: IC User creates voucher V1 for S1
     * THEN: SP user can't view V1 details
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "Scalepoint SupplyManager can't open IC voucher")
    public void ecc3038_spSMCantOpenICVoucher(User futureUser,
                                              @UserAttributes(company = CompanyCode.SCALEPOINT) User scalepointUser,
                                              ClaimItem claimItem,
                                              Supplier supplier,
                                              Voucher voucher) {

        VoucherAgreementData data = VoucherAgreementData.newBuilder(futureUser, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(scalepointUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_VISIBLE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC2 child user creates voucher V1
     * THEN: V1 is available for IC1 parent user in Vouchers List
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.TRYGFORSIKRING,
            UserCompanyGroups.TRYGHOLDING},
            dataProvider = "testDataProvider",
            description = "ECC-3038 Child IC voucher is  available for parent IC in Vouchers List")
    public void ecc3038_childICVoucherAvailableParentICVouchersList(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User childUser,
                                                                    @UserAttributes(company = CompanyCode.TRYGHOLDING) User parentUser,
                                                                    ClaimItem claimItem,
                                                                    Supplier supplier,
                                                                    Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(childUser, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(parentUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC1 parent user creates voucher V1
     * THEN: V1 is available for IC1 child user in Vouchers List
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.TRYGFORSIKRING,
            UserCompanyGroups.TRYGHOLDING},
            dataProvider = "testDataProvider",
            description = "ECC-3038 Parent IC voucher is  available for child IC in Vouchers List")
    public void ecc3038_parentICVoucherAvailableChildICVouchersList(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User childUser,
                                                                    @UserAttributes(company = CompanyCode.TRYGHOLDING) User parentUser,
                                                                    ClaimItem claimItem,
                                                                    Supplier supplier,
                                                                    Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(parentUser, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(childUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: IC user with Supply Manager credentials, SP voucher V1 with assigned categories left by IC user
     * WHEN: user navigates to Supply Management and joins V1
     * THEN: V1 statues is "Yes"
     * <p>
     * ecc3038_leaveSPVoucherInactiveStatus
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to join left Shared Voucher for IC SM. Voucher gets status active")
    public void ecc3038_joinLeftSPVoucherActiveStatus(@UserAttributes(company = CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(futureUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();

        String vname = voucher.getVoucherNameSP();

        AgreementsTab agreementsTab = testVoucherVisibilityAcrossCompanies(data);
        Objects.requireNonNull(agreementsTab)
                .doAssert(tab -> tab.assertVoucherStatus(vname, true))
                .doWithAgreement(vname, AgreementsTab.ActionType.LEAVE)
                .closeSupplier()
                .editSupplier(supplier.getSupplierName())
                .selectAgreementsTab()
                .doAssert(tab -> tab.assertVoucherStatus(vname, false))
                .doWithAgreement(vname, AgreementsTab.ActionType.JOIN)
                .doAssert(tab -> tab.assertVoucherStatus(vname, true));
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: SP User creates V1 without any assigned categories
     * THEN: V1 active status is No for IC User
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 SP voucher active status is Noe for IC SM if no categories are assigned to Voucher")
    public void ecc3038_inactiveSPVoucherNoCatAssigned(@UserAttributes(company = CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .thenLoginAnotherUser(futureUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_ACTIVE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: IC1 and IC2 users with Supply Manager credentials, SP voucher V1 with assigned categories left by IC1 user
     * WHEN: IC2 user navigates to Supply Management
     * THEN: V1 statues is "Yes"
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 Voucher left by IC1 is active for IC2")
    public void ecc3038_voucherLeftByIC1ActiveIC2(@UserAttributes(company = CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser1, User futureUser2, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(futureUser1)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();

        String vname = voucher.getVoucherNameSP();
        AgreementsTab agreementsTab = testVoucherVisibilityAcrossCompanies(data);
        Objects.requireNonNull(agreementsTab)
                .doWithAgreement(vname, AgreementsTab.ActionType.LEAVE)
                .doAssert(tab -> tab.assertVoucherStatus(vname, false))
                .closeSupplier()
                .logout();

        loginFlow.loginToEccAdmin(futureUser2)
                .editSupplier(supplier.getSupplierName())
                .selectAgreementsTab()
                .doAssert(tab -> tab.assertVoucherStatus(vname, true))
                .closeSupplier()
                .logout();
    }

    /**
     * GIVEN: IC1 and IC2 Users with Supply Manager credentials, SP supplier S1
     * WHEN: IC1 User creates voucher V1 for S1
     * THEN: V1 details is unavailable for IC2 user in SP Supplier's dialog
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT},
            enabled = false, dataProvider = "testDataProvider",
            description = "ECC-3038 IC1 voucher is not available for IC2 in SP Supplier's dialog")
    public void ecc3038_ic1VoucherNotAvailableIC2(User futureUser1, User futureUser2, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(futureUser1, AUTOTEST_SUPPLIER_VA_TESTS)
                .withVoucherActive(voucher.getVoucherNameSP(), 10)
                .thenLoginAnotherUser(futureUser2)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_VISIBLE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials, SP Voucher V1
     * WHEN: SP User sets V1 as inactive
     * THEN: V1 is not available for IC User in Supplier dialog
     * <p>
     * ecc3038_inactiveSPVoucherNotAvailableICVouchersList
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "ECC-3038 Inactive SP voucher is not available for IC SM in Suppliers dialog")
    public void ecc3038_inactiveSPVoucherNotAvailableIC(@UserAttributes(company = CompanyCode.SCALEPOINT) User scalepointUser, User futureUser, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(scalepointUser, supplier)
                .withVoucherInactive(voucher.getVoucherNameSP(), 10)
                .mapToCategory(claimItem.getCategoryBabyItems())
                .thenLoginAnotherUser(futureUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_VISIBLE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * create voucher
     * open Advanced tab and click shop-only-voucher button
     * assert shop voucher is created from 'standard voucher' and shown in the list
     * assert there is no shop voucher available in SID
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.VOUCHER_AGREEMENT},
            dataProvider = "testDataProvider",
            description = "CLAIMSHOP-5147 Differentiate between which discounts on vouchers we show/sell in shop and in ECC")
    public void createShopOnlyVoucher(User user, Voucher voucher, Claim claim, ClaimItem claimItem) {
        loginFlow.loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherNameSP())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectCategoriesTab()
                .mapToCategory(claimItem.getCategoryBabyItems())
                .selectAdvancedTab()
                .createShopOnlyVoucher()
                .saveVoucherAgreement()
                .doAssert(tab -> tab.assertShopOnlyVoucherIsPresent(voucher.getVoucherNameSP()));

        new SupplierDialog().saveSupplier().logout();

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withNewPrice(PRICE_2400))
                .doAssert(a -> {
                    a.assertVoucherListed(voucher.getVoucherNameSP());
                    a.assertVoucherNotListed(voucher.getVoucherNameSP() + "_SHOP");
                });
    }


    static class VoucherAgreementData {
        private String existingSupplier;
        private Supplier newSupplier;
        private String voucherName;
        private Integer discount;
        private boolean voucherActive = true;
        private PseudoCategory pseudoCategory;
        private User creator;
        private User anotherUser;
        private VoucherAgreementState voucherAgreementState;

        private VoucherAgreementData(User creator, String existingSupplierName) {
            this.creator = creator;
            this.existingSupplier = existingSupplierName;
        }

        private VoucherAgreementData(User creator, Supplier newSupplier) {
            this.creator = creator;
            this.newSupplier = newSupplier;
        }

        static VoucherAgreementData.VoucherAgreementBuilder newBuilder(User creator, String existingSupplierName) {
            return new VoucherAgreementData(creator, existingSupplierName).new VoucherAgreementBuilder();
        }

        static VoucherAgreementData.VoucherAgreementBuilder newBuilder(User creator, Supplier supplier) {
            return new VoucherAgreementData(creator, supplier).new VoucherAgreementBuilder();
        }

        public boolean hasPseudoCategory() {
            return pseudoCategory != null;
        }

        public class VoucherAgreementBuilder {

            VoucherAgreementData.VoucherAgreementBuilder withVoucherActive(String voucherName, Integer discount) {
                VoucherAgreementData.this.voucherName = voucherName;
                VoucherAgreementData.this.discount = discount;
                return this;
            }

            VoucherAgreementData.VoucherAgreementBuilder withVoucherInactive(String voucherName, Integer discount) {
                VoucherAgreementData.this.voucherName = voucherName;
                VoucherAgreementData.this.discount = discount;
                VoucherAgreementData.this.voucherActive = false;
                return this;
            }

            VoucherAgreementData.VoucherAgreementBuilder mapToCategory(PseudoCategory pseudoCategory) {
                VoucherAgreementData.this.pseudoCategory = pseudoCategory;
                return this;
            }

            VoucherAgreementData.VoucherAgreementBuilder thenLoginAnotherUser(User client) {
                VoucherAgreementData.this.anotherUser = client;
                return this;
            }

            VoucherAgreementData.VoucherAgreementBuilder expectVoucherStateForAnotherUser(VoucherAgreementState voucherAgreementState) {
                VoucherAgreementData.this.voucherAgreementState = voucherAgreementState;
                return this;
            }

            public VoucherAgreementData build() {
                return VoucherAgreementData.this;
            }
        }
    }

    private AgreementsTab testVoucherVisibilityAcrossCompanies(VoucherAgreementData voucherAgreementData) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(voucherAgreementData.creator);

        String supplierName;

        /* open or create new supplier */
        GeneralTab generalTab;
        if (voucherAgreementData.newSupplier == null) {
            generalTab = suppliersPage.editSupplier(voucherAgreementData.existingSupplier).selectGeneralTab();
            supplierName = voucherAgreementData.existingSupplier;
        } else {
            generalTab = SharedEccAdminFlows.createSupplier(suppliersPage, voucherAgreementData.newSupplier);
            supplierName = voucherAgreementData.newSupplier.getSupplierName();
        }

        /* create new voucher for him */
        VoucherAgreementGeneralTab voucherAgreementGeneralTab = generalTab
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucherAgreementData.voucherName)
                            .withAgreementDiscount(voucherAgreementData.discount);
                })
                .createVoucherAgreement();
        Wait.waitForLoaded();

        if (!voucherAgreementData.voucherActive) {
            voucherAgreementGeneralTab
                    .fill(tab -> new VoucherAgreementGeneralTab.FormFiller(tab).withActive(false));
        }

        /* assign groups if needs */
        if (voucherAgreementData.hasPseudoCategory()) {
            voucherAgreementGeneralTab.selectCategoriesTab()
                    .mapToCategory(voucherAgreementData.pseudoCategory)
                    .selectGeneralTab();
        }

        /* close supplier and relogin */
        AgreementsTab agreementsTab = voucherAgreementGeneralTab.saveVoucherAgreement();
        if (voucherAgreementData.newSupplier == null) {
            agreementsTab.closeSupplier();
        } else {
            agreementsTab.saveSupplier();
        }
        suppliersPage
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucherAgreementData.voucherName))
                .logout();
        VouchersPage vouchersPage = loginFlow.loginToEccAdmin(voucherAgreementData.anotherUser).toVouchersPage();

        /* convert voucher state for voucher list page*/
        switch (voucherAgreementData.voucherAgreementState) {
            case ACTIVE:
            case NOT_ACTIVE:
                vouchersPage.doAssert(page -> page.assertVoucherPresent(voucherAgreementData.voucherName));
                break;
            case NOT_VISIBLE:
                vouchersPage.doAssert(page -> page.assertVoucherAbsent(voucherAgreementData.voucherName));
                return null;
        }

        /* convert voucher state in supplier window*/
        vouchersPage.toSuppliersPage()
                .editSupplier(supplierName)
                .selectAgreementsTab()
                .doAssert(tab -> {
                    switch (voucherAgreementData.voucherAgreementState) {
                        case ACTIVE:
                            tab.assertVoucherStatus(voucherAgreementData.voucherName, true);
                            break;
                        case NOT_ACTIVE:
                            tab.assertVoucherStatus(voucherAgreementData.voucherName, false);
                            break;
                        case NOT_VISIBLE:
                            tab.assertVoucherAbsent(voucherAgreementData.voucherName);
                    }
                });
        return BaseDialog.at(AgreementsTab.class);
    }

    public enum VoucherAgreementState {
        ACTIVE,
        NOT_ACTIVE,
        NOT_VISIBLE
    }

}

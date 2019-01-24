package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateVoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.VouchersPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.AttachmentFiles;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import java.util.Objects;

import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog.AdvancedTab.EVoucherOptions.EMAIL_REQUIRED;
import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog.AdvancedTab.EVoucherOptions.PERSONAL_CODE_REQUIRED;
import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog.AdvancedTab.EVoucherOptions.PHONE_REQUIRED;
import static com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog.AdvancedTab.EVoucherOptions.USE_PORTAL_REQUIRED;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-499")
public class VoucherAgreementTests extends BaseTest {

    private static final String AUTOTEST_SUPPLIER_VA_TESTS = "Autotest-Supplier-VA-Tests";

    @Test(dataProvider = "testDataProvider",
            description = "Create voucher with brands and tags and later use it in sid")
    public void charlie550_createVoucherWithBrandsAndTags(User user, Claim claim, ClaimItem claimItem, Voucher voucher){
        String brand = "brand_test";
        String tag = "tag_test";
        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectCategoriesTab()
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .selectCoverageTab()
                .setBrands(brand)
                .setTags(tag)
                .saveVoucherAgreement()
                .saveSupplier()
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucher.getVoucherGeneratedName()))
                .signOut();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText("item1")
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryGroupBorn())
                        .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                        .withVoucher(voucher.getVoucherGeneratedName()))
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 Vouchers list on Vouchers page contains new voucher")
    public void ecc3038_newVoucherInVouchersList(User user, Voucher voucher) {
        loginToEccAdmin(user).editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .saveVoucherAgreement()
                .saveSupplier()
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucher.getVoucherGeneratedName()));

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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to update all General tab data for new default voucher")
    public void ecc3038_newVoucherGeneralUpdate(User user, Voucher voucher, AttachmentFiles attachmentFiles) {
        int discount = 27;
        int faceValue = 15;
        int faceValueStep = 3;

        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
                .fill(tab -> {
                    new VoucherAgreementDialog.GeneralTab.FormFiller(tab)
                            .withActive(false)
                            .withDiscount(discount)
                            .withFaceValue(faceValue)
                            .withFaceValueStep(faceValueStep)
                            .withImage(attachmentFiles.getJpgFile2Loc())
                            .withLogo(attachmentFiles.getJpgFile2Loc());
                })
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
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
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to add Advanced data for new voucher")
    public void ecc3038_advancedDataAdding(User user, Voucher voucher) {
        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectAdvancedTab()
                .fill(tab -> {
                    new VoucherAgreementDialog.AdvancedTab.FormFiller(tab)
                            .useOrderEmail(VoucherAgreementDialog.AdvancedTab.OrderMailType.OTHER, "ipo@scalepoint.com")
                            .withDeliveryCost(10)
                            .withDeliveryType("Delivery type Text")
                            .withPopularity(5)
                            .useAsEVoucher(EMAIL_REQUIRED, PERSONAL_CODE_REQUIRED, PHONE_REQUIRED, USE_PORTAL_REQUIRED);
                })
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
                .selectAdvancedTab()
                .doAssert(tab -> {
                    tab.assertOrderType(VoucherAgreementDialog.AdvancedTab.OrderMailType.OTHER);
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to add Conditions and Limitations for new voucher")
    public void ecc3038_initialLegalAdding(User user, Voucher voucher) {
        String conditions = RandomStringUtils.randomAlphabetic(100);
        String limitations = RandomStringUtils.randomAlphabetic(140);

        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectLegalTab()
                .setConditions(conditions)
                .setLimitations(limitations)
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 All discount sent to IC if discount was changed and DD was customized before")
    public void ecc3038_discountUpdateClearDistribution(User user, Voucher voucher) {
        int discount = 25;
        int discountToIc = 10;
        int discountToCustomer = 15;

        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(discount);
                })
                .createVoucherAgreement()
                .selectDiscountDistributionTab()
                .doAssert(tab -> tab.assertDiscountToIc(discount))
                .setDiscountToIc(discountToIc)
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to remove assigned category for new voucher")
    public void ecc3038_removeAssignedCategory(User user, Voucher voucher, ClaimItem claimItem) {
        loginToEccAdmin(user)
                .editSupplier(Constants.getSupplierNameForVATests(user))
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucher.getVoucherGeneratedName())
                            .withAgreementDiscount(10);
                })
                .createVoucherAgreement()
                .selectCategoriesTab()
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .saveVoucherAgreement()
                .editVoucherAgreement(voucher.getVoucherGeneratedName())
                .selectCategoriesTab()
                .doAssert(tab -> tab.assertCategoryMapped(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr()))
                .removeMapping(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .doAssert(tab -> tab.assertCategoryNotMapped(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr()));
    }


    /**
     * GIVEN: SP and IC Users with Supply Manager credentials, SP supplier S1
     * WHEN: IC User creates voucher V1 for S1
     * THEN: SP user can't view V1 details
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to remove assigned category for new voucher")
    public void ecc3038_spSMCantOpenICVoucher(User futureUser,
                                              @UserCompany(CompanyCode.SCALEPOINT) User scalepointUser,
                                              ClaimItem claimItem,
                                              Supplier supplier,
                                              Voucher voucher) {

        VoucherAgreementData data = VoucherAgreementData.newBuilder(futureUser, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 Child IC voucher is  available for parent IC in Vouchers List")
    public void ecc3038_childICVoucherAvailableParentICVouchersList(@UserCompany(CompanyCode.TRYGFORSIKRING) User childUser,
                                                                    @UserCompany(CompanyCode.TRYGHOLDING) User parentUser,
                                                                    ClaimItem claimItem,
                                                                    Supplier supplier,
                                                                    Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(childUser, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 Parent IC voucher is  available for child IC in Vouchers List")
    public void ecc3038_parentICVoucherAvailableChildICVouchersList(@UserCompany(CompanyCode.TRYGFORSIKRING) User childUser,
                                                                    @UserCompany(CompanyCode.TRYGHOLDING) User parentUser,
                                                                    ClaimItem claimItem,
                                                                    Supplier supplier,
                                                                    Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(parentUser, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 It's possible to join left Shared Voucher for IC SM. Voucher gets status active")
    public void ecc3038_joinLeftSPVoucherActiveStatus(@UserCompany(CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .thenLoginAnotherUser(futureUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();

        String vname = voucher.getVoucherGeneratedName();

        SupplierDialog.AgreementsTab agreementsTab = testVoucherVisibilityAcrossCompanies(data);
        Objects.requireNonNull(agreementsTab)
                .doAssert(tab -> tab.assertVoucherStatus(vname, true))
                .doWithAgreement(vname, SupplierDialog.AgreementsTab.ActionType.LEAVE)
                .closeSupplier()
                .editSupplier(supplier.getSupplierName())
                .selectAgreementsTab()
                .doAssert(tab -> tab.assertVoucherStatus(vname, false))
                .doWithAgreement(vname, SupplierDialog.AgreementsTab.ActionType.JOIN)
                .doAssert(tab -> tab.assertVoucherStatus(vname, true));
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: SP User creates V1 without any assigned categories
     * THEN: V1 active status is No for IC User
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 SP voucher active status is Noe for IC SM if no categories are assigned to Voucher")
    public void ecc3038_inactiveSPVoucherNoCatAssigned(@UserCompany(CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 Voucher left by IC1 is active for IC2")
    public void ecc3038_voucherLeftByIC1ActiveIC2(@UserCompany(CompanyCode.SCALEPOINT) User sharedAgreementOwner, User futureUser1, User futureUser2, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(sharedAgreementOwner, supplier)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .thenLoginAnotherUser(futureUser1)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.ACTIVE)
                .build();

        String vname = voucher.getVoucherGeneratedName();
        SupplierDialog.AgreementsTab agreementsTab = testVoucherVisibilityAcrossCompanies(data);
        Objects.requireNonNull(agreementsTab)
                .doWithAgreement(vname, SupplierDialog.AgreementsTab.ActionType.LEAVE)
                .doAssert(tab -> tab.assertVoucherStatus(vname, false))
                .closeSupplier()
                .logout();

        loginToEccAdmin(futureUser2)
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 IC1 voucher is not available for IC2 in SP Supplier's dialog")
    public void ecc3038_ic1VoucherNotAvailableIC2(User futureUser1, User futureUser2, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(futureUser1, AUTOTEST_SUPPLIER_VA_TESTS)
                .withVoucherActive(voucher.getVoucherGeneratedName(), 10)
                .thenLoginAnotherUser(futureUser2)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_VISIBLE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials, SP Voucher V1
     * WHEN: SP User sets V1 as inactive
     * THEN: V1 is not available for IC User in Supplier dialog
     *
     * ecc3038_inactiveSPVoucherNotAvailableICVouchersList
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3038 Inactive SP voucher is not available for IC SM in Suppliers dialog")
    public void ecc3038_inactiveSPVoucherNotAvailableIC(@UserCompany(CompanyCode.SCALEPOINT) User scalepointUser, User futureUser, ClaimItem claimItem, Supplier supplier, Voucher voucher) {
        VoucherAgreementData data = VoucherAgreementData.newBuilder(scalepointUser, supplier)
                .withVoucherInactive(voucher.getVoucherGeneratedName(), 10)
                .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                .thenLoginAnotherUser(futureUser)
                .expectVoucherStateForAnotherUser(VoucherAgreementState.NOT_VISIBLE)
                .build();
        testVoucherVisibilityAcrossCompanies(data);
    }


    static class VoucherAgreementData {
        private String existingSupplier;
        private Supplier newSupplier;
        private String voucherName;
        private Integer discount;
        private boolean voucherActive = true;
        private String categoryGroup;
        private String categoryName;
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

            VoucherAgreementData.VoucherAgreementBuilder mapToCategory(String categoryGroup, String categoryName) {
                VoucherAgreementData.this.categoryGroup = categoryGroup;
                VoucherAgreementData.this.categoryName = categoryName;
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

    private SupplierDialog.AgreementsTab testVoucherVisibilityAcrossCompanies(VoucherAgreementData voucherAgreementData) {
        SuppliersPage suppliersPage = loginToEccAdmin(voucherAgreementData.creator);

        String supplierName;

        /* open or create new supplier */
        SupplierDialog.GeneralTab generalTab;
        if (voucherAgreementData.newSupplier == null) {
            generalTab = suppliersPage.editSupplier(voucherAgreementData.existingSupplier);
            supplierName = voucherAgreementData.existingSupplier;
        } else {
            generalTab = SharedEccAdminFlows.createSupplier(suppliersPage, voucherAgreementData.newSupplier);
            supplierName = voucherAgreementData.newSupplier.getSupplierName();
        }

        /* create new voucher for him */
        VoucherAgreementDialog.GeneralTab voucherAgreementGeneralTab = generalTab.selectAgreementsTab()
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
                    .fill(tab -> new VoucherAgreementDialog.GeneralTab.FormFiller(tab).withActive(false));
        }

        /* assign groups if needs */
        if (StringUtils.isNoneBlank(voucherAgreementData.categoryGroup)) {
            voucherAgreementGeneralTab.selectCategoriesTab()
                    .mapToCategory(voucherAgreementData.categoryGroup, voucherAgreementData.categoryName)
                    .selectGeneralTab();
        }

        /* close supplier and relogin */
        SupplierDialog.AgreementsTab agreementsTab = voucherAgreementGeneralTab.saveVoucherAgreement();
        if (voucherAgreementData.newSupplier == null) {
            agreementsTab.closeSupplier();
        } else {
            agreementsTab.saveSupplier();
        }
        suppliersPage
                .toVouchersPage()
                .doAssert(page -> page.assertVoucherPresent(voucherAgreementData.voucherName))
                .logout();
        VouchersPage vouchersPage = loginToEccAdmin(voucherAgreementData.anotherUser).toVouchersPage();

        /* test voucher state for voucher list page*/
        switch (voucherAgreementData.voucherAgreementState) {
            case ACTIVE:
            case NOT_ACTIVE:
                vouchersPage.doAssert(page -> page.assertVoucherPresent(voucherAgreementData.voucherName));
                break;
            case NOT_VISIBLE:
                vouchersPage.doAssert(page -> page.assertVoucherAbsent(voucherAgreementData.voucherName));
                return null;
        }

        /* test voucher state in supplier window*/
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
        return BaseDialog.at(SupplierDialog.AgreementsTab.class);
    }

    public enum VoucherAgreementState {
        ACTIVE,
        NOT_ACTIVE,
        NOT_VISIBLE
    }

}

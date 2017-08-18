package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.EditVoucherValuationDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi;
import com.scalepoint.automation.services.externalapi.VoucherAgreementApi.AssignedCategory;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Category;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Constants.DEPRECIATION_10;
import static com.scalepoint.automation.utils.Constants.PRICE_100_000;
import static com.scalepoint.automation.utils.Constants.PRICE_2400;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-512")
@RequiredSetting(type = FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG)
@RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
public class SidTests extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to assign existing category for new voucher and select categories in Add/Edit dialog")
    public void ecc3025_selectVoucherExistingCatAddDialog(User user, Claim claim, Voucher voucher) {
        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);
        loginAndCreateClaim(user, claim)
                .openSid()
                .setCategory(categoryInfo)
                .doAssert(sid -> sid.assertVoucherListed(voucher.getVoucherGeneratedName()));
    }

    /**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * WHEN: User adds new price P1
     * WHEN: User adds depreciation D1
     * THAN: Cash compensation is P1 - V1 discount - D1
     * THAN: Depreciation is D1 amount of Cash Compensation
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Cash compensation with depreciation field value is (New price minus voucher percent)" +
            " - depreciation percent if voucher selected in Add settlement dialog")
    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    public void ecc3025_cashCompensationWithAddedDepVoucher(User user, Claim claim, Voucher voucher) {
        AssignedCategory categoryInfo = new VoucherAgreementApi(user).createVoucher(voucher);
        SidCalculator.VoucherValuationWithDepreciation expectedCalculations =
                SidCalculator.calculateVoucherValuation(PRICE_2400, Constants.VOUCHER_DISCOUNT_10, Constants.DEPRECIATION_10);
        Double calculatedCashValue = expectedCalculations.getCashCompensationWithDepreciation();
        Double calculatedDepreciationValue = expectedCalculations.getDepreciatedAmount();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCustomerDemandPrice(PRICE_100_000)
                            .withNewPrice(PRICE_2400)
                            .withDepreciation(DEPRECIATION_10)
                            .withCategory(categoryInfo)
                            .withVoucher(voucher.getVoucherGeneratedName());
                })
                .doAssert(sid -> {
                    sid.assertCashValueIs(calculatedCashValue);
                    sid.assertDepreciationAmountIs(calculatedDepreciationValue);
                });
    }

    /**
     * GIVEN: Voucher1's supplier has shop with zip code that is eqaul to customer zip code
     * GIVEN: Voucher2 suplier's zip code has a valid distance with customer zip code
     * WHEN user selects Voucher1
     * THAN the distance is "0"
     * WHEN user selects Voucher2
     * THAN the distance is equal to predefined value
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to calculate shop distance in Settlement dialog")
    public void ecc3025_calculateShopDistance(User user, Claim claim, ClaimItem item, Voucher voucher) {
        // default postal code is 5000
        String existingVoucher = voucher.getExistingVoucher_10();
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withCustomerDemandPrice(1000.00)
                            .withNewPrice(100.00)
                            .withVoucher(existingVoucher);
                });
        SettlementPage settlementPage = settlementDialog.doAssert(sid -> sid.assertVoucherDropdownWithoutDistance(existingVoucher)).closeSidWithOk();
        changePostalCodeAndReturnToSid(settlementPage, "3000", claim)
                .doAssert(sid -> sid.assertVoucherDropdownKnowsDistance(existingVoucher, 203))
                .closeSidWithOk();

        changePostalCodeAndReturnToSid(settlementPage, "6000", claim)
                .doAssert(sid -> sid.assertVoucherDropdownKnowsDistance(existingVoucher, 72))
                .closeSidWithOk();
    }

    private SettlementDialog changePostalCodeAndReturnToSid(SettlementPage settlementPage, String postalCode, Claim claim) {
        return settlementPage
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .enterZipCode(postalCode)
                .saveClaim()
                .openRecentClaim()
                .reopenClaim()
                .findClaimLine(Constants.TEXT_LINE)
                .editLine();
    }

    /**
     * GIVEN: Voucher1's supplier has shop with zip code that is eqaul to customer zip code
     * GIVEN: Voucher2 suplier's zip code has a valid distance with customer zip code
     * WHEN user searches for Shop with Voucher1 option and predefined Zip code (Supplier has a shop with zip code equal
     * to customer's zip code
     * THAN it's possible to find shop with distance is "0"
     * WHEN user searches for Shop with Voucher2 option and predefined Zip code
     * THAN it's possible to find shop with distance as predefined value
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to calculate shop distance in Settlement dialog")
    public void ecc3025_findShopInDialog(User user, Claim claim, ClaimItem item, Voucher voucher) {
        // default postal code is 5000
        String existingVoucher = voucher.getExistingVoucher_10();
        String existingVoucherShopName = "Test shop " + existingVoucher;
        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withNewPrice(100.00)
                            .withVoucher(existingVoucher);
                })
                .openFindShopDialog()
                .doAssert(findShop -> {
                    findShop.assertDistanceToShopIs(existingVoucher, existingVoucherShopName, "6000", 72);
                    findShop.assertDistanceToShopIs(existingVoucher, existingVoucherShopName, "3000", 203);
                    findShop.assertDistanceToShopIs(existingVoucher, existingVoucherShopName, "1000", 166);
                    findShop.assertDistanceToShopIs(existingVoucher, existingVoucherShopName, "9990", 366);
                });
    }

    /**
     * GIVEN: "Move the Discount Distribution to the Voucher replacement dialog box" is disabled
     * GIVEN: Existing Voucher with specified group and Category
     * GIVEN: Voucher's DD is default
     * WHEN: User opens Vouchers Replacement Dialog
     * THEN: Face value = New Price
     * THEN: Cash value = New Price - Voucher's discount
     * WHEN: User opens Edit Discount Dialog
     * THEN: Face value = New Price
     * THEN: Cash value = New Price - Voucher's discount
     * WHEN: User changes Customer Discount
     * THEN: Company discount = Voucher discount - new Customer discount
     * THEN: Cash Value =  price - (price * icDiscount)/100
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100
     * WHEN: User saves DtDn changes in Edit Discount Dialog by closing it with OK
     * THEN: Cash Value =  price - (price * icDiscount)/100 (It is new because DtDn was changed)
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100 (It is new because DtDn was changed)
     * WHEN: User saves DtDn changes in Voucher Replacement Dialog by closing it with OK
     * THEN: Cash Value =  price - (price * icDiscount)/100 (It is new because DtDn was changed)
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100 (It is new because DtDn was changed)
     */
    @RequiredSetting(type = MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to calculate shop distance in Settlement dialog")
    public void ecc3025_voucherRepDialogDDDialogFVCV(User user, Claim claim, ClaimItem item, Voucher voucher) {
        // default postal code is 5000
        String existingVoucher = voucher.getExistingVoucher_10();

        int voucherDiscount = 10;
        int customerDiscount = 5;
        int companyDiscount = 5;

        SidCalculator.VoucherValuation voucherValuation =
                SidCalculator.calculateVoucherValuationWithDiscountDistribution(Constants.PRICE_2400, voucherDiscount, companyDiscount);

        testDiscountDistributionUpdate(user, claim, item, existingVoucher, customerDiscount, voucherValuation, true);
    }

    /**
     * GIVEN: "Move the Discount Distribution to the Voucher replacement dialog box" is enabled
     * GIVEN: Existing Voucher with specified group and Category
     * GIVEN: Voucher's DD is default
     * WHEN: User opens Vouchers Replacement Dialog
     * THEN: Face value = New Price
     * THEN: Cash value = New Price - Voucher's discount
     * WHEN: User opens Edit Discount Dialog
     * THEN: Face value = New Price
     * THEN: Cash value = New Price - Voucher's discount
     * WHEN: User changes Customer Discount
     * THEN: Company discount = Voucher discount - new Customer discount
     * THEN: Cash Value =  price - (price * icDiscount)/100
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100
     * WHEN: User saves DtDn changes in Edit Discount Dialog by closing it with OK
     * THEN: Cash Value =  price - (price * icDiscount)/100 (It is new because DtDn was changed)
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100 (It is new because DtDn was changed)
     * WHEN: User saves DtDn changes in Voucher Replacement Dialog by closing it with OK
     * THEN: Cash Value =  price - (price * icDiscount)/100 (It is new because DtDn was changed)
     * THEN: Face Value = Cash Value / (1 - comDiscount / 100 (It is new because DtDn was changed)
     */
    @RequiredSetting(type = MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG)
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 It's possible to calculate shop distance in Settlement dialog")
    public void ecc3025_voucherRepDialogCustomDtDnDialogFVCV2(User user, Claim claim, ClaimItem item, Voucher voucher) {
        // default postal code is 5000
        String existingVoucher = voucher.getExistingVoucher_10();

        int voucherDiscount = 10;
        int customerDiscount = 5;
        int companyDiscount = 5;

        SidCalculator.VoucherValuation voucherValuation =
                SidCalculator.calculateVoucherValuationWithDiscountDistribution(Constants.PRICE_2400, voucherDiscount, companyDiscount);

        testDiscountDistributionUpdate(user, claim, item, existingVoucher, customerDiscount, voucherValuation, false);
    }

    /**
     * GIVEN: New Supplier S1
     * GIVEN: New voucher V1 of S1
     * WHEN: User opens Terms and Conditions dialog for V1
     * THEN: Terms and Conditions data contains correct V1 name
     * THEN: Terms and Conditions data contains correct V1 discount
     * THEN: Terms and Conditions data contains correct S1 telephone number
     */
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Voucher's Terms and Conditions contain correct info about voucher and supplier")
    public void ecc3025_voucherTradesTermsConditionsCorrectInfo(User user, Claim claim, Supplier supplier, Voucher voucher, ClaimItem claimItem) {
        String conditionsText = "Autotest Sample Conditions";
        int discount = 10;
        String voucherName = voucher.getVoucherGeneratedName();
        String supplierPhone = supplier.getSupplierPhone();

        SuppliersPage suppliersPage = login(getSystemUser())
                .getMainMenu()
                .toEccAdminPage()
                .toSuppliersPage();

        SupplierDialog.GeneralTab generalTab = SharedEccAdminFlows.createSupplier(suppliersPage, supplier);
        SharedEccAdminFlows.createVoucherAgreement(generalTab,
                SharedEccAdminFlows.VoucherAgreementData.newBuilder(voucher, discount)
                        .mapToCategory(claimItem.getCategoryGroupBorn(), claimItem.getCategoryBornBabyudstyr())
                        .withTermsAndConditions(conditionsText)
                        .build())
                .saveSupplier()
                .logout();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(PRICE_2400)
                            .withCategory(claimItem.getCategoryGroupBorn())
                            .withSubCategory(claimItem.getCategoryBornBabyudstyr())
                            .withVoucher(voucherName);
                })
                .openVoucherTermAndConditions()
                .doAssert(tcdialog -> {
                    tcdialog.assertTermsAndConditionsTextIs(conditionsText);
                    tcdialog.assertOfferBoxContainsCorrectPercentage(discount);
                    tcdialog.assertOfferBoxContainsVoucherName(voucherName);
                    tcdialog.assertQuestionsBoxContainsCorrectPhone(supplierPhone);
                });
    }

    /**
     * GIVEN: New category C1 with random group G1 without mapped vouchers
     * WHEN: User selects C1 and G1 in Settlement dialog
     * WHEN: User adds new price P1
     * THAN: Cash compensation is P1
     * THAN: Depreciation is 0.00
     */
    @SuppressWarnings("AccessStaticViaInstance")
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Cash compensation without depreciation are New price if no vouchers selected in Add settlement dialog")
    public void ecc3025_cashCompensationWithoutDepNoVoucher(User user, Claim claim, Category category) {
        login(user, AdminPage.class)
                .createPsModelWithCategoryAndEnable(category, "All Categories")
                .logout();

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(PRICE_2400)
                            .withCategory(category.getGroupName())
                            .withSubCategory(category.getCategoryName());
                })
                .doAssert(sid -> {
                            sid.assertCashValueIs(PRICE_2400);
                            sid.assertDepreciationAmountIs(0.00);
                        }
                );
    }

    /**
     * GIVEN: Existing category C1 with existing group G1 and mapped to G1-C1 voucher V1
     * WHEN: User selects C1, G1 and V1 in Settlement dialog
     * WHEN: User adds new price P1
     * THAN: Cash compensation is P1 - V1 discount
     * THAN: Depreciation is 0.00
     */
    @SuppressWarnings("AccessStaticViaInstance")
    @Test(dataProvider = "testDataProvider", description = "ECC-3025 Cash compensation without depreciation are New price minus voucher percent if voucher selected in Add settlement dialog")
    public void ecc3025_cashCompensationWithoutDepVoucher(User user, Claim claim, ClaimItem item) {
        SidCalculator.VoucherValuationWithDepreciation voucherValuation = SidCalculator.calculateVoucherValuation(
                Constants.PRICE_2400,
                Constants.VOUCHER_DISCOUNT_10,
                0
        );

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(Constants.PRICE_2400)
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withVoucher(item.getExistingVoucher_10());
                })
                .doAssert(sid -> {
                    sid.assertCashValueIs(voucherValuation.getCashCompensationOfVoucher());
                    sid.assertDepreciationAmountIs(0.0);
                });
    }


    private void testDiscountDistributionUpdate(User user, Claim claim, ClaimItem item, String existingVoucher, int customerDiscount, SidCalculator.VoucherValuation voucherValuation, boolean dialogMode) {
        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withNewPrice(PRICE_2400)
                            .withVoucher(existingVoucher);
                });

        if (dialogMode) {
            settlementDialog.distributeDiscountForVoucherValuationWithDialog(EditVoucherValuationDialog.DistributeTo.CUSTOMER, customerDiscount)
                    .doAssert(discountDialog -> {
                        discountDialog.assertCashValueIs(voucherValuation.getCashValue());
                        discountDialog.assertFaceValueIs(voucherValuation.getFaceValue());
                    })
                    .saveDiscountDistribution()
                    .saveVoucherValuation();
        } else {
            settlementDialog.distributeDiscountForVoucherValuation(EditVoucherValuationDialog.DistributeTo.CUSTOMER, customerDiscount)
                    .doAssert(dialog -> {
                        dialog.assertVoucherCashValueIs(voucherValuation.getCashValue());
                        dialog.assertVoucherFaceValueIs(voucherValuation.getFaceValue());
                    });
        }

        settlementDialog.parseValuationRow(SettlementDialog.Valuation.VOUCHER)
                .doAssert(row -> {
                    row.assertCashCompensationIs(voucherValuation.getCashValue());
                })
                .doAssert(sid -> {
                    sid.assertVoucherCashValueIs(voucherValuation.getCashValue());
                    sid.assertVoucherFaceValueIs(voucherValuation.getFaceValue());
                });
    }

}

package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.AddShopDialogViewMode;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.SupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.AgreementsTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.BannerTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.GeneralTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.OrdersTab;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.VouchersPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.SupplierCompany;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.AttachmentFiles;
import com.scalepoint.automation.utils.data.entity.input.SimpleSupplier;
import com.scalepoint.automation.utils.data.entity.input.Supplier;
import com.scalepoint.automation.utils.data.entity.input.Voucher;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-521")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-522")
public class SupplierTests extends BaseUITest {

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User fills new supplier form with valid data
     * WHEN: and selects Create option
     * WHEN: and saves Supplier changes
     * THEN: New Supplier is displayed in suppliers list
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to create new supplier. Suppliers list contains new supplier")
    public void ecc3037_createNewSupplier(User user, Supplier supplier) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()));
    }

    /**
     * GIVEN: Supplier with filled general data
     * WHEN: User updates general data
     * THEN: Updated General data stored correctly
     */

    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to update all general data for new supplier")
    public void ecc3037_updateSupplierGeneralData(User user, Supplier supplier1, Supplier supplier2) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);
        String updatedWebsite = "http://google.com";
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier1)
                .saveSupplier()
                .editSupplier(supplier1.getSupplierName())
                .selectGeneralTab()
                .fill(editSupplierDialog -> {
                    new GeneralTab.FormFiller(editSupplierDialog)
                            .withSupplierName(supplier2.getSupplierName())
                            .withCvr(supplier2.getSupplierCVR())
                            .withAddress1(supplier2.getAddress1())
                            .withAddress2(supplier2.getAddress2())
                            .withCity(supplier2.getCity())
                            .withPostalCode(supplier2.getPostCode())
                            .withWebsite(updatedWebsite);
                })
                .saveSupplier()
                .editSupplier(supplier2.getSupplierName())
                .selectGeneralTab()
                .doAssert(generalTab -> {
                    generalTab.assertCity(supplier2.getCity());
                    generalTab.assertPostalCode(supplier2.getPostCode());
                    generalTab.assertCvr(supplier2.getSupplierCVR());
                    generalTab.assertAddress(supplier2.getAddress1(), supplier2.getAddress2());
                    generalTab.assertWebsite(updatedWebsite);
                });
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new Supplier
     * WHEN: and fills all General tab with valid data
     * WHEN: and saves Supplier changes
     * THEN: General data stored correctly
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to create new supplier. Suppliers list contains new supplier")
    public void ecc3037_createNewSupplierCompleteGeneral(User user, Supplier supplier, AttachmentFiles attachmentFiles) {
        String webSite = "http://google.com";
        String attachmentImage = attachmentFiles.getJpgFile2Loc();

        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .setWebsite(webSite)
                .uploadLogo(attachmentImage)
                .doAssert(GeneralTab.Asserts::assertLogoPresent)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()));
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User adds banner data for random supplier
     * THEN: Banner Data is stored correctly
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 It is possible to add Banner data")
    public void ecc3037_bannerDataAdding(User user, Supplier supplier, AttachmentFiles attachmentFiles) {
        String attachmentImage = attachmentFiles.getJpgFile2Loc();

        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .selectBannerTab()
                .uploadBanner(attachmentImage)
                .doAssert(BannerTab.Asserts::assertBannerIsPresent)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()));
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User creates new supplier S1
     * WHEN: User fills Orders data with valid values
     * WHEN: User creates new voucher V1
     * WHEN: User saves S! and V1 changes
     * THEN: Orders Data is stored correctly
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to fill orders tab with valid values")
    public void ecc3037_detailedOrder(User user, Supplier supplier, Voucher voucher) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);

        GeneralTab generalTabTab = SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .selectOrdersTab()
                .setOrderEmail(supplier.getSupplierEmail())
                .setOrderMailFormat(SupplierDialog.OrderMailFormat.XML_MAIL_BODY)
                .setDefaultDeliveryTime(7)
                .useProductsAsVouchers()
                .selectRadioOrderService()
                .selectGeneralTab();

        SharedEccAdminFlows.createVoucherAgreement(generalTabTab, SharedEccAdminFlows.VoucherAgreementData.newBuilder(voucher, 10).build())
                .saveSupplier()
                .toSuppliersPage()
                .editSupplier(supplier.getSupplierName())
                .selectOrdersTab()
                .doAssert(OrdersTab.Asserts::assertOldOrderFlowItemsDisabled)
                .selectRadioOldOrderFlow()
                .doAssert(ordersTab -> {
                    ordersTab.assertOrderEmailIs(supplier.getSupplierEmail());
                    ordersTab.assertOrderEmailFormatIs(SupplierDialog.OrderMailFormat.XML_MAIL_BODY);
                    ordersTab.assertDeliveryTimeIs(7);
                    ordersTab.assertProductsUsedAsVouchers();
                });
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: IC User creates supplier S1
     * THEN: S1 is not available for SP User
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER, UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "ECC-3037 IC supplier is not available for SP")
    public void ecc3037_icSupplierUnavailableForSP(@UserAttributes(company = CompanyCode.SCALEPOINT) User spUser, User futureUser, Supplier supplier) {
        checkVisibility(futureUser, spUser, supplier, false);
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: IC User creates supplier S1
     * THEN: S1 is not available for SP User
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER, TestGroups.SUPPLIER}, dataProvider = "testDataProvider",
            description = "ECC-3037 IC supplier is not available for another IC")
    public void ecc3037_icSupplierUnavailableForIC2(User futureUser1, User futureUser2, Supplier supplier) {
        checkVisibility(futureUser1, futureUser2, supplier, false);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC1 parent user creates supplier S1
     * THEN: S1 is available for IC1 child user
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.TRYGHOLDING,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "ECC-3037 Parent IC supplier is available for child IC")
    public void ecc3037_parentICSupplierAvailableForChildIC(@UserAttributes(company = CompanyCode.TRYGHOLDING) User parentCompanyUser, @UserAttributes(company = CompanyCode.TRYGFORSIKRING) User childCompanyUser, Supplier supplier) {
        checkVisibility(parentCompanyUser, childCompanyUser, supplier, true);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC1 child user creates supplier S1
     * THEN: S1 is available for IC1 parent user
     */
    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.TRYGHOLDING,
            UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "ECC-3037 Child IC supplier is available for parent IC")
    public void ecc3037_childICSupplierAvailableForParentIC(@UserAttributes(company = CompanyCode.TRYGHOLDING) User parentCompanyUser, @UserAttributes(company = CompanyCode.TRYGFORSIKRING) User childCompanyUser, Supplier supplier) {
        checkVisibility(childCompanyUser, parentCompanyUser, supplier, true);
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "Voucher tick should not be visible in supply management, suppliers list")
    public void ecc3039_voucherTickIsNotAvailableInSuppliersList(
            @UserAttributes(company = CompanyCode.SCALEPOINT) User user, @SupplierCompany(areWithVouchers = false) SimpleSupplier simpleSupplier) {
        loginFlow.loginToEccAdmin(user)
                .doAssert(asserts -> asserts.assertsIsVoucherTickForSupplierNotDisplayed(simpleSupplier.getName()));
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.BAUTA},
            dataProvider = "testDataProvider")
    public void ecc3039_exclusiveTickIsAvailableForIC(@UserAttributes(company = CompanyCode.BAUTA) User user, @SupplierCompany(CompanyCode.BAUTA) SimpleSupplier simpleSupplier) {
        final String supplierName = simpleSupplier.getName();
        final String agreement = simpleSupplier.getAgreement();

        loginFlow.loginToEccAdmin(user)
                .toSuppliersPage()
                .doAssert(asserts -> asserts.assertsIsExclusiveTickForSupplierDisplayed(supplierName))  // Exclusive tick should be visible in supply management, suppliers list

                .toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsExclusiveTickForVoucherDisplayed(agreement));     // Exclusive tick should be visible in supply management, vouchers list
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider",
            description = "Check if invoiceSetting is set correctly")
    public void contents3950_settingInvoiceSettingTest(@UserAttributes(company = CompanyCode.SCALEPOINT) User user) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(user);
        suppliersPage.openFirstSupplier()
                .selectOrdersTab()
                .selectInvoiceSetting(0)
                .saveSupplier();

        suppliersPage.openFirstSupplier()
                .selectOrdersTab()
                .doAssert(a -> a.assertInvoiceSettingIs(0))
                .selectInvoiceSetting(1)
                .saveSupplier();

        suppliersPage.openFirstSupplier()
                .selectOrdersTab()
                .doAssert(a -> a.assertInvoiceSettingIs(1));
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.SCALEPOINT},
            dataProvider = "testDataProvider")
    public void ecc3039_sharedDataAreEditableForScalepoint(@UserAttributes(company = CompanyCode.SCALEPOINT) User user, SimpleSupplier simpleSupplier) {
        final String supplierName = simpleSupplier.getName();
        final String scalepointAgreement = simpleSupplier.getScalepointAgreement();
        final String inactiveAgreement = simpleSupplier.getInactiveAgreement();

        loginFlow.loginToEccAdmin(user)
                .toSuppliersPage()
                .doAssert(SuppliersPage.Asserts::assertsIsExclusiveColumnNotDisplayed)                          // Exclusive should not be visible in supply management, suppliers list

                .editSupplier(supplierName)
                .selectAgreementsTab()
                .doAssert(asserts -> asserts.assertIsExclusiveTickForVoucherNotVisible(scalepointAgreement))   // Exclusive tick for voucher should be not visible on agreements tab when open supplier from suppliers list
                .cancelSupplier()

                .toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsNotActiveTickForVoucherDisplayed(inactiveAgreement))  // Active tick should be not visible in supply management, vouchers list
                .doAssert(VouchersPage.Asserts::assertsIsExclusiveColumnNotDisplayed);                      // Exclusive should not be visible in supply management, voucher list
    }

    @Test(groups = {TestGroups.SUPPLIER_MANAGER,
            TestGroups.SUPPLIER,
            UserCompanyGroups.BAUTA},
            dataProvider = "testDataProvider")
    public void ecc3039_sharedDataAreInViewModeForIC(@UserAttributes(company = CompanyCode.BAUTA) User user, SimpleSupplier simpleSupplier) {
        final String supplierName = simpleSupplier.getName();
        final String agreement = simpleSupplier.getAgreement();

        loginFlow.loginToEccAdmin(user)
                .doAssert(SuppliersPage.Asserts::assertsIsToMatchingEngineLinkDisplayed)                //To matching engine link should be visible in supply management

                .toSuppliersPage()
                .doAssert(SuppliersPage.Asserts::assertsIsExclusiveColumnDisplayed)                     // Exclusive should be visible in supply management, suppliers list
                .doAssert(asserts -> asserts.assertsIsVoucherTickForSupplierDisplayed(supplierName))    // Voucher tick should be visible in supply management, suppliers list

                .editSupplier(supplierName)
                .selectGeneralTab()
                .doAssert(GeneralTab.Asserts::assertIsDialogNotEditable)                 // GeneralTab data shouldn't be editable

                .selectShopsTab()
                .openShopViewModel(simpleSupplier.getShopName())
                .doAssert(AddShopDialogViewMode.Asserts::assertIsShopDialogNotEditable)                 // Shop data shouldn't be editable
                .cancelViewShopDialog()

                .selectAgreementsTab()
                .doAssert(AgreementsTab.Asserts::assertIsExclusiveTickForVoucherVisible) // Exclusive tick for voucher should be visible on agreements tab when open supplier from suppliers list
                .closeSupplier()

                .toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsActiveTickForVoucherDisplayed(agreement))         // Active tick should be visible in supply management, vouchers list
                .doAssert(VouchersPage.Asserts::assertsIsExclusiveColumnDisplayed);                     // Exclusive should be visible in supply management, voucher list
    }

    private void checkVisibility(User userWhoCreates, User userWhoReads, Supplier supplier, boolean mustBeVisible) {
        SuppliersPage suppliersPage = loginFlow.loginToEccAdmin(userWhoCreates);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()))
                .logout();

        loginFlow.loginToEccAdmin(userWhoReads).doAssert(page -> {
            if (mustBeVisible) {
                page.assertSupplierPresent(supplier.getSupplierName());
            } else {
                page.assertSupplierAbsent(supplier.getSupplierName());
            }
        });
    }
}

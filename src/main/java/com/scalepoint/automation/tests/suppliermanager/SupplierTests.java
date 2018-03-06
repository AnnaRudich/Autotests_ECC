package com.scalepoint.automation.tests.suppliermanager;

import com.scalepoint.automation.pageobjects.dialogs.eccadmin.AddShopDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.VouchersPage;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.SharedEccAdminFlows;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.SupplierCompany;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.entity.AttachmentFiles;
import com.scalepoint.automation.utils.data.entity.SimpleSupplier;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.Voucher;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

@Jira("https://jira.scalepoint.com/browse/CHARLIE-521")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-522")
public class SupplierTests extends BaseTest {

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User fills new supplier form with valid data
     * WHEN: and selects Create option
     * WHEN: and saves Supplier changes
     * THEN: New Supplier is displayed in suppliers list
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to create new supplier. Suppliers list contains new supplier")
    public void ecc3037_createNewSupplier(User user, Supplier supplier) {
        SuppliersPage suppliersPage = loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()));
    }

    /**
     * GIVEN: Supplier with filled general data
     * WHEN: User updates general data
     * THEN: Updated General data stored correctly
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to update all general data for new supplier")
    public void ecc3037_updateSupplierGeneralData(User user, Supplier supplier1, Supplier supplier2) {
        SuppliersPage suppliersPage = loginToEccAdmin(user);
        String updatedWebsite = "http://google.com";
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier1)
                .saveSupplier()
                .editSupplier(supplier1.getSupplierName())
                .fill(editSupplierDialog -> {
                    new SupplierDialog.GeneralTab.FormFiller(editSupplierDialog)
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to create new supplier. Suppliers list contains new supplier")
    public void ecc3037_createNewSupplierCompleteGeneral(User user, Supplier supplier, AttachmentFiles attachmentFiles) {
        String webSite = "http://google.com";
        String attachmentImage = attachmentFiles.getJpgFile2Loc();

        SuppliersPage suppliersPage = loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .setWebsite(webSite)
                .uploadLogo(attachmentImage)
                .doAssert(SupplierDialog.GeneralTab.Asserts::assertLogoPresent)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()));
    }

    /**
     * GIVEN: User with Supply Manager credentials
     * WHEN: User adds banner data for random supplier
     * THEN: Banner Data is stored correctly
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 It is possible to add Banner data")
    public void ecc3037_bannerDataAdding(User user, Supplier supplier, AttachmentFiles attachmentFiles) {
        String attachmentImage = attachmentFiles.getJpgFile2Loc();

        SuppliersPage suppliersPage = loginToEccAdmin(user);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .selectBannerTab()
                .uploadBanner(attachmentImage)
                .doAssert(SupplierDialog.BannerTab.Asserts::assertBannerIsPresent)
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
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 It's possible to fill orders tab with valid values")
    public void ecc3037_detailedOrder(User user, Supplier supplier, Voucher voucher) {
        SuppliersPage suppliersPage = loginToEccAdmin(user);

        SupplierDialog.GeneralTab generalTabTab = SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .selectOrdersTab()
                .setOrderEmail(supplier.getSupplierEmail())
                .setOrderMailFormat(SupplierDialog.OrderMailFormat.XML_MAIL_BODY)
                .setDefaultDeliveryTime(7)
                .useFreightPrice()
                .useProductsAsVouchers()
                .selectGeneralTab();

        SharedEccAdminFlows.createVoucherAgreement(generalTabTab, SharedEccAdminFlows.VoucherAgreementData.newBuilder(voucher, 10).build())
                .saveSupplier()
                .toSuppliersPage()
                .editSupplier(supplier.getSupplierName())
                .selectOrdersTab()
                .doAssert(ordersTab -> {
                    ordersTab.assertOrderEmailIs(supplier.getSupplierEmail());
                    ordersTab.assertOrderEmailFormatIs(SupplierDialog.OrderMailFormat.XML_MAIL_BODY);
                    ordersTab.assertDeliveryTimeIs(7);
                    ordersTab.assertFreightPriceUsed();
                    ordersTab.assertProductsUsedAsVouchers();
                });
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: IC User creates supplier S1
     * THEN: S1 is not available for SP User
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 IC supplier is not available for SP")
    public void ecc3037_icSupplierUnavailableForSP(@UserCompany(CompanyCode.SCALEPOINT) User spUser, User futureUser, Supplier supplier) {
        checkVisibility(futureUser, spUser, supplier, false);
    }

    /**
     * GIVEN: SP and IC Users with Supply Manager credentials
     * WHEN: IC User creates supplier S1
     * THEN: S1 is not available for SP User
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 IC supplier is not available for another IC")
    public void ecc3037_icSupplierUnavailableForIC2(User futureUser1, User futureUser2, Supplier supplier) {
        checkVisibility(futureUser1, futureUser2, supplier, false);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC1 parent user creates supplier S1
     * THEN: S1 is available for IC1 child user
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 Parent IC supplier is available for child IC")
    public void ecc3037_parentICSupplierAvailableForChildIC(@UserCompany(CompanyCode.TRYGHOLDING)User parentCompanyUser, @UserCompany(CompanyCode.TRYGFORSIKRING) User childCompanyUser, Supplier supplier) {
        checkVisibility(parentCompanyUser, childCompanyUser, supplier, true);
    }

    /**
     * GIVEN: IC1 parent and IC2 child users with Supply Manager credentials
     * WHEN: IC1 child user creates supplier S1
     * THEN: S1 is available for IC1 parent user
     */
    @Test(dataProvider = "testDataProvider",
            description = "ECC-3037 Child IC supplier is available for parent IC")
    public void ecc3037_childICSupplierAvailableForParentIC(@UserCompany(CompanyCode.TRYGHOLDING) User parentCompanyUser, @UserCompany(CompanyCode.TRYGFORSIKRING) User childCompanyUser, Supplier supplier) {
        checkVisibility(childCompanyUser, parentCompanyUser, supplier, true);
    }

    @Test(dataProvider = "testDataProvider", description = "To matching engine link should be visible in supply management")
    public void ecc3039_matchingEngineLinkShouldBeVisibleForSupplyManager(@UserCompany(CompanyCode.BAUTA) User user){
        loginToEccAdmin(user)
                .doAssert(SuppliersPage.Asserts::assertsIsToMatchingEngineLinkDisplayed);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive should be visible in supply management, suppliers list")
    public void eccl3039_exclusiveColumnShouldBeVisibleInSuppliersList(@UserCompany(CompanyCode.BAUTA) User user){
        loginToEccAdmin(user)
                .doAssert(SuppliersPage.Asserts::assertsIsExclusiveColumnDisplayed);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive should not be visible in supply management, suppliers list")
    public void eccl3039_exclusiveColumnShouldNotBeVisibleInSuppliersList(@UserCompany(CompanyCode.SCALEPOINT) User user){
        loginToEccAdmin(user)
                .doAssert(SuppliersPage.Asserts::assertsIsExclusiveColumnNotDisplayed);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive should be visible in supply management, voucher list")
    public void ecc3039_exclusiveColumnShouldBeVisibleInVoucherList(@UserCompany(CompanyCode.BAUTA) User user){
        SuppliersPage suppliersPage = loginToEccAdmin(user);
        suppliersPage.toVouchersPage()
                .doAssert(VouchersPage.Asserts::assertsIsExclusiveColumnDisplayed);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive should not be visible in supply management, voucher list")
    public void ecc3039_exclusiveColumnShouldNotBeVisibleInVoucherList(@UserCompany(CompanyCode.SCALEPOINT) User user){
        SuppliersPage suppliersPage = loginToEccAdmin(user);
        suppliersPage.toVouchersPage()
                .doAssert(VouchersPage.Asserts::assertsIsExclusiveColumnNotDisplayed);
    }

    @Test(dataProvider = "testDataProvider", description = "Voucher tick should be visible in supply management, suppliers list")
    public void ecc3039_voucherTickIsAvailableInSuppliersList(@UserCompany(CompanyCode.BAUTA) User user, SimpleSupplier simpleSupplier){
       loginToEccAdmin(user)
                .doAssert(asserts -> asserts.assertsIsVoucherTickForSupplierDisplayed(simpleSupplier.getName()));
    }

    @Test(dataProvider = "testDataProvider", description = "Voucher tick should not be visible in supply management, suppliers list")
    public void ecc3039_voucherTickIsNotAvailableInSuppliersList(
            @UserCompany(CompanyCode.SCALEPOINT) User user, @SupplierCompany(areWithVouchers = false) SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .doAssert(asserts -> asserts.assertsIsVoucherTickForSupplierNotDisplayed(simpleSupplier.getName()));
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive tick should be visible in supply management, suppliers list")
    public void ecc3039_exclusiveTickIsAvailableInSuppliersList(@UserCompany(CompanyCode.BAUTA) User user, @SupplierCompany(CompanyCode.BAUTA) SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .doAssert(asserts -> asserts.assertsIsExclusiveTickForSupplierDisplayed(simpleSupplier.getName()));
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive tick should be visible in supply management, vouchers list")
    public void ecc3039_exclusiveTickIsAvailableInVoucherList(@UserCompany(CompanyCode.BAUTA) User user, @SupplierCompany(CompanyCode.BAUTA) SimpleSupplier simpleSupplier){
        loginToEccAdmin(user).toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsExclusiveTickForVoucherDisplayed(simpleSupplier.getAgreement()));
    }

    @Test(dataProvider = "testDataProvider", description = "Active tick should be visible in supply management, vouchers list")
    public void ecc3039_activeTickIsAvailableInVoucherList(@UserCompany(CompanyCode.BAUTA) User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user).toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsActiveTickForVoucherDisplayed(simpleSupplier.getAgreement()));
    }

    @Test(dataProvider = "testDataProvider", description = "Active tick should be not visible in supply management, vouchers list")
    public void ecc3039_activeTickIsNotAvailableInVoucherList(@UserCompany(CompanyCode.SCALEPOINT)User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user).toVouchersPage()
                .doAssert(asserts -> asserts.assertsIsNotActiveTickForVoucherDisplayed(simpleSupplier.getInactiveAgreement()));
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive tick for voucher should be visible on agreements tab when open supplier from suppliers list")
    public void ecc3039_exclusiveTickShouldBeVisibleForVoucherInSupplierDialog(@UserCompany(CompanyCode.BAUTA) User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .editSupplier(simpleSupplier.getName())
                .selectAgreementsTab()
                .doAssert(SupplierDialog.AgreementsTab.Asserts::assertIsExclusiveTickForVoucherVisible);
    }

    @Test(dataProvider = "testDataProvider", description = "Exclusive tick for voucher should be not visible on agreements tab when open supplier from suppliers list")
    public void ecc3039_exclusiveTickShouldBeNotVisibleForVoucherInSupplierDialog(@UserCompany(CompanyCode.SCALEPOINT) User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .editSupplier(simpleSupplier.getName())
                .selectAgreementsTab()
                .doAssert(asserts -> asserts.assertIsExclusiveTickForVoucherNotVisible(simpleSupplier.getScalepointAgreement()));
    }

    @Test(dataProvider = "testDataProvider")
    public void ecc3039_generalDataOfSupplierShouldBeNotEditable(@UserCompany(CompanyCode.BAUTA)User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .editSupplier(simpleSupplier.getName())
                .doAssert(SupplierDialog.GeneralTab.Asserts::assertIsDialogNotEditable);
    }

    @Test(dataProvider = "testDataProvider")
    public void ecc3039_shopDataOfSupplierShouldBeNotEditable(@UserCompany(CompanyCode.BAUTA)User user, SimpleSupplier simpleSupplier){
        loginToEccAdmin(user)
                .editSupplier(simpleSupplier.getName())
                .selectShopsTab()
                .openShop(simpleSupplier.getShopName())
                .doAssert(AddShopDialog.Asserts::assertIsShopDialogNotEditable);
    }

    private void checkVisibility(User userWhoCreates, User userWhoReads, Supplier supplier, boolean mustBeVisible) {
        SuppliersPage suppliersPage = loginToEccAdmin(userWhoCreates);
        SharedEccAdminFlows.createSupplier(suppliersPage, supplier)
                .saveSupplier()
                .doAssert(spage -> spage.assertSupplierPresent(supplier.getSupplierName()))
                .logout();

        loginToEccAdmin(userWhoReads).doAssert(page -> {
            if (mustBeVisible) {
                page.assertSupplierPresent(supplier.getSupplierName());
            } else {
                page.assertSupplierAbsent(supplier.getSupplierName());
            }
        });
    }
}
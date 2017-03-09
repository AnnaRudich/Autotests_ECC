package com.scalepoint.automation.tests;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateSupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateVoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.SupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.VoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.utils.data.entity.Supplier;
import com.scalepoint.automation.utils.data.entity.Voucher;
import org.apache.commons.lang.StringUtils;

public class SharedEccAdminFlows implements Actions {

    public static SupplierDialog.GeneralTab createSupplier(SuppliersPage suppliersPage, Supplier supplier) {
        return suppliersPage.selectCreateSupplier()
                .fill(createSupplierDialog -> {
                    new CreateSupplierDialog.FormFiller(createSupplierDialog)
                            .withSupplierName(supplier.getSupplierName())
                            .withCvr(supplier.getSupplierCVR())
                            .withAddress1(supplier.getAddress1())
                            .withAddress2(supplier.getAddress2())
                            .withCity(supplier.getCity())
                            .withPostalCode(supplier.getPostCode())
                            .withPhone(supplier.getSupplierPhone())
                            .withOrderEmail(supplier.getSupplierEmail());
                })
                .createSupplier();
    }

    public static SupplierDialog.AgreementsTab createVoucherAgreement(SupplierDialog.GeneralTab supplierTab, VoucherAgreementData voucherAgreementData) {
        VoucherAgreementDialog.GeneralTab generalTab = supplierTab
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucherAgreementData.voucherGeneralData.getVoucherGeneratedName())
                            .withAgreementDiscount(voucherAgreementData.discount);
                })
                .saveVoucherAgreement();

        if (StringUtils.isNotBlank(voucherAgreementData.categoryGroup)) {
            generalTab.selectCategoriesTab()
                    .mapToCategory(voucherAgreementData.categoryGroup, voucherAgreementData.categoryName);
        }

        if (StringUtils.isNotBlank(voucherAgreementData.termsAndConditions)) {
            generalTab
                    .selectLegalTab()
                    .setConditions(voucherAgreementData.termsAndConditions)
                    .selectGeneralTab();
        }
        return generalTab.saveVoucherAgreement();
    }

    public static class VoucherAgreementData {
        private Voucher voucherGeneralData;
        private int discount;
        private String categoryGroup;
        private String categoryName;
        private String termsAndConditions;

        VoucherAgreementData(Voucher voucherGeneralData, int discount) {
            this.voucherGeneralData = voucherGeneralData;
            this.discount = discount;
        }

        public static VoucherAgreementBuilder newBuilder(Voucher voucherGeneralData, int discount) {
            return new VoucherAgreementData(voucherGeneralData, discount).new VoucherAgreementBuilder();
        }

        public class VoucherAgreementBuilder {

            public VoucherAgreementBuilder withTermsAndConditions(String termsAndConditions) {
                VoucherAgreementData.this.termsAndConditions = termsAndConditions;
                return this;
            }

            public VoucherAgreementBuilder mapToCategory(String categoryGroup, String categoryName) {
                VoucherAgreementData.this.categoryGroup = categoryGroup;
                VoucherAgreementData.this.categoryName = categoryName;
                return this;
            }

            public VoucherAgreementData build() {
                return VoucherAgreementData.this;
            }
        }
    }



}
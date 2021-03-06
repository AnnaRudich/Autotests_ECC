package com.scalepoint.automation.tests;

import com.scalepoint.automation.Actions;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateSupplierDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.CreateVoucherAgreementDialog;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.AgreementsTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.suppliersdialog.supplierdialogtab.GeneralTab;
import com.scalepoint.automation.pageobjects.dialogs.eccadmin.voucheagreementtab.VoucherAgreementGeneralTab;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import com.scalepoint.automation.utils.data.entity.input.Supplier;
import com.scalepoint.automation.utils.data.entity.input.Voucher;
import org.apache.commons.lang.StringUtils;

public class SharedEccAdminFlows implements Actions {
    public static GeneralTab createSupplier(SuppliersPage suppliersPage, Supplier supplier) {
        return suppliersPage.selectCreateSupplier()
                .fill(supplierDialog -> new CreateSupplierDialog.FormFiller(supplierDialog)
                        .withSupplierName(supplier.getSupplierName())
                        .withCvr(supplier.getSupplierCVR())
                        .withAddress1(supplier.getAddress1())
                        .withAddress2(supplier.getAddress2())
                        .withCity(supplier.getCity())
                        .withPostalCode(supplier.getPostCode())
                        .withPhone(supplier.getSupplierPhone())
                        .withOrderEmail(supplier.getSupplierEmail()))
                .createSupplier(GeneralTab.class);
    }

    public static AgreementsTab createVoucherAgreement(GeneralTab supplierTab, VoucherAgreementData voucherAgreementData) {
        VoucherAgreementGeneralTab generalTab = supplierTab
                .selectAgreementsTab()
                .openCreateVoucherAgreementDialog()
                .fill(createVoucherAgreementDialog -> {
                    new CreateVoucherAgreementDialog.FormFiller(createVoucherAgreementDialog)
                            .withVoucherName(voucherAgreementData.voucherGeneralData.getVoucherNameSP())
                            .withAgreementDiscount(voucherAgreementData.discount);
                })
                .createVoucherAgreement();

        if (voucherAgreementData.hasPseudoCategory()) {
            generalTab.selectCategoriesTab()
                    .mapToCategory(voucherAgreementData.pseudoCategory);
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
        private PseudoCategory pseudoCategory;
        private String termsAndConditions;

        VoucherAgreementData(Voucher voucherGeneralData, int discount) {
            this.voucherGeneralData = voucherGeneralData;
            this.discount = discount;
        }

        public boolean hasPseudoCategory() {
            return pseudoCategory != null;
        }

        public static VoucherAgreementBuilder newBuilder(Voucher voucherGeneralData, int discount) {
            return new VoucherAgreementData(voucherGeneralData, discount).new VoucherAgreementBuilder();
        }

        public class VoucherAgreementBuilder {

            public VoucherAgreementBuilder withTermsAndConditions(String termsAndConditions) {
                VoucherAgreementData.this.termsAndConditions = termsAndConditions;
                return this;
            }

            public VoucherAgreementBuilder mapToCategory(PseudoCategory pseudoCategory) {
                VoucherAgreementData.this.pseudoCategory = pseudoCategory;
                return this;
            }

            public VoucherAgreementData build() {
                return VoucherAgreementData.this;
            }
        }
    }


}

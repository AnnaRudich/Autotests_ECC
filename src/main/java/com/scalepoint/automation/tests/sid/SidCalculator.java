package com.scalepoint.automation.tests.sid;

import java.math.BigDecimal;

class SidCalculator {

    static PriceValuation calculatePriceValuation(Double price, Integer depreciationPercentage) {
        double depreciation = price * (double)depreciationPercentage / 100;
        double cashCompensation = price - depreciation;
        return new PriceValuation(cashCompensation, depreciation);
    }

    static VoucherValuationWithDepreciation calculateVoucherValuation(Double price, Integer voucherDiscount, Integer depreciationPercentage) {
        double discountedAmountByVoucher = price * voucherDiscount / 100;
        double cashCompensationOfVoucher = price - discountedAmountByVoucher;
        double depreciatedAmount = (cashCompensationOfVoucher*depreciationPercentage)/100;
        double cashCompensationWithDepreciation = cashCompensationOfVoucher - (cashCompensationOfVoucher*depreciationPercentage)/100;

        return new VoucherValuationWithDepreciation(cashCompensationOfVoucher, discountedAmountByVoucher, cashCompensationWithDepreciation, depreciatedAmount);
    }

    static VoucherValuation calculateVoucherValuationWithDiscountDistribution(Double price, Integer voucherDiscount, Integer companyDiscount) {
        double discountedAmountByVoucher = price * voucherDiscount / 100;
        double cashValue = price - (discountedAmountByVoucher * companyDiscount/voucherDiscount);
        double faceValue = BigDecimal.valueOf(cashValue/((100.0-voucherDiscount)/100.0)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new VoucherValuation(cashValue, faceValue);
    }

    static ValuationWithReduction calculatePriceValuationWithReduction(double price, Integer depreciationPercentage, Integer reductionPercentage) {
        double depreciation = price * depreciationPercentage / 100;
        double cashCompensation = price - depreciation;
        double reduction = price * reductionPercentage/100;
        double cashCompensationWithReduction = price - reduction;

        return new ValuationWithReduction(cashCompensation, depreciation, cashCompensationWithReduction, reduction);
    }

    static class ValuationWithReduction {
        private double cashCompensation;
        private double depreciation;
        private double cashCompensationWithReduction;
        private double reduction;

        ValuationWithReduction(double cashCompensation, double depreciation, double cashCompensationWithReduction, double reduction) {
            this.cashCompensation = cashCompensation;
            this.depreciation = depreciation;
            this.cashCompensationWithReduction = cashCompensationWithReduction;
            this.reduction = reduction;
        }

        double getCashCompensation() {
            return cashCompensation;
        }

        double getDepreciation() {
            return depreciation;
        }

        double getCashCompensationWithReduction() {
            return cashCompensationWithReduction;
        }

        double getReduction() {
            return reduction;
        }
    }

    static class PriceValuation {
        private double cashValue;
        private double depreciation;

        PriceValuation(double cashValue, double depreciation) {
            this.cashValue = cashValue;
            this.depreciation = depreciation;
        }

        double getCashValue() {
            return cashValue;
        }

        double getDepreciation() {
            return depreciation;
        }
    }

    static class VoucherValuationWithDepreciation {
        private double cashCompensationOfVoucher;
        private double discountedAmountByVoucher;
        private double cashCompensationWithDepreciation;
        private double depreciatedAmount;

        VoucherValuationWithDepreciation(double cashCompensationOfVoucher, double discountedAmountByVoucher, double cashCompensationWithDepreciation, double depreciatedAmount) {
            this.cashCompensationOfVoucher = cashCompensationOfVoucher;
            this.discountedAmountByVoucher = discountedAmountByVoucher;
            this.cashCompensationWithDepreciation = cashCompensationWithDepreciation;
            this.depreciatedAmount = depreciatedAmount;
        }

        double getDiscountedAmountByVoucher() {
            return discountedAmountByVoucher;
        }

        double getCashCompensationOfVoucher() {
            return cashCompensationOfVoucher;
        }

        double getCashCompensationWithDepreciation() {
            return cashCompensationWithDepreciation;
        }

        double getDepreciatedAmount() {
            return depreciatedAmount;
        }
    }

    static class VoucherValuation {
        private double cashValue;
        private double faceValue;

        public VoucherValuation(double cashValue, double faceValue) {
            this.cashValue = cashValue;
            this.faceValue = faceValue;
        }

        public double getCashValue() {
            return cashValue;
        }

        public double getFaceValue() {
            return faceValue;
        }
    }
}

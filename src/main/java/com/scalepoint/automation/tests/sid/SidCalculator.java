package com.scalepoint.automation.tests.sid;

class SidCalculator {

    static PriceValuation calculatePriceValuation(int price, int depreciationPercentage) {
        double depreciation = price * ((double)depreciationPercentage) / 100;
        double cashCompensation = price - depreciation;
        return new PriceValuation(cashCompensation, depreciation);
    }

    static VoucherValuation calculateVoucherValuation(double price, int voucherDiscount, int depreciationPercentage) {
        double discountedAmountByVoucher = price * voucherDiscount / 100;
        double cashCompensationOfVoucher = price - discountedAmountByVoucher;
        double depreciatedAmount = (cashCompensationOfVoucher*depreciationPercentage)/100;
        double cashCompensationWithDepreciation = cashCompensationOfVoucher - (cashCompensationOfVoucher*depreciationPercentage)/100;

        return new VoucherValuation(cashCompensationOfVoucher, discountedAmountByVoucher, cashCompensationWithDepreciation, depreciatedAmount);
    }

    static ValuationWithReduction calculatePriceValuationWithReduction(double price, double depreciationPercentage, int reductionPercentage) {
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

    static class VoucherValuation {
        private double cashCompensationOfVoucher;
        private double discountedAmountByVoucher;
        private double cashCompensationWithDepreciation;
        private double depreciatedAmount;

        VoucherValuation(double cashCompensationOfVoucher, double discountedAmountByVoucher, double cashCompensationWithDepreciation, double depreciatedAmount) {
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
}

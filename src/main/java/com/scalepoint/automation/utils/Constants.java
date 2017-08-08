package com.scalepoint.automation.utils;

import com.scalepoint.automation.utils.data.entity.credentials.User;

public class Constants {

    public static final String TEXT_LINE = "AutomatedTest-ClaimLine";
    public static final String PASSWORD = "12341234";

    public static final String APRIL = "Apr";

    public static final Double PRICE_2400 = 2400.00;
    public static final Double PRICE_100_000 = 100_000.00;
    public static final Double PRICE_500 = 500.00;
    public static final Double PRICE_1000 = 1000.00;

    public static final Integer DEPRECIATION_10 = 10;
    public static final Integer VOUCHER_DISCOUNT_10 = 10;

    private static final String SUPPLIER_FOR_VOUCHERAGREEMENTS = "AutotestSupplier-$CompanyId-ForVaTests";

    public static String getSupplierNameForVATests(User user) {
        return SUPPLIER_FOR_VOUCHERAGREEMENTS.replace("$CompanyId", user.getCompanyId().toString());
    }
}

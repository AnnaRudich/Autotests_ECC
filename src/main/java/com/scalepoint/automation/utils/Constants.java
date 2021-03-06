package com.scalepoint.automation.utils;

import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.utils.data.entity.credentials.User;

import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.*;

public class Constants {

    public static final String TEXT_LINE = "AutomatedTest-ClaimLine ÆæØøÅåß";
    public static final String DEFAULT_PASSWORD = "12341234";

    public static final String JANUARY = "Jan";

    public static final Double PRICE_2400 = 2400.00;
    public static final Double PRICE_100_000 = 100_000.00;
    public static final Double PRICE_500 = 500.00;
    public static final Double PRICE_50 = 50.00;
    public static final Double PRICE_30 = 30.00;
    public static final Double PRICE_10 = 10.00;
    public static final Double PRICE_100 = 100.00;
    public static final Double VAT_20 = 20.00;


    public static final int AGE_MONTH = 1;
    public static final int AGE_YEAR = 1;

    public static final Integer DEPRECIATION_10 = 10;
    public static final Integer DEPRECIATION_5 = 5;
    public static final Integer VOUCHER_DISCOUNT_10 = 10;
    public static final int WAIT_UNTIL_MS = 6000;

    private static final String SUPPLIER_FOR_VOUCHERAGREEMENTS = "AutotestSupplier-$CompanyId-ForVaTests";

    public static String getSupplierNameForVATests(User user) {
        return SUPPLIER_FOR_VOUCHERAGREEMENTS.replace("$CompanyId", user.getCompanyId().toString());
    }

    public static final UserAddEditPage.UserType[] ALL_ROLES = {ADMIN, CLAIMSHANDLER, SUPPLYMANAGER};
}

package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;

import static com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation.OperationType.*;

public enum FTSetting {
    //Various function around the system
    SETTLE_WITHOUT_MAIL("input[name=ftfunctionflag][value='1']", CHECKBOX),
    ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE("input[name=ftfunctionflag3][value='4194304']", CHECKBOX),
    ENABLE_REGISTRATION_LINE_SELF_SERVICE("input[name=ftSelfServiceFlag][value='2048']", CHECKBOX),
    USE_INTERNAL_NOTES("input[name=ftfunctionflag][value='2048']", CHECKBOX),
    SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON("input[name=ftfunctionflag][value='4096']", CHECKBOX),
    SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON("input[name=ftfunctionflag][value='8192']", CHECKBOX),
    DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION("input[name=ftfunctionflag5][value='512']", CHECKBOX),
    ENABLE_DEPRECIATION_COLUMN("input[name=ftfunctionflag5][value='131072']", CHECKBOX),
    SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED("input[name=ftfunctionflag5][value='32768']", CHECKBOX),
    SHOW_SUGGESTED_DEPRECIATION_SECTION("input[name=ftfunctionflag5][value='4096']", CHECKBOX),
    ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED("input[name=ftfunctionflag4][value='1073741824']", CHECKBOX),
    ENABLE_MANUAL_REDUCTION("input[name=ftfunctionflag4][value='512']", CHECKBOX),
    REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM("input[name=ftfunctionflag4][value='262144']", CHECKBOX, true),
    SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG("input[name=ftfunctionflag2][value='128']", CHECKBOX),
    SHOW_POLICY_TYPE("input[name=ftfunctionflag][value='64']", CHECKBOX),
    SHOW_MARKET_PRICE("input[name=ftfunctionflag5][value='128']", CHECKBOX),
    SHOW_SCALEPOINT_SUPPLIER("input[name=ftfunctionflag5][value='1024']", CHECKBOX),
    USE_UCOMMERCE_SHOP("input[name=ftfunctionflag5][value='16777216']", CHECKBOX),
    SHOW_DISCREATIONARY_REASON("input[name=ftfunctionflag5][value='65536']", CHECKBOX),
    MAKE_DISCREATIONARY_REASON_MANDATORY("input[name=ftfunctionflag5][value='262144']", CHECKBOX),
    MAKE_REJECT_REASON_MANDATORY("input[name=ftfunctionflag5][value='67108864']", CHECKBOX),
    SHOW_NOT_CHEAPEST_CHOICE_POPUP("input[name=ftfunctionflag5][value='16384']", CHECKBOX),
    MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG("input[name=ftfunctionflag3][value='262144']", CHECKBOX),
    SUFFICIENT_DOCUMENTATION_CHECKBOX("input[name=ftfunctionflag5][value='8192']", CHECKBOX),

    //Settings for Self Service
    ENABLE_SELF_SERVICE("input[name=ftSelfServiceFlag][value='1']", CHECKBOX),
    USE_SELF_SERVICE2("input[name=ftSelfServiceFlag][value='65536']", CHECKBOX),
    INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='256']", CHECKBOX),
    INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='32']", CHECKBOX),
    INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='64']", CHECKBOX),
    INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='512']", CHECKBOX),
    VALIDATE_AGE("input[name=ftSelfServiceFlag][value='2']", CHECKBOX),
    SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH("input[name=ftSelfServiceFlag][value='131072']", CHECKBOX),

    //Match FTSetting
    ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS("input[name=ftnoflag][value='1']", CHECKBOX),
    ALLOW_NONORDERABLE_PRODUCTS("select[name=ftnoselflags]", SELECT),
    NUMBER_BEST_FIT_RESULTS("input[name=ftnumberbestfitresults]", INPUT),
    USE_BRAND_LOYALTY_BY_DEFAULT("input[name=ftfunctionflag3][value='2048']", CHECKBOX),

    //FTSetting for Comparison of Depreciation and Discount
    COMPARISON_OF_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value='1']", CHECKBOX),
    COMBINE_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value='2']", CHECKBOX),
    DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD("input[name=ftDnD2Related][value='4']", CHECKBOX),

    REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION("select[name=RequiredValuationId]", SELECT),
    ENABLE_3RD_VALUATION_FIELD("input[name=ftfunctionflag4][value='4096']", CHECKBOX),

    ENABLE_REPAIR_VALUATION_AUTO_SETTLING("input[name=ftAutoApprovalFlag][value='1']", CHECKBOX),
    ENABLE_COLLECTING_SELFRISK_BY_IC("input[name=ftCollectSelfRisk][value='1']", CHECKBOX),

    PAYOUT_TO_CHEQUE_CLAIMSHANDLER("input[name=ftpaymentflag][value='1']", CHECKBOX),

    USER_PASSWORD_VALIDATION_STRATEGY("select[name='ftUserPasswordValidationStrategy']", SELECT);


    private String locator;
    private FtOperation.OperationType operationType;
    private Boolean hasDependency = Boolean.FALSE;

    FTSetting(String locator, FtOperation.OperationType operationType) {
        this.locator = locator;
        this.operationType = operationType;
    }

    FTSetting(String locator, FtOperation.OperationType operationType, Boolean hasDependency) {
        this.locator = locator;
        this.operationType = operationType;
        this.hasDependency = hasDependency;
    }

    public String getLocator() {
        return locator;
    }

    public String getDescription() {
        return name();
    }

    public FtOperation.OperationType getOperationType() {
        return operationType;
    }

    public Boolean hasDependency() {
        return hasDependency;
    }
}

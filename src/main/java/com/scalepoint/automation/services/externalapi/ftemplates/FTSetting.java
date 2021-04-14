package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;

import static com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation.OperationType.*;

public enum FTSetting {
    //Various function around the system
    SETTLE_EXTERNALLY("input[name=ftfunctionflag][value='1']", CHECKBOX),
    SETTLE_WITHOUT_MAIL("input[name=ftfunctionflag][value='512']", CHECKBOX),
    ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE("input[name=ftfunctionflag3][value='4194304']", CHECKBOX),
    ENABLE_REGISTRATION_LINE_SELF_SERVICE("input[name=ftSelfServiceFlag][value='2048']", CHECKBOX),
    USE_INTERNAL_NOTES("input[name=ftfunctionflag][value='2048']", CHECKBOX),
    SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON("input[name=ftfunctionflag][value='4096']", CHECKBOX),
    SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON("input[name=ftfunctionflag][value='8192']", CHECKBOX),
    USE_REPLACEMENT_FROM_ME("input[name=ftfunctionflag][value='8388608']", CHECKBOX),
    USE_REPLACEMENT_THROUGH_THE_SHOP("input[name=ftfunctionflag2][value='4096']", CHECKBOX),
    DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION("input[name=ftfunctionflag5][value='512']", CHECKBOX),
    ENABLE_DEPRECIATION_COLUMN("input[name=ftfunctionflag5][value='131072']", CHECKBOX),
    SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED("input[name=ftfunctionflag5][value='32768']", CHECKBOX),
    SHOW_SUGGESTED_DEPRECIATION_SECTION("input[name=ftfunctionflag5][value='4096']", CHECKBOX),
    ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED("input[name=ftfunctionflag4][value='1073741824']", CHECKBOX),
    ENABLE_MANUAL_REDUCTION("input[name=ftfunctionflag4][value='512']", CHECKBOX),
    REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM("input[name=ftfunctionflag4][value='262144']", CHECKBOX, true),
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
    DISALLOW_DUPLICATE_CLAIMS_NUMBER("input[name=ftfunctionflag3][value='2']", CHECKBOX),
    ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT("input[name=ftfunctionflag3][value='1048576']", CHECKBOX),
    ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD("input[name=ftfunctionflag3][value='8388608']", CHECKBOX),
    ENABLE_CANCEL_BUTTON_IN_SETTLEMENT_PAGE("input[name=ftfunctionflag3][value='2097152']", CHECKBOX),
    DO_NOT_DEPRECIATE_CUSTOMER_DEMAND("input[name=ftfunctionflag][value='4']", CHECKBOX),
    ENABLE_VOUCHER_PREDICTION("input[name=ftfunctionflag3][value='4096']", CHECKBOX),
    INCLUDE_AGENT_DATA("input[name=ftfunctionflag2][value='536870912']", CHECKBOX),
    SHOW_COPY_PASTE_TEXTAREA("input[name=copyPasteNewClaim]", CHECKBOX),
    ENABLE_BULK_UPDATE_CATEGORY("input[name=enableBulkUpdateCategory]",CHECKBOX),

    //Settings for Self Service
    ENABLE_SELF_SERVICE("input[name=ftSelfServiceFlag][value='1']", CHECKBOX),
    USE_SELF_SERVICE2("input[name=ftSelfServiceFlag][value='65536']", CHECKBOX),
    INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='256']", CHECKBOX),
    INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='32']", CHECKBOX),
    INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='64']", CHECKBOX),
    INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE("input[name=ftSelfServiceFlag][value='512']", CHECKBOX),
    VALIDATE_AGE("input[name=ftSelfServiceFlag][value='2']", CHECKBOX),
    SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH("input[name=ftSelfServiceFlag][value='131072']", CHECKBOX),

    //Integration settings

    CREATE_AND_PUSH_SETTLEMENT_DOCUMENTS("input[id='integrationSetting']", CHECKBOX),
    ENABLE_ALL_PAYMENT_INTEGRATION("input[id='enableAllPaymentIntegrations']", CHECKBOX),
    ENABLE_AUTOMATIC_RV_INVOICE_PAYMENT("input[id='enablePushInvoice']", CHECKBOX),
    DEFAULT_AUTOMATIC_INVOICE_PAYMENTS("select[id=DefaultInvoicePayerTypeId]", SELECT),

    //Match FTSetting
    ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS("input[name=ftnoflag][value='1']", CHECKBOX),
    ALLOW_NONORDERABLE_PRODUCTS("select[name=ftnoselflags]", SELECT),
    NUMBER_BEST_FIT_RESULTS("input[name=ftnumberbestfitresults]", INPUT),
    USE_BRAND_LOYALTY_BY_DEFAULT("input[name=ftfunctionflag3][value='2048']", CHECKBOX),

    //mail setting
    ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT("input[name=ftfunctionflag3][value='524288']", CHECKBOX),

    //FTSetting for Comparison of Depreciation and Discount
    COMPARISON_OF_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value='1']", CHECKBOX),
    COMBINE_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value='2']", CHECKBOX),
    DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD("input[name=ftDnD2Related][value='4']", CHECKBOX),

    REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION("select[name=RequiredValuationId]", SELECT),
    ENABLE_3RD_VALUATION_FIELD("input[name=ftfunctionflag4][value='4096']", CHECKBOX),

    //Repair & Valuation settings
    ENABLE_COLLECTING_SELFRISK_BY_IC("input[name=ftCollectSelfRisk][value='1']", CHECKBOX),
    ENABLE_DAMAGE_TYPE("input[name=ftfunctionflag5][value='1073741824']", CHECKBOX),

    DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER("input[name='ftpaymentflag'][value='16384']", CHECKBOX),
    DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER("input[name='ftpaymentflag'][value='32768']", CHECKBOX),

    CPR_NUMBER_ON_REPLACEMENT_REQUIRED("input[name='ftpaymentflag'][value='1048576']", CHECKBOX),

    SHOW_DAMAGE_TYPE_CONTROLS_IN_SID("input[name='showDamageTypeControlsInSid'][value='1']", CHECKBOX),
    SPLIT_REPLACEMENT_EMAIL("input[name='ftfunctionflag'][value='16384']", CHECKBOX),
    WARNING_DEDUCTIBLE("input[name='ftfunctionflag'][value='65536']", CHECKBOX);

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

package com.scalepoint.automation.services.externalapi.ftemplates;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;

import static com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation.OperationType.*;

public enum FTSetting {
    //Various function around the system
    COMPARISON_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value=1]", "Comparison of discount and depreciation", CHECKBOX),
    ENABLE_SELF_SERVICE("input[name=ftSelfServiceFlag][value=1]", "Enable Self Service", CHECKBOX),
    USE_SELF_SERVICE2("input[name=ftSelfServiceFlag][value=65536]", "Use Self Service 2.0", CHECKBOX),
    ENABLE_REGISTRATION_LINE_SELF_SERVICE("input[name=ftSelfServiceFlag][value=2048]", "Enable registration line notes in Self Service", CHECKBOX),
    USE_INTERNAL_NOTES("input[name=ftfunctionflag][value=2048]", "Use Internal Notes", CHECKBOX),
    SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON("input[name=ftfunctionflag][value=4096]", "Settlement page internal notebutton", CHECKBOX),
    SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON("input[name=ftnoselflags][value=8192]", "Settlement page customer notebutton", CHECKBOX),
    DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION("input[name=ftfunctionflag5][value=512]", "Display voucher value with depreciation deducted", CHECKBOX),
    ENABLE_DEPRECIATION_COLUMN("input[name=ftfunctionflag5][value=131072]", "Enable Depreciation column", CHECKBOX),
    SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED("input[name=ftfunctionflag5][value=32768]", "Show \"Depreciation is automatically updated\" checkbox on Settlement Item Dialog", CHECKBOX),
    SHOW_SUGGESTED_DEPRECIATION_SECTION("input[name=ftfunctionflag5][value=4096]", "Show suggested depreciation section", CHECKBOX),
    ALLOW_MARK_SETTLEMENT_REVIEWED("input[name=ftfunctionflag4][value=1073741824]", "Allow users to mark settlement items as reviewed", CHECKBOX),
    REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM("input[name=ftfunctionflag4][value=1073741824]", "Review of all claim lines are required to complete claim", CHECKBOX),
    SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG("input[name=ftfunctionflag2][value=128]", "Show compact settlement item dialog", CHECKBOX),
    SHOW_POLICY_TYPE("input[name=ftfunctionflag][value=64]", "Show policy type", CHECKBOX),
    SHOW_MARKET_PRICE("input[name=ftfunctionflag5][value=128]", "Show Market Price in ME catalog", CHECKBOX),
    ENABLE_NEW_SETTLEMENT_ITEM_DIALOG("input[name=ftEnableNewSettlementItemDialog]", "Enable new Settlement Item Dialog", CHECKBOX),
    USE_UCOMMERCE_SHOP("input[name=ftfunctionflag5][value=16777216]", "Use Ucommerce shop", CHECKBOX),
    FUNC_ENABLE_MARK_REVIEWED_REQUIRED("input[name=ftfunctionflag4][value=1073741824]", "Allow users to mark settlement items as reviewed", CHECKBOX),

    //Match FTSetting
    BEST_FIT_FOR_NONORDERABLE_PRODUCTS("input[name=ftnoflag][value=1]", "Allow BestFit for nonorderable products if possible", CHECKBOX),
    ALLOW_NONORDERABLE_PRODUCTS("select[name=ftnoselflags] option[selected]", "Allow nonorderable products", SELECT),
    NUMBER_BEST_FIT_RESULTS("input[name=ftnumberbestfitresults]", "Number of best fit results", INPUT),
    USE_BRAND_LOYALTY_BY_DEFAULT("input[name=ftfunctionflag3][value=2048]", "Use brand loyalty by default", CHECKBOX),

    //FTSetting for Comparison of Depreciation and Discount
    COMPARISON_DEPRECATION_DISCOUNT("input[name=ftDnD2Related][value=1]", "Comparison of discount and depreciation", CHECKBOX),
    COMBINE_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value=2]", "Combine discount and depreciation", CHECKBOX),
    DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD("input[name=ftDnD2Related][value=4]", "Disable discounted valuations for \"Old\" claims", CHECKBOX);

    private String locator;
    private String description;
    private FtOperation.OperationType operationType;

    FTSetting(String locator, String description, FtOperation.OperationType operationType) {
        this.locator = locator;
        this.description = description;
        this.operationType = operationType;
    }

    public String getLocator() {
        return locator;
    }

    public String getDescription() {
        return description;
    }

    public FtOperation.OperationType getOperationType() {
        return operationType;
    }
}

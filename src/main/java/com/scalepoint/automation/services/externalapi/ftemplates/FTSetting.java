package com.scalepoint.automation.services.externalapi.ftemplates;

public enum FTSetting {
    //Various function around the system
    COMPARISON_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value=1]", "Comparison of discount and depreciation"),
    ENABLE_SELF_SERVICE("input[name=ftSelfServiceFlag][value=1]", "Enable Self Service"),
    USE_SELF_SERVICE2("input[name=ftSelfServiceFlag][value=65536]", "Use Self Service 2.0"),
    ENABLE_REGISTRATION_LINE_SELF_SERVICE("input[name=ftSelfServiceFlag][value=2048]", "Enable registration line notes in Self Service"),
    USE_INTERNAL_NOTES("input[name=ftfunctionflag][value=2048]", "Use Internal Notes"),
    SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON("input[name=ftfunctionflag][value=4096]", "Settlement page internal notebutton"),
    SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON("input[name=ftnoselflags][value=8192]", "Settlement page customer notebutton"),
    DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION("input[name=ftfunctionflag5][value=512]", "Display voucher value with depreciation deducted"),
    ENABLE_DEPRECIATION_COLUMN("input[name=ftfunctionflag5][value=131072]", "Enable Depreciation column"),
    SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED("input[name=ftfunctionflag5][value=32768]", "Show \"Depreciation is automatically updated\" checkbox on Settlement Item Dialog"),
    SHOW_SUGGESTED_DEPRECIATION_SECTION("input[name=ftfunctionflag5][value=4096]", "Show suggested depreciation section"),
    ALLOW_MARK_SETTLEMENT_REVIEWED("input[name=ftfunctionflag4][value=1073741824]", "Allow users to mark settlement items as reviewed"),
    REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM("input[name=ftfunctionflag4][value=1073741824]", "Review of all claim lines are required to complete claim"),
    SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG("input[name=ftfunctionflag2][value=128]", "Show compact settlement item dialog"),
    SHOW_POLICY_TYPE("input[name=ftfunctionflag][value=64]", "Show policy type"),
    SHOW_MARKET_PRICE("input[name=ftfunctionflag5][value=128]", "Show Market Price in ME catalog"),
    ENABLE_NEW_SETTLEMENT_ITEM_DIALOG("input[name=ftEnableNewSettlementItemDialog]", "Enable new Settlement Item Dialog"),

    //Match FTSetting
    BEST_FIT_FOR_NONORDERABLE_PRODUCTS("input[name=ftnoflag][value=1]", "Allow BestFit for nonorderable products if possible"),
    ALLOW_NONORDERABLE_PRODUCTS("select[name=ftnoselflags] option[selected]", "Allow nonorderable products"),
    NUMBER_BEST_FIT_RESULTS("input[name=ftnumberbestfitresults]", "Number of best fit results"),
    USE_BRAND_LOYALTY_BY_DEFAULT("input[name=ftfunctionflag3][value=2048]", "Use brand loyalty by default"),

    //FTSetting for Comparison of Depreciation and Discount
    COMPARISON_DEPRECATION_DISCOUNT("input[name=ftDnD2Related][value=1]", "Comparison of discount and depreciation"),
    COMBINE_DISCOUNT_DEPRECATION("input[name=ftDnD2Related][value=2]", "Combine discount and depreciation"),
    DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD("input[name=ftDnD2Related][value=4]", "Disable discounted valuations for \"Old\" claims");

    private String locator;
    private String description;

    FTSetting(String locator, String description) {
        this.locator = locator;
        this.description = description;
    }

    public String getLocator() {
        return locator;
    }

    public String getDescription() {
        return description;
    }
}

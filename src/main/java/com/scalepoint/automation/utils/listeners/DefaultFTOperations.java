package com.scalepoint.automation.utils.listeners;

import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtCheckbox;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtSelect;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtTextField;

import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.*;

public class DefaultFTOperations {

    public static List<FtOperation> future() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.ENABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "Yes, Always"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "Select valuation type..."),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.DISABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> alka() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.DISABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "No, Never"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "Select valuation type..."),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.ENABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> topdanmark() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.DISABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "No, Never"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "Select valuation type..."),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.ENABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> scalepoint() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.ENABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "Yes, Always"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "Select valuation type..."),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.DISABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> trygforsikring() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.DISABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "No, Never"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "NEW_PRICE"),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.ENABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> bauta() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.DISABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "No, Never"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "NEW_PRICE"),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.ENABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }

    public static List<FtOperation> trygholding() {

        return Arrays.asList(new FtOperation[]{new FtCheckbox(SETTLE_EXTERNALLY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLE_WITHOUT_MAIL, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_REGISTRATION_LINE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_INTERNAL_NOTES, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SETTLEMENT_PAGE_INTERNAL_NOTEBUTTON, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SETTLEMENT_PAGE_CUSTOMER_NOTEBUTTON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_FROM_ME, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_REPLACEMENT_THROUGH_THE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISPLAY_VOUCHER_VALUE_WITH_DEPRECATION_DEDUCTION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DEPRECIATION_COLUMN, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DEPRECIATION_AUTOMATICALLY_UPDATED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SUGGESTED_DEPRECIATION_SECTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ALLOW_USERS_TO_MARK_SETTLEMENT_REVIEWED, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_MANUAL_REDUCTION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(REVIEW_ALL_CLAIM_TO_COMPLETE_CLAIM, FtCheckbox.OperationType.DISABLE),
//                new FtCheckbox(SHOW_COMPACT_SETTLEMENT_ITEM_DIALOG, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_POLICY_TYPE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_MARKET_PRICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_SCALEPOINT_SUPPLIER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_UCOMMERCE_SHOP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SHOW_DISCREATIONARY_REASON, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_DISCREATIONARY_REASON_MANDATORY, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MAKE_REJECT_REASON_MANDATORY, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SHOW_NOT_CHEAPEST_CHOICE_POPUP, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(MOVE_DISCOUNT_DISTRIBUTION_TO_DIALOG, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SUFFICIENT_DOCUMENTATION_CHECKBOX, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISALLOW_DUPLICATE_CLAIMS_NUMBER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DO_NOT_DEPRECIATE_CUSTOMER_DEMAND, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_VOUCHER_PREDICTION, FtCheckbox.OperationType.DISABLE),

                //Settings for Self Service
                new FtCheckbox(ENABLE_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(USE_SELF_SERVICE2, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_PURCHASE_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(VALIDATE_AGE, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(SELF_SERVICE_2_DEFINE_AGE_BY_YEAR_AND_MONTH, FtCheckbox.OperationType.DISABLE),

                //Match FTSetting
                new FtCheckbox(ALLOW_BEST_FIT_FOR_NONORDERABLE_PRODUCTS, FtCheckbox.OperationType.ENABLE),
                new FtSelect(ALLOW_NONORDERABLE_PRODUCTS, "No, Never"),
                new FtTextField(NUMBER_BEST_FIT_RESULTS, "5"),
                new FtCheckbox(USE_BRAND_LOYALTY_BY_DEFAULT, FtCheckbox.OperationType.ENABLE),

                //mail setting
                new FtCheckbox(ENABLE_ORDER_CONFIRMATION_EMAIL_TO_CUSTOMER_WHEN_SETTLEMENT_METHOD_IS_REPLACEMENT, FtCheckbox.OperationType.ENABLE),

                //FTSetting for Comparison of Depreciation and Discount
                new FtCheckbox(COMPARISON_OF_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(COMBINE_DISCOUNT_DEPRECATION, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(DISABLE_DISCOUNTED_VALUATIONS_FOR_OLD, FtCheckbox.OperationType.DISABLE),

                new FtSelect(REQUIRED_VALUATION_FOR_DISCRETIONARY_VALUATION, "NEW_PRICE"),
                new FtCheckbox(ENABLE_3RD_VALUATION_FIELD, FtCheckbox.OperationType.ENABLE),

                //Repair & Valuation settings
                new FtCheckbox(ENABLE_COLLECTING_SELFRISK_BY_IC, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(ENABLE_DAMAGE_TYPE, FtCheckbox.OperationType.DISABLE),

                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CLAIMS_HANDLER, FtCheckbox.OperationType.ENABLE),
                new FtCheckbox(DISABLE_NEMKONTO_ON_REPLACEMENT_CUSTOMER, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(CPR_NUMBER_ON_REPLACEMENT_REQUIRED, FtCheckbox.OperationType.ENABLE),

                new FtCheckbox(SHOW_DAMAGE_TYPE_CONTROLS_IN_SID, FtCheckbox.OperationType.DISABLE),
                new FtCheckbox(SPLIT_REPLACEMENT_EMAIL, FtCheckbox.OperationType.DISABLE)
        });
    }
}

package com.scalepoint.automation.tests.dnd2;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.testng.annotations.Test;

import java.time.Year;


/**
 * The class represents smoke tests set for D&D2 functionality
 * run only on DK
 */

@Jira("https://jira.scalepoint.com/browse/CHARLIE-586")
@RequiredSetting(type = FTSetting.COMBINE_DISCOUNT_DEPRECATION, enabled = false)
public class DnD2_CompareCombineDDTests extends BaseTest {

    private int deprecationValue = 10;

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie586_addManually(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSid()
                .setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(claimItem.getTrygNewPrice())
                .setCustomerDemand(claimItem.getCustomerDemand())
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeUsedPrice())
                .addValuationPrice(claimItem.getUsedPrice())
                .closeValuationDialogWithOk()
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(deprecationValue)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, SettlementDialog.Valuation.NEW_PRICE);
                    asserts.assertCashCompensationIsDepreciated(deprecationValue, SettlementDialog.Valuation.CUSTOMER_DEMAND);
                    asserts.assertPriceIsSameInTwoColumns(SettlementDialog.Valuation.USED_PRICE);
                    asserts.assertIsVoucherDiscountApplied(claimItem.getTrygNewPrice());
                    asserts.assertIsLowestPriceValuationSelected(SettlementDialog.Valuation.VOUCHER, SettlementDialog.Valuation.NEW_PRICE,
                            SettlementDialog.Valuation.USED_PRICE, SettlementDialog.Valuation.CUSTOMER_DEMAND);
                });
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie586_addManuallyWithVoucherAndDepreciationLowerThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSid();
        settlementDialog.setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP())
                .setDepreciation(settlementDialog.getVoucherPercentage()/2)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(settlementDialog.getVoucherPercentage()/2, SettlementDialog.Valuation.NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(SettlementDialog.Valuation.VOUCHER, SettlementDialog.Valuation.NEW_PRICE);
                })
                .parseValuationRow(SettlementDialog.Valuation.VOUCHER)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(0));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie586_addManuallyWithVoucherAndDepreciationHigherThanVoucherDiscount(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSid();
        settlementDialog.setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP());
        int depreciationPercentage = settlementDialog.getVoucherPercentage()*2;
        settlementDialog.setDepreciation(depreciationPercentage)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, SettlementDialog.Valuation.NEW_PRICE);
                    asserts.assertIsLowestPriceValuationSelected(SettlementDialog.Valuation.VOUCHER, SettlementDialog.Valuation.NEW_PRICE);
                })
                .parseValuationRow(SettlementDialog.Valuation.NEW_PRICE)
                .doAssert(asserts -> asserts.assertDepreciationPercentageIs(depreciationPercentage));
    }

    @RequiredSetting(type = FTSetting.COMPARISON_OF_DISCOUNT_DEPRECATION, enabled = false)
    @RequiredSetting(type = FTSetting.ENABLE_DEPRECIATION_COLUMN)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie586_addManuallyWithComparisionOfDiscountAndDeprecationDisabled(User user, Claim claim, ClaimItem claimItem){

        SettlementDialog settlementDialog = loginAndCreateClaim(user, claim)
                .openSid();
        settlementDialog.setCategory(claimItem.getCategoryGroupBorn())
                .setSubCategory(claimItem.getCategoryBornBabyudstyr())
                .setNewPrice(claimItem.getTrygNewPrice())
                .setDescription(claimItem.getTextFieldSP());
        int depreciationPercentage = settlementDialog.getVoucherPercentage()*2;
        settlementDialog.setDepreciation(depreciationPercentage)
                .doAssert(asserts -> {
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, SettlementDialog.Valuation.NEW_PRICE);
                    asserts.assertCashCompensationIsDepreciated(depreciationPercentage, SettlementDialog.Valuation.VOUCHER);
                    asserts.assertIsLowestPriceValuationSelected(SettlementDialog.Valuation.VOUCHER, SettlementDialog.Valuation.NEW_PRICE);
                });
    }

    //@TODO: find solution how to deal with react.js components in ss
    @Ignore
    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @RequiredSetting(type = FTSetting.ENABLE_REGISTRATION_LINE_SELF_SERVICE)
    @Test(dataProvider = "testDataProvider", description = "")
    public void charlie586_addFromSelfServiceWithRedRule(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .enableAuditForIc(user.getCompanyName())
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.PASSWORD)
                .toMailsPage()
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .enterPassword(Constants.PASSWORD)
                .login()
                .addDescriptionWithOutSuggestions("test_product")
                .selectCategory("Foto & Video")
                .selectSubCategory("Videokamera")
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth("Apr")
                .addNewPrice("500")
                .saveItem()
                .sendResponseToEcc();

        login(user)
                .openActiveRecentClaim()
                .ensureAuditInfoPanelVisible()
                .checkStatusFromAudit("APPROVED");
    }

}

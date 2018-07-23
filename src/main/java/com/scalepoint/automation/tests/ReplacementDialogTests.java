package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.CompleteClaimPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.sid.SidCalculator;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG)
@RequiredSetting(type = FTSetting.ENABLE_CHANGING_OF_VOUCHER_PRICE_IN_REPLACEMENT_WIZARD)
    public class ReplacementDialogTests extends BaseTest{
    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-3281 Enable Changing of Voucher Price in Replacement Wizard")
    public void contents3281_changeVoucherPriceInReplacementWizard(User user, Claim claim, ClaimItem item) {
        Double newVoucherFaceValue = Constants.PRICE_500;
        Integer voucherDiscount = Constants.VOUCHER_DISCOUNT_10;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(Constants.PRICE_2400)
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withVoucher(item.getExistingVoucher_10());
                });
        new SettlementDialog().closeSidWithOk().toCompleteClaimPage().fillClaimForm(claim)
                .openReplacementWizard()
                .editVoucherFaceValue(newVoucherFaceValue)

        .doAssert(replacementDialog -> {
            replacementDialog.assertVoucherFaceValueIs(newVoucherFaceValue);
            replacementDialog.assertItemPriceValueIs(newVoucherFaceValue*(100-voucherDiscount)/100);
        });
    }

    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-592")
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_THROUGH_THE_SHOP)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-592 manual line is not shown in replacement dialog")
    public void contents3281_manualLineIsNotShownInReplacementDialog(User user, Claim claim, ClaimItem claimItem) {
        Double newPrice = Constants.PRICE_500;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(newPrice)
                        .withCategory(claimItem.getExistingCatWithoutVoucherAndSubCategory()))
                .closeSidWithOk();
        new SettlementPage().toCompleteClaimPage().fillClaimForm(claim)
                .openReplacementWizard()

        .doAssert(ReplacementDialog.Asserts::assertItemsListIsEmpty);
    }

    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-601")
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_THROUGH_THE_SHOP)
    @RequiredSetting(type = FTSetting.ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-601 allow shop access to remaining amount")
    public void contents601_allowShopAccessToRemainingAmount(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .addLinesForChosenCategories(claimItem.getCategoryGroupBorn(), claimItem.getExistingCat3_Telefoni());


        new SettlementPage().toCompleteClaimPage().fillClaimForm(claim)
                .openReplacementWizard()
                .replaceItemByIndex(0);



                //assert there is an extra option Giv kunden adgang og overfør resterende  til shoppen


               //assert there is replacement mail
    }

    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @RequiredSetting(type= FTSetting.USE_REPLACEMENT_FROM_ME, enabled = false)
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_THROUGH_THE_SHOP, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-592 Replacement can be disabled")
    public void contents592_turnOffReplacement(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .doAssert(CompleteClaimPage.Asserts::assertReplacementIsDisabled);

    }

    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @RequiredSetting(type= FTSetting.USE_REPLACEMENT_FROM_ME)
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_THROUGH_THE_SHOP, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-592 Replacement through the shop is disabled")
    public void contents592_turnOffReplacementThroughTheShop(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()

                .doAssert(ReplacementDialog.Asserts::assertGoToShopIsNotDisplayed);
    }
}





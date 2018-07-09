package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
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

@SuppressWarnings("AccessStaticViaInstance")
@RequiredSetting(type = FTSetting.USE_UCOMMERCE_SHOP, enabled = false)
@RequiredSetting(type = FTSetting.USE_NEW_REPLACEMENT_DIALOG, enabled = true)
public class ReplacementDialogTests extends BaseTest{
    @RunOn(DriverType.CHROME)
    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-3281 Enable Changing of Voucher Price in Replacement Wizard")
    public void contents3281_changeVoucherPriceInReplacementWizard(User user, Claim claim, ClaimItem item) {
        SidCalculator.VoucherValuationWithDepreciation voucherValuation = SidCalculator.calculateVoucherValuation(
                Constants.PRICE_2400,
                Constants.VOUCHER_DISCOUNT_10,
                0
        );

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(Constants.PRICE_2400)
                            .withCategory(item.getCategoryGroupBorn())
                            .withSubCategory(item.getCategoryBornBabyudstyr())
                            .withVoucher(item.getExistingVoucher_10());
                });
        new SettlementDialog().closeSidWithOk().toCompleteClaimPage().fillClaimForm(claim)
                .openReplacementWizard();


    }
}

package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.dialogs.ReplacementDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.CompleteClaimPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.NEW_PRICE;
import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.REPLACEMENT_WITH_MAIL;

@SuppressWarnings("AccessStaticViaInstance")
public class ReplacementDialogTests extends BaseTest {

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-3281 changing of VoucherPrice in ReplacementWizard")
    public void contents3281_changeVoucherPriceInReplacementWizard(User user, Claim claim, ClaimItem item) {
        Double newVoucherFaceValue = Constants.PRICE_500;
        Integer voucherDiscount = Constants.VOUCHER_DISCOUNT_10;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withNewPrice(Constants.PRICE_2400)
                            .withCategory(item.getCategoryBabyItems())
                            .withVoucher(item.getExistingVoucher_10());
                });

        new SettlementDialog()
                .closeSidWithOk()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()
                .editVoucherFaceValue(newVoucherFaceValue)

                .doAssert(replacementDialog -> {
                    replacementDialog.assertVoucherFaceValueIs(newVoucherFaceValue);
                    replacementDialog.assertItemPriceValueIs(newVoucherFaceValue * (100 - voucherDiscount) / 100);
                });
    }

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-592")
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-592 manual line is not shown in replacement dialog")
    public void contents592_manualLineIsNotShownInReplacementDialog(User user, Claim claim, ClaimItem claimItem) {
        Double newPrice = Constants.PRICE_500;

        loginAndCreateClaim(user, claim)
                .openSidAndFill(formFiller -> formFiller
                        .withNewPrice(newPrice)
                        .withCategory(claimItem.getCategoryOther()))
                .setValuation(NEW_PRICE)
                .closeSidWithOk();
        new SettlementPage()
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .openReplacementWizard()

                .doAssert(ReplacementDialog.Asserts::assertItemsListIsEmpty);
    }

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-601")
    @RequiredSetting(type = FTSetting.ENABLE_CLAIMHANDLERS_ALLOW_SHOP_ACCESS_FOR_REMAINING_AMOUNT_IN_REPLACEMENT)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-601 allow shop access to remaining amount")
    public void contents601_allowShopAccessToRemainingAmount(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .addLinesForChosenCategories(claimItem.getCategoryBabyItems().getGroupName(), claimItem.getCategoryBicycles().getGroupName());


        new SettlementPage().toCompleteClaimPage().fillClaimForm(claim)
                .openReplacementWizard()

                .replaceItemByIndex(0)
                .getAccessToShopForRemainingAmount()
                .to(MyPage.class)

                .doAssert(MyPage.Asserts::assertClaimCompleted)
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail ->
                        mail.isMailExist(REPLACEMENT_WITH_MAIL));
    }

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_FROM_ME, enabled = false)
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_THROUGH_THE_SHOP, enabled = false)
    @Test(dataProvider = "testDataProvider",
            description = "CONTENTS-592 ReplacementButton can be invisible")
    public void contents592_turnOffReplacementOption(User user, Claim claim) {

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .doAssert(CompleteClaimPage.Asserts::assertReplacementButtonIsNotVisible);

    }

    @Jira("https://jira.scalepoint.com/browse/CONTENTS-3281")
    @RequiredSetting(type = FTSetting.USE_REPLACEMENT_FROM_ME)
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





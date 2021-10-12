package com.scalepoint.automation.tests;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.ecc.thirdparty.integrations.model.enums.LossType;
import org.testng.annotations.Test;

import java.time.Year;

import static com.scalepoint.automation.utils.Constants.JANUARY;
import static com.scalepoint.automation.utils.Constants.PRICE_100;

@RequiredSetting(type = FTSetting.ENABLE_VOUCHER_PREDICTION)
public class VoucherPredictionTest extends BaseTest {

    private static final String lineDescription = "claimLine1";

    @Test(groups = {TestGroups.VOUCHER_PREDICTION}, dataProvider = "testDataProvider",
            description = "MIKE-41 - call Improved voucher match service is SID, manual line")
    public void mike41_improvedVoucherMatchForManualLines(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withText(lineDescription)
                        .withCategory(claimItem.getCategoryBicycles())
                        .withNewPrice(PRICE_100))
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .editLine()
                .doAssert(sid -> sid.assertVoucherIsSelected(mongoDbApi.getVoucherPredictedObjectsBy(claim.getClaimNumber(), lineDescription)
                        .get(0).getPredictedVoucher().getVoucherName()));
    }

    @RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
    @RequiredSetting(type = FTSetting.ENABLE_SELF_SERVICE)
    @Test(groups = {TestGroups.VOUCHER_PREDICTION}, dataProvider = "testDataProvider",
            description = "MIKE-42 - call Improved voucher match service in SelfService")
    public void mike41_improvedVoucherMatchForSelfService(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .requestSelfServiceWithEnabledAutoClose(claim, Constants.DEFAULT_PASSWORD)
                .savePoint(SettlementPage.class)
                .toMailsPage(mailserviceStub, databaseApi)
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions(lineDescription)
                .selectCategory(claimItem.getCategoryBicycles())
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .setLossType(LossType.DAMAGED)
                .isRepaired(true)
                .addRepairPrice(Constants.PRICE_100)
                .addNewPrice(Constants.PRICE_50)
                .addCustomerDemandPrice(PRICE_100)
                .saveItem()
                .sendResponseToEcc()
                .backToSavePoint(SettlementPage.class)
                .doAssert(asserts -> asserts.assertItemIsPresent(lineDescription))
                .findClaimLine(lineDescription)
                .editLine()
                .doAssert(sid -> sid.assertVoucherIsSelected(mongoDbApi.getVoucherPredictedObjectsBy(claim.getClaimNumber(), lineDescription)
                        .get(0).getPredictedVoucher().getVoucherName()));
    }
    @Test(groups = {TestGroups.VOUCHER_PREDICTION}, dataProvider = "testDataProvider",
            description = "MIKE-17 - call Improved voucher match service in excel")
    public void mike41_improvedVoucherMatchForExcelImport(User user, Claim claim, ClaimItem claimItem) {

        loginAndCreateClaim(user, claim)
                .importExcelFile(claimItem.getExcelPathVoucherPrediction())
                .doAssert(sid -> sid.assertItemIsPresent(lineDescription))
                .findClaimLine(lineDescription)
                .editLine()
                .doAssert(sid -> sid.assertVoucherIsSelected(mongoDbApi.getVoucherPredictedObjectsBy(claim.getClaimNumber(), lineDescription)
                        .get(0).getPredictedVoucher().getVoucherName()));
    }
}

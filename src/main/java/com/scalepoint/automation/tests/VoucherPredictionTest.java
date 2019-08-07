package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Constants.PRICE_100;

public class VoucherPredictionTest extends BaseTest {
@RunOn(DriverType.CHROME)
@RequiredSetting(type= FTSetting.ENABLE_VOUCHER_PREDICTION)
    @Test(dataProvider = "testDataProvider",
            description = "MIKE-41 - call Improved voucher match service is SID, manual line")
    public void mike41_improvedVoucherMatchForManualLines(User user, Claim claim, ClaimItem claimItem) {
        String lineDescription = RandomUtils.randomName("claimLine");

        loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> {
                    sid
                            .withText(lineDescription)
                            .withCategory(claimItem.getCategoryBicycles())
                            .withNewPrice(PRICE_100);
                }).closeSidWithOk()
                .findClaimLine(lineDescription)
                .editLine()
                .getVoucherPercentage();

    }
}

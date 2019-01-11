package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.RnvTaskType;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;


@RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING, enabled = false)
public class RnVSmokeTest extends BaseTest {

    @Test(dataProvider = "testDataProvider", description = "sendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {
        String lineDescription = "Line_1";

        loginAndCreateClaim(user, claim)
                .openSid()
                .fill(lineDescription, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100.00)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .changeTask(lineDescription, rnvTaskType.getRepair())
                .nextRnVstep()
                .sendRnV(agreement)

                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService().sendFeedback(claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }
}


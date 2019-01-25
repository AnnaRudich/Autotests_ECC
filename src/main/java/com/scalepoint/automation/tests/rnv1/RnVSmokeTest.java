package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.RnvTaskType;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverType;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.AuditResultEvaluationStatus.*;


public class RnVSmokeTest extends BaseTest {


    @RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnv_SendFeedbackIsSuccess(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {

        String lineDescription = RandomUtils.randomName("RnVLine");

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

        new RnvService().sendDefaultFeedback(claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }
    @RunOn(DriverType.CHROME)
    @RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING)
    @Test(dataProvider = "testDataProvider", description = "IntelligentRepair2. Audit Approved")
    public void IR2(User user, Claim claim, ServiceAgreement agreement, RnvTaskType rnvTaskType) {
        String lineDescription = RandomUtils.randomName("RnVLine");

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

        new RnvService().sendFeedbackWithRepairPrice(BigDecimal.valueOf(Constants.PRICE_50), claim);

        new ClaimNavigationMenu().toRepairValuationProjectsPage().getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);

        new RnvProjectsPage().expandTopTaskDetails()
                .getAssertion().assertAuditResponseText(APPROVE);
    }
}


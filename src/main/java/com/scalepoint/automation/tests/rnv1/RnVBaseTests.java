package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvCommunicationPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RnVBaseTests extends BaseTest {

    @Test(dataProvider = "testDataProvider",
            description = "verify Repair task start")
    public void eccs2961_2816_startRepairTask(User user, Claim claim, ServiceAgreement agreement) {
        String description = agreement.getRandomCLNameForRnV();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, agreement.getRepairType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement);

        //verify lines locked
        Assert.assertTrue(settlementPage.isLineExcludedAndReviewed(description));
        //verify icon on settlement page

        Assert.assertTrue(settlementPage.isClaimLineSendToRepairAndIconDisplays(description));

        //verify task started on RnV overview page

        RnvProjectsPage rnvProjectsPage = settlementPage.toRepairValuationProjectsPage();
        Assert.assertTrue(rnvProjectsPage.isTaskDisplayed(agreement.getTestAgreementForRnV()));
        Assert.assertEquals(rnvProjectsPage.getTaskStatus(agreement.getTestAgreementForRnV()), agreement.getWaitingStatus());

        rnvProjectsPage.expandTopTaskDetails();
        Assert.assertEquals(rnvProjectsPage.getTaskTypeByCLName(description), agreement.getRepairType());

        //verify email sent
        RnvCommunicationPage rnvCommunicationPage = rnvProjectsPage.navigateToCommunicationTab();
        Assert.assertTrue(rnvCommunicationPage.isMessageSent());

        boolean requiredMailSent = rnvCommunicationPage.
                toMailsPage().
                isRequiredMailSent(agreement.getNewTaskMailSubject());
        Assert.assertTrue(requiredMailSent);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Valuation task start")
    public void eccs2961_startValuationTask(User user, Claim claim, ServiceAgreement agreement) {
        String description = agreement.getRandomCLNameForRnV();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, agreement.getValuaitonType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement);

        //verify lines locked
        Assert.assertTrue(settlementPage.isLineExcludedAndReviewed(description));
        //verify icon on settlement page

        Assert.assertTrue(settlementPage.isClaimLineSendNotToRepairAndIconDisplays(description));

        //verify task started on RnV overview page

        RnvProjectsPage rnvProjectsPage = settlementPage.toRepairValuationProjectsPage();
        Assert.assertTrue(rnvProjectsPage.isTaskDisplayed(agreement.getTestAgreementForRnV()));
        Assert.assertEquals(rnvProjectsPage.getTaskStatus(agreement.getTestAgreementForRnV()), agreement.getWaitingStatus());

        rnvProjectsPage.expandTopTaskDetails();
        Assert.assertEquals(rnvProjectsPage.getTaskTypeByCLName(description), agreement.getValuaitonType());

        //verify email sent
        RnvCommunicationPage rnvCommunicationPage = rnvProjectsPage.navigateToCommunicationTab();
        Assert.assertTrue(rnvCommunicationPage.isMessageSent());

        boolean requiredMailSent = rnvCommunicationPage.
                toMailsPage().
                isRequiredMailSent(agreement.getNewTaskMailSubject());
        Assert.assertTrue(requiredMailSent);
    }

    private void testStartTask(User user, Claim claim, ServiceAgreement agreement, String taskType, boolean sentToRVIcon) {

    }


}

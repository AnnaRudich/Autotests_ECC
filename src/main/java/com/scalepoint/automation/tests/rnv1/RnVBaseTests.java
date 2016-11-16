package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvCommunicationPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import static com.scalepoint.automation.pageobjects.pages.Page.to;

public class RnVBaseTests extends BaseTest {

    @DataProvider(name = "RnVBaseTests.startTestDataProvider")
    public static Object[][] startTestDataProvider(Method method) {
        try {
            ServiceAgreement serviceAgreement = (ServiceAgreement) TestData.Data.getInstance(ServiceAgreement.class);

            Object[][] params = new Object[4][];
            //verify Repair task start
            params[0] = combine(getTestDataParameters(method), serviceAgreement.getRepairType(), (StartTaskAssert) (settlementPage, description) -> {
                Assert.assertTrue(settlementPage.isLineExcludedAndReviewed(description));
                Assert.assertTrue(settlementPage.isClaimLineSendToRepairAndIconDisplays(description));
            });

            //verify Valuation task start
            params[1] = combine(getTestDataParameters(method), serviceAgreement.getValuaitonType(), (StartTaskAssert) (settlementPage, description) -> {
                Assert.assertTrue(settlementPage.isLineExcludedAndReviewed(description));
                Assert.assertTrue(settlementPage.isClaimLineSendNotToRepairAndIconDisplays(description));
            });


            //verify Match task start
            params[2] = combine(getTestDataParameters(method), serviceAgreement.getMatchServiceType(), (StartTaskAssert) (settlementPage, description) -> {
                Assert.assertTrue(settlementPage.isLineExcludedAndNotReviewed(description));
                Assert.assertTrue(settlementPage.isClaimLineSendNotToRepairAndIconDisplays(description));
            });

            //verify Repair estimate task start
            params[3] = combine(getTestDataParameters(method), serviceAgreement.getRepairEstimateType(), (StartTaskAssert) (settlementPage, description) -> {
                Assert.assertTrue(settlementPage.isLineExcludedAndNotReviewed(description));
                Assert.assertTrue(settlementPage.isClaimLineSendNotToRepairAndIconDisplays(description));

            });

            return params;
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[][]{{}};
        }
    }

    interface StartTaskAssert {
        void doAssert(SettlementPage page, String description);
    }

    @Test(dataProvider = "RnVBaseTests.startTestDataProvider",
            description = "verify tasks start")
    public void eccs2961_2816_startTaskTest(User user, Claim claim, ServiceAgreement agreement, String serviceAgreementType, StartTaskAssert taskAssert) {
        String description = agreement.getRandomCLNameForRnV();

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, serviceAgreementType)
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement);

        taskAssert.doAssert(settlementPage, description);

        //verify task started on RnV overview page
        RnvProjectsPage rnvProjectsPage = settlementPage.toRepairValuationProjectsPage();
        Assert.assertTrue(rnvProjectsPage.isTaskDisplayed(agreement.getTestAgreementForRnV()));
        Assert.assertEquals(rnvProjectsPage.getTaskStatus(agreement.getTestAgreementForRnV()), agreement.getWaitingStatus());

        rnvProjectsPage.expandTopTaskDetails();
        Assert.assertEquals(rnvProjectsPage.getTaskTypeByCLName(description), serviceAgreementType);

        //verify email sent
        RnvCommunicationPage rnvCommunicationPage = rnvProjectsPage.navigateToCommunicationTab();
        Assert.assertTrue(rnvCommunicationPage.isMessageSent());

        boolean requiredMailSent = rnvCommunicationPage.
                toMailsPage().
                isRequiredMailSent(agreement.getNewTaskMailSubject());
        Assert.assertTrue(requiredMailSent);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Repair task cancel")
    public void eccs2828_2847_cancelRepairTask(User user, Claim claim, ServiceAgreement agreement) {
        String description = agreement.getRandomCLNameForRnV();

        RnvProjectsPage rnvProjectsPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, agreement.getRepairType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement).toRepairValuationProjectsPage();

        rnvProjectsPage.expandTopTaskDetails().cancelTopTask();
        Assert.assertEquals(rnvProjectsPage.getTaskStatus(agreement.getTestAgreementForRnV()), agreement.getCancelledStatus());

        MailsPage mailsPage = rnvProjectsPage.navigateToCommunicationTab().toMailsPage();
        String latestMailSubject = mailsPage.getLatestMailSubject();
        Assert.assertTrue(latestMailSubject.contains(agreement.getCancelledTaskMailSubj()));

        RnvCommunicationPage rnvCommunicationPage = mailsPage.toRepairValuationProjectsPage().navigateToCommunicationTab();
        Assert.assertTrue(rnvCommunicationPage.isLatestMessageContains(agreement.getCancelledMessageText()), "Sent message contains text: " + agreement.getCancelledMessageText().toUpperCase());
        boolean lineIncludedAndNotReviewed = to(SettlementPage.class).isLineIncludedAndNotReviewed(agreement.getRandomCLNameForRnV());
        Assert.assertTrue(lineIncludedAndNotReviewed, "Line unlocked and included");
    }

    @Test(dataProvider = "testDataProvider",
            description = "eccs2605, 2847, 2965 verify no feedback received action and Waiting for Feedback status")
    public void eccs2605_2847_noFeedbackActionAndWaitingFeedbackStatus(User user, Claim claim, ServiceAgreement agreement) {
        String description = agreement.getRandomCLNameForRnV();

        RnvProjectsPage rnvProjectsPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, agreement.getRepairType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement).toRepairValuationProjectsPage();

        Assert.assertEquals(rnvProjectsPage.getTaskStatus(agreement.getTestAgreementForRnV()), agreement.getWaitingStatus());
        rnvProjectsPage.expandTopTaskDetails();
        Assert.assertEquals(rnvProjectsPage.getActionByCLName(agreement.getRandomCLNameForRnV()), "NO_FEEDBACK_RECEIVED");

        Assert.assertFalse(rnvProjectsPage.isAcceptBtnDisplays(), "Btn Accept NOT displays");
        Assert.assertFalse(rnvProjectsPage.isRejectBtnDisplays(), "Btn Reject NOT displays");
        Assert.assertTrue(rnvProjectsPage.isCancelBtnDisplays(), "Btn Cancel displays");
    }


}

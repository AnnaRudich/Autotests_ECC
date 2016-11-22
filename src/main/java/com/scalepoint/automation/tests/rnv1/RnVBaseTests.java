package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvCommunicationPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.ButtonPresence;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.ButtonType;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.ExcelDocUtil;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

@RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING, enabled = false)
public class RnVBaseTests extends BaseTest {

    @Test(dataProvider = "testDataProvider")
    public void test(User user) {
        MailsPage mailsPage = login(user, SettlementPage.class).toMailsPage();
        MailsPage.Mails mails = mailsPage.parseMails();
        System.out.println(mails);
    }

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
        String description = agreement.getClaimLineNameForRnV();

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
        RnvProjectsPage rnvPage = settlementPage.toRepairValuationProjectsPage()
                .getAssertion()
                .assertTaskDisplayed(agreement)
                .assertTaskHasWaitingStatus(agreement)
                .getPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasType(agreement, serviceAgreementType)
                .getPage();

        //verify email sent
        RnvCommunicationPage rnvCommunicationPage = rnvPage.navigateToCommunicationTab();
        Assert.assertTrue(rnvCommunicationPage.isMessageSent());

        boolean requiredMailSent = rnvCommunicationPage.
                toMailsPage().
                isRequiredMailSent(agreement.getNewTaskMailSubject());

        Assert.assertTrue(requiredMailSent);
    }

    @Test(dataProvider = "testDataProvider", description = "verify repair task can be cancelled")
    public void eccs2828_2847_cancelRepairTask(User user, Claim claim, ServiceAgreement agreement) {
        RnvProjectsPage rnvPage = prepareTask(user, claim, agreement)
                .expandTopTaskDetails()
                .cancelTopTask()
                .getAssertion()
                .assertTaskHasCancelledStatus(agreement)
                .getPage();

        MailsPage mailsPage = rnvPage
                .navigateToCommunicationTab()
                .toMailsPage();

        String latestMailSubject = mailsPage.getLatestMail(MailsPage.MailType.REPAIR_AND_VALUATION).getSubject();
        Assert.assertTrue(latestMailSubject.contains(agreement.getCancelledTaskMailSubj()));

        RnvCommunicationPage communicationPage = mailsPage
                .toRepairValuationProjectsPage()
                .navigateToCommunicationTab();

        Assert.assertTrue(communicationPage.isLatestMessageContains(agreement.getCancelledMessageText()), "Sent message contains text: " + agreement.getCancelledMessageText().toUpperCase());
        boolean lineIncludedAndNotReviewed = to(SettlementPage.class).isLineIncludedAndNotReviewed(agreement.getClaimLineNameForRnV());
        Assert.assertTrue(lineIncludedAndNotReviewed, "Line unlocked and included");
    }


    @Test(dataProvider = "testDataProvider",
            description = "eccs2605, 2847, 2965 verify no feedback received action and Waiting for Feedback status")
    public void eccs2605_2847_noFeedbackActionAndWaitingFeedbackStatus(User user, Claim claim, ServiceAgreement agreement) {
        prepareTask(user, claim, agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasWaitingStatus(agreement)
                .assertClaimLineHasFeedback(agreement.getClaimLineNameForRnV())
                .assertButtonPresence(ButtonType.ACCEPT, ButtonPresence.HIDDEN)
                .assertButtonPresence(ButtonType.REJECT, ButtonPresence.HIDDEN)
                .assertButtonPresence(ButtonType.CANCEL, ButtonPresence.SHOWN);
    }


    @Test(dataProvider = "testDataProvider",
            description = "verify no changes feedback")
    public void eccs2605_2847_noChangesFeedbackReceived(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertClaimLineHasNoChanges(agreement.getClaimLineNameForRnV())
                .getPage()
                .acceptTaskCompletly()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Update line feedback")
    public void eccs2847_updateLineAction(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_INVOICE)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertClaimLineHasUpdates(agreement.getClaimLineNameForRnV())
                .getPage()
                .acceptTaskCompletly()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify new line and exlude action feedback")
    public void eccs2847_createAndExcludeLineAction(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.DELETE_LINE)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertClaimLineExcluded(agreement.getClaimLineNameForRnV())
                .assertClaimLineCreated(agreement.getUpdDesc())
                .getPage().
                acceptTaskCompletly().
                getAssertion().
                assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify feedback received task details")
    public void eccs2847_2828_feedbackReceivedUI(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertButtonPresence(ButtonType.ACCEPT, ButtonPresence.SHOWN)
                .assertButtonPresence(ButtonType.REJECT, ButtonPresence.SHOWN)
                .assertButtonPresence(ButtonType.CANCEL, ButtonPresence.HIDDEN)
                .assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Completed feedback Accepted action")
    public void eccs2847_2605_feedbackCompetedAcceptAction(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .getPage()
                .acceptCL(agreement.getClaimLineNameForRnV())
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertClaimLineAccepted(agreement.getClaimLineNameForRnV());
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Completed feedback Reject action")
    public void eccs2847_2605_feedbackCompetedRejectAction(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .navigateToCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .getPage()
                .rejectCL(agreement.getClaimLineNameForRnV())
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertClaimLineRejected(agreement.getClaimLineNameForRnV());
    }

    private RnvProjectsPage prepareTask(User user, Claim claim, ServiceAgreement agreement) {
        String description = agreement.getClaimLineNameForRnV();
        return loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectClaimItemByDescription(description)
                .sendToRnV()
                .changeTask(description, agreement.getRepairType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage();
    }


}

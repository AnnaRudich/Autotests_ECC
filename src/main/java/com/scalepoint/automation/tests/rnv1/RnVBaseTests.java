package com.scalepoint.automation.tests.rnv1;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvCommunicationPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.ButtonPresence;
import com.scalepoint.automation.pageobjects.pages.rnv1.RnvProjectsPage.ButtonType;
import com.scalepoint.automation.services.externalapi.DatabaseApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.ExcelDocUtil;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.scalepoint.automation.pageobjects.pages.Page.to;

@RequiredSetting(type = FTSetting.ENABLE_REPAIR_VALUATION_AUTO_SETTLING, enabled = false)
public class RnVBaseTests extends BaseTest {

    @Autowired
    private DatabaseApi databaseApi;

    @BeforeMethod
    public void createDefaultServiceAgreement() {
        User scalepointUser = UsersManager.takeUser(CompanyCode.SCALEPOINT);
        try {
            databaseApi.createDefaultServiceAgreementIfNotExists(scalepointUser.getCompanyId());
        } finally {
            UsersManager.returnUser(scalepointUser);
        }
    }

    @DataProvider(name = "RnVBaseTests.startTestDataProvider")
    public static Object[][] startTestDataProvider(Method method) {
        try {
            ServiceAgreement serviceAgreement = (ServiceAgreement) TestData.Data.getInstance(ServiceAgreement.class);

            Object[][] params = new Object[4][];
            //verify Repair task start
            params[0] = combine(getTestDataParameters(method), serviceAgreement.getRepairType(), (StartTaskAssert) (settlementPage, description) -> {
                SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
                Assert.assertTrue(claimLine.isLineExcludedAndReviewed());
                Assert.assertTrue(claimLine.isClaimLineSentToRepair());
            });

            //verify Valuation task start
            params[1] = combine(getTestDataParameters(method), serviceAgreement.getValuaitonType(), (StartTaskAssert) (settlementPage, description) -> {
                SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
                Assert.assertTrue(claimLine.isLineExcludedAndReviewed());
                Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());
            });


            //verify Match task start
            params[2] = combine(getTestDataParameters(method), serviceAgreement.getMatchServiceType(), (StartTaskAssert) (settlementPage, description) -> {
                SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
                Assert.assertTrue(claimLine.isLineExcludedAndNotReviewed());
                Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());
            });

            //verify Repair estimate task start
            params[3] = combine(getTestDataParameters(method), serviceAgreement.getRepairEstimateType(), (StartTaskAssert) (settlementPage, description) -> {
                SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
                Assert.assertTrue(claimLine.isLineExcludedAndNotReviewed());
                Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());

            });

            return params;
        } catch (Exception e) {
            LoggerFactory.getLogger(RnVBaseTests.class).error(e.getMessage(), e);
            return new Object[][]{{}};
        }
    }

    interface StartTaskAssert {
        void doAssert(SettlementPage page, String description);
    }

    @Test(dataProvider = "RnVBaseTests.startTestDataProvider",
            description = "verify tasks start")
    public void eccs2961_2816_startTaskTest(User user, Claim claim, ServiceAgreement agreement, String serviceAgreementType, StartTaskAssert taskAssert) {
        String description = "Line 1";;

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .findClaimLine(description)
                .selectLine()
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
                .assertTaskHasType(description, serviceAgreementType)
                .getPage();

        //verify email sent
        RnvCommunicationPage rnvCommunicationPage = rnvPage.toCommunicationTab();
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
                .toCommunicationTab()
                .toMailsPage();

        String latestMailSubject = mailsPage.getLatestMail(MailsPage.MailType.REPAIR_AND_VALUATION).getSubject();
        Assert.assertTrue(latestMailSubject.contains(agreement.getCancelledTaskMailSubj()));

        RnvCommunicationPage communicationPage = mailsPage
                .toRepairValuationProjectsPage()
                .toCommunicationTab();

        communicationPage.assertLatestMessageContains(agreement.getCancelledMessageText());
        boolean lineIncludedAndNotReviewed = to(SettlementPage.class).findClaimLine(agreement.getClaimLineNameForRnV()).isLineIncludedAndNotReviewed();
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
                .toCommunicationTab()
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
                .toCommunicationTab()
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
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.DELETE_LINE)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertClaimLineExcluded(agreement.getClaimLineNameForRnV())
                .assertClaimLineCreated(agreement.getUpdDesc())
                .getPage()
                .acceptTaskCompletly()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify feedback received task details")
    public void eccs2847_2828_feedbackReceivedUI(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertButtonPresence(ButtonType.ACCEPT, ButtonPresence.SHOWN)
                .assertButtonPresence(ButtonType.REJECT, ButtonPresence.SHOWN)
                .assertButtonPresence(ButtonType.CANCEL, ButtonPresence.HIDDEN);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify Completed feedback Accepted action")
    public void eccs2847_2605_feedbackCompetedAcceptAction(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        prepareTask(user, claim, agreement)
                .toCommunicationTab()
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
                .toCommunicationTab()
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
                .findClaimLine(description)
                .selectLine()
                .sendToRnV()
                .changeTask(description, agreement.getRepairType())
                .changeAgreement(description, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage();
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify new agreement displays in the wizard only when it's assigned to CL category")
    public void eccs2605_feedbackPartiallyCompleted(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        String lineTwo = "Line 2";
        loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .ok()
                .addManually()
                .fillBaseData(lineTwo, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .ok()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .changeAgreement(lineTwo, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .getPage()
                .acceptCL(lineOne)
                .getAssertion()
                .assertTaskHasPartlyCompletedStatus(agreement)
                .assertClaimLineAccepted(lineOne)
                .assertClaimLineHasNoChanges(lineTwo);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify status Completed wasn't changes after communication between CH and SePa")
    public void eccs2965_2828_completedNotChangedToWaitingFeedbackStatus(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .ok()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .getPage()
                .acceptCL(lineOne)
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .getPage()
                .toCommunicationTab()
                .sendTextMailToSePa("Test text")
                .assertLatestMessageContains("Test text")
                .toRepairValuationProjectsPage()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify status Feedback received wasn't changes after communication between CH and SePa")
    public void eccs2965_feedbackReceivedNotChangedToWaitingFeedbackStatus(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .ok()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .getPage()
                .toCommunicationTab()
                .sendTextMailToSePa("Test text")
                .assertLatestMessageContains("Test text")
                .toRepairValuationProjectsPage()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify status Cancelled wasn't changes after communication between CH and SePa")
    public void eccs2965_2828_cancelledNotChangedToWaitingFeedbackStatus(User user, Claim claim, ServiceAgreement agreement) {
        String lineOne = "Line 1";
        loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .ok()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .cancelTopTask()
                .getAssertion()
                .assertTaskHasCancelledStatus(agreement)
                .getPage()
                .toCommunicationTab()
                .sendTextMailToSePa("Test text")
                .assertLatestMessageContains("Test text")
                .toRepairValuationProjectsPage()
                .getAssertion()
                .assertTaskHasCancelledStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify status Competed changed to Feedback received after next feedback")
    public void eccs2965_completedChangedToFeedbackReceived(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        loginAndCreateClaim(user, claim)
                .addManually()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .ok()
                .selectAllLines()
                .sendToRnV()
                .changeTask(lineOne, agreement.getRepairType())
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES_KEEP_FILE)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .acceptCL(lineOne)
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .getPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify task is Repair for new line if all other lines sent to Repair")
    public void eccs3305_repairTaskForNewLine(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        String lineTwo = "Line 2";
        String lineThree = "Line 3";
        loginAndCreateClaim(user, claim)
                .addManually(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .addManually(lineTwo, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .addManually(lineThree, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .selectAllLines()
                .sendToRnV()
                .updateTaskTypeAndAgrForAllLines(agreement.getRepairType(), agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, ExcelDocUtil.FeedbackActionType.DELETE_CLAIM_LINE_ID)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasType(lineOne, agreement.getRepairType());
    }
}

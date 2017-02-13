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
import com.scalepoint.automation.utils.ExcelDocUtil.FeedbackActionType;
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
            UsersManager.releaseUser(scalepointUser);
        }
    }

    interface StartTaskAssert {
        void doAssert(SettlementPage page, String description);
    }

    @Test(dataProvider = "testDataProvider", description = "verify repair tasks start")
    public void eccs2961_2816_verifyRepairTaskStart(User user, Claim claim, ServiceAgreement agreement) {
        startTaskTest(user, claim, agreement, agreement.getRepairType(), (settlementPage, description) -> {
            SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
            Assert.assertTrue(claimLine.isLineExcludedAndReviewed());
            Assert.assertTrue(claimLine.isClaimLineSentToRepair());
        });
    }

    @Test(dataProvider = "testDataProvider", description = "verify valuation tasks start")
    public void eccs2961_2816_verifyValuationTaskStart(User user, Claim claim, ServiceAgreement agreement) {
        startTaskTest(user, claim, agreement, agreement.getValuaitonType(), (settlementPage, description) -> {
            SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
            Assert.assertTrue(claimLine.isLineExcludedAndReviewed());
            Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());
        });
    }

    @Test(dataProvider = "testDataProvider", description = "verify match tasks start")
    public void eccs2961_2816_verifyMatchTaskStart(User user, Claim claim, ServiceAgreement agreement) {
        startTaskTest(user, claim, agreement, agreement.getMatchServiceType(), (settlementPage, description) -> {
            SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
            Assert.assertTrue(claimLine.isLineExcludedAndNotReviewed());
            Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());
        });
    }

    @Test(dataProvider = "testDataProvider", description = "verify repair estimate tasks start")
    public void eccs2961_2816_verifyRepairEstimateTaskStart(User user, Claim claim, ServiceAgreement agreement) {
        startTaskTest(user, claim, agreement, agreement.getRepairEstimateType(), (settlementPage, description) -> {
            SettlementPage.ClaimLine claimLine = settlementPage.findClaimLine(description);
            Assert.assertTrue(claimLine.isLineExcludedAndNotReviewed());
            Assert.assertTrue(claimLine.isClaimLineSendNotToRepairAndIconDisplays());
        });
    }

    private void startTaskTest(User user, Claim claim, ServiceAgreement agreement, String serviceAgreementType, StartTaskAssert taskAssert) {
        String description = "Line 1";

        SettlementPage settlementPage = loginAndCreateClaim(user, claim)
                .openAddManuallyDialog()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .closeSidWithOk()
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
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .doFeedback(user, agreement, FeedbackActionType.NO_INVOICE)
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
                .doFeedback(user, agreement, FeedbackActionType.DELETE_LINE)
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
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .openAddManuallyDialog()
                .fillBaseData(description, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .closeSidWithOk()
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
                .openAddManuallyDialog()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .closeSidWithOk()
                .openAddManuallyDialog()
                .fillBaseData(lineTwo, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .closeSidWithOk()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .changeAgreement(lineTwo, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .openAddManuallyDialog()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .closeSidWithOk()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .openAddManuallyDialog()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .closeSidWithOk()
                .selectAllLines()
                .sendToRnV()
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
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
                .openAddManuallyDialog()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .closeSidWithOk()
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
                .openAddManuallyDialog()
                .fillBaseData(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .setReviewed(true)
                .closeSidWithOk()
                .selectAllLines()
                .sendToRnV()
                .changeTask(lineOne, agreement.getRepairType())
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES_KEEP_FILE)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .acceptCL(lineOne)
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .getPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.NO_CHANGES)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement);
    }

    //verify task is Repair for new line if all other lines sent to Repair
    @Test(dataProvider = "testDataProvider")
    public void eccs3305_testTaskIsRepairForNewLineIfAllRepair(User user, Claim claim, ServiceAgreement agreement) {
        testTaskTypesForNewLine(user, claim, agreement,  agreement.getRepairType(), FeedbackActionType.DELETE_CLAIM_LINE_ID, agreement.getRepairType());
    }

    //verify task is Repair for new line if Repair price was added
    @Test(dataProvider = "testDataProvider")
    public void eccs3305_testTaskIsRepairForNewLineIfRepairPriceAdded(User user, Claim claim, ServiceAgreement agreement) {
        testTaskTypesForNewLine(user, claim, agreement,  agreement.getValuaitonType(), FeedbackActionType.DELETE_CLAIM_LINE_ID_ADD_REPAIR_PRICE, agreement.getRepairType());
    }

    //verify task is Valuation for new line if all other lines sent to Valuation
    @Test(dataProvider = "testDataProvider")
    public void eccs3305_testTaskIsValuationForNewLineIfAllValuation(User user, Claim claim, ServiceAgreement agreement) {
        testTaskTypesForNewLine(user, claim, agreement,  agreement.getValuaitonType(), FeedbackActionType.DELETE_CLAIM_LINE_ID, agreement.getValuaitonType());
    }

    //verify task is match Service for new line if all other lines sent to match service
    @Test(dataProvider = "testDataProvider")
    public void eccs3305_testTaskIsServiceForNewLineIfAllService(User user, Claim claim, ServiceAgreement agreement) {
        testTaskTypesForNewLine(user, claim, agreement,  agreement.getMatchServiceType(), FeedbackActionType.DELETE_CLAIM_LINE_ID, agreement.getMatchServiceType());
    }

    private void testTaskTypesForNewLine(User user, Claim claim, ServiceAgreement agreement,
                                              String taskTypeForAllLines, FeedbackActionType feedbackActionType, String taskTypeForFirstLine) {
        String lineOne = "Line 1";
        String lineTwo = "Line 2";
        String lineThree = "Line 3";
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .openAddManuallyDialog(lineTwo, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .openAddManuallyDialog(lineThree, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .selectAllLines()
                .sendToRnV()
                .updateTaskTypeAndAgrForAllLines(taskTypeForAllLines, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, feedbackActionType)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasType(agreement.getUpdDesc(), taskTypeForFirstLine);
    }

    @Test(dataProvider = "testDataProvider",
            description = "verify task is Valuation for new line if all other lines sent to different tasks")
    public void eccs3305_valuationTaskForNewLine2(User user, Claim claim, ServiceAgreement agreement) throws Exception {
        String lineOne = "Line 1";
        String lineTwo = "Line 2";
        String lineThree = "Line 3";
        loginAndCreateClaim(user, claim)
                .openAddManuallyDialog(lineOne, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .openAddManuallyDialog(lineTwo, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .openAddManuallyDialog(lineThree, agreement.getClaimLineCat_PersonligPleje(), agreement.getClaimLineSubCat_Medicin(), 100)
                .selectAllLines()
                .sendToRnV()
                .changeTask(lineOne, agreement.getRepairType())
                .changeTask(lineTwo, agreement.getRepairEstimateType())
                .changeTask(lineThree, agreement.getMatchServiceType())
                .changeAgreement(lineOne, agreement.getTestAgreementForRnV())
                .changeAgreement(lineTwo, agreement.getTestAgreementForRnV())
                .changeAgreement(lineThree, agreement.getTestAgreementForRnV())
                .nextRnVstep()
                .sendRnV(agreement)
                .toRepairValuationProjectsPage()
                .toCommunicationTab()
                .doFeedback(user, agreement, FeedbackActionType.DELETE_CLAIM_LINE_ID)
                .toRepairValuationProjectsPage()
                .waitForFeedbackReceived(agreement)
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasType(agreement.getUpdDesc(), agreement.getValuaitonType());
    }
}

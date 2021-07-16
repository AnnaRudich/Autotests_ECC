package com.scalepoint.automation.tests.rnv.rnv2;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.ClaimNavigationMenu;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage;
import com.scalepoint.automation.pageobjects.pages.rnv.tabs.InvoiceTab;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.RnvService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.ServiceAgreement;
import com.scalepoint.automation.utils.data.entity.input.Translations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.CUSTOMER_WELCOME;
import static com.scalepoint.automation.pageobjects.pages.rnv.ProjectsPage.AuditResultEvaluationStatus.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.SHOW_DAMAGE_TYPE_CONTROLS_IN_SID;

public class IntelligentRepair2WebServiceTest extends BaseTest {

    private static final String FEEDBACK_CLAIM_AUTO_COMPLETED_DATA_PROVIDER = "feedbackApprovedClaimAutoCompletedDataProvider";
    private static final String FEEDBACK_REJECTED_DATA_PROVIDER = "feedbackRejectedDataProvider";
    private static final String FEEDBACK_MANUAL_DATA_PROVIDER = "feedbackManualDataProvider";
    private static final String DAMAGE_TYPE_EDITED_IN_RNV_DATA_PROVIDER = "damageTypeEditedInRnvDataProvider";
    private static final String SEND_LINE_TO_RNV_FAILS_ON_SERVICE_PARTNER_SIDE_DATA_PROVIDER = "sendLineToRnvFailsOnServicePartnerSideDataProvider";
    private static final String ON_LINE_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER = "onLineSentToRnVTaskLineDataProvider";
    private static final String MULTIPLE_LINES_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER = "multipleLinesSentToRnVTaskLineDataProvider";

    @BeforeMethod
    public void toSettlementDialog(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        Claim claim = getObjectByClass(parameters, Claim.class).get(0);

        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .openRecentClaim()
                .reopenClaim()
                .openSid();
    }
    /*
     * send line to RnV
     * send feedback via RnV Rest service (with Invoice)
     * Assert: the task will be auto accepted if RepairPrice=50 (ECC/wiremock/mappings/RnVFeedbackApprove.json)
     *          and claim auto completed (ECC/wiremock/mappings/ClaimAutoComplete.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Acceptation
     * Assert: InvoiceTab/InvoiceDialog. Invoice total is correct
     * Assert: Repair Price in Invoice is correct
     * Assert: Mail "Invoice is accepted" (Faktura godkendt) is sent
     *
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = FEEDBACK_CLAIM_AUTO_COMPLETED_DATA_PROVIDER,
            description = "Feedback(with invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackWithInvoiceApprovedClaimAutoCompletedTest(User user, Claim claim, ServiceAgreement agreement,
                                                                  Translations translations, String lineDescription,
                                                                  BigDecimal repairPrice) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> {
                    mail.isMailExist(CUSTOMER_WELCOME);
                });

        new CustomerDetailsPage()
                .toRepairValuationProjectsPage()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled();

        new ProjectsPage().expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .openInvoiceDialogForLineWithIndex(0)
                .findInvoiceLineByIndex(1)
                .assertTotalForTheLineWithIndex(1, repairPrice.doubleValue());
    }
    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: the task will be auto accepted if RepairPrice=50(ECC/wiremock/mappings/RnVFeedbackApprove.json)
     *          and claim auto completed (ECC/wiremock/mappings/ClaimAutoComplete.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Acceptation
     * Assert: InvoiceTab/InvoiceDialog is opened without errors
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = FEEDBACK_CLAIM_AUTO_COMPLETED_DATA_PROVIDER,
            description = "Feedback(no invoice) evaluation status: Approved. Claim auto-completed")
    public void feedbackNoInvoiceApprovedClaimAutoCompleted(User user, Claim claim, ServiceAgreement agreement,
                                                            Translations translations, String lineDescription,
                                                            BigDecimal repairPrice) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithoutInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        Page.to(MyPage.class)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim().toMailsPage();

        new CustomerDetailsPage().toRepairValuationProjectsPage()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled();

        new ProjectsPage().expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertAuditResponseText(APPROVE);

        new ProjectsPage().toInvoiceTab()
                .doAssert(InvoiceTab.Asserts::assertThereIsNoInvoiceGrid);

}
    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: the task will be auto rejected if RepairPrice=10(ECC/wiremock/mappings/RnVFeedbackRejected.json)
     * Assert: evaluateTaskButton is disabled when task is auto completed by Rejection
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = FEEDBACK_REJECTED_DATA_PROVIDER,
            description = "Feedback evaluation status: Reject")
    public void feedbackRejectedTest(User user, Claim claim, ServiceAgreement agreement, Translations translations,
                                     String lineDescription, BigDecimal repairPrice) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertEvaluateTaskButtonIsDisabled()
                .assertTaskHasCompletedStatus(agreement);

        new ProjectsPage().getAssertion().assertAuditResponseText(REJECT);
    }
    /*
     * send line to RnV
     * send feedback via RnV Rest service (withOUT Invoice)
     * Assert: audit response is Manual and the task will still be active if RepairPrice=100(ECC/wiremock/mappings/RnVFeedbackManual.json)
     * Assert: evaluateTaskButton is enabled when audit replied "MANUAL"
     * acceptFeedback manually through Evaluate Task dialog
     * Assert: task is Completed, evaluateTaskButton is disabled
     */
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE, enabled = false)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE}, dataProvider = FEEDBACK_MANUAL_DATA_PROVIDER,
            description = "Feedback evaluation status: Manual")
    public void feedbackManualTest(User user, Claim claim, ServiceAgreement agreement, Translations translations,
                                   String lineDescription, BigDecimal repairPrice) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        new RnvService()
                .sendFeedbackWithInvoiceWithRepairPrice(repairPrice, claim, rnvStub);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFeedbackReceivedStatus(agreement)
                .assertAuditResponseText(MANUAL);

        new ProjectsPage()
                .openEvaluateTaskDialog()
                .doAssert(evaluateTaskDialog->
                        evaluateTaskDialog.assertRepairPriceForTheTaskWithIndexIs(0, Constants.PRICE_100))
                .acceptFeedback()
                .getAssertion()
                .assertTaskHasCompletedStatus(agreement)
                .assertEvaluateTaskButtonIsDisabled();
    }

    @RequiredSetting(type = FTSetting.SHOW_NOT_CHEAPEST_CHOICE_POPUP, enabled = false)
    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = DAMAGE_TYPE_EDITED_IN_RNV_DATA_PROVIDER,
            description = "damageType is actualized in SID when it was changed in RnV wizard")
    public void damageTypeEditedInRnvTest(User user, Claim claim, ServiceAgreement agreement, Translations translations,
                                          ClaimItem claimItem, String lineDescription) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .selectDamageType(lineDescription, claimItem.getCategoryPersonalMedicine().getDamageTypes().get(1))
                .nextRnVstep()
                .sendRnvIsSuccess(agreement)
                .findClaimLine(lineDescription)
                .editLine()
                .doAssert(claimLine -> {
                    claimLine.assertDamageTypeEqualTo(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(1));
                });
    }
    /*
     * send line to RnV
     * Service Partner response is 500
     * Assert: the error is displayed while sending
     * Assert: the task has 'fail' status on projects page in RnV Wizard
     */
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = SEND_LINE_TO_RNV_FAILS_ON_SERVICE_PARTNER_SIDE_DATA_PROVIDER,
            description = "RnV1. SendLine to RnV, send Service Partner feedback")
    public void sendLineToRnvFailsOnServicePartnerSideTest(User user, Claim claim, ServiceAgreement agreement,
                                                           Translations translations, String lineDescription) {

        BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.UNAUTHORIZED_PRICE)
                .closeSidWithOk()
                .findClaimLine(lineDescription)
                .selectLine()
                .sendToRnV()
                .selectRnvType(lineDescription, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsFailOnServicePartnerSide(agreement)
                .findClaimLine(lineDescription)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsNotSentToRepair);

        new ClaimNavigationMenu().toRepairValuationProjectsPage()
                .expandTopTaskDetails()
                .getAssertion()
                .assertTaskHasFailStatus(agreement);
    }

    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = ON_LINE_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER,
            description = "One line is set as repair type in RnV")
    public void onLineSentToRnVTaskLineTest(User user, Claim claim, ServiceAgreement agreement,
                                            Translations translations, ClaimItem claimItem, String lineDescription1,
                                            String lineDescription2) {

        SettlementPage settlementPage = BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription1, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription2, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .selectLinesByDescriptions(lineDescription1, lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription1, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsNotSentToRepair);

        settlementPage
                .selectLinesByDescriptions(lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription2,translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }

    @RequiredSetting(type = SHOW_DAMAGE_TYPE_CONTROLS_IN_SID)
    @RequiredSetting(type = FTSetting.ENABLE_DAMAGE_TYPE)
    @Test(groups = {TestGroups.RNV, TestGroups.RNV2, TestGroups.INTELLIGENT_REPAIR2_WEB_SERVICE},
            dataProvider = MULTIPLE_LINES_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER,
            description = "Two lines are set as repair type in RnV")
    public void multipleLinesSentToRnVTaskLineTest(User user, Claim claim, ServiceAgreement agreement,
                                                   Translations translations, ClaimItem claimItem,
                                                   String lineDescription1, String lineDescription2) {

        SettlementPage settlementPage = BaseDialog.at(SettlementDialog.class)
                .fill(lineDescription1, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .openSid()
                .fill(lineDescription2, agreement.getLineCategory(), agreement.getLineSubCategory(), RnVMock.OK_PRICE)
                .enableDamage()
                .selectDamageType(claimItem.getCategoryPersonalMedicine().getDamageTypes().get(0))
                .closeSidWithOk()
                .selectLinesByDescriptions(lineDescription1, lineDescription2)
                .sendToRnV()
                .selectRnvType(lineDescription1, translations.getRnvTaskType().getRepair())
                .selectRnvType(lineDescription2, translations.getRnvTaskType().getRepair())
                .nextRnVstep()
                .sendRnvIsSuccess(agreement);

        settlementPage
                .findClaimLine(lineDescription1)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);

        settlementPage
                .findClaimLine(lineDescription2)
                .doAssert(SettlementPage.ClaimLine.Asserts::assertLineIsSentToRepair);
    }

    @DataProvider(name = MULTIPLE_LINES_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER)
    public static Object[][] multipleLinesSentToRnVTaskLineDataProvider(Method method) {

        String lineDescription1 = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        String lineDescription2 = RandomUtils.randomName(RV_LINE_DESCRIPTION);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription1, lineDescription2);
    }

    @DataProvider(name = FEEDBACK_CLAIM_AUTO_COMPLETED_DATA_PROVIDER)
    public static Object[][] feedbackApprovedClaimAutoCompletedDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = BigDecimal.valueOf(50.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, repairPrice);
    }

    @DataProvider(name = FEEDBACK_REJECTED_DATA_PROVIDER)
    public static Object[][] feedbackRejectedDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = BigDecimal.valueOf(10.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, repairPrice);
    }

    @DataProvider(name = FEEDBACK_MANUAL_DATA_PROVIDER)
    public static Object[][] feedbackManualDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        BigDecimal repairPrice = BigDecimal.valueOf(100.00);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription, repairPrice);
    }

    @DataProvider(name = DAMAGE_TYPE_EDITED_IN_RNV_DATA_PROVIDER)
    public static Object[][] damageTypeEditedInRnvDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription);
    }

    @DataProvider(name = SEND_LINE_TO_RNV_FAILS_ON_SERVICE_PARTNER_SIDE_DATA_PROVIDER)
    public static Object[][] sendLineToRnvFailsOnServicePartnerSideDataProvider(Method method) {

        String lineDescription = RandomUtils.randomName(RV_LINE_DESCRIPTION);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription);
    }

    @DataProvider(name = ON_LINE_SENT_TO_RNV_TASK_LINE_DATA_PROVIDER)
    public static Object[][] onLineSentToRnVTaskLineDataProvider(Method method) {

        String lineDescription1 = RandomUtils.randomName(RV_LINE_DESCRIPTION);
        String lineDescription2 = RandomUtils.randomName(RV_LINE_DESCRIPTION);

        return addNewParameters(TestDataActions.getTestDataParameters(method), lineDescription1, lineDescription2);
    }
}

package com.scalepoint.automation.tests.sid;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.scalepoint.automation.utils.Constants.*;

public class RejectReasonTests extends BaseTest {

    private ITestResult iTestResult;

    @BeforeMethod(alwaysRun = true)
    public void setITestResult(ITestResult iTestResult) {
        this.iTestResult = iTestResult;
    }

    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON},
            dataProvider = "testDataProvider",
            description = "Check if reject reason dropdown is disabled if there is 0 or 1 reason available for IC")
    public void charlie_549_checkIsRejectReasonDropdownDisabled(User user, Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withDepreciation(DEPRECIATION_10)
                )
                .rejectClaim()
                .doAssert(SettlementDialog.Asserts::assertRejectReasonDisabled)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Add reason to claim created before reason was created")
    public void charlie_549_checkIfCanAddNewRejectReasonToClaimCreatedBefore(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                             InsuranceCompany insuranceCompany, EccIntegration eccIntegration) {
        String location = createClaimAndLineUsingEccIntegration(user, eccIntegration).getResponse().extract().header("Location");
        String reason = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        loginFlow.login(user);
        Browser.driver().get(location);
        new SettlementPage()
                .editFirstClaimLine()
                .rejectClaim()
                .clickOK()
                .doAssert(SettlementDialog.Asserts::assertRejectReasonHasRedBorder)
                .selectRejectReason(reason)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check what happens with reasone after disabling it", enabled = false)
    public void charlie_549_disableReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                          ClaimItem claimItem, InsuranceCompany insuranceCompany, Claim claim) {
        String reason = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, true)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(claimItem.getCategoryBabyItems(), sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withAge(2, 2))
                .rejectClaim()
                .selectRejectReason(reason)
                .doAssert(sid -> sid.assertRejectReasonEqualTo(reason))
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected)
                .getMainMenu()
                .logOut();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, true)
                .findReason(reason)
                .disable()
                .assertReasonDisabled(reason)
                .logout();

        loginFlow.login(user)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .editFirstClaimLine()
                .doAssert(sid -> {
                    sid.assertRejectReasonEqualTo(reason);
                    sid.assertRejectReasonIsDisabled(reason);
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if reason is mandatory w/o discretionary and next w/o reject")
    public void charlie_549_makeRejectReasonMandatory(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                      Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reason = "Discretionary reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertDiscretionaryReasonHasRedBorder();
                    asserts.assertDiscretionaryReasonEnabled();
                })
                .selectDiscretionaryReason(reason)
                .rejectClaim()
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertRejectReasonHasRedBorder();
                    asserts.assertRejectReasonEnabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if reason is mandatory w/o discretionary but with filled reject")
    public void charlie_549_makeRejectReasonMandatoryWithRejectReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                      Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reason = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .selectRejectReason(reason)
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertDiscretionaryReasonHasRedBorder();
                    asserts.assertDiscretionaryReasonEnabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if reasons are mandatory and filled claim will be created")
    public void charlie_549_makeRejectReasonMandatoryRejectClaim(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                 Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        createClaimWithItemAndCloseWithReasons(user, claim, claimItem, insuranceCompany);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if DISCREATIONARY reason is mandatory and filled claim will be created")
    public void charlie_549_makeRejectReasonNotMandatoryRejectClaim(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                    Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        createClaimWithItemAndCloseWithReasons(user, claim, claimItem, insuranceCompany);
    }


    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if reject reasons will be not filled claim will be created")
    public void charlie_549_makeRejectReasonNotMandatoryRejectClaimWithoutReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                 Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonD = "Discretionary reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .addReason(reasonD)
                .findReason(reasonD)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaim(user, claim)
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .selectDiscretionaryReason(reasonD)
                .rejectClaim()
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if reject reasons will be not filled claim will be created")
    public void charlie_549_makeRejectReasonNotMandatoryRejectClaimWithoutDiscretionaryReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                              Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonR = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reasonR)
                .findReason(reasonR)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertDiscretionaryReasonHasRedBorder();
                    asserts.assertDiscretionaryReasonEnabled();
                })
                .selectRejectReason(reasonR)
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertDiscretionaryReasonHasRedBorder();
                    asserts.assertDiscretionaryReasonEnabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reason is mandatory and filled claim will be created")
    public void charlie_549_makeDiscretionaryReasonNotMandatoryRejectClaim(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                           Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        createClaimWithItemAndCloseWithReasons(user, claim, claimItem, insuranceCompany);
    }


    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reasons will be not filled claim will be created")
    public void charlie_549_makeDiscretionaryReasonNotMandatoryRejectClaimWithoutReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                        Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonR = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reasonR)
                .findReason(reasonR)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .selectRejectReason(reasonR)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reasons will be not filled claim will be created")
    public void charlie_549_makeDiscretionaryReasonNotMandatoryRejectClaimWithoutDiscretionaryReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                                     Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonD = "Discretionary reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .addReason(reasonD)
                .findReason(reasonD)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertRejectReasonHasRedBorder();
                    asserts.assertRejectReasonEnabled();
                })
                .selectDiscretionaryReason(reasonD)
                .clickOK()
                .doAssert(asserts -> {
                    asserts.assertRejectReasonHasRedBorder();
                    asserts.assertRejectReasonEnabled();
                });
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if any reason is mandatory and filled claim will be created")
    public void charlie_549_makeAnyReasonsNotMandatoryRejectClaim(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                  Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        createClaimWithItemAndCloseWithReasons(user, claim, claimItem, insuranceCompany);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reasons will be not filled claim will be created")
    public void charlie_549_makeAnyReasonsNotMandatoryRejectClaimWithoutReasons(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                Claim claim, ClaimItem claimItem) {
        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reasons will be not filled claim will be created")
    public void charlie_549_makeAnyReasonsNotMandatoryRejectClaimWithRejectReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                  Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonR = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reasonR)
                .findReason(reasonR)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .rejectClaim()
                .selectRejectReason(reasonR)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }

    @RequiredSetting(type = FTSetting.MAKE_REJECT_REASON_MANDATORY, enabled = false)
    @RequiredSetting(type = FTSetting.MAKE_DISCREATIONARY_REASON_MANDATORY, enabled = false)
    @Test(groups = {TestGroups.SID, TestGroups.REJECT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = "testDataProvider",
            description = "Check if discretionary reasons will be not filled claim will be created")
    public void charlie_549_makeAnyReasonsNotMandatoryRejectClaimWithDiscretionaryReason(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user,
                                                                                         Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonD = "Discretionary reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .addReason(reasonD)
                .findReason(reasonD)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(claimItem.getCategoryBabyItems(), sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .selectDiscretionaryReason(reasonD)
                .rejectClaim()
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }


    private void createClaimWithItemAndCloseWithReasons(@UserAttributes(company = CompanyCode.TRYGFORSIKRING) User user, Claim claim, ClaimItem claimItem, InsuranceCompany insuranceCompany) {
        String reasonD = "Discretionary reason åæéø " + System.currentTimeMillis();
        String reasonR = "Reject reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.REJECT, false)
                .addReason(reasonR)
                .findReason(reasonR)
                .getPage()
                .logout();

        openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .addReason(reasonD)
                .findReason(reasonD)
                .getPage()
                .logout();

        loginFlow.loginAndCreateClaimToEditPolicyDialog(user, claim)
                .cancel()
                .openSidAndFill(sid -> sid
                        .withCustomerDemandPrice(PRICE_100_000)
                        .withNewPrice(PRICE_2400)
                        .withCategory(claimItem.getCategoryBabyItems())
                        .withAge(0, 6))
                .openAddValuationForm()
                .addValuationType(claimItem.getValuationTypeDiscretionary())
                .addValuationPrice(1000.00)
                .closeValuationDialogWithOk()
                .selectDiscretionaryReason(reasonD)
                .rejectClaim()
                .selectRejectReason(reasonR)
                .closeSidWithOk()
                .doAssert(SettlementPage.Asserts::assertFirstLineIsRejected);
    }
}

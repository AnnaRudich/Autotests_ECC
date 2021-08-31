package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.testGroups.UserCompanyGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.grid.ValuationGrid.Valuation.DISCRETIONARY;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TRYGFORSIKRING;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
public class EditReasonTests extends BaseTest {

    private static final String EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER = "editReasonPageFromAdminExceededLengthDataProvider";
    private static final String EDIT_REASON_PAGE_FROM_ADMIN_DATA_PROVIDER = "editReasonPageFromAdminDataProvider";
    private static final String DELETE_REASON_IN_USE_DATA_PROVIDER = "deleteReasonInUseDataProvider";
    private static final String DELETE_REASON_NOT_IN_USE_DATA_PROVIDER = "deleteReasonNotInUseDataProvider";
    private static final String EDIT_REASON_IN_USE_DATA_PROVIDER = "editReasonInUseDataProvider";
    private static final String EDIT_REASON_NOT_IN_USE_DATA_PROVIDER = "editReasonNotInUseDataProvider";
    private static final String DISABLE_REASON_IN_USE_DATA_PROVIDER = "disableReasonInUseDataProvider";

    @BeforeMethod
    public void toEditReasonPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        InsuranceCompany insuranceCompany = getObjectByClass(parameters, InsuranceCompany.class).get(0);

        openEditReasonPage(insuranceCompany);
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input Reason text 501 char
     * THEN: The value should be trimmed to 500 char
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON},
            dataProvider = EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER,
            description = "Verify Reason text length is restricted to 500 characters")
    public void editReasonPageFromAdminExceededLengthTest(InsuranceCompany insuranceCompany,
                                                          String reasonWithExceededLength,
                                                          String reasonWithAllowedLength) {

        Page.at(EditReasonsPage.class)
                .addReason(reasonWithExceededLength)
                .findReason(reasonWithAllowedLength)
                .delete();
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason with native letters into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: logout and login as tryg user
     * AND: Go to Sid
     * THEN: Saved discretionary reason is visible in drop-down
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = EDIT_REASON_PAGE_FROM_ADMIN_DATA_PROVIDER,
            description = "CHARLIE-508 Verify  that native letters are applicable for reason and it's seen in SID.")
    public void editReasonPageFromAdminTest(@UserAttributes(company = TRYGFORSIKRING) User trygUser, Claim claim,
                                            InsuranceCompany insuranceCompany, ClaimItem claimItem, String reason,
                                            String description, Double customerDemandPrice, String month,
                                            int depreciation, Double discretionaryReasonPrice, Double newPrice) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason, description, customerDemandPrice, month,
                depreciation, discretionaryReasonPrice, newPrice);

        openEditReasonPage(insuranceCompany)
                .findReason(reason)
                .disable()
                .assertReasonDisabled(reason);
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: go to settlement page
     * AND: create a line and choose saved reason in drop-down
     * AND: Save the line
     * AND: go to Edit reason page,
     * AND: try to delete a reason
     * THEN: delete button is disabled
     */
    @RequiredSetting(type = FTSetting.SHOW_DISCREATIONARY_REASON)
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider =DELETE_REASON_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is not possible to delete reasons which are in use")
    public void deleteReasonInUseTest(@UserAttributes(company = TRYGFORSIKRING) User trygUser, Claim claim,
                                      InsuranceCompany insuranceCompany, ClaimItem claimItem, String reason,
                                      String description, Double customerDemandPrice, String month,
                                      int depreciation, Double discretionaryReasonPrice, Double newPrice) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason, description, customerDemandPrice, month,
                depreciation, discretionaryReasonPrice, newPrice);

        openEditReasonPage(insuranceCompany)
                .findReason(reason)
                .doAssert(EditReasonsPage.ReasonRow.Asserts::assertDeleteIsDisabled)
                .disable();
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: try to delete a reason
     * THEN: reason is deleted
     */
    @Bug(bug = "CHARLIE-1379 - fixed")
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON},
            dataProvider = DELETE_REASON_NOT_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is possible to delete reasons which are in not use")
    public void deleteReasonNotInUseTest(InsuranceCompany insuranceCompany, String reason) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .findReason(reason)
                .delete()
                .assertReasonNotFound(reason);
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: go to settlement page
     * AND: create a line and choose saved reason in drop-down
     * AND: Save the line
     * AND: go to Edit reason page,
     * AND: verify if the reason input field enabled
     * THEN: reason input field is disabled
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = EDIT_REASON_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is not possible to edit reasons which are in use")
    public void editReasonInUseTest(@UserAttributes(company = TRYGFORSIKRING) User trygUser, Claim claim,
                                    InsuranceCompany insuranceCompany, ClaimItem claimItem, String reason,
                                    String description, Double customerDemandPrice, String month,
                                    int depreciation, Double discretionaryReasonPrice, Double newPrice) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason, description, customerDemandPrice, month,
                depreciation, discretionaryReasonPrice, newPrice);

        openEditReasonPage(insuranceCompany)
                .findReason(reason)
                .doAssert(EditReasonsPage.ReasonRow.Asserts::assertReasonIsNotEditable)
                .disable();
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: try to edit a reason
     * THEN: the field is enabled and user can edit the reason
     */
    @Bug(bug = "CHARLIE-1379 - fixed")
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON},
            dataProvider = EDIT_REASON_NOT_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is possible to edit reasons which are in not use")
    public void editReasonNotInUseTest(InsuranceCompany insuranceCompany, String reason) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .findReason(reason)
                .doAssert(EditReasonsPage.ReasonRow.Asserts::assertReasonIsEditable)
                .delete()
                .assertReasonNotFound(reason);
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: go to settlement page
     * AND: Add the settlement item that matches the discretionary depreciation rule
     * AND: Add the "reason 1"
     * AND: Complete the claim.
     * AND: Disable "reason 1" on Admin
     * AND: go to settlement page, open claimline
     * THEN: "reason 1 deaktiveret" is displayed in the SID and as a hover
     */
    //TODO https://jira.scalepoint.com/browse/CHARLIE-1514
    @Test(enabled = false, groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, UserCompanyGroups.TRYGFORSIKRING},
            dataProvider = DISABLE_REASON_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is possible to disable reasons which are in use and they are still visible in SID")
    public void disableReasonInUseTest(@UserAttributes(company = TRYGFORSIKRING) User trygUser, Claim claim,
                                       InsuranceCompany insuranceCompany, ClaimItem claimItem, String reason,
                                       String description, Double customerDemandPrice, String month,
                                       int depreciation, Double discretionaryReasonPrice, Double newPrice) {

        openEditReasonPage(insuranceCompany).addReason(reason);

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason, description, customerDemandPrice, month,
                depreciation, discretionaryReasonPrice, newPrice);

        openEditReasonPage(insuranceCompany, true)
                .findReason(reason)
                .disable()
                .applyFilters(insuranceCompany.getFtTrygHolding(), EditReasonsPage.ReasonType.DISCRETIONARY, false)
                .assertReasonDisabled(reason)
                .logout();

        login(trygUser, CustomerDetailsPage.class, CurrentUser.getClaimId())
                .reopenClaim()
                .findClaimLine(reason)
                .doAssert(claimLine -> claimLine.assertTooltipPresent(reason))
                .editLine()
                .doAssert(sid -> sid.assertDiscretionaryReasonValuePresent(reason));
    }

    @DataProvider(name = EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER)
    public static Object[][] editReasonPageFromAdminExceededLengthDataProvider(Method method) {

        String reasonWithExceededLength = RandomStringUtils.randomAlphabetic(501);
        String reasonWithAllowedLength = reasonWithExceededLength.substring(0, 500);

        return new Object[][]{

            TestDataActions.getTestDataWithExternalParameters(method, reasonWithExceededLength, reasonWithAllowedLength).toArray()
        };
    }

    @DataProvider(name = EDIT_REASON_PAGE_FROM_ADMIN_DATA_PROVIDER)
    public static Object[][] editReasonPageFromAdminDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        String description = TEST_LINE_DESCRIPTION;
        Double customerDemandPrice = 1000.00;
        String month = "6";
        int depreciation = 5;
        Double discretionaryReasonPrice = 400.00;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason, description, customerDemandPrice, month, depreciation, discretionaryReasonPrice, newPrice).toArray()
        };
    }

    @DataProvider(name = DELETE_REASON_IN_USE_DATA_PROVIDER)
    public static Object[][] deleteReasonInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        String description = TEST_LINE_DESCRIPTION;
        Double customerDemandPrice = 1000.00;
        String month = "6";
        int depreciation = 5;
        Double discretionaryReasonPrice = 400.00;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason, description, customerDemandPrice, month, depreciation, discretionaryReasonPrice, newPrice).toArray()
        };
    }

    @DataProvider(name = DELETE_REASON_NOT_IN_USE_DATA_PROVIDER)
    public static Object[][] deleteReasonNotInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason).toArray()
        };
    }

    @DataProvider(name = EDIT_REASON_IN_USE_DATA_PROVIDER)
    public static Object[][] editReasonInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        String description = TEST_LINE_DESCRIPTION;
        Double customerDemandPrice = 1000.00;
        String month = "6";
        int depreciation = 5;
        Double discretionaryReasonPrice = 400.00;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason, description, customerDemandPrice, month, depreciation, discretionaryReasonPrice, newPrice).toArray()
        };
    }

    @DataProvider(name = EDIT_REASON_NOT_IN_USE_DATA_PROVIDER)
    public static Object[][] editReasonNotInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason).toArray()
        };
    }

    @DataProvider(name = DISABLE_REASON_IN_USE_DATA_PROVIDER)
    public static Object[][] disableReasonInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        String description = TEST_LINE_DESCRIPTION;
        Double customerDemandPrice = 1000.00;
        String month = "6";
        int depreciation = 5;
        Double discretionaryReasonPrice = 400.00;
        Double newPrice = 3000.00;

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason, description, customerDemandPrice, month, depreciation, discretionaryReasonPrice, newPrice).toArray()
        };
    }

    private void addReasonToClaimAndLogout(User trygUser, Claim claim, ClaimItem claimItem, String reason,
                                           String description, Double customerDemandPrice, String month, int depreciation,
                                           Double discretionaryReasonPrice, Double newPrice) {
        loginAndCreateClaimToEditPolicyDialog(trygUser, claim)
                .cancel()
                .openSid()
                .setDescription(description)
                .setCategory(claimItem.getCategoryShoes())
                .setCustomerDemand(customerDemandPrice)
                .enableAge()
                .selectMonth(month)
                .setDepreciationType(SettlementDialog.DepreciationType.DISCRETIONARY)
                .setDepreciation(depreciation)
                .setDiscretionaryPrice(discretionaryReasonPrice)
                .setNewPrice(newPrice)
                .setValuation(DISCRETIONARY)
                .selectDiscretionaryReason(reason)
                .doAssert(sid -> sid.assertDiscretionaryReasonEqualTo(reason))
                .closeSidWithOk()
                .getMainMenu()
                .logOut();
    }

    private EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany) {
        return openEditReasonPage(insuranceCompany, false);
    }

}





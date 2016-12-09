package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.ANDEN_VURDERING;
import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TRYGFORSIKRING;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static org.testng.Assert.*;

/**
 * Created by asa on 11/14/2016.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class EditReasonTests extends BaseTest {

    private static final String TEST_REASON_LINE = "Test reason line";

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input Reason text 501 char
     * THEN: The value should be trimmed to 500 char
     */
    @Bug(bug = "CHARLIE-1378 - not a bug")
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Reason text length is restricted to 500 characters")
    public void charlie508_2_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany) {
        String reasonWithExceededLength = RandomStringUtils.randomAlphabetic(501);
        String reasonWithAllowedLength = reasonWithExceededLength.substring(0, 500);

        openEditReasonPage(insuranceCompany)
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
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify  that native letters are applicable for reason and it's seen in SID.")
    public void charlie508_3_EditReasonPageFromAdmin(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                     Claim claim,
                                                     InsuranceCompany insuranceCompany,
                                                     ClaimItem claimItem) {

        String reason = "Sample reason åæéø " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason);

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
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE, enabled = false)
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is not possible to delete reasons which are in use")
    public void charlie508_5_DeleteReasonInUse(@UserCompany(TRYGFORSIKRING) User trygUser,
                                               Claim claim,
                                               InsuranceCompany insuranceCompany,
                                               ClaimItem claimItem) {
        String reason = "New reason " + System.currentTimeMillis();
        openEditReasonPage(insuranceCompany)
                .addReason(reason)
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason);

        openEditReasonPage(insuranceCompany)
                .findReason(reason)
                .assertDeleteIsDisabled()
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is possible to delete reasons which are in not use")
    public void charlie508_6_DeleteReasonNotInUse(InsuranceCompany insuranceCompany) {
        String reason = "New reason " + System.currentTimeMillis();
        openEditReasonPage(insuranceCompany)
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is not possible to edit reasons which are in use")
    public void charlie508_7_EditReasonInUse(@UserCompany(TRYGFORSIKRING) User trygUser,
                                             Claim claim,
                                             InsuranceCompany insuranceCompany,
                                             ClaimItem claimItem) {
        String reason = "New reason " + System.currentTimeMillis();
        openEditReasonPage(insuranceCompany)
                .addReason(reason)
                .findReason(reason)
                .getPage()
                .logout();

        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason);

        openEditReasonPage(insuranceCompany)
                .findReason(reason)
                .assertReasonIsNotEditable()
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is possible to edit reasons which are in not use")
    public void charlie508_8_EditReasonNotInUse(InsuranceCompany insuranceCompany) {
        String reason = "New reason " + System.currentTimeMillis();
        openEditReasonPage(insuranceCompany)
                .addReason(reason)
                .findReason(reason)
                .assertReasonIsEditable()
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

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is possible to disable reasons which are in use and they are still visible in SID")
    public void charlie508_9_DisableReasonInUse(@UserCompany(TRYGFORSIKRING) User trygUser,
                                                Claim claim,
                                                InsuranceCompany insuranceCompany,
                                                ClaimItem claimItem) {
        String reason = "New reason " + System.currentTimeMillis();

        openEditReasonPage(insuranceCompany).addReason(reason);
        addReasonToClaimAndLogout(trygUser, claim, claimItem, reason);

        openEditReasonPage(insuranceCompany, true)
                .findReason(reason)
                .disable()
                .assertReasonDisabled(reason)
                .logout();

        SettlementPage.ClaimLine claimLine = login(trygUser, SettlementPage.class).findClaimLine(TEST_REASON_LINE);
        assertTrue(claimLine.isTooltipPresent(reason), "Tooltip is not equal to " + reason);

        SettlementDialog settlementDialog = claimLine.editLine();
        assertTrue(settlementDialog.isDiscretionaryReasonValuePresent(reason), "Incorrect text discretionary reason");
    }


    private void addReasonToClaimAndLogout(User trygUser, Claim claim, ClaimItem claimItem, String reason) {
        SettlementPage settlementPage = loginAndCreateClaim(trygUser, claim);
        SettlementDialog dialog = settlementPage
                .addManually()
                .fillDescription(TEST_REASON_LINE)
                .fillCategory(claimItem.getExistingCat4())
                .fillSubCategory(claimItem.getExistingSubCat4())
                .fillCustomerDemand(1000)
                .enableAge()
                .selectMonth("6 " + claimItem.getMonths())
                .selectDepreciationType(1)
                .fillDepreciation(5)
                .fillDiscretionaryPrice(400)
                .fillNewPrice(3000)
                .selectValuation(ANDEN_VURDERING)
                .selectDiscretionaryReason(reason);
        assertEquals(dialog.getDiscretionaryReasonText(), reason, "Incorrect text discretionary reason");
        dialog.ok().getMainMenu().logOut();
    }

    private EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany) {
        return openEditReasonPage(insuranceCompany, false);
    }

    private EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany, boolean showDisable) {
        return login(getSystemUser(), AdminPage.class)
                .to(EditReasonsPage.class)
                .applyFilters(insuranceCompany.getFtTrygHolding(), EditReasonsPage.ReasonType.DISCRETIONARY, showDisable)
                .assertEditReasonsFormVisible();
    }
}





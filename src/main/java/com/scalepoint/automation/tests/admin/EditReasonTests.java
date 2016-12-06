package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.data.entity.*;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import java.util.Date;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.*;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.*;

import static org.testng.Assert.*;

/**
 * Created by asa on 11/14/2016.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class EditReasonTests extends BaseTest {

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that Edit reasons page is visible from admin")
    public void charlie508_1_EditReasonPageFromAdmin(User user,InsuranceCompany insuranceCompany) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
    }

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input Reason text more than 70 char
     * THEN: The value should be trimmed to 70 char
     */
    @Bug(bug = "CHARLIE-1378")
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Reason text length is restricted to 70 characters")
    public void charlie508_2_EditReasonPageFromAdmin(User user,InsuranceCompany insuranceCompany, DiscretionaryReason discretionaryReason) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String expectedReasonValue = discretionaryReason.getDiscretionaryReason70();
        String newValue = discretionaryReason.getDiscretionaryReason71();
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(expectedReasonValue);
        assertTrue(valueReasonPresent, "Reason should be trimmed to 70 char and equal to " + expectedReasonValue);
        editReasonsPage.deleteReason(newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
    }

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input into reason field native letters
     * AND: Click Save button
     * THEN: Reason is saved; no error message
     */
    @Bug(bug = "CHARLIE-1379 - fixed")
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify  that native letters are applicable.")
    public void charlie508_3_EditReasonPageFromAdmin(User user,InsuranceCompany insuranceCompany, DiscretionaryReason discretionaryReason) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String newValue = discretionaryReason.getDiscretionaryReasonNativeLet();
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be " + newValue);
        editReasonsPage.deleteReason(newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
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
    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify  that native letters are applicable for reason and it's seen in SID.")
    public void charlie508_4_EditReasonPageFromAdmin(User user,
                                                     InsuranceCompany insuranceCompany,
                                                     ClaimItem claimItem) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String claimLine = claimItem.getTextFieldSP();
        long timestamp = new Date().getTime();
        String newValue = "New reason " + timestamp;
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be equal to " + newValue);
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), MyPage.class,
                enable(FTSetting.SHOW_DISCREATIONARY_REASON),
                disable(FTSetting.SHOW_POLICY_TYPE)).
                getClaimMenu().
                logout();
        User trygUser = takeUser(CompanyCode.TRYGFORSIKRING);
        try {
            login(trygUser);
            SettlementPage line = new MyPage().
                    openRecentClaim().
                    reopenClaim();
        SettlementDialog settlementDialog = createClaimLine(line, claimItem, claimLine, newValue);
        assertEquals(settlementDialog.getDiscretionaryReasonText(), newValue, "Incorrect text discretionary reason");
        } finally {
            returnUser(trygUser);
        }
        functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), MyPage.class,
                enable(FTSetting.SHOW_DISCREATIONARY_REASON),
                disable(FTSetting.SHOW_POLICY_TYPE)).
                getClaimMenu().
                logout();
                goToEditReasonPage(user,insuranceCompany);
        editReasonsPage.deleteReason(newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
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

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify that it is not possible to delete reasons which are in use")
    public void charlie508_5_DeleteReasonInUse(User user, InsuranceCompany insuranceCompany,
                                               ClaimItem claimItem) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String claimLine = claimItem.getTextFieldSP();
        long timestamp = new Date().getTime();
        String newValue = "New reason " + timestamp;
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be equal to " + newValue);
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), MyPage.class,
                enable(FTSetting.SHOW_DISCREATIONARY_REASON),
                disable(FTSetting.SHOW_POLICY_TYPE)).
                getClaimMenu().
                logout();
        User trygUser = takeUser(CompanyCode.TRYGFORSIKRING);
        SettlementPage line;
        try {
            login(trygUser);
            line = new MyPage().
                    openRecentClaim().
                    reopenClaim();
        SettlementDialog settlementDialog = createClaimLine(line, claimItem, claimLine, newValue);
        assertEquals(settlementDialog.getDiscretionaryReasonText(), newValue, "Incorrect text discretionary reason");
        settlementDialog.ok();
        } finally {
            returnUser(trygUser);
        }
        line.saveClaim().
                getClaimMenu().
                logout();
        goToEditReasonPage(user,insuranceCompany);
        assertFalse(editReasonsPage.isDeleteEnable(newValue), "Delete button should be disabled!");
        cleanUpClaimLineAndReason(editReasonsPage,user,insuranceCompany,newValue);
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
    public void charlie508_6_DeleteReasonNotInUse(User user, InsuranceCompany insuranceCompany) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(),"Edit Reasons Form should be visible");
        long timestamp = new Date().getTime();
        String newValue = "New reason " + timestamp;
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be equal to " + newValue);
        editReasonsPage.deleteReason(newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
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
    public void charlie508_7_EditReasonInUse(User user, InsuranceCompany insuranceCompany,
                                                        ClaimItem claimItem) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String claimLine = claimItem.getTextFieldSP();
        long timestamp = new Date().getTime();
        String newValue = "New reason " + timestamp;
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be equal to " + newValue);
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(getSystemUser());
        functionalTemplatesApi.updateTemplate(user.getFtId(), MyPage.class,
                enable(FTSetting.SHOW_DISCREATIONARY_REASON),
                disable(FTSetting.SHOW_POLICY_TYPE)).
                getClaimMenu().
                logout();
        User trygUser = takeUser(CompanyCode.TRYGFORSIKRING);
        SettlementPage line;
        try {
            login(trygUser);
            line = new MyPage().
                    openRecentClaim().
                    reopenClaim();
        SettlementDialog settlementDialog = createClaimLine(line,claimItem,claimLine,newValue);
        assertEquals(settlementDialog.getDiscretionaryReasonText(), newValue, "Incorrect text discretionary reason");
        settlementDialog.ok();
        } finally {
            returnUser(trygUser);
        }
        line.saveClaim().
                getClaimMenu().
                logout();
        goToEditReasonPage(user,insuranceCompany);
        assertFalse(editReasonsPage.isReasonEditable(newValue), "The reason field should be disabled!");
        cleanUpClaimLineAndReason(editReasonsPage,user,insuranceCompany,newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
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
    public void charlie508_8_EditReasonNotInUse(User user, InsuranceCompany insuranceCompany) {
        EditReasonsPage editReasonsPage = goToEditReasonPage(user,insuranceCompany);
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        long timestamp = new Date().getTime();
        String newValue = "New reason " + timestamp;
        boolean valueReasonPresent = editReasonsPage.addReason(newValue)
                .save()
                .isValueReason(newValue);
        assertTrue(valueReasonPresent, "Reason should be equal to " + newValue);
        assertTrue(editReasonsPage.isReasonEditable(newValue),"The reason field should be enabled!");
        editReasonsPage.deleteReason(newValue);
        assertFalse(editReasonsPage.isReasonVisible(newValue),"Reason should be deleted!");
    }


    private SettlementDialog createClaimLine(SettlementPage settlementPage, ClaimItem claimItem, String claimLine, String reason) {
        String month = "6 ";
        return settlementPage.
                addManually().
                fillCategory(claimItem.getExistingCat4()).
                fillSubCategory(claimItem.getExistingSubCat4()).
                fillCustomerDemand(1000).
                enableAge().
                selectMonth(month + claimItem.getMonths()).
                selectDepreciationType(1).
                fillDepreciation(5).
                fillDiscretionaryPrice(400).
                fillNewPrice(3000).
                fillDescription(claimLine).
                selectValuation(ANDEN_VURDERING).
                selectDiscretionaryReason(reason);
    }


    private EditReasonsPage goToEditReasonPage(User user, InsuranceCompany insuranceCompany){
         return login(user, AdminPage.class).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
    }

    private void cleanUpClaimLineAndReason(EditReasonsPage editReasonPage, User user,InsuranceCompany insuranceCompany,String reason) {
        User trygUser = takeUser(CompanyCode.TRYGFORSIKRING);
        SettlementPage line = new SettlementPage();
        try {
            login(trygUser);
            line = new MyPage().
                    openRecentClaim().
                    reopenClaim();
            line.getToolBarMenu().
                    selectAll().
                    removeSelected();
        } finally {
            returnUser(trygUser);
        }
        line.saveClaim().
                getClaimMenu().
                logout();
            goToEditReasonPage(user,insuranceCompany);
            editReasonPage.deleteReason(reason);
    }
}





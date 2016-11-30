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
import com.scalepoint.automation.utils.data.entity.ClaimItem;
import com.scalepoint.automation.utils.data.entity.DiscretionaryReason;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.dialogs.SettlementDialog.Valuation.*;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.disable;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSettings.enable;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.returnUser;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.takeUser;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
    public void charlie508_1_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany) {
        EditReasonsPage editReasonsPage = login(getSystemUser()).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
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
    public void charlie508_2_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany, DiscretionaryReason discretionaryReason) {
        EditReasonsPage editReasonsPage = login(getSystemUser()).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String newValue = discretionaryReason.getDiscretionaryReason71();
        editReasonsPage.addReason(newValue);
        editReasonsPage.save();
        String expectedValue = discretionaryReason.getDiscretionaryReason70();
        assertTrue(editReasonsPage.isValueReason(expectedValue), "Reason should be trimmed to 70 char and equal to "
                + expectedValue);
    }

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input into reason field native letters
     * AND: Click Save button
     * THEN: Reason is saved; no error message
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify  that native letters are applicable.")
    public void charlie508_3_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany, DiscretionaryReason discretionaryReason) {
        EditReasonsPage editReasonsPage = login(getSystemUser()).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String newValue = discretionaryReason.getDiscretionaryReasonNativeLet();
        editReasonsPage.addReason(newValue);
        editReasonsPage.save();
        String expectedValue = discretionaryReason.getDiscretionaryReasonNativeLet();
        assertTrue(editReasonsPage.isValueReason(expectedValue), "Reason should be saved and equal to "
                + expectedValue);
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
                                                     DiscretionaryReason discretionaryReason,
                                                     ClaimItem claimItem) {
        String month = "6 ";
        EditReasonsPage editReasonsPage = login(user, AdminPage.class).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
        assertTrue(editReasonsPage.isEditReasonsFormVisible(), "Edit Reasons Form should be visible");
        String newValue = discretionaryReason.getDiscretionaryReasonNativeLet();
        editReasonsPage.addReason(newValue).save();
        editReasonsPage.save();
        String expectedValue = discretionaryReason.getDiscretionaryReasonNativeLet();
        assertTrue(editReasonsPage.isValueReason(expectedValue), "Reason should be saved and equal to "
                + expectedValue);
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
            SettlementDialog settlementDialog = line.
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
                    setDiscountAndDepreciation(false).
                    fillDescription(claimItem.getTextFieldSP()).
                    selectValuation(ANDEN_VURDERING).
                    selectDiscretionaryReason(discretionaryReason.getDiscretionaryReasonNativeLet());
            assertEquals(settlementDialog.getDiscretionaryReasonText(), discretionaryReason.getDiscretionaryReasonNativeLet(), "Incorrect text discretionary reason");
        } finally {
            returnUser(trygUser);
        }
    }
}

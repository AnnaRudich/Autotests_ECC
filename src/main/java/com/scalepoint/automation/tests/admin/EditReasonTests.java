package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.utils.data.entity.DiscretionaryReason;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static org.testng.Assert.assertTrue;

/**
 * Created by asa on 11/14/2016.
 */
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
        assertTrue(editReasonsPage.isEditReasonsFormVisible(),"Edit Reasons Form should be visible");
    }

    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input Reason text more than 70 char
     * THEN: The value should be trimmed to 70 char
     */

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Verify Reason text length is restricted to 70 characters")
    public void charlie508_2_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany, DiscretionaryReason discretionaryReason) {
        EditReasonsPage editReasonsPage = login(getSystemUser()).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
        assertTrue(editReasonsPage.isEditReasonsFormVisible(),"Edit Reasons Form should be visible");
        String newValue = discretionaryReason.getDiscretionaryReason71();
        editReasonsPage.addReason(newValue);
        editReasonsPage.save();
        String expectedValue = discretionaryReason.getDiscretionaryReason70();
        assertTrue(editReasonsPage.isValueReason(expectedValue),"Reason should be trimmed to 70 char and equal to "
                + expectedValue);
    }
}

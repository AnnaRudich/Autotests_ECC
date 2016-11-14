package com.scalepoint.automation.tests.admin;

import com.scalepoint.automation.BaseTest;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
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

    @Test(dataProvider = "testDataProvider", description = "CHARLIE-508 Edit reasons page from admin")
    public void charlie508_1_EditReasonPageFromAdmin(InsuranceCompany insuranceCompany) {
        EditReasonsPage editReasonsPage = login(getSystemUser()).
                to(EditReasonsPage.class).
                selectCompany(insuranceCompany.getFtTrygHolding()).
                selectReasonType("Discretionary choice").
                refresh();
        assertTrue(editReasonsPage.isEditReasonsFormVisible(),"Edit Reasons Form should be visible");
    }
}

package com.scalepoint.automation.tests.scalepointId;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.annotations.Bug;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.FUTURE;

@SuppressWarnings("AccessStaticViaInstance")
@Jira("https://jira.scalepoint.com/browse/CHARLIE-508")
public class EditReasonScalepointIdTests extends BaseTest {

    private static final String EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER = "editReasonPageFromAdminExceededLengthDataProvider";
    private static final String DELETE_REASON_NOT_IN_USE_DATA_PROVIDER = "deleteReasonNotInUseDataProvider";
    private static final String EDIT_REASON_NOT_IN_USE_DATA_PROVIDER = "editReasonNotInUseDataProvider";


    @BeforeMethod(alwaysRun = true)
    public void toEditReasonPage(Object[] objects) {

        List parameters = Arrays.asList(objects);

        InsuranceCompany insuranceCompany = getLisOfObjectByClass(parameters, InsuranceCompany.class).get(0);
        User user = getLisOfObjectByClass(parameters, User.class).get(0);

        openEditReasonPage(user, insuranceCompany);
    }
    /**
     * WHEN: Go to the Edit reasons page from admin.
     * AND: Select the Tryg Holding
     * THEN: Edit "Discretionary choice" reasons section is added.
     * WHEN: Try to input Reason text 501 char
     * THEN: The value should be trimmed to 500 char
     */
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, TestGroups.SCALEPOINT_ID},
            dataProvider = EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER,
            description = "Verify Reason text length is restricted to 500 characters")
    public void editReasonPageFromAdminExceededLengthTest(@UserAttributes(company = FUTURE, type = User.UserType.SCALEPOINT_ID) User user, InsuranceCompany insuranceCompany,
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
     * WHEN: Enter reason into reason field
     * AND: Click Save button
     * THEN: Reason is saved
     * WHEN: try to delete a reason
     * THEN: reason is deleted
     */
    @Bug(bug = "CHARLIE-1379 - fixed")
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, TestGroups.SCALEPOINT_ID},
            dataProvider = DELETE_REASON_NOT_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is possible to delete reasons which are in not use")
    public void deleteReasonNotInUseTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = FUTURE) User user, InsuranceCompany insuranceCompany, String reason) {

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
     * WHEN: try to edit a reason
     * THEN: the field is enabled and user can edit the reason
     */
    @Bug(bug = "CHARLIE-1379 - fixed")
    @Test(groups = {TestGroups.ADMIN, TestGroups.EDIT_REASON, TestGroups.SCALEPOINT_ID},
            dataProvider = EDIT_REASON_NOT_IN_USE_DATA_PROVIDER,
            description = "CHARLIE-508 Verify that it is possible to edit reasons which are in not use")
    public void editReasonNotInUseTest(@UserAttributes(type = User.UserType.SCALEPOINT_ID, company = FUTURE) User user, InsuranceCompany insuranceCompany, String reason) {

        Page.at(EditReasonsPage.class)
                .addReason(reason)
                .findReason(reason)
                .doAssert(EditReasonsPage.ReasonRow.Asserts::assertReasonIsEditable)
                .delete()
                .assertReasonNotFound(reason);
    }

    @DataProvider(name = EDIT_REASON_PAGE_FROM_ADMIN_EXCEEDED_LENGTH_DATA_PROVIDER)
    public static Object[][] editReasonPageFromAdminExceededLengthDataProvider(Method method) {

        String reasonWithExceededLength = RandomStringUtils.randomAlphabetic(501);
        String reasonWithAllowedLength = reasonWithExceededLength.substring(0, 500);

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reasonWithExceededLength, reasonWithAllowedLength).toArray()
        };
    }

    @DataProvider(name = DELETE_REASON_NOT_IN_USE_DATA_PROVIDER)
    public static Object[][] deleteReasonNotInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason).toArray()
        };
    }

    @DataProvider(name = EDIT_REASON_NOT_IN_USE_DATA_PROVIDER)
    public static Object[][] editReasonNotInUseDataProvider(Method method) {

        String reason = SAMPLE_REASON_TEXT + System.currentTimeMillis();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, reason).toArray()
        };
    }

    private EditReasonsPage openEditReasonPage(User user, InsuranceCompany insuranceCompany) {
        return loginFlow.openEditReasonPage(user, insuranceCompany, false);
    }
}





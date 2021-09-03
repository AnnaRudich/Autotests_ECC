package com.scalepoint.automation.tests.createCase;

import com.scalepoint.automation.pageobjects.pages.NewCustomerPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.PastedData;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ManualCreateClaimTest extends BaseTest {

    private static final String CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_DATA_PROVIDER = "createClaimUsingCopyPasteOnCreateClaimPageDataProvider";
    private static final String CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_EMPTY_TEXT_AREA_DATA_PROVIDER = "createClaimUsingCopyPasteOnCreateClaimPageEmptyTextAreaDataProvider";

    @BeforeMethod
    public void toNewCustomerPage(Object[] objects){

        List parameters = Arrays.asList(objects);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);

        login(user)
                .clickCreateNewCase();
    }

    @RequiredSetting(type = FTSetting.SHOW_COPY_PASTE_TEXTAREA)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @Test(groups = {TestGroups.CREATE_CASE, TestGroups.MANUAL_CREATE_CLAIM},
            dataProvider = CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_DATA_PROVIDER,
            description = "Check textArea on 'NewClaim' page that allows to copy-paste claim in specific format")
    public void createClaimUsingCopyPasteOnCreateClaimPageTest(User user, Claim claim, PastedData pastedData, String text) {

        Page.at(NewCustomerPage.class)
                .enterCopyPasteTextArea(text)
                .doAssert(newCustomerPage ->
                        newCustomerPage.assertCopyPasteMechanism()
                )
                .create()
                .toCustomerDetails()
                .doAssert(customeDetails ->
                        customeDetails
                                .assertClaimNumber(pastedData.getClaimNumber())
                                .assertFirstName(pastedData.getFirstName())
                                .assertLastName(pastedData.getLastName())
                                .assertAddress1(pastedData.getAddress())
                                .assertZipCode(pastedData.getZipCode())
                                .assertCity(pastedData.getCity())
                                .assertPolicyType(pastedData.getPolicyType())
                );
    }

    @RequiredSetting(type = FTSetting.SHOW_COPY_PASTE_TEXTAREA)
    @RequiredSetting(type = FTSetting.SHOW_POLICY_TYPE)
    @Test(groups = {TestGroups.CREATE_CASE, TestGroups.MANUAL_CREATE_CLAIM},
            dataProvider = CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_EMPTY_TEXT_AREA_DATA_PROVIDER,
            description = "Check textArea on 'NewClaim' page that allows to copy-paste claim in specific format")
    public void createClaimUsingCopyPasteOnCreateClaimPageEmptyTextAreaTest(User user, Claim claim, PastedData pastedData,
                                                                            LocalDate localDate) {

        Page.at(NewCustomerPage.class)
                .enterClaimNumber(claim.getClaimNumber())
                .enterFirstName(claim.getFirstName())
                .enterSurname(claim.getLastName())
                .selectDamageDate(localDate)
                .selectPolicyType(pastedData.getPolicyType())
                .enterCopyPasteTextArea("")
                .doAssert(newCustomerPage ->
                        newCustomerPage
                                .assertClaimNumber(claim.getClaimNumber())
                                .assertFirstName(claim.getFirstName())
                                .assertLastName(claim.getLastName())
                                .assertPolicyType(pastedData.getPolicyType())
                )
                .create()
                .toCustomerDetails()
                .doAssert(customeDetails ->
                        customeDetails
                                .assertClaimNumber(claim.getClaimNumber())
                                .assertFirstName(claim.getFirstName())
                                .assertLastName(claim.getLastName())
                                .assertAddress1("")
                                .assertZipCode("")
                                .assertCity("")
                                .assertPolicyType(pastedData.getPolicyType())
                );
    }

    @DataProvider(name = CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_DATA_PROVIDER)
    public static Object[][] createClaimUsingCopyPasteOnCreateClaimPageDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        Claim claim = getLisOfObjectByClass(parameters, Claim.class).get(0);

        String text = claim.getTextAreaWithRandomClaimNumber();
        PastedData pastedData = PastedData.parsePastedData(text);

        parameters.add(pastedData);
        parameters.add(text);

        return new Object[][]{

                parameters.toArray()
        };
    }

    @DataProvider(name = CREATE_CLAIM_USING_COPY_PASTE_ON_CREATE_CLAIM_PAGE_EMPTY_TEXT_AREA_DATA_PROVIDER)
    public static Object[][] createClaimUsingCopyPasteOnCreateClaimPageEmptyTextAreaDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        Claim claim = getLisOfObjectByClass(parameters, Claim.class).get(0);

        String text = claim.getTextAreaWithRandomClaimNumber();
        PastedData pastedData = PastedData.parsePastedData(text);

        parameters.add(pastedData);
        parameters.add(LocalDate.now().minusDays(3));

        return new Object[][]{

                parameters.toArray()
        };
    }
}

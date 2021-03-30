package com.scalepoint.automation.tests.createCase;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.PastedData;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

import java.time.LocalDate;

public class ManualCreateClaimTest extends BaseTest {

    @RequiredSetting(type = FTSetting.SHOW_COPY_PASTE_TEXTAREA)
    @Test(dataProvider = "testDataProvider", description = "Check textArea on 'NewClaim' page that allows to copy-paste claim in specific format")
    public void createClaimUsingCopyPasteOnCreateClaimPageTest(User user, Claim claim) {

        String text = claim.getTextAreaWithRandomClaimNumber();
        PastedData pastedData = PastedData.parsePastedData(text);

        login(user)
                .clickCreateNewCase()
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
    @Test(dataProvider = "testDataProvider", description = "Check textArea on 'NewClaim' page that allows to copy-paste claim in specific format")
    public void createClaimUsingCopyPasteOnCreateClaimPageEmptyTextAreaTest(User user, Claim claim) {

        LocalDate threeDaysBefore = LocalDate.now().minusDays(3);

        String text = claim.getTextAreaWithRandomClaimNumber();
        PastedData pastedData = PastedData.parsePastedData(text);

        login(user)
                .clickCreateNewCase()
                .enterClaimNumber(claim.getClaimNumber())
                .enterFirstName(claim.getFirstName())
                .enterSurname(claim.getLastName())
                .selectDamageDate(threeDaysBefore)
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
}

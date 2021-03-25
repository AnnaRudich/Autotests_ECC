package com.scalepoint.automation.tests.createCase;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.PastedData;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

public class ManualCreateClaimTest extends BaseTest {

    @RequiredSetting(type = FTSetting.SHOW_COPY_PASTE_TEXTAREA)
    @Test(dataProvider = "testDataProvider", description = "Check textArea on 'NewClaim' page that allows to copy-paste claim in specific format")
    public void createClaimUsingCopyPasteOnCreateClaimPageTest(User user, Claim claim) {

        String text = claim.getTextAreaWithRandomClaimNumber();
        PastedData pastedData = PastedData.parsePastedData(text);

        login(user)
                .clickCreateNewCase()
                .enterCopyPasteTextArea(text)
//                .selectPolicyType(1)
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
}

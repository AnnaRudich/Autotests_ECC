package com.scalepoint.automation.tests;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.annotations.Jira;
import com.scalepoint.automation.utils.annotations.ScalepointIdTest;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import org.testng.annotations.Test;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL;

public class CompleteClaimExternally extends BaseTest {

    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-515")
    @Test(groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY}, dataProvider = "testDataProvider",
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Base info page. Completed externally claim is added to the latest claims list with Closed Externally status")
    public void charlie515_completeClaimExternallyFromBaseInfoPage(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeExternally(claim, databaseApi)
                .doAssert(myPage ->
                        myPage.assertClaimHasStatus(claim.getStatusClosedExternally())
                )
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail ->
                        mail.isMailExist(SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL));
    }

    @ScalepointIdTest
    @RequiredSetting(type = FTSetting.ENABLE_SETTLE_EXTERNALLY_BUTTON_IN_SETTLEMENT_PAGE)
    @RequiredSetting(type = FTSetting.SETTLE_EXTERNALLY)
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-515")
    @Jira("https://jira.scalepoint.com/browse/CHARLIE-2152")
    @Test(groups = {TestGroups.COMPLETE_CLAIM_EXTERNALLY}, dataProvider = "testDataProvider",
            description = "CHARLIE-515 Completing of claim Externally (External email)" +
                    "Possible to Complete claim Externally from Settlement page")
    public void charlie515_completeClaimExternallyFromSettlementPage(User user, Claim claim) {
        loginAndCreateClaim(user, claim)
                .completeClaimWithoutMail(claim)
                .doAssert(myPage ->
                        myPage.assertClaimHasStatus(claim.getStatusClosedExternally())
                )
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail ->
                        mail.isMailExist(SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL));
    }
}





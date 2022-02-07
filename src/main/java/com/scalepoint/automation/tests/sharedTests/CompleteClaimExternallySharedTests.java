package com.scalepoint.automation.tests.sharedTests;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.SETTLEMENT_NOTIFICATION_CLOSED_EXTERNAL;

public class CompleteClaimExternallySharedTests extends BaseTest {

    public void completeClaimExternallyFromSettlementPageSharedTest(User user, Claim claim) {
        loginFlow.loginAndCreateClaim(user, claim)
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





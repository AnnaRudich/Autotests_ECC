package com.scalepoint.automation.tests.sharedTests;

import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;

import static com.scalepoint.automation.pageobjects.pages.MailsPage.MailType.CUSTOMER_WELCOME;

public class ClaimSharedTests extends BaseUITest {

    private final String POLICY_TYPE = "testPolicy ÆæØøÅåß";
    private final String EMPTY = "";

    public void reopenSavedClaimSharedTest(User user, Claim claim) {
        loginFlow.loginAndCreateClaim(user, claim)
                .saveClaim(claim)
                .openRecentClaim()
                .startReopenClaimWhenViewModeIsEnabled()
                .reopenClaim()
                .doAssert(settlementPage -> settlementPage.assertSettlementPagePresent("Settlement page is not loaded"));
    }

    /**
     * GIVEN: SP User, saved claim C1
     * WHEN: User cancels C1
     * THEN: "Cancelled" is the status of C1
     */
    public void cancelSavedClaimSharedTest(User user, Claim claim) throws Exception {
        loginFlow.loginAndCreateClaim(user, claim)
                .saveClaim(claim)
                .openRecentClaim()
                .cancelClaim()
                .to(MyPage.class)
                .doAssert(MyPage.Asserts::assertRecentClaimCancelled);
    }

    public void completeClaimWithMailSharedTest(User user, Claim claim) {

        loginFlow.loginAndCreateClaim(user, claim)
                .toCompleteClaimPage()
                .fillClaimForm(claim)
                .completeWithEmail(claim, databaseApi, true)
                .doAssert(myPage -> myPage.assertClaimHasStatus(claim.getStatusCompleted()))
                .openRecentClaim()
                .toMailsPage()
                .doAssert(mail -> mail.isMailExist(CUSTOMER_WELCOME));
    }
}

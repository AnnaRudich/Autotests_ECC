package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.DEPRECIATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_3 extends SendingCaseSettledEventV3Case2Base {


    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_3},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMailSendingCaseSettledEventV3Case2_3(User user, InsertSettlementItem item1, InsertSettlementItem item2) {
        close(user, item1, item2, CLOSE_WITH_MAIL);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_3},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMailSendingCaseSettledEventV3Case2_3(User user, InsertSettlementItem item1, InsertSettlementItem item2) {
        close(user, item1, item2, CLOSE_WITHOUT_MAIL);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_3},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeExternallySendingCaseSettledEventV3Case2_3(User user, InsertSettlementItem item1, InsertSettlementItem item2) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        makeFirstExternalSettlementAndAssert();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item2, 1000, 20);
        claimSettlementItemsService
                .editLines(item1, item2);

        closeExternally();
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2250.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1250.0, CLAIMANT, INSURANCE_COMPANY},
                        {COMPENSATION, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


    public void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        makeFirstSettlementAndAssert(closeCaseReason);

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item2, 1000, 20);
        claimSettlementItemsService
                .editLines(item1, item2);

        close(closeCaseReason);
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2250.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1250.0, CLAIMANT, SCALEPOINT},
                        {DEPRECIATION, 1250.0, SCALEPOINT, INSURANCE_COMPANY},
                        {COMPENSATION, 1000.0, CLAIMANT, SCALEPOINT},
                        {COMPENSATION, 1000.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


}

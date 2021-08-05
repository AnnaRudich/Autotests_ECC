package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.DEPRECIATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_2 extends SendingCaseSettledEventV3Case2Base {

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_2},
            dataProvider = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeWithMailSendingCaseSettledEventV3Case2_2(User user, ClaimRequest claimRequest,
                                                              InsertSettlementItem item1, InsertSettlementItem item2,
                                                              InsertSettlementItem item3, CreateClaimInput createClaimInput,
                                                              SettlementClaimService.CloseCaseReason closeCaseReason) {
        close(user, claimRequest, item1, item2, item3, closeCaseReason);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_2},
            dataProvider = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeExternallySendingCaseSettledEventV3Case2_2(User user, ClaimRequest claimRequest,
                                                                InsertSettlementItem item1, InsertSettlementItem item2,
                                                                InsertSettlementItem item3, CreateClaimInput createClaimInput) {

        makeFirstExternalSettlementAndAssert(claimRequest);

        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item3, 1000, 20);

        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        closeExternally(claimRequest);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {250.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1000.0, CLAIMANT, CLAIMANT},
                        {DEPRECIATION, 250.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }


    public void close(User user, ClaimRequest claimRequest, InsertSettlementItem item1, InsertSettlementItem item2,
                      InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {

        makeFirstSettlementAndAssert(claimRequest, closeCaseReason);

        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item3, 1000, 20);

        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {250.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1000.0, CLAIMANT, CLAIMANT},
                        {DEPRECIATION, 250.0, CLAIMANT, SCALEPOINT},
                        {DEPRECIATION, 250.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }
}

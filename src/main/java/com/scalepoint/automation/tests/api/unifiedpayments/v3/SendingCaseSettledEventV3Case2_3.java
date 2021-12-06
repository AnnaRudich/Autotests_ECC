package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.DEPRECIATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_3 extends SendingCaseSettledEventV3Case2Base {


    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeWithMailSendingCaseSettledEventV3Case2_3(User user, ClaimRequest claimRequest,
                                                              InsertSettlementItem item1, InsertSettlementItem item2,
                                                              CreateClaimInput createClaimInput,
                                                              SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(user, claimRequest, item1, item2, closeCaseReason);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeExternallySendingCaseSettledEventV3Case2_3(User user, ClaimRequest claimRequest,
                                                                InsertSettlementItem item1, InsertSettlementItem item2,
                                                                CreateClaimInput createClaimInput) {

        makeFirstExternalSettlementAndAssert(claimRequest);

        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item2, 1000, 20);

        claimSettlementItemsService
                .editLines(item1, item2);

        closeExternally(claimRequest);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }


    public void close(User user, ClaimRequest claimRequest, InsertSettlementItem item1, InsertSettlementItem item2,
                      SettlementClaimService.CloseCaseReason closeCaseReason) {

        makeFirstSettlementAndAssert(claimRequest, closeCaseReason);

        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item2, 1000, 20);

        claimSettlementItemsService
                .editLines(item1, item2);

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }
}

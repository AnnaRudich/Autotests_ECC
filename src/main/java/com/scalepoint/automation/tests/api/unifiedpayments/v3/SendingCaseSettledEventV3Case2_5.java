package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CREDIT_NOTE;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_5 extends SendingCaseSettledEventV3Case2Base {


    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_5},
            dataProvider = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeWithMailSendingCaseSettledEventV3Case2_5(User user, ClaimRequest claimRequest,
                                                              InsertSettlementItem item1, InsertSettlementItem item2,
                                                              InsertSettlementItem item3, CreateClaimInput createClaimInput,
                                                              SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(user, claimRequest, item1, item2, item3, closeCaseReason);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE2_5},
            dataProvider = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public void closeExternallySendingCaseSettledEventV3Case2_5(User user, ClaimRequest claimRequest,
                                                                InsertSettlementItem item1, InsertSettlementItem item2,
                                                                InsertSettlementItem item3, CreateClaimInput createClaimInput) {

        makeFirstExternalSettlementAndAssert(claimRequest);

        reopenClaim();

        setPrice(item1, 4000, 15);
        setPrice(item3, 1000, 20);

        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        setManualReduction(500);

        closeExternally(claimRequest);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, 0.0, 200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {1300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 1300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertThatSecondCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 4000, 5);
        setPrice(item2, 1000, 0);
        setPrice(item3, 1000, 40);

        claimSettlementItemsService
                .editLines(item1, item2, item3);

        setManualReduction(1000);
        setSelfRisk(750);

        closeExternally(claimRequest);

        event = getThirdEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, -250.0, -200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {1050.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 250.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, INSURANCE_COMPANY},
                        {COMPENSATION, 550.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertThatThirdCloseCaseEventWasCreated(claimRequest);
    }


    public void close(User user, ClaimRequest claimRequest, InsertSettlementItem item1, InsertSettlementItem item2,
                      InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {

        makeFirstSettlementAndAssert(claimRequest, closeCaseReason);

        reopenClaim();

        setPrice(item1, 4000, 15);
        setPrice(item3, 1000, 20);

        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        setManualReduction(500);

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getSecondEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, 0.0, 200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {1300.0, INSURANCE_COMPANY, SCALEPOINT}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 1300.0, INSURANCE_COMPANY, SCALEPOINT},
                        {COMPENSATION, 1300.0, SCALEPOINT, CLAIMANT}
                }
        );
        assertThatSecondCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 4000, 5);
        setPrice(item2, 1000, 0);
        setPrice(item3, 1000, 40);

        claimSettlementItemsService
                .editLines(item1, item2, item3);

        setManualReduction(1000);
        setSelfRisk(750);

        close(claimRequest, closeCaseReason);

        event = getThirdEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, -250.0, -200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {1050.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 250.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, SCALEPOINT},
                        {MANUAL_REDUCTION, 500.0, SCALEPOINT, INSURANCE_COMPANY},
                        {COMPENSATION, 550.0, CLAIMANT, SCALEPOINT},
                        {COMPENSATION, 550.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );
        assertThatThirdCloseCaseEventWasCreated(claimRequest);
    }
}

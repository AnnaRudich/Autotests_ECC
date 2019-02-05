package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;

import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.DEDUCTIBLE;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.DEPRECIATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.CLAIMANT;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.INSURANCE_COMPANY;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.SCALEPOINT;
import static com.scalepoint.automation.tests.api.unifiedpayments.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case4 extends BaseUnifiedPaymentsApiTest {

    @BeforeMethod
    void setUp(Object[] testArgs){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");


        User user = (User)testArgs[0];
        InsertSettlementItem item1 = (InsertSettlementItem) testArgs[1];
        InsertSettlementItem item2 = (InsertSettlementItem) testArgs[2];

        setPrice(item1, 3000, 20);
        setPrice(item2, 2000, 0);

        createClaim(user, 1000, 0, item1, item2);
    }

    void makeFirstExternalSettlementAndAssert() {
        closeExternally();
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 1000.0, 600.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 5000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {3400.0,INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 600.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 1000.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 3400.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();
    }



    void makeFirstSettlementAndAssert(SettlementClaimService.CloseCaseReason closeCaseReason) {
        close(closeCaseReason);
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 1000.0, 600.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 5000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {3400.0,INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 600.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 1000.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 3400.0, INSURANCE_COMPANY, SCALEPOINT},
                        {COMPENSATION, 3400.0, SCALEPOINT, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();
    }
}

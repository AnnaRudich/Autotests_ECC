package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.ReopenClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.*;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.CLAIMANT;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.INSURANCE_COMPANY;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.SCALEPOINT;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SendingCaseSettledEventV3 extends BaseUnifiedPaymentsApiTest {



    @BeforeMethod
    private void prepareClaimRequest(){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");
    }


    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void test(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        //GIVEN
        setPrice(item1, 1000, 50);
        setPrice(item2, 100, 0);
        setPrice(item3, 500, 20);
        setPrice(item4, 500, 20);

        createClaim(user, 250, 50, item1, item2, item3, item4);


        //WHEN
        settlementClaimService.close(claimRequest, CLOSE_WITH_MAIL);
        EventClaimSettled event = eventDatabaseApi.getEventClaimSettled(claimRequest);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, 250.0, 0.0, 700.0, 50.0);

        List<Obligation> obligations = event.getObligations();
        assertEquals(obligations.size(), 5);
        assertObligation(obligations.get(0), COMPENSATION, 1100.0, SCALEPOINT, CLAIMANT);
        assertObligation(obligations.get(1), COMPENSATION, 1100.0, INSURANCE_COMPANY, SCALEPOINT);
        assertObligation(obligations.get(2), DEPRECIATION, 700.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations.get(3), DEDUCTIBLE, 250.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations.get(4), MANUAL_REDUCTION, 50.0, CLAIMANT, CLAIMANT);

        List<Expense> expenses = event.getExpenses();
        assertEquals(expenses.size(), 1);
        assertExpense(expenses.get(0), CASH_COMPENSATION, 2100.0, INSURANCE_COMPANY, CLAIMANT);

        List<Payment> payments = event.getPayments();
        assertEquals(payments.size(), 1);
        assertPayment(payments.get(0), 1100.0,INSURANCE_COMPANY, SCALEPOINT);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);

        //WHEN
        new ReopenClaimService().reopenClaim();

        setPrice(item1, 100, 0);
        claimSettlementItemsService
                .removeLines(item1, item2, item3, item4)
                .addLines(item1);

        settlementClaimService.close(claimRequest, CLOSE_WITH_MAIL);
        event = eventDatabaseApi.getEventClaimSettled(claimRequest, 1);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, -150.0, 0.0, -700.0, -50.0);

        obligations = event.getObligations();
        assertEquals(obligations.size(), 5);
        assertObligation(obligations.get(0), DEPRECIATION, 700.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations.get(1), DEDUCTIBLE, 150.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations.get(2), MANUAL_REDUCTION, 50.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations.get(3), COMPENSATION, 1100.0, CLAIMANT, SCALEPOINT);
        assertObligation(obligations.get(4), COMPENSATION, 1100.0, SCALEPOINT, INSURANCE_COMPANY);

        expenses = event.getExpenses();
        assertEquals(expenses.size(), 1);
        assertExpense(expenses.get(0), ExpenseType.CREDIT_NOTE, 2000.0, CLAIMANT, INSURANCE_COMPANY);

        payments = event.getPayments();
        assertEquals(payments.size(), 1);
        assertPayment(payments.get(0), 1100.0,SCALEPOINT, INSURANCE_COMPANY);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 1);

    }





}

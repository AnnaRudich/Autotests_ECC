package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.ReopenClaimService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.*;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.CLAIMANT;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.INSURANCE_COMPANY;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.SCALEPOINT;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class SendingCaseSettledEventV3Case1 extends BaseUnifiedPaymentsApiTest {



    @BeforeMethod
    private void setUp(Object[] testArgs){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");


        User user = (User)testArgs[0];
        InsertSettlementItem item1 = (InsertSettlementItem) testArgs[1];
        InsertSettlementItem item2 = (InsertSettlementItem) testArgs[2];
        InsertSettlementItem item3 = (InsertSettlementItem) testArgs[3];
        InsertSettlementItem item4 = (InsertSettlementItem) testArgs[4];

        setPrice(item1, 1000, 50);
        setPrice(item2, 100, 0);
        setPrice(item3, 500, 20);
        setPrice(item4, 500, 20);

        createClaim(user, 250, 50, item1, item2, item3, item4);
    }


    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        close(user, item1, item2, item3, item4, CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        close(user, item1, item2, item3, item4, CLOSE_WITHOUT_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeExternally(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        //GIVEN
        //item1 with price=1000 and depreciation=500    (50%)
        //item2 with price=100  and depreciation=0      (0%)
        //item3 with price=500  and depreciation=100    (20%)
        //item4 with price=500  and depreciation=100    (20%)


        //WHEN
        settlementClaimService.close(claimRequest, CLOSE_EXTERNAL);
        EventClaimSettled event = eventDatabaseApi.getEventClaimSettled(claimRequest);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, 250.0, 0.0, 700.0, 50.0);

        List<Obligation> obligations = event.getObligations();
        assertEquals(obligations.size(), 4);
        assertObligation(obligations, DEPRECIATION, 700.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, DEDUCTIBLE, 250.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, MANUAL_REDUCTION, 50.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, COMPENSATION, 1100.0, INSURANCE_COMPANY, CLAIMANT);

        List<Expense> expenses = event.getExpenses();
        assertEquals(expenses.size(), 1);
        assertExpense(expenses.get(0), CASH_COMPENSATION, 2100.0, INSURANCE_COMPANY, CLAIMANT);

        List<Payment> payments = event.getPayments();
        assertEquals(payments.size(), 1);
        assertPayment(payments.get(0), 1100.0,INSURANCE_COMPANY, CLAIMANT);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);

        //WHEN
        new ReopenClaimService().reopenClaim();

        setPrice(item1, 100, 0);
        claimSettlementItemsService
                .removeLines(item1, item2, item3, item4)
                .addLines(item1);

        settlementClaimService.close(claimRequest, CLOSE_EXTERNAL);
        event = eventDatabaseApi.getEventClaimSettled(claimRequest, 1);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, -150.0, 0.0, -700.0, -50.0);

        obligations = event.getObligations();
        assertEquals(obligations.size(), 4);
        assertObligation(obligations, DEPRECIATION, 700.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, DEDUCTIBLE, 150.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, MANUAL_REDUCTION, 50.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, COMPENSATION, 1100.0, CLAIMANT, INSURANCE_COMPANY);

        expenses = event.getExpenses();
        assertEquals(expenses.size(), 1);
        assertExpense(expenses.get(0), ExpenseType.CREDIT_NOTE, 2000.0, CLAIMANT, INSURANCE_COMPANY);

        payments = event.getPayments();
        assertEquals(payments.size(), 1);
        assertPayment(payments.get(0), 1100.0,CLAIMANT, INSURANCE_COMPANY);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 1);

    }

    private void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        //item1 with price=1000 and depreciation=500    (50%)
        //item2 with price=100  and depreciation=0      (0%)
        //item3 with price=500  and depreciation=100    (20%)
        //item4 with price=500  and depreciation=100    (20%)


        //WHEN
        settlementClaimService.close(claimRequest, closeCaseReason);
        EventClaimSettled event = eventDatabaseApi.getEventClaimSettled(claimRequest);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, 250.0, 0.0, 700.0, 50.0);

        List<Obligation> obligations = event.getObligations();
        assertEquals(obligations.size(), 5);
        assertObligation(obligations, DEPRECIATION, 700.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, DEDUCTIBLE, 250.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, MANUAL_REDUCTION, 50.0, CLAIMANT, CLAIMANT);
        assertObligation(obligations, COMPENSATION, 1100.0, INSURANCE_COMPANY, SCALEPOINT);
        assertObligation(obligations, COMPENSATION, 1100.0, SCALEPOINT, CLAIMANT);

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

        settlementClaimService.close(claimRequest, closeCaseReason);
        event = eventDatabaseApi.getEventClaimSettled(claimRequest, 1);


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, -150.0, 0.0, -700.0, -50.0);

        obligations = event.getObligations();
        assertEquals(obligations.size(), 5);
        assertObligation(obligations, DEPRECIATION, 700.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, DEDUCTIBLE, 150.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, MANUAL_REDUCTION, 50.0, INSURANCE_COMPANY, INSURANCE_COMPANY);
        assertObligation(obligations, COMPENSATION, 1100.0, CLAIMANT, SCALEPOINT);
        assertObligation(obligations, COMPENSATION, 1100.0, SCALEPOINT, INSURANCE_COMPANY);

        expenses = event.getExpenses();
        assertEquals(expenses.size(), 1);
        assertExpense(expenses.get(0), ExpenseType.CREDIT_NOTE, 2000.0, CLAIMANT, INSURANCE_COMPANY);

        payments = event.getPayments();
        assertEquals(payments.size(), 1);
        assertPayment(payments.get(0), 1100.0,SCALEPOINT, INSURANCE_COMPANY);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest, 1);

    }





}

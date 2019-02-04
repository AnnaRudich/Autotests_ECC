package com.scalepoint.automation.tests.api.unifiedpayments;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.ObligationType.COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.CLAIMANT;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.INSURANCE_COMPANY;
import static com.scalepoint.automation.tests.api.unifiedpayments.BaseUnifiedPaymentsApiTest.PartyReference.SCALEPOINT;
import static com.scalepoint.automation.tests.api.unifiedpayments.UnifiedPaymentsAssertUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertTrue;

public class SendingCaseSettledEventV3Case4_1 extends SendingCaseSettledEventV3Case4 {



    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITHOUT_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    private void closeExternally(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        //GIVEN
        //1st item with price 3000 and depreciation  600    (20%)
        //2nd item with price 2000  and depreciation  0      (0%)


        //WHEN
        closeExternally();
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, 1000.0, 0.0, 600.0, 0.0);

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

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);

        //WHEN
        reopenClaim();

        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .addLines(item3);

        closeExternally();
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 200.0, 0.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                    {ExpenseType.CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                    {800.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                    {DEPRECIATION, 200.0, CLAIMANT, CLAIMANT},
                    {COMPENSATION, 800.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }



    private void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        //1st item with price 3000 and depreciation  600    (20%)
        //2nd item with price 2000  and depreciation  0      (0%)


        //WHEN
        close(closeCaseReason);
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        assertTrue(matchesJsonSchemaInClasspath("schema/case_settled.schema.json").matches(event.getJsonString()));

        assertSummary(event, 1000.0, 0.0, 600.0, 0.0);

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

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);

        //WHEN
        reopenClaim();

        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .addLines(item3);

        close(closeCaseReason);
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 200.0, 0.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                    {ExpenseType.CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                    {800.0, INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                    {DEPRECIATION, 200.0, CLAIMANT, CLAIMANT},
                    {COMPENSATION, 800.0, SCALEPOINT, CLAIMANT},
                    {COMPENSATION, 800.0, INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


}

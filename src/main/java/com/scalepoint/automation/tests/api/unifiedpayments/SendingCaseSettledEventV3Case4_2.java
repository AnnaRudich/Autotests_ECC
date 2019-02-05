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

public class SendingCaseSettledEventV3Case4_2 extends SendingCaseSettledEventV3Case4 {



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
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        settleExternallyAndAssert();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        closeExternally();
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
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

        assertThatSecondCloseCaseEventWasCreated();

    }



    private void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
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

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 3000, 55);
        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);

        close(closeCaseReason);
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 1250.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                    {ExpenseType.CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
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

        assertThatSecondCloseCaseEventWasCreated();

    }


}

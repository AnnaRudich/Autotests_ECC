package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;


public class SendingCaseSettledEventV3Case3 extends BaseUnifiedPaymentsApiTest {



    @BeforeMethod
    private void setUp(Object[] testArgs){
        initClaimRequest();

        User user = (User)testArgs[0];
        InsertSettlementItem item1 = (InsertSettlementItem) testArgs[1];
        InsertSettlementItem item2 = (InsertSettlementItem) testArgs[2];
        InsertSettlementItem item3 = (InsertSettlementItem) testArgs[3];

        setPrice(item1, 2000, 25);
        setPrice(item2, 800, 0);
        setPrice(item3, 3500, 20);

        createClaim(user, 1500, 900, item1, item2, item3);
    }


    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMail(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITHOUT_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeExternally(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        //GIVEN
        /*
            1st item with price 2000 and depreciation  500    (25%)
            2nd item with price 800  and depreciation  0      (0%)
            3rd item with price 3500 and depreciation  700    (20%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        closeExternally();
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 900.0, 0.0, 1500.0, 1200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 6300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0,INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1200.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 1500.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 900.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 2700.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();


        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 2000, 75);
        setPrice(item3, 3500, 90);
        claimSettlementItemsService
                .editLines(item1, item3);

        closeExternally();
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -750.0, 0.0, 0.0, 3450.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 0.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {MANUAL_REDUCTION, 750.0, INSURANCE_COMPANY, CLAIMANT},
                        {DEPRECIATION, 3450.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();
    }


    private void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        /*
            1st item with price 2000 and depreciation  500    (25%)
            2nd item with price 800  and depreciation  0      (0%)
            3rd item with price 3500 and depreciation  700    (20%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        close(closeCaseReason);
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 900.0, 0.0, 1500.0, 1200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 6300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0,INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 1200.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 1500.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 900.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 2700.0, INSURANCE_COMPANY, SCALEPOINT},
                        {COMPENSATION, 2700.0, SCALEPOINT, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();


        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 2000, 75);
        setPrice(item3, 3500, 90);
        claimSettlementItemsService
                .editLines(item1, item3);

        close(closeCaseReason);
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -750.0, 0.0, 0.0, 3450.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 0.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {MANUAL_REDUCTION, 750.0, SCALEPOINT, CLAIMANT},
                        {MANUAL_REDUCTION, 750.0, INSURANCE_COMPANY, SCALEPOINT},
                        {DEPRECIATION, 3450.0, CLAIMANT, SCALEPOINT},
                        {DEPRECIATION, 3450.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


}

package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
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


public class SendingCaseSettledEventV3Case1 extends BaseUnifiedPaymentsApiTest {


    @BeforeMethod
    private void setUp(Object[] testArgs) {
        initClaimRequest();

        User user = (User) testArgs[0];
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


    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE1},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMailSendingCaseSettledEventV3Case1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        close(user, item1, item2, item3, item4, CLOSE_WITH_MAIL);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE1},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMailSendingCaseSettledEventV3Case1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        close(user, item1, item2, item3, item4, CLOSE_WITHOUT_MAIL);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V3,
            TestGroups.CASE1},
            dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeExternallySendingCaseSettledEventV3Case1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4) {
        //GIVEN
        /*
            1st item with price 1000 and depreciation  500    (50%)
            2nd item with price 100  and depreciation  0      (0%)
            3rd item with price 500  and depreciation  100    (20%)
            4th item with price 500  and depreciation  100    (20%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        closeExternally();
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 50.0, 0.0, 250.0, 700.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2100.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );


        assertPayments(event.getPayments(), new Object[][]
                {
                        {1100.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 700.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 250.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 50.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 1100.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 100, 0);
        claimSettlementItemsService
                .removeLines(item2, item3, item4)
                .editLines(item1);

        closeExternally();
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -50.0, 0.0, -150.0, -700.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 2000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {1100.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 700.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 150.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 50.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {COMPENSATION, 1100.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


    public void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, InsertSettlementItem item4, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        /*
            1st item with price 1000 and depreciation 500    (50%)
            2nd item with price 100  and depreciation 0      (0%)
            3rd item with price 500  and depreciation 100    (20%)
            4th item with price 500  and depreciation 100    (20%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        close(closeCaseReason);
        EventClaimSettled event = getEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 50.0, 0.0, 250.0, 700.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2100.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {1100.0, INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 700.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 250.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 50.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 1100.0, INSURANCE_COMPANY, SCALEPOINT},
                        {COMPENSATION, 1100.0, SCALEPOINT, CLAIMANT}
                }
        );

        assertThatCloseCaseEventWasCreated();


        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 100, 0);
        claimSettlementItemsService
                .removeLines(item2, item3, item4)
                .editLines(item1);

        close(closeCaseReason);
        event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -50.0, 0.0, -150.0, -700.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 2000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {1100.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 700.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 150.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 50.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {COMPENSATION, 1100.0, CLAIMANT, SCALEPOINT},
                        {COMPENSATION, 1100.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

    }


}

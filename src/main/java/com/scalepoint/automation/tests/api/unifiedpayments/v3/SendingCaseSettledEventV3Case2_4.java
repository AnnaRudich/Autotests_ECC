package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_4 extends SendingCaseSettledEventV3Case2Base {


    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class, groups = {"backend"})
    public void closeWithMailSendingCaseSettledEventV3Case2_4(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class, groups = {"backend"})
    public void closeWithoutMailSendingCaseSettledEventV3Case2_4(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITHOUT_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class, groups = {"backend"})
    public void closeExternallySendingCaseSettledEventV3Case2_4(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        makeFirstExternalSettlementAndAssert();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 4000, 15);
        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);
        setManualReduction(500);
        setSelfRisk(1500);

        closeExternally();
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, 500.0, 200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2000.0, INSURANCE_COMPANY, CLAIMANT}
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
                        {DEDUCTIBLE, 500.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 800.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 4000, 5);
        setPrice(item2, 1000, 0);
        setPrice(item3, 1000, 40);
        claimSettlementItemsService
                .editLines(item1, item2, item3);
        setManualReduction(0);
        setSelfRisk(700);

        closeExternally();
        event = getThirdEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -500.0, 0.0, -800.0, -200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {500.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 800.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 500.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );

        assertThatThirdCloseCaseEventWasCreated();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        claimSettlementItemsService
                .removeLines(item3);
        setManualReduction(600);
        setSelfRisk(1200);

        closeExternally();
        event = getFourthEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 600.0, 0.0, 500.0, -400.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {1700.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 400.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 500.0, CLAIMANT, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 600.0, CLAIMANT, INSURANCE_COMPANY},
                        {COMPENSATION, 600.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertThatFourthCloseCaseEventWasCreated();

    }


    public void close(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        makeFirstSettlementAndAssert(closeCaseReason);

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 4000, 15);
        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .editLines(item1)
                .addLines(item3);
        setManualReduction(500);
        setSelfRisk(1500);

        close(closeCaseReason);
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 500.0, 0.0, 500.0, 200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 2000.0, INSURANCE_COMPANY, CLAIMANT}
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
                        {DEDUCTIBLE, 500.0, CLAIMANT, CLAIMANT},
                        {MANUAL_REDUCTION, 500.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 800.0, INSURANCE_COMPANY, SCALEPOINT},
                        {COMPENSATION, 800.0, SCALEPOINT, CLAIMANT}
                }
        );

        assertThatSecondCloseCaseEventWasCreated();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item1, 4000, 5);
        setPrice(item2, 1000, 0);
        setPrice(item3, 1000, 40);
        claimSettlementItemsService
                .editLines(item1, item2, item3);
        setManualReduction(0);
        setSelfRisk(700);

        close(closeCaseReason);
        event = getThirdEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, -500.0, 0.0, -800.0, -200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {500.0, INSURANCE_COMPANY, SCALEPOINT}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 200.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 800.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 500.0, INSURANCE_COMPANY, SCALEPOINT},
                        {MANUAL_REDUCTION, 500.0, SCALEPOINT, CLAIMANT}
                }
        );

        assertThatThirdCloseCaseEventWasCreated();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        claimSettlementItemsService
                .removeLines(item3);
        setManualReduction(600);
        setSelfRisk(1200);

        close(closeCaseReason);
        event = getFourthEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 600.0, 0.0, 500.0, -400.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {ExpenseType.CREDIT_NOTE, 1000.0, CLAIMANT, INSURANCE_COMPANY}
                }
        );

        assertPayments(event.getPayments(), new Object[][]
                {
                        {1700.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 400.0, INSURANCE_COMPANY, INSURANCE_COMPANY},
                        {DEDUCTIBLE, 500.0, CLAIMANT, SCALEPOINT},
                        {DEDUCTIBLE, 500.0, SCALEPOINT, INSURANCE_COMPANY},
                        {MANUAL_REDUCTION, 600.0, CLAIMANT, SCALEPOINT},
                        {MANUAL_REDUCTION, 600.0, SCALEPOINT, INSURANCE_COMPANY},
                        {COMPENSATION, 600.0, CLAIMANT, SCALEPOINT},
                        {COMPENSATION, 600.0, SCALEPOINT, INSURANCE_COMPANY}
                }
        );

        assertThatFourthCloseCaseEventWasCreated();

    }


}

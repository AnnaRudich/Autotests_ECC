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
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.DEPRECIATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2_1 extends SendingCaseSettledEventV3Case2Base {


    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithMailSendingCaseSettledEventV3Case2_1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITH_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeWithoutMailSendingCaseSettledEventV3Case2_1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        close(user, item1, item2, item3, CLOSE_WITHOUT_MAIL);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeExternallySendingCaseSettledEventV3Case2_1(User user, InsertSettlementItem item1, InsertSettlementItem item2, InsertSettlementItem item3) {
        //GIVEN
        /*
            1st item with price 3000 and depreciation  600    (20%)
            2nd item with price 2000 and depreciation  0      (0%)
        */


        //WHEN----------------------------------------------------------------------------------------------------------
        makeFirstExternalSettlementAndAssert();

        //WHEN----------------------------------------------------------------------------------------------------------
        reopenClaim();

        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .addLines(item3);

        closeExternally();
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
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

        setPrice(item3, 1000, 20);
        claimSettlementItemsService
                .addLines(item3);

        close(closeCaseReason);
        EventClaimSettled event = getSecondEventClaimSettled();


        //THEN
        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 0.0, 200.0);

        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 1000.0, INSURANCE_COMPANY, CLAIMANT}
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

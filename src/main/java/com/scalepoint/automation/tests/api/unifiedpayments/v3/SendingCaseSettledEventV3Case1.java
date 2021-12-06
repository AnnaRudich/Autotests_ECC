package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;


public class SendingCaseSettledEventV3Case1 extends BaseUnifiedPaymentsApiTest {

    private static final String CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER = "closeSendingCaseSettledEventV3Case1DataProvider";
    private static final String CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER = "closeExternallySendingCaseSettledEventV3Case1DataProvider";

    @BeforeMethod(alwaysRun = true)
    private void setUp(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        CreateClaimInput createClaimInput = getObjectByClass(parameters, CreateClaimInput.class).get(0);
        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        createClaim(user, claimRequest, createClaimInput.getSelfRisk(), createClaimInput.getManualReduction(),
                insertSettlementItemList.get(0),
                insertSettlementItemList.get(1),
                insertSettlementItemList.get(2),
                insertSettlementItemList.get(3));
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER)
    public void closeSendingCaseSettledEventV3Case1Test(User user, ClaimRequest claimRequest, InsertSettlementItem item1,
                                                        InsertSettlementItem item2, InsertSettlementItem item3,
                                                        InsertSettlementItem item4, CreateClaimInput createClaimInput,
                                                        SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(user, claimRequest, item1, item2, item3, item4, closeCaseReason);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER)
    public void closeExternallySendingCaseSettledEventV3Case1(User user, ClaimRequest claimRequest,
                                                              InsertSettlementItem item1, InsertSettlementItem item2,
                                                              InsertSettlementItem item3, InsertSettlementItem item4,
                                                              CreateClaimInput createClaimInput) {

        closeExternally(claimRequest);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

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
        assertThatCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 100, 0);

        claimSettlementItemsService
                .removeLines(item2, item3, item4)
                .editLines(item1);

        closeExternally(claimRequest);

        event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }


    public void close(User user, ClaimRequest claimRequest, InsertSettlementItem item1, InsertSettlementItem item2,
                      InsertSettlementItem item3, InsertSettlementItem item4,
                      SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

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
        assertThatCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 100, 0);
        claimSettlementItemsService
                .removeLines(item2, item3, item4)
                .editLines(item1);

        close(claimRequest, closeCaseReason);

        event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);

    }

    @DataProvider(name = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER)
    public static Object[][] closeSendingCaseSettledEventV3Case1DataProvider(Method method) {

        List closeWithMailParameters = setCloseSendingCaseSettledEventV3Case1Parameters(method);
        closeWithMailParameters.add(CLOSE_WITH_MAIL);

        List closeWithoutMailParameters = setCloseSendingCaseSettledEventV3Case1Parameters(method);
        closeWithoutMailParameters.add(CLOSE_WITHOUT_MAIL);

        return new Object[][]{

                closeWithMailParameters.toArray(),
                closeWithoutMailParameters.toArray()
        };
    }

    @DataProvider(name = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE1_DATA_PROVIDER)
    public static Object[][] closeExternallySendingCaseSettledEventV3Case1DataProvider(Method method) {

        return new Object[][]{

                setCloseSendingCaseSettledEventV3Case1Parameters(method).toArray()
        };
    }

    private static List setCloseSendingCaseSettledEventV3Case1Parameters(Method method){

        List parameters = TestDataActions.getTestDataParameters(method);

        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        initClaimRequest(claimRequest);

        setPrice(insertSettlementItemList.get(0), 1000, 50);
        setPrice(insertSettlementItemList.get(1), 100, 0);
        setPrice(insertSettlementItemList.get(2), 500, 20);
        setPrice(insertSettlementItemList.get(3), 500, 20);

        parameters.add(CreateClaimInput.builder().selfRisk(250).manualReduction(50).build());

        return parameters;
    }
}

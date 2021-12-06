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


public class SendingCaseSettledEventV3Case3 extends BaseUnifiedPaymentsApiTest {


    private static final String CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER = "closeSendingCaseSettledEventV3Case3DataProvider";
    private static final String CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER = "closeExternallySendingCaseSettledEventV3Case3DataProvider";

    @BeforeMethod(alwaysRun = true)
    private void setUp(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        SendingCaseSettledEventV3Case1.CreateClaimInput createClaimInput = getObjectByClass(parameters, SendingCaseSettledEventV3Case1.CreateClaimInput.class).get(0);
        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        createClaim(user, claimRequest, createClaimInput.getSelfRisk(), createClaimInput.getManualReduction(),
                insertSettlementItemList.get(0),
                insertSettlementItemList.get(1),
                insertSettlementItemList.get(2));
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER)
    public void closeWithMailSendingCaseSettledEventV3Case3(User user, ClaimRequest claimRequest,
                                                            InsertSettlementItem item1, InsertSettlementItem item2,
                                                            InsertSettlementItem item3, CreateClaimInput createClaimInput,
                                                            SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(user, claimRequest, item1, item2, item3, closeCaseReason);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V3,
            TestGroups.BACKEND},
            dataProvider =CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER)
    public void closeExternallySendingCaseSettledEventV3Case3(User user, ClaimRequest claimRequest,
                                                              InsertSettlementItem item1, InsertSettlementItem item2,
                                                              InsertSettlementItem item3, CreateClaimInput createClaimInput) {

        closeExternally(claimRequest);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 900.0, 0.0, 1500.0, 1200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 6300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0, INSURANCE_COMPANY, CLAIMANT}
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
        assertThatCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 2000, 75);
        setPrice(item3, 3500, 90);

        claimSettlementItemsService
                .editLines(item1, item3);

        closeExternally(claimRequest);

        event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }

    public void close(User user, ClaimRequest claimRequest, InsertSettlementItem item1, InsertSettlementItem item2,
                      InsertSettlementItem item3, SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 900.0, 0.0, 1500.0, 1200.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 6300.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {2700.0, INSURANCE_COMPANY, SCALEPOINT}
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
        assertThatCloseCaseEventWasCreated(claimRequest);

        reopenClaim();

        setPrice(item1, 2000, 75);
        setPrice(item3, 3500, 90);

        claimSettlementItemsService
                .editLines(item1, item3);

        close(claimRequest, closeCaseReason);

        event = getSecondEventClaimSettled(claimRequest);

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
        assertThatSecondCloseCaseEventWasCreated(claimRequest);
    }

    @DataProvider(name = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER)
    public static Object[][] closeSendingCaseSettledEventV3Case3DataProvider(Method method) {

        List closeWithMailParameters = setCloseSendingCaseSettledEventV3Case3Parameters(method);
        closeWithMailParameters.add(CLOSE_WITH_MAIL);

        List closeWithoutMailParameters = setCloseSendingCaseSettledEventV3Case3Parameters(method);
        closeWithoutMailParameters.add(CLOSE_WITHOUT_MAIL);

        return new Object[][]{

                closeWithMailParameters.toArray(),
                closeWithoutMailParameters.toArray()
        };
    }

    @DataProvider(name = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE3_DATA_PROVIDER)
    public static Object[][] closeExternallySendingCaseSettledEventV3Case3DataProvider(Method method) {

        return new Object[][]{

                setCloseSendingCaseSettledEventV3Case3Parameters(method).toArray()
        };
    }

    private static List setCloseSendingCaseSettledEventV3Case3Parameters(Method method){

        List parameters = TestDataActions.getTestDataParameters(method);

        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        initClaimRequest(claimRequest);

        setPrice(insertSettlementItemList.get(0), 2000, 25);
        setPrice(insertSettlementItemList.get(1), 800, 0);
        setPrice(insertSettlementItemList.get(2), 3500, 20);

        parameters.add(CreateClaimInput.builder().selfRisk(1500).manualReduction(900).build());

        return parameters;
    }
}

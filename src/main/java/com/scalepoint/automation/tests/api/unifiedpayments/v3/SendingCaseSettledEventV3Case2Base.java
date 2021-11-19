package com.scalepoint.automation.tests.api.unifiedpayments.v3;

import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITHOUT_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ExpenseType.CASH_COMPENSATION;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.ObligationType.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.BaseUnifiedPaymentsApiTest.PartyReference.*;
import static com.scalepoint.automation.tests.api.unifiedpayments.v3.UnifiedPaymentsAssertUtils.*;

public class SendingCaseSettledEventV3Case2Base extends BaseUnifiedPaymentsApiTest {

    protected static final String CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER = "closeSendingCaseSettledEventV3Case2DataProvider";
    protected static final String CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER = "closeExternallySendingCaseSettledEventV3Case2DataProvider";

    @BeforeMethod(alwaysRun = true)
    void setUp(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        SendingCaseSettledEventV3Case1.CreateClaimInput createClaimInput = getObjectByClass(parameters, SendingCaseSettledEventV3Case1.CreateClaimInput.class).get(0);
        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        createClaim(user, claimRequest, createClaimInput.getSelfRisk(), createClaimInput.getManualReduction(),
                insertSettlementItemList.get(0),
                insertSettlementItemList.get(1));
    }

    void makeFirstExternalSettlementAndAssert(ClaimRequest claimRequest) {

        closeExternally(claimRequest);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 1000.0, 600.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 5000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {3400.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertObligations(event.getObligations(), new Object[][]
                {
                        {DEPRECIATION, 600.0, CLAIMANT, CLAIMANT},
                        {DEDUCTIBLE, 1000.0, CLAIMANT, CLAIMANT},
                        {COMPENSATION, 3400.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertThatCloseCaseEventWasCreated(claimRequest);
    }


    void makeFirstSettlementAndAssert(ClaimRequest claimRequest, SettlementClaimService.CloseCaseReason closeCaseReason) {

        close(claimRequest, closeCaseReason);

        EventClaimSettled event = getEventClaimSettled(claimRequest);

        validateJsonSchema(event);

        assertSummary(event, 0.0, 0.0, 1000.0, 600.0);
        assertExpenses(event.getExpenses(), new Object[][]
                {
                        {CASH_COMPENSATION, 5000.0, INSURANCE_COMPANY, CLAIMANT}
                }
        );
        assertPayments(event.getPayments(), new Object[][]
                {
                        {3400.0, INSURANCE_COMPANY, SCALEPOINT}
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
        assertThatCloseCaseEventWasCreated(claimRequest);
    }

    @DataProvider(name = CLOSE_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public static Object[][] closeSendingCaseSettledEventV3Case2DataProvider(Method method) {

        List closeWithMailParameters = setCloseSendingCaseSettledEventV3Case2Parameters(method);
        closeWithMailParameters.add(CLOSE_WITH_MAIL);

        List closeWithoutMailParameters = setCloseSendingCaseSettledEventV3Case2Parameters(method);
        closeWithoutMailParameters.add(CLOSE_WITHOUT_MAIL);

        return new Object[][]{

                closeWithMailParameters.toArray(),
                closeWithoutMailParameters.toArray()
        };
    }

    @DataProvider(name = CLOSE_EXTERNALLY_SENDING_CASE_SETTLED_EVENT_V3_CASE2_DATA_PROVIDER)
    public static Object[][] closeExternallySendingCaseSettledEventV3Case2DataProvider(Method method) {

        return new Object[][]{

                setCloseSendingCaseSettledEventV3Case2Parameters(method).toArray()
        };
    }

    private static List setCloseSendingCaseSettledEventV3Case2Parameters(Method method){

        List parameters = TestDataActions.getTestDataParameters(method);

        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        List<InsertSettlementItem> insertSettlementItemList = getObjectByClass(parameters, InsertSettlementItem.class);

        initClaimRequest(claimRequest);

        setPrice(insertSettlementItemList.get(0), 3000, 20);
        setPrice(insertSettlementItemList.get(1), 2000, 0);

        parameters.add(CreateClaimInput.builder().selfRisk(1000).manualReduction(0).build());

        return parameters;
    }
}

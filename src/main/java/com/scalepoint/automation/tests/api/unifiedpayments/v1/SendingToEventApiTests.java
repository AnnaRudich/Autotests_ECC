package com.scalepoint.automation.tests.api.unifiedpayments.v1;

import com.scalepoint.automation.services.restService.*;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseUITest;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.services.restService.common.BaseService.loginAndOpenClaimWithItems;
import static com.scalepoint.automation.services.restService.common.BaseService.loginUser;
import static org.assertj.core.api.Assertions.assertThat;


public class SendingToEventApiTests extends BaseApiTest {

    private static final String SEND_TO_EVENT_API_DATA_PROVIDER = "sendToEventApiDataProvider";
    private static final String CLOSE_CLAIM_WITH_NEGATIVE_SETTLEMENT_AMOUNT_SHOULD_BE_SENT_TO_EVENT_API_DATA_PROVIDER = "closeClaimWithNegativeSettlementAmountShouldBeSendToEventApiDataProvider";

    private static final String LB_TENANT = "lb";

    private SettlementClaimService settlementClaimService;
    private EccSettlement eccSettlementSummaryService;

    @BeforeMethod(alwaysRun = true)
    private void prepareClaimRequest(Object[] objects) {

        List parameters = Arrays.asList(objects);

        ClaimRequest claimRequest = getObjectByClass(parameters, ClaimRequest.class).get(0);
        claimRequest.setTenant(LB_TENANT);
        claimRequest.setCompany(LB_TENANT);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = SEND_TO_EVENT_API_DATA_PROVIDER)
    public void sendToEventApiTest(User user, ClaimRequest claimRequest, InsertSettlementItem item,
                                   SettlementClaimService.CloseCaseReason reason) {

        createClaimWithItem(user, item, claimRequest)
                .close(claimRequest, reason);

        assertEvent(claimRequest);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void closeClaimWithSettlementWithAmountEqual0ShouldBeSendToEventApiTest(User user, InsertSettlementItem item,
                                                                                   ClaimRequest claimRequest) {

        createClaimWithItem(user, item, claimRequest);

        new OwnRiskService()
                .setSelfRiskForClaim(item
                        .getSettlementItem()
                        .getValuations()
                        .getValuation()[0]
                        .getPrice()[0]
                        .getAmount());

        settlementClaimService.close(claimRequest, CLOSE_WITH_MAIL);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = CLOSE_CLAIM_WITH_NEGATIVE_SETTLEMENT_AMOUNT_SHOULD_BE_SENT_TO_EVENT_API_DATA_PROVIDER)
    public void closeClaimWithNegativeSettlementAmountShouldBeSendToEventApiTest(User user, InsertSettlementItem item,
                                                                                 ClaimRequest claimRequest,
                                                                                 int selfRiskForClaim) {

        createClaimWithItem(user, item, claimRequest)
                .close(claimRequest, CLOSE_WITH_MAIL);

        EventClaimSettled eventClaimSettledBeforeReopen = eventDatabaseApi.getEventClaimSettled(claimRequest);

        assertThat(eventClaimSettledBeforeReopen.getTotal())
                .isEqualTo(eccSettlementSummaryService.getSubtotalCashPayoutValue());

        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest, 1);

        new ReopenClaimService()
                .reopenClaim();

        new OwnRiskService()
                .setSelfRiskForClaim(String.valueOf(
                        selfRiskForClaim + Integer.valueOf(item
                                .getSettlementItem()
                                .getValuations()
                                .getValuation()[0]
                                .getPrice()[0]
                                .getAmount())));

        settlementClaimService
                .close(claimRequest, CLOSE_WITH_MAIL);

        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest, 2);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void cancelNotSettledClaimShouldSendClaimUpdatedCaseClosedEventTest(User user, InsertSettlementItem item,
                                                                               ClaimRequest claimRequest) {

        createClaimWithItem(user, item, claimRequest)
                .cancel(claimRequest);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void cancelSettledClaimShouldSendClaimUpdatedCaseClosedAndCaseSettledEvensTest(User user,
                                                                                          InsertSettlementItem item,
                                                                                          ClaimRequest claimRequest) {

        createClaimWithItem(user, item, claimRequest)
                .close(claimRequest, CLOSE_WITH_MAIL);

        new ReopenClaimService().reopenClaim();

        settlementClaimService.cancel(claimRequest);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
        eventDatabaseApi.assertThatCaseSettledEventWasCreated(claimRequest);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void cancelClaimShouldBeNotSendToEventApiTest(User user, EccIntegration eccIntegration,
                                                         ClaimRequest claimRequest) {

        loginUser(user);

        EccIntegrationService eccIntegrationService = new EccIntegrationService();
        eccIntegrationService.createAndOpenClaim(eccIntegration);
        eccIntegrationService.openCaseAndRedirect();

        new SettlementClaimService().cancel(eccIntegration);

        eventDatabaseApi.assertThatCloseCaseEventWasNotCreated(claimRequest);
    }

    @Test(groups = {TestGroups.UNIFIED_PAYMENTS_V1,
            TestGroups.BACKEND,
            TestGroups.SENDING_TO_EVENT_API},
            dataProvider = BaseUITest.TEST_DATA_PROVIDER, dataProviderClass = BaseUITest.class)
    public void cancelClaimPreviouslySettledShouldBeSendToEventApiTest(User user, InsertSettlementItem item,
                                                                       ClaimRequest claimRequest) {

        createClaimWithItem(user, item, claimRequest)
                .close(claimRequest, CLOSE_WITH_MAIL);

        EventClaimSettled eventClaimSettledAfterClose = assertEvent(claimRequest);

        settlementClaimService
                .cancel(claimRequest);

        EventClaimSettled eventClaimSettledAfterCanceled = eventDatabaseApi
                .getEventClaimSettled(claimRequest, 1);

        assertThat(eventClaimSettledAfterCanceled.getPayments().get(0).getPayeeParty())
                .isEqualToComparingFieldByField(eventClaimSettledAfterClose.getPayments().get(0).getPayerParty());
        assertThat(eventClaimSettledAfterClose.getPayments().get(0).getPayeeParty())
                .isEqualToComparingFieldByField(eventClaimSettledAfterCanceled.getPayments().get(0).getPayerParty());
        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest, 2);
    }

    private SettlementClaimService createClaimWithItem(User user, InsertSettlementItem item, ClaimRequest claimRequest) {

        settlementClaimService = loginAndOpenClaimWithItems(user, claimRequest, item).closeCase();

        eccSettlementSummaryService = new EccSettlement().getSummaryTotals();

        return settlementClaimService;
    }

    private EventClaimSettled assertEvent(ClaimRequest claimRequest) {

        EventClaimSettled eventClaimSettled = eventDatabaseApi.getEventClaimSettled(claimRequest);

        assertThat(eventClaimSettled.getTotal()).isEqualTo(eccSettlementSummaryService.getSubtotalCashPayoutValue());
        assertThat(eventClaimSettled.getTotal()).isEqualTo(getSettlementData(claimRequest).getSubTotal());
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);

        return eventClaimSettled;
    }

    @DataProvider(name = SEND_TO_EVENT_API_DATA_PROVIDER)
    public static Object[][] sendToEventApiDataProvider(Method method) {

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, CLOSE_EXTERNAL).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, CLOSE_WITH_MAIL).toArray(),
                //TODO: fix
//                TestDataActions.getTestDataWithExternalParameters(method, REPLACEMENT).toArray()
        };
    }

    @DataProvider(name = CLOSE_CLAIM_WITH_NEGATIVE_SETTLEMENT_AMOUNT_SHOULD_BE_SENT_TO_EVENT_API_DATA_PROVIDER)
    public static Object[][] closeClaimWithNegativeSettlementAmountShouldBeSendToEventApiDataProvider(Method method) {

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, 1000).toArray()
        };
    }
}

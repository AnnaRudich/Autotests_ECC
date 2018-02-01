package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.restService.EccSettlementSummaryService;
import com.scalepoint.automation.services.restService.OwnRiskService;
import com.scalepoint.automation.services.restService.ReopenClaimService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.settled.EventClaimSettled;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.restService.Common.BaseService.loginAndOpenClaimWithItem;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.REPLACEMENT;
import static org.assertj.core.api.Assertions.assertThat;


public class SendingToEventApiTests extends BaseApiTest {

    private ClaimRequest claimRequest;
    private SettlementClaimService settlementClaimService;
    private EccSettlementSummaryService eccSettlementSummaryService;

    @BeforeMethod
    private void prepareClaimRequest(){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closedExternalClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .close(claimRequest, CLOSE_EXTERNAL);

        assertEvent();
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closedWithMailClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .close(claimRequest, CLOSE_WITH_MAIL);

        assertEvent();
    }

    //TODO: fix
    @Test(enabled = false, dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void replacedClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .close(claimRequest, REPLACEMENT);

        assertEvent();
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeClaimWithSettlementWithAmountEqual0ShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item);

        new OwnRiskService()
                .setSelfRiskForClaim(item.getSettlementItem().getValuations().getValuation()[0].getPrice()[0].getAmount());
        settlementClaimService.close(claimRequest, CLOSE_WITH_MAIL);

        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closeClaimWithNegativeSettlementAmountShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .close(claimRequest, CLOSE_WITH_MAIL);

        EventClaimSettled eventClaimSettledBeforeReopen = eventDatabaseApi.getEventClaimSettled(claimRequest);

        assertThat(eventClaimSettledBeforeReopen.getTotal()).isEqualTo(eccSettlementSummaryService.getSubtotalCashPayoutValue());
        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest,1);

        new ReopenClaimService()
                .reopenClaim();
        new OwnRiskService()
                .setSelfRiskForClaim(String.valueOf(1000 + Integer.valueOf(item.getSettlementItem().getValuations().getValuation()[0].getPrice()[0].getAmount())));
        settlementClaimService
                .close(claimRequest, CLOSE_WITH_MAIL);

        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest,3);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void cancelClaimShouldBeNotSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .cancel(claimRequest);

        eventDatabaseApi.assertThatCloseCaseEventWasNotCreated(claimRequest);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void cancelClaimPreviouslySettledShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        createClaimWithItem(user, item)
                .close(claimRequest, CLOSE_WITH_MAIL);
        EventClaimSettled eventClaimSettledAfterClose = assertEvent();

        settlementClaimService
                .cancel(claimRequest);

        EventClaimSettled eventClaimSettledAfterCanceled = eventDatabaseApi.getEventClaimSettled(claimRequest);

        assertThat(eventClaimSettledAfterCanceled.getPayments().get(0).getPayeeParty()).isEqualToComparingFieldByField(eventClaimSettledAfterClose.getPayments().get(0).getPayerParty());
        assertThat(eventClaimSettledAfterClose.getPayments().get(0).getPayeeParty()).isEqualToComparingFieldByField(eventClaimSettledAfterCanceled.getPayments().get(0).getPayerParty());
        eventDatabaseApi.assertNumberOfCloseCaseEventsThatWasCreatedForClaim(claimRequest,2);
    }

    private SettlementClaimService createClaimWithItem(User user, InsertSettlementItem item){
        settlementClaimService =
                loginAndOpenClaimWithItem(user, claimRequest, item).closeCase();
        eccSettlementSummaryService = new EccSettlementSummaryService()
                .getSummaryTotals();
        return settlementClaimService;
    }

    private EventClaimSettled assertEvent() {
        EventClaimSettled eventClaimSettled = eventDatabaseApi.getEventClaimSettled(claimRequest);

        assertThat(eventClaimSettled.getTotal()).isEqualTo(eccSettlementSummaryService.getSubtotalCashPayoutValue());
        assertThat(eventClaimSettled.getTotal()).isEqualTo(getSettlementData(claimRequest).getSubTotal());
        eventDatabaseApi.assertThatCloseCaseEventWasCreated(claimRequest);
        return eventClaimSettled;
    }

}

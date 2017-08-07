package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
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

    @BeforeMethod
    private void prepareClaimRequest(){
        claimRequest = TestData.getClaimRequest();
        claimRequest.setTenant("topdanmark");
        claimRequest.setCompany("topdanmark");
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closedExternalClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, CLOSE_EXTERNAL);

        assertThatEventWasCreated(claimRequest);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void closedWithMailClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, CLOSE_WITH_MAIL);

        assertThatEventWasCreated(claimRequest);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void replacedClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, REPLACEMENT);

        assertThatEventWasCreated(claimRequest);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void cancelClaimShouldBeSendToEventApi(User user, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .cancel(claimRequest);

        assertThatEventWasNotCreated(claimRequest);
    }

    private void assertThatEventWasCreated(ClaimRequest claimRequest) {
        assertThat(eventApiDatabaseApi.getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().anyMatch(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())))
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was created in event-api")
                .isTrue();
    }

    private void assertThatEventWasNotCreated(ClaimRequest claimRequest) {
        assertThat(eventApiDatabaseApi.getEventsForClaimUpdate(claimRequest.getCompany())
                .stream().anyMatch(event -> event.getCase().getNumber().equals(claimRequest.getCaseNumber())))
                .as("Check if event with case number: " + claimRequest.getCaseNumber() + " was not created in event-api")
                .isFalse();
    }
}

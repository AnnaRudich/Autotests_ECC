package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.CaseClaimLineChanged;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.CaseFraudStatus;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.ClaimLineChanged;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.restassured.RestAssured.given;

public class EventApiService{

    protected Logger log = LogManager.getLogger(EventApiService.class);

    private Token token;
    private final static String EVENT_API_URL = Configuration.getEventApiUrl();

    public EventApiService() {

        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.EVENTS_INTERNAL).getToken();
    }

    public void scheduleSubscription(String id) {

        given().baseUri(EVENT_API_URL).basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .queryParam("subscriptionId", id)
                .when()
                .post("/v1/management/subscriptions/schedule")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public void sendFraudStatus(ClaimLineChanged claimLineChanged, String status){

        Token token = new OauthTestAccountsApi()
                .sendRequest(OauthTestAccountsApi.Scope.EVENTS, "topdanmark_dk_integration", "bVHJV2qLzkzVOlFrAg72hbiwLOdUWxgIKvyvrlyacQE")
                .getToken();

        CaseClaimLineChanged caseClaimLineChanged = claimLineChanged.getCaseClaimLineChanged();
        CaseFraudStatus caseFraudStatus = new CaseFraudStatus();
        caseFraudStatus.setCaseType(caseClaimLineChanged.getCaseType());
        caseFraudStatus.setNumber(caseClaimLineChanged.getNumber());
        caseFraudStatus.setToken(caseClaimLineChanged.getToken());
        caseFraudStatus.setUuid(caseClaimLineChanged.getToken().split("\\.")[1]);

        com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.FraudStatus fraudStatus= new com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.FraudStatus();
        fraudStatus.setEventId(claimLineChanged.getEventId());
        fraudStatus.setEventType("fraud_status");
        fraudStatus.setPayloadVersion("1.0.0");
        fraudStatus.setStatus(status);
        fraudStatus.setCaseFraudStatus(caseFraudStatus);
        fraudStatus.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toString());
        given().baseUri(EVENT_API_URL).basePath("/api/events/")
                .header(token.getAuthorizationHeader())
                .header("Event-Type", "fraud_status")
                .contentType(ContentType.JSON)
                .pathParam("country", "dk")
                .pathParam("tenant", "topdanmark")
                .body(fraudStatus)
                .when()
                .post("/v1/{country}/external/{tenant}/send")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    public enum FraudStatus {

        NOT_FRAUDULENT(2),
        FRAUDULENT(1);

        private int status;

        FraudStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}

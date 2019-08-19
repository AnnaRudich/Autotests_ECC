package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.FraudStatus;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class EventApiService{

    protected Logger log = LogManager.getLogger(EventApiService.class);

    Token token;

    public EventApiService() {
        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.EVENTS_INTERNAL).getToken();
    }

    public void scheduleSubscription(String id) {
        log.info(Configuration.getEnvironmentUrl());
        given().baseUri("http://ecc-qa05.spcph.local:86").basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .queryParam("subscriptionId", id)
                .when()
                .post("/v1/management/subscriptions/schedule")
                .then()
                .log()
                .all();
    }

    public void sendFraudStatus(Case caseChanged, String status){
        Token token = new OauthTestAccountsApi()
                .sendRequest(OauthTestAccountsApi.Scope.EVENTS, "topdanmark_dk_integration", "fT8nw3fMVWryIFTmjUqcWgSmb9wki4YNRcoBAG53uZQ")
                .getToken();
        com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.Case fraudStatusCase = new com.scalepoint.automation.utils.data.entity.eventsApiEntity.fraudStatus.Case();
        fraudStatusCase.setCaseType(caseChanged.getCaseType());
        fraudStatusCase.setNumber(caseChanged.getCaseNumber());
        fraudStatusCase.setToken(caseChanged.getToken());
        fraudStatusCase.setUuid(UUID.randomUUID().toString());

        FraudStatus fraudStatus= new FraudStatus();
        fraudStatus.setEventType("fraud_status");
        fraudStatus.setPayloadVersion("1.0.0");
        fraudStatus.setStatus(status);
        fraudStatus.setCaseChanged(fraudStatusCase);
        fraudStatus.setUuid(String.valueOf(UUID.randomUUID()));
        fraudStatus.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toString());
        log.info(Configuration.getEnvironmentUrl());
        given().baseUri("http://ecc-qa05.spcph.local:86").basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .header("Event-Type", "fraud_status")
                .contentType(ContentType.JSON)
                .pathParam("country", "dk")
                .pathParam("tenant", "topdanmark")
                .body(fraudStatus)
                .when()
                .post("/v1/{country}/external/{tenant}/send")
                .then()
                .log()
                .all();
    }
    public void subscribeChangeLineChanged(){
        log.info(Configuration.getEnvironmentUrl());
        given().baseUri("http://ecc-qa05.spcph.local:86").basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"authorizationInfo\": {\n" +
                        "    \"clientId\": \"37e33d49-4434-4b94-9d8e-5e020acadea3\",\n" +
                        "    \"grantType\": \"client_credentials\",\n" +
                        "    \"resource\": \"https://ecc-tools.spcph.local/mock/test\",\n" +
                        "    \"scopes\": null,\n" +
                        "    \"secretKey\": \"E4K15xEpFIcrWDE+QhCci7qyIPihI3jNs3Pg4R9LQow=\",\n" +
                        "    \"tokenEndPoint\": \"https://ecc-tools.spcph.local/mock/token\"\n" +
                        "  },\n" +
                        "  \"contactEmail\": \"bna@scalepoint.com\",\n" +
                        "  \"eventType\": \"claimline_changed\",\n" +
                        "  \"id\": 0,\n" +
                        "  \"poisonMessagesEnabled\": true,\n" +
                        "  \"queue\": \"dk_outbound_queue_topdanmark\",\n" +
                        "  \"routes\": [\n" +
                        "    {\n" +
                        "      \"eventType\": \"claimline_changed\",\n" +
                        "      \"url\": \"https://ecc-tools.spcph.local/mock/fraudAlert\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"tenant\": \"topdanmark\"\n" +
                        "}")
                .when()
                .post("/v1/subscriptions/admin/create")
                .then()
                .log()
                .all();
    }
    public void subscribeFraudStatus(){
        log.info(Configuration.getEnvironmentUrl());
        given().baseUri("http://ecc-qa05.spcph.local:86").basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .body("    {\n" +
                        "  \"authorizationInfo\": {\n" +
                        "    \"certificateData\": {\n" +
                        "      \"certificateFolderPath\": \"C:\\\\ECC\\\\certificates\",\n" +
                        "      \"keyStoreFileName\": \"qa-ecc_dk-auth.jks\",\n" +
                        "      \"keyStorePassword\": \"changeit\"\n" +
                        "    },\n" +
                        "    \"clientId\": \"ecc_dk\",\n" +
                        "    \"grantType\": \"client_credentials\",\n" +
                        "    \"resource\": \"https://test-accounts.scalepoint.com\",\n" +
                        "    \"scopes\": [\n" +
                        "      \"events-internal\"\n" +
                        "    ],\n" +
//                        "    \"secretKey\": null,\n" +
                        "    \"tokenEndPoint\": \"https://test-accounts.scalepoint.com/connect/token\"\n" +
                        "  },\n" +
                        "  \"contactEmail\": \"bna@scalepoint.com\",\n" +
                        "  \"eventType\": \"fraud_status\",\n" +
                        "  \"id\": 0,\n" +
                        "  \"poisonMessagesEnabled\": true,\n" +
                        "  \"queue\": \"dk_inbound_queue\",\n" +
                        "  \"routes\": [\n" +
                        "    {\n" +
                        "      \"eventType\": \"fraud_status\",\n" +
                        "      \"url\": \"https://qa05.scalepoint.com/webapp/ScalePoint/dk/fraud/topdanmark/status\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"tenant\": \"topdanmark\"\n" +
                        "}")
                .when()
                .post("/v1/subscriptions/admin/create")
                .then()
                .log()
                .all();
    }
}

package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class EventApiService{

    protected Logger log = LogManager.getLogger(EventApiService.class);

    Token token;

    public EventApiService() {
        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.EVENTS_INTERNAL).getToken();;
    }

    public void scheduleSubscription(String id) {
        log.info(Configuration.getEnvironmentUrl());
        given().baseUri("http://ecc-qa05.spcph.local:86").basePath("/api/events/").log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .queryParam("subscriptionId", id)
                .when()
                .post("/v1/management/subscriptions/schedule");
    }
    public void subscribe(){
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
}

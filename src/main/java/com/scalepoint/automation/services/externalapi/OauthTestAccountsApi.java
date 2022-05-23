package com.scalepoint.automation.services.externalapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.utils.data.response.Token;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static io.restassured.RestAssured.given;

public class OauthTestAccountsApi {

    private static Logger logger = LogManager.getLogger(OauthTestAccountsApi.class);

    private Token token;

    public OauthTestAccountsApi sendRequest() {

        return sendRequest(Scope.CASE_INTEGRATION);
    }

    public OauthTestAccountsApi sendRequest(Scope scope) {

        String body = given().log().all().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "test_integration_all_tenants")
                .formParam("client_secret", "GXUF5XZ1f0TYzIRzJKP483LuxqtKxc9tVDf-k17iPaU")
                .formParam("scope", scope.getScope())
                .formParam("tenantId")
                .when().log().all()
                .post()
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asString();

        token = readToken(body);

        return this;
    }

    public OauthTestAccountsApi sendRequest(Scope scope, String clientId, String clientSecret) {

        String body = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("scope", scope.getScope())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body()
                .asString();

        token = readToken(body);

        return this;
    }

    public Token getToken() {

        return this.token;
    }

    public OauthTestAccountsApi setToken(Token token) {

        this.token = token;
        return this;
    }

    private Token readToken(String body){

        Token token;
        try {

            token = new ObjectMapper().readValue(body, Token.class);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        return Optional.of(token).orElseThrow(() -> new NullPointerException(String.format("Token is null for value: %s", body)));
    }

    public enum Scope {

        CASE_INTEGRATION("case_integration"),
        AUDIT_REPORT_CREATE("audit-report:create"),
        EVENTS_INTERNAL("events-internal"),
        EVENTS("events"),
        PLATFORM_CASE_READ("platform-case:read"),
        FILES_READ("files:read"),
        SHOP("shop:ecc");

        private String scope;

        Scope(String scope) {

            this.scope = scope;
        }

        public String getScope() {

            return scope;
        }
    }
}

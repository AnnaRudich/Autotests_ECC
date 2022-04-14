package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.response.Token;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;

public class OauthTestAccountsApi {

    private static Logger logger = LogManager.getLogger(OauthTestAccountsApi.class);

    private Token token;

    public OauthTestAccountsApi sendRequest() {

        return sendRequest(Scope.CASE_INTEGRATION);
    }

    public OauthTestAccountsApi sendRequest(Scope scope) {

        try {

            this.token = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token")
                    .formParam("grant_type", "client_credentials")
                    .formParam("client_id", "test_integration_all_tenants")
                    .formParam("client_secret", "GXUF5XZ1f0TYzIRzJKP483LuxqtKxc9tVDf-k17iPaU")
                    .formParam("scope", scope.getScope())
                    .formParam("tenantId")
                    .when()
                    .post()
                    .then().log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .extract().as(Token.class);
        }catch (Exception e){


            logger.warn("Request failed, randomDelay {} : {}", RandomUtils.randomDelay(1,5), e);
        }

        return this;
    }

    public OauthTestAccountsApi sendRequest(Scope scope, String clientId, String clientSecret) {

        this.token = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("scope", scope.getScope())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Token.class);
        return this;
    }

    public Token getToken() {

        return this.token;
    }

    public OauthTestAccountsApi setToken(Token token) {

        this.token = token;
        return this;
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

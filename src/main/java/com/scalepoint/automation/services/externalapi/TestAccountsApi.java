package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.response.Token;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class TestAccountsApi {

    private Token token;

    public TestAccountsApi sendRequest(){
        return sendRequest(Scope.CASE_INTEGRATION);
    }

    public TestAccountsApi sendRequest(Scope scope){
        this.token = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token").log().all()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "test_integration_all_tenants")
                .formParam("client_secret", "-N64TJmEy5konAWGy7fSo7CbZ6sDdUJhHrXBIbJlE-Y")
                .formParam("scope", scope.getScope())
                .when()
                .post()
                .then().log().all().statusCode(HttpStatus.SC_OK).extract().as(Token.class);
        return this;
    }

    public Token getToken(){
        return this.token;
    }

    public TestAccountsApi setToken(Token token){
        this.token = token;
        return this;
    }

    public enum Scope {
        CASE_INTEGRATION("case_integration"),
        AUDIT_REPORT_CREATE("audit-report:create"),
        EVENTS_INTERNAL("events-internal"),
        PLATFORM_CASE_READ("platform-case:read");

        private String scope;

        Scope(String scope){
            this.scope = scope;
        }

        public String getScope() {
            return scope;
        }
    }

}

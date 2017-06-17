package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.response.Token;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class TestAccountsApi {

    private Token token;

    public TestAccountsApi sendRequest(){
        this.token = given().baseUri("https://test-accounts.scalepoint.com").basePath("/connect/token").log().all()
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", "test_integration_all_tenants")
                .formParam("client_secret", "-N64TJmEy5konAWGy7fSo7CbZ6sDdUJhHrXBIbJlE-Y")
                .formParam("scope", "case_integration")
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

}

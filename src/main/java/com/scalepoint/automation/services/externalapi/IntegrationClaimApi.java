package com.scalepoint.automation.services.externalapi;

import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class IntegrationClaimApi {

    private Token token;
    private ValidatableResponse response;

    public IntegrationClaimApi(Token token){
        this.token = token;
    }

    public IntegrationClaimApi sendRequest() {
        sendRequest(TestData.getClaimRequest());
        return this;
    }

    public IntegrationClaimApi sendRequest(ClaimRequest claimRequest){
        this.response = given().baseUri(getEccUrl()).basePath("/Integration/UnifiedIntegration").log().all()
                .body(claimRequest)
                .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                .when()
                .post()
                .then().log().all().statusCode(HttpStatus.SC_OK);
        return this;
    }

    public ValidatableResponse getResponse(){
        return this.response;
    }

    public String getClaimTokenString(){
        return this.response.extract().jsonPath().get("token");
    }

}

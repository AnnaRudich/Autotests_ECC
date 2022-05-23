package com.scalepoint.automation.services.ucommerce;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.getBalance.GetBalanceRequest;
import com.scalepoint.automation.utils.data.entity.getBalance.GetBalanceResponse;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class GetBalanceService extends BaseService {

    private GetBalanceResponse getBalanceResponse;

    private final String locale = Configuration.getLocale().getValue().toUpperCase();

    private Token token;

    public GetBalanceService(Token token){
        super();
        this.token = token;
    }
    public GetBalanceService getBalance(String claimNumber) {

        RestAssured.defaultParser = Parser.XML;
        getBalanceResponse = given().log().all()
                .header(token.getAuthorizationHeader())
                .contentType("application/xml")
                .body(buildGetBalanceRequest(claimNumber))
                .when()
                .post(Configuration.getGetBalanceWebServiceUrl())
                .then().statusCode(200).log().body()
                .extract().as(GetBalanceResponse.class);
        return this;
    }

    private GetBalanceRequest buildGetBalanceRequest(String claimNumber){
        setUserIdByClaimNumber(claimNumber);
        return GetBalanceRequest.builder().accountId(locale + data.getUserId()).build();
    }

    public void assertBalanceIs(Double expectedBalance){
        Double actualBalance = Double.parseDouble(getBalanceResponse.getBalance());
        assertThat(actualBalance.equals(expectedBalance))
                .as("balance value was: " + actualBalance + "but should be: " + expectedBalance)
                .isTrue();
    }
}

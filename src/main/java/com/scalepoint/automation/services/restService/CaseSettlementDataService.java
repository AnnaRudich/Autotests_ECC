package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.ValidatableResponse;

import static com.scalepoint.automation.services.restService.Common.BasePath.CASE_GET_REVISION;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class CaseSettlementDataService extends BaseService {

    private ValidatableResponse response;
    private Token token;

    public ValidatableResponse getResponse() {
        return response;
    }

    public CaseSettlementDataService(Token token){
        this.token = token;
    }

    public CaseSettlementDataService getSettlementData(String revisionToken){
        getSettlementData(revisionToken, "scalepoint");
        return this;
    }

    public CaseSettlementDataService getSettlementData(String revisionToken, String tenant){
        this.response = given().baseUri(getEccUrl()).log().all()
                .header(token.getAuthorizationHeader())
                .pathParam("revisionToken", revisionToken)
                .pathParam("tenant", tenant)
                .get(CASE_GET_REVISION)
                .then().log().all();
        return this;
    }

}

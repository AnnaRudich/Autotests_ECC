package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class CreateClaimService extends BaseService {

    private Token token;
    private Response response;

    public CreateClaimService(Token token) {
        this.token = token;
    }

    public Response getResponse() {
        return this.response;
    }

    public CreateClaimService setToken(Token token) {

        this.token = token;
        return this;
    }

    public CreateClaimService addClaim(ClaimRequest claimRequest) {

        this.response = given()
                .baseUri(getEccUrl())
                .basePath(UNIFIED_INTEGRATION)
                .body(claimRequest)
                .header(token.getAuthorizationHeader())
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        data.setClaimToken(response.jsonPath().get("token"));

        return this;
    }

    public CreateClaimService openClaim() {
        setUserIdByClaimToken();

        response = given().baseUri(getEccUrl())
                .queryParam("token", data.getClaimToken())
                .basePath(OPEN_CLAIM)
                .sessionId(data.getEccSessionId())
                .post()
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();
        return this;
    }

    public CreateClaimService saveClaim(ClaimRequest claimRequest) {

        setUserIdByClaimNumber(claimRequest.getCaseNumber());

        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("url", "/webapp/ScalePoint/dk/webshop/jsp/matching_engine/my_page.jsp");
        queryParams.put("last_name", claimRequest.getCustomer().getLastName());
        queryParams.put("first_name", claimRequest.getCustomer().getFirstName());
        queryParams.put("claim_number", claimRequest.getCaseNumber());
        queryParams.put("policy_number", claimRequest.getPolicy().getNumber());
        queryParams.put("updateCustomer", "false");

        response = given().baseUri(getEccUrl())
                .pathParam("userId", data.getUserId())
                .queryParams(queryParams)
                .basePath(SAVE_CUSTOMER)
                .sessionId(data.getEccSessionId())
                .post()
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        return this;
    }

    public ReopenClaimService reopenClaim(){

        return new ReopenClaimService().reopenClaim();
    }


    public SelfServiceService requestSelfService(SelfServiceRequest selfServiceRequest){

        return new SelfServiceService().requestSelfService(selfServiceRequest);
    }

    public ClaimSettlementItemsService claimLines() {
        return new ClaimSettlementItemsService();
    }

    public TextSearchService searchText(String text) throws JsonProcessingException {

        return new TextSearchService().searchText(text);
    }
}






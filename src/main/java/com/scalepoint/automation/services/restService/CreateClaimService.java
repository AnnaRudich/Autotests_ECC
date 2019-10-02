package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.services.restService.Common.BasePath.OPEN_CLAIM;
import static com.scalepoint.automation.services.restService.Common.BasePath.UNIFIED_INTEGRATION;
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
        this.response = given().baseUri(getEccUrl()).basePath(UNIFIED_INTEGRATION).log().all()
                .body(claimRequest)
                .header(token.getAuthorizationHeader())
                .when()
                .post()
                .then().log().all()
                .extract().response();
        data.setClaimToken(response.jsonPath().get("token"));
        return this;
    }

    public CreateClaimService openClaim() {
        setUserIdByClaimToken();

        given().log().all().baseUri(getEccUrl()).queryParam("token", data.getClaimToken())
                .basePath(OPEN_CLAIM)
                .sessionId(data.getEccSessionId())
                .post()
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).log().all();
        return this;
    }

    public SelfServiceService requestSelfService(SelfServiceRequest selfServiceRequest){

        return new SelfServiceService()
                .requestSelfService(selfServiceRequest);
    }

    public ClaimSettlementItemsService claimLines() {
        return new ClaimSettlementItemsService();
    }

}






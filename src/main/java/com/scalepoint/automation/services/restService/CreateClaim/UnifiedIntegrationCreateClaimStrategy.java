package com.scalepoint.automation.services.restService.CreateClaim;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import java.util.Optional;

import static com.scalepoint.automation.services.restService.Common.BasePath.UNIFIED_INTEGRATION;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 7/11/2017.
 */
public class UnifiedIntegrationCreateClaimStrategy extends BaseService implements CreateClaimStrategy {

    private Optional<ValidatableResponse> response;
    private ClaimRequest claimRequest;
    private Token token;

    public UnifiedIntegrationCreateClaimStrategy(ClaimRequest claimRequest, Token token){
        this.claimRequest = claimRequest;
        this.token = token;
    }

    @Override
    public UnifiedIntegrationCreateClaimStrategy createClaim() {
        this.response = Optional.ofNullable(given().baseUri(getEccUrl()).basePath(UNIFIED_INTEGRATION).log().all()
                .body(claimRequest)
                .header(token.getAuthorizationHeder())
                .when()
                .post()
                .then().log().all().statusCode(HttpStatus.SC_OK));
        return this;
    }

    @Override
    public UnifiedIntegrationCreateClaimStrategy saveData() {
        if(response.isPresent()) {
            String token = response.get().extract().jsonPath().get("token");
            data.setClaimToken(token);
            logger.info(String.format("Save claim token: '%s'", token));
        }else{
            throw new IllegalStateException("Can't save data because claim response is not present");
        }
        return this;
    }

    @Override
    public ValidatableResponse getResponse() {
        return getValidResponse(response);
    }


}

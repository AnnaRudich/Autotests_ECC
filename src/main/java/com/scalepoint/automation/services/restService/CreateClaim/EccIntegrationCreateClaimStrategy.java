package com.scalepoint.automation.services.restService.CreateClaim;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import java.util.Optional;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 7/12/2017.
 */
public class EccIntegrationCreateClaimStrategy extends BaseService implements CreateClaimStrategy {

    private Optional<ValidatableResponse> response;
    private EccIntegration eccIntegration;
    private String sessionId;

    public EccIntegrationCreateClaimStrategy(EccIntegration eccIntegration, String sessionId){
        this.eccIntegration = eccIntegration;
        this.sessionId = sessionId;
    }

    @Override
    public EccIntegrationCreateClaimStrategy createClaim() {
        this.response = Optional.ofNullable(given().baseUri(getEccUrl()).log().all()
                .sessionId(sessionId)
                .basePath("Integration/CreateClaim")
                .formParam("xml", TestData.objectAsXml(eccIntegration))
                .post()
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).log().all());

        return this;
    }

    @Override
    public EccIntegrationCreateClaimStrategy saveData() {
        return null;
    }

    @Override
    public ValidatableResponse getResponse() {
        return getValidResponse(response);
    }
}

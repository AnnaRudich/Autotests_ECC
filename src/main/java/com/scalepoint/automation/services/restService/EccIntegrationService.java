package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;


public class EccIntegrationService extends BaseService {

    private ValidatableResponse response;

    public ValidatableResponse getResponse() {
        return response;
    }

    public EccIntegrationService createClaim(EccIntegration eccIntegration) {
        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .basePath(CREATE_CLAIM)
                .formParam("xml", TestData.objectAsXml(eccIntegration))
                .post()
                .then().log().all();

        data.setUserId(data.getDatabaseApi().getUserIdByClaimNumber(eccIntegration.getClaim().getClaimNumber()));
        return this;
    }

    private Map<String, String> getClaimParams(EccIntegration eccIntegration) {
        Map<String, String> claimParams = new HashMap<>();
        claimParams.put("firstName", eccIntegration.getClaimant().getFirstName());
        claimParams.put("lastName", eccIntegration.getClaimant().getLastName());
        claimParams.put("addressLine1", eccIntegration.getClaimant().getAddress1());
        claimParams.put("zipCode", eccIntegration.getClaimant().getPostalCode());
        claimParams.put("city", eccIntegration.getClaimant().getCity());
        claimParams.put("phone1", eccIntegration.getClaimant().getPhone());
        claimParams.put("damageDate", eccIntegration.getClaim().getDamage().getDamageDate());
        claimParams.put("deductible", "600");
        claimParams.put("claimsNumber", eccIntegration.getClaim().getClaimNumber());
        claimParams.put("updateAction", "U");
        return claimParams;
    }

    public EccIntegrationService createAndOpenClaim(EccIntegration eccIntegration) {
        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .basePath(CREATE_AND_OPEN_CLAIM)
                .queryParams(getClaimParams(eccIntegration))
                .post()
                .then().log().all();

        data.setUserId(data.getDatabaseApi().getUserIdByClaimNumber(eccIntegration.getClaim().getClaimNumber()));
        return this;
    }

    public EccIntegrationService openCaseAndRedirect() {
        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .basePath(OPEN_CLAIM)
                .queryParam("shnbr", data.getUserId())
                .get()
                .then().log().all().extract().response();

        response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", data.getUserId())
                .basePath("webshop/jsp/matching_engine/customer_details.jsp")
                .get()
                .then().log().all();
        return this;
    }

}

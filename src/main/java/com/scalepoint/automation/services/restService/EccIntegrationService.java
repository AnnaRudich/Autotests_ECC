package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;


public class EccIntegrationService extends BaseService {

    private ValidatableResponse response;

    public EccIntegrationService createClaim(EccIntegration eccIntegration) {
        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .basePath("Integration/CreateClaim")
                .formParam("xml", TestData.objectAsXml(eccIntegration))
                .post()
                .then().log().all();

        data.setUserId(data.getDatabaseApi().getUserIdByClaimNumber(eccIntegration.getClaim().getClaimNumber()));
        return this;
    }

    public void openCase(){
        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .basePath("Integration/Open")
                .queryParam("shnbr", data.getUserId())
                .get()
                .then().log().all().extract().response();

        Response response1 = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", data.getUserId())
                .basePath("webshop/jsp/matching_engine/customer_details.jsp")
                .get()
                .then().log().all().extract().response();
    }

}

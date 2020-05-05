package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BasePath;
import com.scalepoint.automation.services.restService.common.BaseService;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;


public class OwnRiskService extends BaseService {

    public OwnRiskService setSelfRiskForClaim(String selfRisk) {

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParam("caseId", data.getUserId())
                .formParam("ownRisk", selfRisk)
                .post(BasePath.OWN_RISK)
                .then().statusCode(HttpStatus.SC_OK);

        return this;
    }
}

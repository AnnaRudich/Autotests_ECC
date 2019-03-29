package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BasePath;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.request.ManualReduction;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class ManualReductionService extends BaseService {


    public ManualReductionService setManualReductionForClaim(String amount) {

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/json")
                .body(new ManualReduction(data.getUserId(), amount))
                .post(BasePath.MANUAL_REDUCTION)
                .then().statusCode(HttpStatus.SC_OK);

        return this;
    }
}

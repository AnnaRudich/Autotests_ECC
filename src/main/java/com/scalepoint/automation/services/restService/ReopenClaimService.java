package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BasePath;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.restService.Common.Data;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class ReopenClaimService extends BaseService {

    private Data data;

    public ReopenClaimService(){
        this.data = getData();
    }

    public ReopenClaimService reopenClaim(){

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", data.getUserId())
                .get(BasePath.REOPEN)
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .pathParam("shnbr", data.getUserId())
                .get("/{shnbr}/webshop/jsp/matching_engine/settlement.jsp")
                .then().statusCode(HttpStatus.SC_OK);

        return this;
    }
}

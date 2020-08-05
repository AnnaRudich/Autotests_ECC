package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BasePath;
import com.scalepoint.automation.services.restService.common.BaseService;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class ReopenClaimService extends BaseService {

    public ClaimSettlementItemsService reopenClaim() {

        given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .queryParam("shnbr", data.getUserId())
                .get(BasePath.REOPEN)
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY);

        given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .pathParam("shnbr", data.getUserId())
                .get("/{shnbr}/webshop/jsp/matching_engine/settlement.jsp")
                .then()
                .statusCode(HttpStatus.SC_OK);

        return new ClaimSettlementItemsService();
    }
}

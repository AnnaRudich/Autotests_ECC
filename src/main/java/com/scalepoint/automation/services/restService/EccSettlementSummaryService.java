package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BasePath;
import com.scalepoint.automation.services.restService.common.BaseService;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class EccSettlementSummaryService extends BaseService {

    private final String SUBTOTAL_CASH_PAYOUT_VALUE = "subtotalCashPayoutValue";

    private ValidatableResponse response;

    public EccSettlementSummaryService getSummaryTotals() {
        response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .get(BasePath.SETTLEMENT_TOTALS)
                .then().statusCode(HttpStatus.SC_OK);
        return this;
    }

    public Double getSubtotalCashPayoutValue() {
        return response.extract().jsonPath().getDouble(SUBTOTAL_CASH_PAYOUT_VALUE);
    }
}

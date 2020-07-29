package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BasePath;
import com.scalepoint.automation.services.restService.common.BaseService;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class EccSettlement extends BaseService {

    private final String SUBTOTAL_CASH_PAYOUT_VALUE = "subtotalCashPayoutValue";

    private ValidatableResponse response;

    public EccSettlement getSummaryTotals() {

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .pathParam("userId", data.getUserId())
                .get(BasePath.SETTLEMENT_TOTALS)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public EccSettlement getCheckSelfServiceResponses() {

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .pathParam("userId", data.getUserId())
                .get(CHECK_SELF_SERVICE_RESPONSES)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public EccSettlement getAuditRequestData() {

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .pathParam("userId", data.getUserId())
                .get(AUDIT_REQUEST_DATA)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public EccSettlement getClaimLines() {

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .pathParam("userId", data.getUserId())
                .get(CLAIM_LINES)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public EccSettlement getVoucherAmountTotal(String isGroup) {

        response = given().baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .queryParam("is_group", isGroup)
                .pathParam("userId", data.getUserId())
                .get(CLAIM_LINES)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public Double getSubtotalCashPayoutValue() {

        return response.extract()
                .jsonPath()
                .getDouble(SUBTOTAL_CASH_PAYOUT_VALUE);
    }
}

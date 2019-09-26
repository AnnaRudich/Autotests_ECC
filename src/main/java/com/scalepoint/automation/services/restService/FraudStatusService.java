package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BasePath;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.awaitility.core.ConditionTimeoutException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

public class FraudStatusService extends BaseService {

    private static final int POLL_MS = 10;
    private static final int STATUS_CHANGE_TIMEOUT = 120;

    private Response response;

    public Response getResponse() {
        return this.response;
    }

    public String getFraudStatus() {

        return given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .queryParam("_dc", LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond())
                .pathParam("userId", data.getUserId())
                .get(BasePath.FRAUD_STATUS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .log().all()
                .extract().response().jsonPath().getString("data.status");
    }

    public String waitForFraudStatus(String status, Case caseData){
        try {
            return await()
                    .pollInterval(POLL_MS, TimeUnit.MILLISECONDS)
                    .timeout(STATUS_CHANGE_TIMEOUT, TimeUnit.SECONDS)
                    .until(() -> getFraudStatus(), equalTo(status));
        }catch (ConditionTimeoutException e){

            throw new ConditionTimeoutException("UI not updated. Case number: ".concat(caseData.getCaseNumber()));
        }
    }
}

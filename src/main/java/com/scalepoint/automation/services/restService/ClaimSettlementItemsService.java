package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.UUID;

import static com.scalepoint.automation.services.restService.Common.BasePath.INSERT_SETTLEMENT_ITEM;
import static com.scalepoint.automation.services.restService.Common.BasePath.REMOVE_SETTLEMENT_ITEM;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class ClaimSettlementItemsService extends BaseService {

    private Response response;

    public Response getResponse() {
        return this.response;
    }


    public ClaimSettlementItemsService addLines(InsertSettlementItem... items) {
        Arrays.stream(items)
                .forEach(i ->
                        addLine(i));
        return this;
    }

    public ClaimSettlementItemsService removeLines(InsertSettlementItem... items) {
        Arrays.stream(items).filter(i -> i.eccItemId != null).forEach(i -> removeLine(i.eccItemId));
        return this;
    }

    public ClaimSettlementItemsService editLines(InsertSettlementItem... items) {
        Arrays.stream(items).filter(i -> i.eccItemId != null).forEach(i -> {
            removeLine(i.eccItemId);
            addLine(i);
        });

        return this;
    }


    private ClaimSettlementItemsService addLine(InsertSettlementItem item) {
        item.setCaseId(data.getUserId().toString());
        item.getSettlementItem().getClaim().setClaimToken(UUID.randomUUID().toString());

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParam("xml", TestData.objectAsXml(item))
                .formParam("productId", -1)
                .formParam("replacedProductId", -1)
                .formParam("GenericItemsreturnValue", "")
                .formParam("fromNewSid", true)
                .when()
                .post(INSERT_SETTLEMENT_ITEM)
                .then().log().all().statusCode(HttpStatus.SC_OK).extract().response();
        item.eccItemId = response.jsonPath().get("itemId");
        return this;
    }


    private ClaimSettlementItemsService removeLine(Integer id) {

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/json")
                .body("{\"selectedGroups\":[], \"selectedLines\":[\"" + id + "\"]}")
                .post(REMOVE_SETTLEMENT_ITEM)
                .then().statusCode(HttpStatus.SC_OK).extract().response();

        return this;
    }


    public SettlementClaimService closeCase() {
        return new SettlementClaimService();
    }
}

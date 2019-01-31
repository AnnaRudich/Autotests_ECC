package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.scalepoint.automation.services.restService.Common.BasePath.INSERT_SETTLEMENT;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class ClaimSettlementItemsService extends BaseService {

    private Response response;

    public Response getResponse(){
        return this.response;
    }


    public ClaimSettlementItemsService addLines(List<InsertSettlementItem> items){
        items.forEach(item -> addLine(item));

        return this;
    }

    public ClaimSettlementItemsService addLine(InsertSettlementItem item){
        item.setCaseId(data.getUserId().toString());
        item.getSettlementItem().getClaim().setClaimToken(getClaimTokenWithoutPrefix());

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParam("xml", TestData.objectAsXml(item))
                .formParam("productId", -1)
                .formParam("replacedProductId", -1)
                .formParam("GenericItemsreturnValue", "")
                .formParam("fromNewSid", true)
                .when()
                .post(INSERT_SETTLEMENT)
                .then().log().all().statusCode(HttpStatus.SC_OK).extract().response();
        return this;
    }

    public SettlementClaimService closeCase(){
        return new SettlementClaimService();
    }
}

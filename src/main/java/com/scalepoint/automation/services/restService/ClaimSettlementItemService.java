package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.services.restService.Common.BasePath.INSERT_SETTLEMENT;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class ClaimSettlementItemService extends BaseService {

    private Response response;
    private InsertSettlementItem item;

    public Response getResponse(){
        return this.response;
    }

    public InsertSettlementItem getItem() {
        return item;
    }

    public ClaimSettlementItemService setItem(InsertSettlementItem item) {
        this.item = item;
        return this;
    }

    public ClaimSettlementItemService setItemCaseAndClaimNumber(){
        this.item.setCaseId(data.getUserId().toString());
        this.item.getSettlementItem().getClaim().setClaimToken(getClaimTokenWithoutPrefix());
        return this;
    }

    public ClaimSettlementItemService addLines(InsertSettlementItem item){
        this.item = item;
        setItemCaseAndClaimNumber();

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParam("xml", TestData.objectAsXml(this.item))
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

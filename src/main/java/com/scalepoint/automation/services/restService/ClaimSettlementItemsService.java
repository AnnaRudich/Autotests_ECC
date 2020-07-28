package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation.SelectedLinesAndCategories;
import com.scalepoint.automation.utils.data.entity.rnv.sendToRepairAndValuation.SendToRepairAndValuation;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
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

    public ClaimSettlementItemsService getClaimLines(){

        new EccSettlement()
                .getCheckSelfServiceResponses()
                .getSummaryTotals()
                .getAuditRequestData()
                .getClaimLines()
                .getVoucherAmountTotal("no");

        return this;
    }

    public SettlementItemDialogService sid(){

        return new SettlementItemDialogService();
    }


    public ClaimSettlementItemsService addLine(InsertSettlementItem item) {
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

    public RnvService sendToRepairAndValuation(InsertSettlementItem insertSettlementItem) throws MalformedURLException {

        SelectedLinesAndCategories selectedLineCategory = SelectedLinesAndCategories.builder()
                .claimLineId(1)
                .description(insertSettlementItem.getSettlementItem().getClaim().getDescription())
                .isIsRvRepairTaskSentOrApproved(false)
                .itemId(insertSettlementItem.getEccItemId().toString())
                .build();

        SendToRepairAndValuation sendToRepairAndValuation = SendToRepairAndValuation.builder()
                .selectedGroups(new ArrayList<>())
                .selectedLines(Arrays.asList(insertSettlementItem.getEccItemId().toString()))
                .selectedLinesAndCategories(Arrays.asList(selectedLineCategory)).build();

        this.response = given().baseUri(getEccUrl())
                .log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType(ContentType.JSON)
                .body(sendToRepairAndValuation)
                .when()
                .post(SEND_TO_REPAIR_AND_VALUATION)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK).extract().response();

        this.response = given()
                .log().all()
                .sessionId(data.getEccSessionId())
                .redirects().follow(false)
                .when()
                .get(new URL(response.body().jsonPath().get("data")))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        data.setRnvSessionId(response.getSessionId());

        String location = response.header("Location");

        Matcher matcher = Pattern.compile("(orderToken=)(.+)(&)").matcher(location);
        matcher.find();

        String orderToken = matcher.group(2);

        data.setOrderToken(orderToken);

        this.response = given()
                .log().all()
                .sessionId(data.getRnvSessionId())
                .when()
                .get(new URL(location))
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return new RnvService().sendToRepairAndValuation();
    }




    public AttachmentsService toAttachments() throws IOException {
        return new AttachmentsService();
    }

    public SettlementClaimService closeCase() {
        return new SettlementClaimService();
    }
}

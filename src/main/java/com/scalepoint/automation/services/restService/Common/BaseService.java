package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static com.scalepoint.automation.services.restService.Common.BasePath.EXCEL;
import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

    protected Logger logger = LogManager.getLogger(getClass());
    protected Data data;

    public Data getData() {
        return data;
    }

    public BaseService() {
        this.data = ServiceData.getData();
        if (getEccAdminUrl().contains("localhost")) {
            RestAssured.port = 80;
        }
    }

    public String getClaimTokenWithoutPrefix() {
        return data.getClaimToken().replace("c.", "");
    }

    public void setUserIdByClaimToken() {
        data.setUserId(data.getDatabaseApi().getUserIdByClaimToken(getClaimTokenWithoutPrefix()));
    }

    public static ClaimSettlementItemsService loginAndOpenClaimWithItems(User user, ClaimRequest claimRequest, InsertSettlementItem... items) {
        return loginAndOpenClaim(user, claimRequest)
                .claimLines()
                .addLines(items);
    }

    public static CreateClaimService loginAndOpenClaim(User user, ClaimRequest claimRequest){
        return new LoginProcessService()
                .login(user)
                .createClaim(new OauthTestAccountsApi().sendRequest().getToken())
                .addClaim(claimRequest)
                .openClaim();
    }

    public static LoginProcessService loginUser(User user) {
        return new LoginProcessService()
                .login(user);
    }

    public SelfServiceService importExcel(){

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("multipart/form-data")
                .multiPart("file", new File("C:\\Users\\bna\\IdeaProjects\\automatedtest\\src\\main\\resources\\excelImport\\DK_NYT ARK(3)(a).xls"))
                .post(EXCEL)
                .then().log().all().statusCode(HttpStatus.SC_OK).extract().response();

        return new SelfServiceService();
    }
}

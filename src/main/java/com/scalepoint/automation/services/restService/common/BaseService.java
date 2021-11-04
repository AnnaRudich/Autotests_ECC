package com.scalepoint.automation.services.restService.common;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.ImportExcelService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

    protected static final String ZONE_OFFSET = "+02:00";

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

    public void setUserIdByClaimNumber(String claimNumber){
        data.setUserId(data.getDatabaseApi().getUserIdByClaimNumber(claimNumber));
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

    public static LoginProcessService setEccSessionId(String eccSessionId){
        return new LoginProcessService(eccSessionId);
    }

    public ImportExcelService importExcel() throws IOException {

        return new ImportExcelService()
                .excel();
    }
}

package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

    protected Logger logger = LogManager.getLogger(getClass());
    protected Data data;

    public Data getData(){
        return data;
    }

    public BaseService(){
        this.data = ServiceData.getData();
        if(getEccAdminUrl().contains("localhost")) {
            RestAssured.port = 80;
        }
    }

    public String getClaimTokenWithoutPrefix(){
        return data.getClaimToken().replace("c.", "");
    }

    public void setUserIdByClaimToken(){
        data.setUserId(data.getDatabaseApi().getUserIdByClaimToken(getClaimTokenWithoutPrefix()));
    }

    public static ClaimSettlementItemService loginAndOpenClaimWithItem(User user, ClaimRequest claimRequest, InsertSettlementItem item){
        return new LoginProcessService()
                .login(user)
                .createClaim(new TestAccountsApi().sendRequest().getToken())
                .addClaim(claimRequest)
                .openClaim()
                .claimLines()
                .addLines(item);
    }

    public static LoginProcessService loginUser(User user){
        return new LoginProcessService()
                .login(user);
    }
}

package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemsService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;
import static java.util.Arrays.asList;

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

    public static ClaimSettlementItemsService loginAndOpenClaimWithItem(User user, ClaimRequest claimRequest, InsertSettlementItem item){
        return loginAndOpenClaimWithItems(user, claimRequest, asList(item));
    }

    public static ClaimSettlementItemsService loginAndOpenClaimWithItems(User user, ClaimRequest claimRequest, List<InsertSettlementItem> items){
        return new LoginProcessService()
                .login(user)
                .createClaim(new OauthTestAccountsApi().sendRequest().getToken())
                .addClaim(claimRequest)
                .openClaim()
                .claimLines()
                .addLines(items);
    }

    public static LoginProcessService loginUser(User user){
        return new LoginProcessService()
                .login(user);
    }
}

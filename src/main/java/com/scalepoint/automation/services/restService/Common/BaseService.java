package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemService;
import com.scalepoint.automation.services.restService.CreateClaim.CreateClaimStrategy;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected Data data;

    public BaseService(){
        this.data = ServiceData.getData();
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

    public static void loginUser(User user){
        new LoginProcessService()
                .login(user);
    }

    public static CreateClaimStrategy createClaim(CreateClaimStrategy createClaimStrategy){
        return createClaimStrategy.createClaim().saveData();
    }

}

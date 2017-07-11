package com.scalepoint.automation.services.restService.Common;

import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.ClaimSettlementItemService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;

/**
 * Created by bza on 6/29/2017.
 */
public class BaseService {

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

}

package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.response.Token;

public class RnvService extends BaseService {

    private Token token;

   public RnvService(Token token){
       this.token=token;
   }

}

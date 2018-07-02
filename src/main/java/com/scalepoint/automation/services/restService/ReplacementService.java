package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.services.restService.helper.PrepareSaveCustomerParams;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.scalepoint.automation.services.externalapi.DatabaseApi.PriceConditions.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;


public class ReplacementService extends BaseService{

    private Map<String,String> formParams = new HashMap<>();
    private PrepareSaveCustomerParams prepareSaveCustomerParams;

    public ReplacementService(){
        this.prepareSaveCustomerParams = new PrepareSaveCustomerParams();
    }

    public Map<String,String> getFormParams(String itemId){
        formParams.put("userId", data.getUserId().toString());
        formParams.put("select_all", "on");
        formParams.put("replaceType", "product");
        formParams.put("variant" + itemId, "375");
        formParams.put("quantity" + itemId, "1");
        formParams.put("price_quantity" + itemId, "375");
        formParams.put("remainingWithdrawalAmount", "375");
        formParams.put("balance", "0.0");
        formParams.put("own_risk", "0.0");
        formParams.put("claims_sum", "375");
        formParams.put("paytocustomeramount", "99510.98");
        formParams.put("customer_note", "");
        return formParams;
    }

    public ReplacementService makeReplacement(Object claimRequest){

        String itemId = String.valueOf(SolrApi.findProduct(getData().getDatabaseApi().findProduct(ORDERABLE, PRODUCT_AS_VOUCHER_ONLY_FALSE,INVOICE_PRICE_LOWER_THAN_10)).getId());

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParams(getFormParams(itemId))
                .post("{userId}/ReplacementWizardStep1")
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParams(getFormParams(itemId))
                .formParam("bankAccountType", "")
                .formParam("__acctnbr", "")
                .formParam("withdrawal", "0.0")
                .formParam("deposit", "0.0")
                .formParam("own_risk", "0.0")
                .formParam("hasEvoucher", "")
                .post("{userId}/ReplacementWizardStep2")
                .then().statusCode(HttpStatus.SC_OK);

        given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParams(prepareSaveCustomerParams.prepareSaveCustomerParams(claimRequest, data).getSaveCustomerParams())
                .post("{userId}/PostReplacedFromME")
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).log().all();

        return this;
    }
}

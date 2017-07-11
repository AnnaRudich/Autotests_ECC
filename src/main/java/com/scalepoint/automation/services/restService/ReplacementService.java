package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;


public class ReplacementService extends BaseService{

    private Map<String,String> formParams = new HashMap<>();

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

    public ReplacementService makeReplacement(ClaimRequest claimRequest){

        String itemId = String.valueOf(SolrApi.findProductWithPriceLowerThan("10").getId());

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
                .formParam("replacement", "false")
                .formParam("customer_id", data.getUserId())
                .formParam("claimNumber", claimRequest.getCaseNumber())
                .formParam("phone", claimRequest.getCustomer().getMobile())
                .formParam("fname", claimRequest.getCustomer().getFirstName())
                .formParam("lname", claimRequest.getCustomer().getLastName())
                .formParam("email", claimRequest.getCustomer().getEmail())
                .formParam("adr1", claimRequest.getCustomer().getAddress().getStreet1())
                .formParam("zipcode", claimRequest.getCustomer().getAddress().getPostalCode())
                .formParam("changepassword", "1")
                .formParam("password", Constants.PASSWORD)
                .post("{userId}/PostReplacedFromME")
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).log().all();

        return this;
    }
}

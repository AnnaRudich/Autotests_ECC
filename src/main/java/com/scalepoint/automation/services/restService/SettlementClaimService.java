package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BasePath;
import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.scalepoint.automation.services.restService.Common.BasePath.SAVE_CLAIM;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/29/2017.
 */
public class SettlementClaimService extends BaseService {

    private Response response;
    private Map<String,String> saveCustomerParams = new HashMap<>();

    public Map<String, String> getSaveCustomerParams() {
        return saveCustomerParams;
    }

    public void setSaveCustomerParams(Map<String, String> saveCustomerParams) {
        this.saveCustomerParams = saveCustomerParams;
    }

    public Response getResponse(){
        return this.response;
    }

    public Map<String, String> getFilledSaveCustomerParams(ClaimRequest claimRequest){
        saveCustomerParams.put("last_name", "");
        saveCustomerParams.put("first_name", "");
        saveCustomerParams.put("replacement", "");
        saveCustomerParams.put("validate_zipcode", "");
        saveCustomerParams.put("send_settlement_preview", "");
        saveCustomerParams.put("customer_id", data.getUserId().toString());
        saveCustomerParams.put("sendSMSPassword", "false");
        saveCustomerParams.put("smsPassword", "");
        saveCustomerParams.put("checkForCancelProcura", "");
        saveCustomerParams.put("isNewProcuraRequested", "");
        saveCustomerParams.put("policy_number", claimRequest.getCaseNumber());
        saveCustomerParams.put("cellPhoneNumber", claimRequest.getCustomer().getMobile());
        saveCustomerParams.put("phone", claimRequest.getCustomer().getMobile());
        saveCustomerParams.put("fname", claimRequest.getCustomer().getFirstName());
        saveCustomerParams.put("lname", claimRequest.getCustomer().getLastName());
        saveCustomerParams.put("shopper_id", "");
        saveCustomerParams.put("adr1", claimRequest.getCustomer().getAddress().getStreet1());
        saveCustomerParams.put("adr2", "");
        saveCustomerParams.put("zipcode", claimRequest.getCustomer().getAddress().getPostalCode());
        saveCustomerParams.put("city", claimRequest.getCustomer().getAddress().getCity());
        saveCustomerParams.put("email", "ecc_auto@scalepoint.com");
        saveCustomerParams.put("changepassword", "1");
        saveCustomerParams.put("password", Constants.PASSWORD);
        saveCustomerParams.put("customer_note", "");
        saveCustomerParams.put("caseid", data.getUserId().toString());

        return saveCustomerParams;
    }

    private SettlementClaimService saveCustomer(ClaimRequest claimRequest, CloseCaseReason closeCaseReason){
        saveCustomer(getFilledSaveCustomerParams(claimRequest), closeCaseReason);
        return this;
    }

    private SettlementClaimService saveCustomer(Map<String,String> formParams, CloseCaseReason closeCaseReason){
        formParams.put("url", "/webapp/ScalePoint/dk"+ closeCaseReason.getPath().replace("{userId}", data.getUserId().toString()));

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getSessionId())
                .pathParam("userId", data.getUserId())
                .formParams(formParams)
                .post(SAVE_CLAIM)
                .then().log().headers().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        given().log().all().baseUri(response.getHeader("Location"))
                .sessionId(data.getSessionId())
                .port(80)
                .get(response.getHeader("Location"))
                .then();

        return this;
    }

    public SettlementClaimService close(ClaimRequest claimRequest, CloseCaseReason reason ){
        saveCustomer(claimRequest, reason);

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getSessionId())
                .pathParam("userId", data.getUserId())
                .get(reason.getPath())
                .then().statusCode(HttpStatus.SC_OK).extract().response();
        return this;
    }

    public enum CloseCaseReason{

        CLOSE_EXTERNAL(BasePath.CLOSE_EXTERNAL),
        CLOSE_WITH_MAIL(BasePath.CLOSE_WITH_MAIL);

        private String path;

        CloseCaseReason(String path){
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}

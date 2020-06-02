package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BasePath;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.services.restService.helper.PrepareSaveCustomerParams;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Map;

import static com.scalepoint.automation.services.restService.common.BasePath.CANCEL_CLAIM;
import static com.scalepoint.automation.services.restService.common.BasePath.SAVE_CLAIM;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.REPLACEMENT;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

public class SettlementClaimService extends BaseService {

    private Response response;
    private PrepareSaveCustomerParams prepareSaveCustomerParams;

    public SettlementClaimService() {
        this.prepareSaveCustomerParams = new PrepareSaveCustomerParams();
    }

    public Response getResponse() {
        return this.response;
    }

    private SettlementClaimService saveCustomer(Object object, CloseCaseReason closeCaseReason) {
        Map<String, String> params = prepareSaveCustomerParams.prepareSaveCustomerParams(object, data).getSaveCustomerParams();

        if (closeCaseReason.equals(REPLACEMENT)) {
            params.put("claim_number", ((ClaimRequest) object).getCaseNumber());
            params.put("replacement", "true");
        }

        saveCustomer(params, closeCaseReason.getPath());
        return this;
    }

    private SettlementClaimService saveCustomer(Map<String, String> formParams, String path) {
        formParams.put("url", "/webapp/ScalePoint/dk" + path.replace("{userId}", data.getUserId().toString()));

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .formParams(formParams)
                .post(SAVE_CLAIM)
                .then().log().headers().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        given().log().all().baseUri(response.getHeader("Location"))
                .sessionId(data.getEccSessionId())
                .get(response.getHeader("Location"))
                .then();

        return this;
    }

    public SettlementClaimService close(Object claimRequest, CloseCaseReason reason) {
        //make request to save customer with NULL claim_number in order to be redirected to enter_base_info.jsp
        //in order to set session attribute SESSION_CUSTOMER_SAVED
        //For this we need ft FUNC_DISALLOW_DUPLICATE_CLAIMS_NUMBER to be enabled
        saveCustomer(claimRequest, reason);
        if (reason.equals(REPLACEMENT)) {
            new ReplacementService().makeReplacement(claimRequest);
        } else {
            this.response = given().baseUri(getEccUrl()).log().all()
                    .sessionId(data.getEccSessionId())
                    .pathParam("userId", data.getUserId())
                    .get(reason.getPath())
                    .then().log().headers().statusCode(HttpStatus.SC_OK).extract().response();
        }

        return this;
    }

    public SettlementClaimService cancel(Object claimRequest) {
        saveCustomer(prepareSaveCustomerParams.prepareSaveCustomerParams(claimRequest, data).getSaveCustomerParams(), CANCEL_CLAIM);

        this.response = given().baseUri(getEccUrl()).log().all()
                .sessionId(data.getEccSessionId())
                .queryParam("shnbr", data.getUserId())
                .queryParam("closeClaim", false)
                .get(CANCEL_CLAIM)
                .then().statusCode(HttpStatus.SC_OK).extract().response();
        return this;
    }

    public enum CloseCaseReason {

        CLOSE_EXTERNAL(BasePath.CLOSE_EXTERNAL),
        CLOSE_WITH_MAIL(BasePath.CLOSE_WITH_MAIL),
        CLOSE_WITHOUT_MAIL(BasePath.CLOSE_WITHOUT_MAIL),
        REPLACEMENT(BasePath.REPLACEMENT);

        private String path;

        CloseCaseReason(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}

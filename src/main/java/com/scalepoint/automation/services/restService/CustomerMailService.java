package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.services.restService.common.Data;
import com.scalepoint.automation.utils.data.request.CustomerMailListItem;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.services.restService.common.BasePath.CUSTOMER_MAIL_LIST;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/27/2017.
 */

public class CustomerMailService extends BaseService {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public CustomerMailService(){
        super();
    }

    public CustomerMailService(Data data){
        super();
        this.data = data;
    }

    public CustomerMailListItem[] getCustomerMailList() {

        return given().log().all()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
//                .pathParam("userId1", data.getUserId())
                .pathParam("userId2", data.getUserId())
                .get(CUSTOMER_MAIL_LIST)
                .then().log().all()
//                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().as(CustomerMailListItem[].class);
    }

//    public CreateClaimService createClaim(Token token) {
//        return new CreateClaimService(token);
//    }
//
//    public SelfServiceService reloadFunctionTemplate(){return new SelfServiceService().reloadFunctionTemplate();}
//
//    private String getLocationHeader(Response response) {
//        return response.getHeader("Location");
//    }
}

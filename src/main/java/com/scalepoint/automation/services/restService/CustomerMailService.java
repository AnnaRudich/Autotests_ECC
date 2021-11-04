package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.services.restService.common.Data;
import com.scalepoint.automation.utils.data.request.CustomerMailListItem;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.services.restService.common.BasePath.CUSTOMER_MAIL_LIST;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

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
                .pathParam("userId", data.getUserId())
                .get(CUSTOMER_MAIL_LIST)
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().as(CustomerMailListItem[].class);
    }
}

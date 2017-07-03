package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.scalepoint.automation.utils.Configuration.getEccAdminUrl;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static io.restassured.RestAssured.given;

/**
 * Created by bza on 6/27/2017.
 */

public class LoginProcessService extends BaseService {

    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public LoginProcessService login(User user){
        Response loginProcessResponse = given().log().all().baseUri(getEccAdminUrl()).port(80)
                .redirects().follow(false)
                .formParam("j_username", user.getLogin())
                .formParam("j_password", user.getPassword())
                .post("/loginProcess")
                .then().log().all()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        Response loginActionResponse = given().log().all().baseUri(getLocationHeader(loginProcessResponse)).port(80)
                .redirects().follow(false)
                .sessionId(loginProcessResponse.getSessionId())
                .get()
                .then().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).log().all().extract().response();

        this.response = given().log().all().baseUri(getLocationHeader(loginActionResponse)).port(80)
                .redirects().follow(false)
                .get(getLocationHeader(loginActionResponse))
                .then().log().all().statusCode(HttpStatus.SC_MOVED_TEMPORARILY).extract().response();

        given().log().all().baseUri(getEccUrl()).port(80)
                .sessionId(response.getSessionId())
                .queryParam("sessionident", response.getSessionId())
                .get("webshop/jsp/matching_engine/start.jsp")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        data.setSessionId(response.getSessionId());

        return this;
    }

    public CreateClaimService createClaim(Token token){
        return new CreateClaimService(token);
    }

    private String getLocationHeader(Response response){
        return response.getHeader("Location");
    }
}

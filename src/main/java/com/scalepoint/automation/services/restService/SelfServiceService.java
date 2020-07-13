package com.scalepoint.automation.services.restService;

import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.utils.data.request.SelfServiceLossItems;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.Configuration.getEnvironmentUrl;
import static io.restassured.RestAssured.given;

public class SelfServiceService extends BaseService {

    private Response response;
    private String linkToSS;

    public Response getResponse() {
        return this.response;
    }

    public SelfServiceService requestSelfService(SelfServiceRequest selfServiceRequest) {

        this.response = given()
                .log().all()
                .baseUri(getEccUrl())
                .sessionId(data.getEccSessionId())
                .pathParam("userId", data.getUserId())
                .contentType("application/json")
                .body(selfServiceRequest)
                .post(SELF_SERVICE_REQUEST)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();


        this.linkToSS = response
                .jsonPath()
                .get("data.linkToSS")
                .toString()
                .concat("&selfService=true");

        return this;
    }

    public SelfServiceService loginToSS(String password){

        String body = given()
                .get(linkToSS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().print();

        Matcher matcher = Pattern.compile("<.+id=\"username\".+>").matcher(body);
        matcher.find();
        matcher = Pattern.compile("\\w+(-\\w+){4}").matcher(matcher.group());
        matcher.find();
        String username = matcher.group();

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .formParam("username", username)
                .formParam("password", password)
                .post(SELF_SERVICE_LOGIN)
                .then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .extract().response();

        data.setSelfServiceAccessToken(response.getHeader("Access-Token"));

        this.response = given()
                .get(response.getHeader("Location"))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }

    public SelfServiceService addLossItem(SelfServiceLossItems selfServiceLossItems){

        this.response = given()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .contentType("application/json")
                .body(selfServiceLossItems)
                .post(SELF_SERVICE_LOSS_ITEMS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }

    public SelfServiceService submitted(){

        this.response = given().log().uri()
                .baseUri(getEnvironmentUrl())
                .header("Access-Token", data.getSelfServiceAccessToken())
                .post(SELF_SERVICE_SUBMITTED)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();

        return this;
    }

}

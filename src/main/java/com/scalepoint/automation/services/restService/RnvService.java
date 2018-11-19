package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RnvService extends BaseService {

    private String supplierSecurityToken = "589356A5-2E39-4438-B45F-8E05C545ABD3";


    public void pullRnVTaskData(){
        given().log().all()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("x-www-form-urlencoded, charset=UTF-8",
                                        ContentType.TEXT)))
                .formParam("securityToken", supplierSecurityToken)
                .when().post(Configuration.getRnvPullTaskDataUrl()).then().statusCode(201).log().all();

    }

    public void sendFeedback(){

    }
}

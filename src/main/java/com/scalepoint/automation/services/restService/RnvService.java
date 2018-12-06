package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.serviceTaskEntity.copy.ServiceTasksExport;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RnvService extends BaseService {

    private String supplierSecurityToken = "589356A5-2E39-4438-B45F-8E05C545ABD3";

    public ServiceTasksExport pullRnVTaskData() {

        return given().log().all()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("x-www-form-urlencoded, charset=UTF-8",
                                        ContentType.TEXT)))
                .formParam("securityToken", supplierSecurityToken)
                .when().post(Configuration.getRnvPullTaskDataUrl())
                .as(ServiceTasksExport.class);
    }


    public void sendFeedback(){

        given().log().all()
                    .multiPart("securityToken", supplierSecurityToken)
                    .multiPart("xmlString", new ServiceTaskImport(), "application/xml")
                    .when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(200);
    }

    enum InvoiceType{
        INVOICE,
        CREDIT_NOTE
    }

    enum TaskType{
        REPAIR,
        REPAIR_ESTIMATE,
        VALUATION,
        MATCH_SERVICE
    }
}

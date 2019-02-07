package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceTaskImportBuilder;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RnvService extends BaseService {

    private String supplierSecurityToken = TestData.getServiceAgreement().getSupplierSecurityToken();

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


    public void sendDefaultFeedback(Claim claim) {

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, pullRnVTaskData()).buildDefault();

        given().log().all()
                .multiPart("securityToken", supplierSecurityToken)
                .multiPart("xmlString", TestData.objectAsXml(serviceTaskImport))
                .when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(201);
    }

    public void sendFeedbackWithRepairPrice(BigDecimal repairPrice, Claim claim) {

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, pullRnVTaskData()).buildWithRepairPrice(repairPrice);

        given().log().all()
                .multiPart("securityToken", supplierSecurityToken)
                .multiPart("xmlString", TestData.objectAsXml(serviceTaskImport))
                .when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(201);
    }
}

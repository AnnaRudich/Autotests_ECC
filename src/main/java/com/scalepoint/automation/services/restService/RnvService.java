package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImportBuilder;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RnvService extends BaseService {

    private String supplierSecurityToken = "7D1B2289-9365-4294-BD11-A7EB865B94E3";
    //private String supplierSecurityToken = new ServiceAgreement().getSupplierSecurityToken();

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


    public void sendFeedback(Claim claimEnt) {
        StringWriter writer = new StringWriter();
        try {
            JAXBContext.newInstance(ServiceTaskImport.class)
                    .createMarshaller()
                    .marshal(new ServiceTaskImportBuilder().setDefault(pullRnVTaskData(), claimEnt).build(), writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        given().log().all()
                    .multiPart("securityToken", supplierSecurityToken)
                    .multiPart("xmlString", writer.toString())
                    .when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(201);
    }
}

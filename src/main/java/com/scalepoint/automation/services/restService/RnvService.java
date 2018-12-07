package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
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

        String str = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>\n" +
                "<serviceTask createdDate=\"2018-11-30T14:15:09\" uniqueId=\"2cca98a6-8344-4a6d-9f18-efbf7265a9d3\"\n" +
                "\ttakenSelfRisk=\"20.05\">\n" +
                "    <invoice invoiceDate=\"2014-11-10\" invoiceNumber=\"X23heDwVoZGc\" invoiceType=\"INVOICE\" netAmount=\"60\"\n" +
                "             paymentDueDate=\"2014-11-10\" totalAmount=\"69\" vat=\"9\">\n" +
                "        <invoiceLines>\n" +
                "            <invoiceLine description=\"item1\" lineTotal=\"11.50\" quantity=\"1.00\" unitNetAmount=\"10.00\" unitPrice=\"10.00\"\n" +
                "                         unitVatAmount=\"1.50\" units=\"piece\"/>\n" +
                "            <invoiceLine description=\"item2\" lineTotal=\"57.50\" quantity=\"2.00\" unitNetAmount=\"50.00\" unitPrice=\"25.00\"\n" +
                "                         unitVatAmount=\"7.50\" units=\"piece\"/>\n" +
                "        </invoiceLines>\n" +
                "    </invoice>\n" +
                "    <serviceLines>\n" +
                "      <serviceLine uniqueId=\"08488B87-1DAD-4E0C-B677-9EAB899C6145\" claimLineId=\"10422655\" taskType=\"REPAIR\">\n" +
                "\t\t\t<category parentCategory=\"Personlig Pleje\" name=\"Medicin\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/>\n" +
                "\t\t\t<item servicePartnerNote=\"\" productMatchDescription=\"Diners Club\" customerDescription=\"\" quantity=\"2\"/>\n" +
                "            <valuations customerDemand=\"11.31\" newPrice=\"151.00\" usedPrice=\"11.30\"/>\n" +
                "\t\t</serviceLine>\n" +
                "    </serviceLines>   \n" +
                "    <servicePartner address1=\"489-499 Avebury Boulevard\" city=\"Copenhagen\" cvrNumber=\"10209685\"\n" +
                "                   email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\">\n" +
                "        <bank  bankName = \"bankName1\" accountNumber=\"12345678\" fikCreditorCode=\"fik123\" fikType=\"fik\" regNumber=\"1234\"/>\n" +
                "\t\t\n" +
                "    </servicePartner>\n" +
                "</serviceTask>";

        given().log().all()
                    .multiPart("securityToken", supplierSecurityToken)
                    //.multiPart("xmlString", new ServiceTaskImportBuilder().setDefault().build(), "application/xml")
                    .multiPart("xmlString", str)
                    .when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(201);
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

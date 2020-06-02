package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTasksExport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceTaskImportBuilder;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

public class RnvService extends BaseService {

    private String supplierSecurityToken = TestData.getServiceAgreement().getSupplierSecurityToken();

    public void sendDefaultFeedbackWithInvoice(Claim claim, ServiceTasksExport serviceTasksExport) {
        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, serviceTasksExport).buildDefaultWithInvoice();
        sendFeedback(serviceTaskImport);
    }

    public void sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal repairPrice, Claim claim, RnVMock.RnvStub rnvStub){

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithoutInvoiceWithRepairPrice(repairPrice);
        sendFeedback(serviceTaskImport);
    }

    public void sendFeedbackWithInvoiceWithRepairPrice(BigDecimal repairPrice, Claim claim, RnVMock.RnvStub rnvStub) {
        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithInvoiceWithRepairPrice(repairPrice);
        sendFeedback(serviceTaskImport);
    }

    private void sendFeedback(ServiceTaskImport serviceTaskImport){
        given().log().all()
                .multiPart("securityToken", supplierSecurityToken)
                .multiPart("xmlString", TestData.objectAsXml(serviceTaskImport))
                .when()
                .post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(201);
    }
}

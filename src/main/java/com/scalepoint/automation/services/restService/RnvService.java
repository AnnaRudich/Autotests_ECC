package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.Common.BaseService;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.serviceTaskEntity.InvoiceBuilder;
import com.scalepoint.automation.utils.data.entity.serviceTaskEntity.ServiceTask;
import com.scalepoint.automation.utils.data.entity.serviceTaskEntity.ServiceTasks;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class RnvService extends BaseService {

    private String supplierSecurityToken = "589356A5-2E39-4438-B45F-8E05C545ABD3";


    public Response pullRnVTaskData(){
        return given().log().all()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("x-www-form-urlencoded, charset=UTF-8",
                                        ContentType.TEXT)))
                .formParam("securityToken", supplierSecurityToken)
                .when().post(Configuration.getRnvPullTaskDataUrl());

    }

    public ServiceTasks deserializeTaskData() {

        return given().log().all()
                .config(RestAssuredConfig.config()
                        .encoderConfig(encoderConfig()
                                .encodeContentTypeAs("x-www-form-urlencoded, charset=UTF-8",
                                        ContentType.TEXT)))
                .formParam("securityToken", supplierSecurityToken)
                .when().post(Configuration.getRnvPullTaskDataUrl()).as(ServiceTasks.class);

        //expect().parser("application/something", Parser.XML).when().get("/message").as(Message.class);

    }


    public void sendFeedback(){
        ServiceTask serviceTask = deserializeTaskData().getServiceTasks().get(0);
        serviceTask.setInvoice(new InvoiceBuilder().setDefault().build());
        serviceTask.getInvoice().getInvoiceLines().getInvoiceLinesList().get(0);

            given().log().all().contentType("application/xml").formParam("securityToken", supplierSecurityToken)
                    .body(serviceTask).when().post(Configuration.getRnvTaskFeedbackUrl()).then().assertThat().statusCode(200);
    }

    public static void main(String[] args) throws JAXBException {
        String rq = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><serviceTasks><serviceTask damageType=\"\" replyAddress=\"152746+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"d264cfe4-9b24-44d2-b48b-89bc78e27e26\"><claim policyType=\"\" claimDate=\"2018-11-19\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"1378cac3-9b0a-42ec-a2ee-ead161f686de\" claimReferenceNumber=\"2763739\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"5327780\" selfRisk=\"0.00\"/><claimant name=\"fname274771 lname91342\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"B115FA88-BE54-4CF0-8080-5D68F8BC22D8\" claimLineId=\"10422626\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152749+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"7a36d1e5-0d8a-49f6-add3-7dded69ed18c\"><claim policyType=\"\" claimDate=\"2018-11-20\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"49daa6c8-8500-42ca-8398-4bbda3f4dec3\" claimReferenceNumber=\"2763742\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"5786873\" selfRisk=\"0.00\"/><claimant name=\"fname637379 lname382880\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"4439F9A0-0BE6-4BD4-9201-D0EC7BA8ADB3\" claimLineId=\"10422629\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152750+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"c8816610-7bde-4a3a-b3bf-f85655b8e26b\"><claim policyType=\"\" claimDate=\"2018-11-20\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"6be203b4-fb42-48c4-a225-2346f6be536b\" claimReferenceNumber=\"2763743\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"2558084\" selfRisk=\"0.00\"/><claimant name=\"fname924683 lname289427\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"9E52B239-DBD3-4F3E-A959-E6EC53A1FEDB\" claimLineId=\"10422630\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152753+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"fe77bcc4-70f7-4a4f-8753-b575223be8f2\"><claim policyType=\"\" claimDate=\"2018-11-20\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"7a304859-681a-4789-a530-d1468370351e\" claimReferenceNumber=\"2763746\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"8341770\" selfRisk=\"0.00\"/><claimant name=\"fname190872 lname35997\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"3A1F5745-DD36-4DAC-B242-11C95519C4CB\" claimLineId=\"10422633\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152745+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"2916f881-9185-4e4b-9332-56ce7d093beb\"><claim policyType=\"\" claimDate=\"2018-11-19\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"80496da8-27b7-4ff3-ac15-13dbc85b9756\" claimReferenceNumber=\"2763738\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"7291264\" selfRisk=\"0.00\"/><claimant name=\"fname462983 lname56843\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"45189F9C-5833-4783-B8E4-7BC0311AE34E\" claimLineId=\"10422625\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152742+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"103ef465-c592-498b-a31c-b961be898f7d\"><claim policyType=\"\" claimDate=\"2018-11-15\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"98eb47b9-4c14-49aa-aa68-7ca4979c5ea2\" claimReferenceNumber=\"2763734\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"3031060\" selfRisk=\"0.00\"/><claimant name=\"fname941649 lname791505\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"D120BAB4-6BC6-4A8E-B736-DFC442F3E2BB\" claimLineId=\"10422621\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152743+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"2ab725be-234e-4a26-b1b2-2cad887772d0\"><claim policyType=\"\" claimDate=\"2018-11-15\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"9a3e6063-18b9-47cb-9d7f-d8876d9d5f5f\" claimReferenceNumber=\"2763736\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"9996706\" selfRisk=\"0.00\"/><claimant name=\"fname920699 lname610988\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"2065C3F5-1AF8-405D-A217-F0435CFCFC2A\" claimLineId=\"10422623\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152752+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"1e4ef334-a8ed-4809-ad60-77940d7ef20c\"><claim policyType=\"\" claimDate=\"2018-11-20\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"a7926d0b-015a-49d1-80d2-4de137c19505\" claimReferenceNumber=\"2763745\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"2632733\" selfRisk=\"0.00\"/><claimant name=\"fname352855 lname407501\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"465B2E97-3D0E-4E28-B932-0F87BDF4379D\" claimLineId=\"10422632\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152744+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"6083b9f0-1b77-4397-8286-98aeb3d98a05\"><claim policyType=\"\" claimDate=\"2018-11-15\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"a83175a5-9c4e-445d-90f7-1a9b026d1163\" claimReferenceNumber=\"2763737\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"4886190\" selfRisk=\"0.00\"/><claimant name=\"fname437781 lname79994\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"F72AFF56-C665-48D5-80D0-8D4A90503191\" claimLineId=\"10422624\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152751+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"17f47537-6597-4015-9a86-040a7fab582c\"><claim policyType=\"\" claimDate=\"2018-11-20\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"b43ea538-aabd-489a-8eb6-8487d3257b44\" claimReferenceNumber=\"2763744\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"5786584\" selfRisk=\"0.00\"/><claimant name=\"fname323733 lname675942\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"3B0CF420-C0B9-4FCA-98E4-53F77E682BDF\" claimLineId=\"10422631\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152747+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"d4cc1f0b-efe7-40a3-af59-abf429a3ea4a\"><claim policyType=\"\" claimDate=\"2018-11-19\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"bb2ccd3f-ad4c-4389-b3cd-12ae73487b79\" claimReferenceNumber=\"2763740\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"3061695\" selfRisk=\"0.00\"/><claimant name=\"fname703351 lname257984\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"488D1C6C-5B56-486D-AC59-1E6E8EF7626B\" claimLineId=\"10422627\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152748+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"a27dea12-500c-448f-a3d9-d18b07e628bf\"><claim policyType=\"\" claimDate=\"2018-11-19\" claimHandlerEmail=\"ecc_auto@scalepoint.com\" claimHandlerFullName=\"FirstName LastName\" claimHandlerName=\"autotest-future50\" claimNumber=\"d016ebf8-79f6-4f65-9269-3f0bebfad930\" claimReferenceNumber=\"2763741\" customerNoteToClaim=\"\" insuranceCompanyId=\"50\" insuranceCompanyName=\"Future50\" policyNumber=\"605949\" selfRisk=\"0.00\"/><claimant name=\"fname372073 lname987118\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"7EF77040-C76A-4778-AB05-18B13D007E83\" claimLineId=\"10422628\" taskType=\"REPAIR\"><category name=\"Medicin\" parentCategory=\"Personlig Pleje\" uniqueId=\"AEC8A3E2-CB0A-4972-9911-D405BDFDAF7C\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"Line_1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"100.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask><serviceTask damageType=\"\" replyAddress=\"152741+dk+ecc-qa09.spcph.local%2FrepairValuation%2Fdk%2Fmailgun%2Fupload.action@${contextParameters.repairValuationMailGunDomain}\" createdDate=\"2018-11-20T13:27:41\" uniqueId=\"fe7046af-f45e-4cd8-bed9-80b4ec120aa9\"><claim policyType=\"\" claimDate=\"2018-11-13\" claimHandlerEmail=\"aru@scalepoint.com\" claimHandlerFullName=\"Julia Tsibulko\" claimHandlerName=\"jtsadmin\" claimNumber=\"jtsadmin_2018-11-13T12:47:41.365Z\" claimReferenceNumber=\"2763732\" customerNoteToClaim=\"\" insuranceCompanyId=\"7\" insuranceCompanyName=\"Scalepoint\" policyNumber=\"\" selfRisk=\"0.00\"/><claimant name=\"Dylan Morgan\"/><invoicePaidBy>Scalepoint</invoicePaidBy><noteToServicePartner></noteToServicePartner><serviceLines><serviceLine uniqueId=\"9168A462-AA7E-4C17-99FC-17C87B9C2D01\" claimLineId=\"10422619\" taskType=\"REPAIR\"><category name=\"Babyudstyr\" parentCategory=\"Børn\" uniqueId=\"A758E0B9-66E5-4229-A5BB-1D86FA1B2AC8\"/><item customerNotesToClaimLine=\"\" damageDescription=\"\" depreciation=\"0\" manufacturer=\"\" serialNumber=\"\" productMatchDescription=\"line1\" customerDescription=\"\" quantity=\"1\"/><valuations newPrice=\"150.00\"/></serviceLine></serviceLines><servicePartner serviceAgreementName=\"serviceAgreement_autotests\" address1=\"489-499 Avebury Boulevard\" address2=\"Milton Keynes, MK9 2NW\" city=\"Copenhagen\" cvrNumber=\"10209685\" email=\"ecc_auto@scalepoint.com\" name=\"Autotest-Supplier-RnV-Tests\" phone=\"+4588818001\" postalCode=\"4321\"><bank fikCreditorCode=\"\" fikType=\"\"/><location address1=\"Test address 1\" address2=\"Test address 2\" city=\"Test city\" email=\"ecc_auto@scalepoint.com\" name=\"Test shop 7\" phone=\"0800 0833113\" postalCode=\"4321\"/></servicePartner></serviceTask></serviceTasks>";
        JAXBContext.newInstance(ServiceTasks.class).createUnmarshaller().unmarshal(new StringReader(rq));
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

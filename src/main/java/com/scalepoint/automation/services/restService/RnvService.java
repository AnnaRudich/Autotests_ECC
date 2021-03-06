package com.scalepoint.automation.services.restService;


import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.stubs.RnVMock;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.Invoice;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.ServiceTaskImport;
import com.scalepoint.automation.utils.data.entity.rnv.serviceTask.dataBuilders.ServiceTaskImportBuilder;
import com.scalepoint.automation.utils.data.entity.rnv.webService.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

import static com.scalepoint.automation.services.restService.common.BasePath.*;
import static com.scalepoint.automation.utils.Configuration.getRnvWebServiceUrl;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class RnvService extends BaseService {

    private String supplierSecurityToken = TestData.getServiceAgreement().getSupplierSecurityToken();

    private Response response;

    private Claimant claimant;
    private com.scalepoint.automation.utils.data.entity.rnv.webService.Claim claim;
    private Order order;
    private Task[] tasks;

    public Response getResponse() {

        return this.response;
    }

    public RnvService sendDefaultFeedbackWithCreditNote(ServiceTaskImport serviceTaskImport, BigDecimal totalAmount){

        Invoice invoice = serviceTaskImport.getInvoice();
        invoice.setCreditNoteNumber(String.valueOf(RandomUtils.randomInt()));
        invoice.setTotalAmount(totalAmount);
        invoice.setInvoiceType(String.valueOf(RnvInvoiceType.CREDIT_NOTE));
        sendFeedback(serviceTaskImport);
        return this;
    }

    public RnvService doAssert(Consumer<RnvService.Asserts> assertFunc) {
        assertFunc.accept(new RnvService.Asserts());
        return this;
    }


    public class Asserts {
        public RnvService.Asserts assertStatusCode(int statusCode) {
            response.then().statusCode(statusCode);
            return this;
        }

        public RnvService.Asserts assertValidationError(String errorMessage) {
            response.then().assertThat().body("Response.ValidationErrors.ValidationError", containsString(errorMessage));
            return this;
        }

        public RnvService.Asserts assertCreated() {
            assertStatusCode(HttpStatus.SC_CREATED);
            return this;
        }

        public RnvService.Asserts assertCreditNoteHigherThanInvoice() {
            assertValidationError("The credit note amount is higher than the existing invoice with");
            return this;
        }

        public RnvService.Asserts assertMissingInvoiceReferenceNumber() {
            assertValidationError("Felt = invoiceNumber - feltet er obligatorisk, men er ikke udfyldt");
            return this;
        }

        public RnvService.Asserts assertTakenSelfRiskNotWithinAllowedRange() {
            assertValidationError("Felt = takenSelfRisk - v??rdi i feltet er ikke inden for tilladte interval");
            return this;
        }
    }

    public void sendFeedbackWithoutInvoiceWithRepairPrice(BigDecimal repairPrice, Claim claim, RnVMock.RnvStub rnvStub){

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithoutInvoiceWithRepairPrice(repairPrice);
        sendFeedback(serviceTaskImport);
    }

    public ServiceTaskImport sendFeedbackWithInvoiceWithRepairPrice(BigDecimal repairPrice, Claim claim, RnVMock.RnvStub rnvStub) {

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithInvoiceWithRepairPrice(repairPrice);
        sendFeedback(serviceTaskImport);
        return serviceTaskImport;
    }

    public RnvService sendFeedbackWithInvoiceWithRepairPriceAndTakenSelfRisk(BigDecimal repairPrice, BigDecimal takenSelfRisk, Claim claim, RnVMock.RnvStub rnvStub) {

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithInvoiceWithRepairPrice(repairPrice);
        serviceTaskImport
                .setTakenSelfRisk(takenSelfRisk);
        sendFeedback(serviceTaskImport);
        return this;
    }

    public void test(BigDecimal repairPrice, Claim claim, RnVMock.RnvStub rnvStub) {

        ServiceTaskImport serviceTaskImport = new ServiceTaskImportBuilder(claim, rnvStub.waitForServiceTask(claim.getClaimNumber()))
                .buildDefaultWithInvoiceWithRepairPrice(repairPrice);
        sendFeedback(serviceTaskImport);
    }

    private Response sendFeedback(ServiceTaskImport serviceTaskImport){

        return response = given().log().all()
                .multiPart("securityToken", supplierSecurityToken)
                .multiPart("xmlString", TestData.objectAsXml(serviceTaskImport))
                .when()
                .post(Configuration.getRnvTaskFeedbackUrl());
    }

    public RnvService sendToRepairAndValuation(){

        getClaimant();
        getClaim();
        getOrderStatus();
        getOrder();

        return this;
    }

    public RnvService rnvNextStep(){

        postOrderPrepareAction();
        getOrderPrepare();

        return this;
    }

    public RnvService send(){

        postOrderAction();
        getTasksStatuses();

        return this;
    }

    private RnvService getClaimant(){

        claimant = given().baseUri(getRnvWebServiceUrl())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .queryParam("orderToken", data.getOrderToken())
                .sessionId(data.getRnvSessionId())
                .when()
                .get(CLAIMANT)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Claimant.class);

        return this;
    }

    private RnvService getClaim(){

        claim = given().baseUri(getRnvWebServiceUrl())
                .contentType("application/json;charset=UTF-8")
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .queryParam("orderToken", data.getOrderToken())
                .sessionId(data.getRnvSessionId())
                .when()
                .get(CLAIM)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(com.scalepoint.automation.utils.data.entity.rnv.webService.Claim.class);

        return this;
    }

    private RnvService getOrder(){

        order = given().baseUri(getRnvWebServiceUrl())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .queryParam("orderToken", data.getOrderToken())
                .sessionId(data.getRnvSessionId())
                .when()
                .get(ORDER)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Order.class);

        return this;
    }

    private RnvService getOrderStatus(){

        given().baseUri(getRnvWebServiceUrl())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .queryParam("orderToken", data.getOrderToken())
                .sessionId(data.getRnvSessionId())
                .when()
                .get(ORDER_STATUS)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    private RnvService postOrderPrepareAction(){

        OrderPrepare orderPrepare = OrderPrepare.builder()
                .claim(claim)
                .claimant(claimant)
                .serviceLines(order.getServiceLines())
                .build();

        given().baseUri(getRnvWebServiceUrl())
                .redirects().follow(false)
                .sessionId(data.getRnvSessionId())
                .contentType(ContentType.JSON)
                .queryParam("orderToken", data.getOrderToken())
                .body(orderPrepare)
                .when()
                .post(ORDER_PREPARE_ACTION)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    private RnvService getOrderPrepare(){

        tasks = given().baseUri(getRnvWebServiceUrl())
                .redirects().follow(false)
                .sessionId(data.getRnvSessionId())
                .queryParam("orderToken", data.getOrderToken())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .when()
                .get(ORDER_PREPARE)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Task[].class);

        return this;
    }

    private RnvService postOrderAction(){

        ServiceLine serviceLine = order
                .getServiceLines()
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);

        Arrays.stream(tasks)
                .forEach(task -> {

                    task.setDamageType(Optional.ofNullable(task.getDamageType()).orElse(""));
                    task.setDamageTypeList(Optional.ofNullable(task.getDamageTypeList()).orElse(""));
                });

        OrderAction orderAction = OrderAction.builder()
                .damageTypes(Collections.singletonList(DamageType.builder()
                        .damageType(Optional.ofNullable(serviceLine.getDamageType()).orElse(""))
                        .matchId(serviceLine.getMatchId())
                        .taskTypeId(serviceLine.getTaskTypeId())
                        .build()))
                .tasks(Arrays.asList(tasks))
                .build();

        given().baseUri(getRnvWebServiceUrl())
                .redirects().follow(false)
                .contentType(ContentType.JSON)
                .sessionId(data.getRnvSessionId())
                .queryParam("orderToken", data.getOrderToken())
                .body(orderAction)
                .when()
                .post(ORDER_ACTION)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }

    public RnvService getTasksStatuses(){

        given().baseUri(getRnvWebServiceUrl())
                .redirects().follow(false)
                .sessionId(data.getRnvSessionId())
                .queryParam("orderToken", data.getOrderToken())
                .queryParam("_dc", LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZONE_OFFSET)))
                .when()
                .get(TASKS_STATUSES)
                .then()
                .statusCode(HttpStatus.SC_OK);

        return this;
    }
}

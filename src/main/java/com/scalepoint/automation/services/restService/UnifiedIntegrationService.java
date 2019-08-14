package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UnifiedIntegrationService{

    protected Logger log = LogManager.getLogger(UnifiedIntegrationService.class);

    private static final String BASE_PATH = "/api/integration/";
    Token token;

    public UnifiedIntegrationService() {
        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.PLATFORM_CASE_READ).getToken();
    }

//    public Response createCase(String country, String tenant) {
//
//        log.info(Configuration.getEnvironmentUrl());
//        return given().baseUri("https://qa05.scalepoint.com").basePath("/api/integration/").log().all()
//                .header(token.getAuthorizationHeader())
//                .contentType(ContentType.JSON)
//                .pathParam("country", country)
//                .pathParam("tenant", tenant)
//                .body("{\n" +
//                        "\"tenant\": \"future60\",\n" +
//                        "  \"company\": \"future60\",\n" +
//                        "  \"country\": \"dk\",\n" +
//                        "  \"caseType\": \"contentItemization\",\n" +
//                        "  \"caseNumber\": \"43397196-b6cd-4239-a2a3-2ff35e390f07\",\n" +
//                        "  \"integrationOptions\": {\n" +
//                        "    \"startSelfService\": false\n" +
//                        "  },\n" +
//                        "  \"policy\": {\n" +
//                        "    \"number\": \"dk1234\"\n" +
//                        "  },\n" +
//                        "  \"externalReference\": \"43397196-b6cd-4239-a2a3-2ff35e390f07\",\n" +
//                        "  \"customer\": {\n" +
//                        "    \"firstName\": \"firstName\",\n" +
//                        "    \"lastName\": \"lastName\",\n" +
//                        "    \"email\": \"bna@scalepoint.com\",\n" +
//                        "    \"mobile\": \"004521234567\",\n" +
//                        "    \"address\": {\n" +
//                        "      \"street1\": \"Ingemandsvej 27\",\n" +
//                        "      \"postalCode\": \"2000\",\n" +
//                        "      \"city\": \"Frederiksberg\"\n" +
//                        "    }\n" +
//                        "  },\n" +
//                        "  \"extraModifiers\": [{\n" +
//                        "    \"type\": \"postItemizationCompletedUrl\",\n" +
//                        "    \"value\": \"http://www.abcinsurance.com/thankyou.aspx?userid=DPMWI29us9IcUvGb\"\n" +
//                        "  }]\n" +
//                        "}")
//                .when()
//                .post("/{country}/{tenant}/v1/case")
//                .then()
//                .log()
//                .all()
//                .statusCode(200)
//                .extract()
//                .response();
//    }

    public Case getCaseEndpointByToken(String country, String tenant, String caseToken) throws IOException {

        log.info(Configuration.getEnvironmentUrl());
        String response = given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH).log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .pathParam("country", country)
                .pathParam("tenant", tenant)
                .pathParam("caseToken", caseToken)
                .when()
                .get("/data/v1/{country}/{tenant}/case/{caseToken}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response()
                .getBody()
                .print();

        return new ObjectMapper().readValue(response, Case.class);
    }

    public Response getCaseEndpointByCaseNumber(String country, String tenant, String caseType, String caseNumber) {

        log.info(Configuration.getEnvironmentUrl());
        return given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH).log().all()
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .pathParam("country", country)
                .pathParam("tenant", tenant)
                .pathParam("caseType", caseType)
                .pathParam("caseNumber", caseNumber)
                .when()
                .get("/data/v1/{country}/{tenant}/{caseType}/{caseNumber}")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .response();
    }
}
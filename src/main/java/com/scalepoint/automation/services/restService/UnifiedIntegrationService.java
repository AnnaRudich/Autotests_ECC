package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class UnifiedIntegrationService{

    protected Logger log = LogManager.getLogger(UnifiedIntegrationService.class);

    private static final String BASE_PATH = "/api/integration/";
    Token token;
    Duration duration;

    public UnifiedIntegrationService() {
        this.token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.PLATFORM_CASE_READ).getToken();
    }

    public Case getCaseEndpointByToken(String country, String tenant, String caseToken, String eventId) throws IOException {

        log.info(Configuration.getEnvironmentUrl());
        RequestSpecification requestSpecification = given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH)
                .header(token.getAuthorizationHeader())
                .header(new Header("X-REQUEST-ID", eventId))
                .contentType(ContentType.JSON)
                .pathParam("country", country)
                .pathParam("tenant", tenant)
                .pathParam("caseToken", caseToken)
                .when();

        LocalDateTime caseStart = LocalDateTime.now();
        Response response = requestSpecification
                .get("/data/v1/{country}/{tenant}/case/{caseToken}");
        LocalDateTime caseEnd = LocalDateTime.now();

        duration = Duration.between(caseStart, caseEnd);

        String body = response
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().print();

        return new ObjectMapper().readValue(body, Case.class);
    }

    public Response getCaseEndpointByCaseNumber(String country, String tenant, String caseType, String caseNumber) {

        log.info(Configuration.getEnvironmentUrl());
        return given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH)
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .pathParam("country", country)
                .pathParam("tenant", tenant)
                .pathParam("caseType", caseType)
                .pathParam("caseNumber", caseNumber)
                .when()
                .get("/data/v1/{country}/{tenant}/{caseType}/{caseNumber}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
    }

    public Duration getDuration(){
        return duration;
    }
}
package com.scalepoint.automation.services.restService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.eventsApiEntity.changed.Case;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class UnifiedIntegrationService{

    protected Logger log = LogManager.getLogger(UnifiedIntegrationService.class);

    private static final String BASE_PATH = "/api/integration/";
    Token token;
    @Getter
    Duration duration;
    @Getter
    Response response;

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
                .body(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL())
                .when()
                .get("/data/v1/{country}/{tenant}/{caseType}/{caseNumber}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().response();
    }

    public String createItemizationCaseFNOL(String locale, String tenant, ClaimRequest claimRequest){

        token = new OauthTestAccountsApi().sendRequest(OauthTestAccountsApi.Scope.CASE_INTEGRATION).getToken();

        return given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH)
                .header(token.getAuthorizationHeader())
                .contentType(ContentType.JSON)
                .pathParam("locale", locale)
                .pathParam("tenant", tenant)
                .body(claimRequest)
                .when()
                .post("/{locale}/{tenant}/v1/case")
                .then().log().all()
                .statusCode(HttpStatus.SC_OK)
                .extract().response().getBody().jsonPath().get("token");
    }

    public String createClaimFNOL(ClaimRequest claimRequest){

        response = new CreateClaimService(token)
                .addClaim(claimRequest)
                .getResponse();

        return response
                .jsonPath()
                .get("token");
    }

    public Response healthCheck(){

        return given().baseUri(Configuration.getEnvironmentUrl()).basePath(BASE_PATH)
                .log().all()
                .when()
                .get("/healthCheck")
                .then()
                .log().all()
                .extract().response();
    }

    public UnifiedIntegrationService doAssert(Consumer<UnifiedIntegrationService.Asserts> assertFunc) {
        assertFunc.accept(new UnifiedIntegrationService.Asserts());
        return this;
    }


    public class Asserts {

        final static String OPEN_TO_ANOTHER_CLAIMS_HANDLER_MESSAGE = "This claim with claim number \\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12} is currently open to another claims handler. Claim was not updated.";
        final static String OPEN_TO_ANOTHER_CLAIMS_HANDLER_PATH = "warnings[1].message";

        public UnifiedIntegrationService.Asserts assertWarningOpenToAnotherClaimHandler() {
            response.then().assertThat().body(OPEN_TO_ANOTHER_CLAIMS_HANDLER_PATH, matchesPattern(OPEN_TO_ANOTHER_CLAIMS_HANDLER_MESSAGE));
            return this;
        }
        public UnifiedIntegrationService.Asserts assertMissingWarningOpenToAnotherClaimHandler() {
            response.then().assertThat().body(OPEN_TO_ANOTHER_CLAIMS_HANDLER_PATH, isEmptyOrNullString());
            return this;
        }
    }
}
package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import static com.scalepoint.automation.services.externalapi.TestAccountsApi.Scope.PLATFORM_CASE_READ;
import static com.scalepoint.automation.services.restService.Common.BaseService.loginAndOpenClaimWithItem;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.REPLACEMENT;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class CaseSettlementDataApiTests extends BaseApiTest {

    private String status = "settlementDetails.status";
    private String settlementType = "settlementDetails.settlementType";

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionByTokenForClosedExternalClaim(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, CLOSE_EXTERNAL);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is("CLOSED_EXTERNAL"))
                .body(settlementType, is("SETTLED_EXTERNALLY"))
                .body(matchesJsonSchemaInClasspath("schema/CaseDataSchema.json"));
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionByTokenForClosedWithMailClaim(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is("SETTLED"))
                .body(settlementType, is("SETTLED_WITH_EMAIL"))
                .body(matchesJsonSchemaInClasspath("schema/CaseDataSchema.json"));
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionByTokenForReplacement(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, REPLACEMENT);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is("SETTLED"))
                .body(settlementType, is("SETTLED_FOR_REPLACEMENT"))
                .body(matchesJsonSchemaInClasspath("schema/CaseDataSchema.json"));
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionForInvalidRevisionShouldReturn400() {

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData("BBBBB594-DDD9-CCBB-B3A8-783BA9B7CAF6")
                .getResponse()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionForInvalidTenantShouldReturn400(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, CLOSE_EXTERNAL);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), "BadScalepoint")
                .getResponse()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionByTokenForCanceledClaim(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, SettlementClaimService.CloseCaseReason.CLOSE_WITH_MAIL)
                .cancel(claimRequest);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumberAndClaimStatusCancelled(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is("CANCELLED"))
                .body(settlementType, is("CANCELLED"))
                .body(matchesJsonSchemaInClasspath("schema/CaseDataSchema.json"));

    }
}

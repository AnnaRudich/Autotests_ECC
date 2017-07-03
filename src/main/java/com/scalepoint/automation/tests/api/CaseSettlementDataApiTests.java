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

import static com.scalepoint.automation.services.restService.Common.BaseService.loginAndOpenClaimWithItem;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class CaseSettlementDataApiTests extends BaseApiTest {

    private String status = "settlementDetails.status";
    private String settlementType = "settlementDetails.settlementType";

    @Test(dataProvider = "testDataProvider", dataProviderClass = BaseTest.class)
    public void getCaseRevisionByTokenForClosedExternalClaim(User user, ClaimRequest claimRequest, InsertSettlementItem item) {
        loginAndOpenClaimWithItem(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, SettlementClaimService.CloseCaseReason.CLOSE_EXTERNAL);

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(TestAccountsApi.Scope.PLATFORM_CASE_READ).getToken())
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

        new CaseSettlementDataService(new TestAccountsApi().sendRequest(TestAccountsApi.Scope.PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is("SETTLED"))
                .body(settlementType, is("SETTLED_WITH_EMAIL"))
                .body(matchesJsonSchemaInClasspath("schema/CaseDataSchema.json"));
    }

}

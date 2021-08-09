package com.scalepoint.automation.tests.api.unifiedpayments.v1;

import com.scalepoint.automation.services.externalapi.OauthTestAccountsApi;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.services.restService.SettlementClaimService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.InsertSettlementItem;
import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.scalepoint.automation.services.externalapi.OauthTestAccountsApi.Scope.PLATFORM_CASE_READ;
import static com.scalepoint.automation.services.restService.SettlementClaimService.CloseCaseReason.*;
import static com.scalepoint.automation.services.restService.common.BaseService.loginAndOpenClaimWithItems;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class CaseSettlementDataApiTests extends BaseApiTest {

    private static final String GET_CASE_REVISION_BY_TOKEN_DATA_PROVIDER = "getCaseRevisionByTokenDataProvider";
    private static final String GET_CASE_REVISION_FOR_INVALID_REVISION_DATA_PROVIDER = "getCaseRevisionForInvalidRevisionDataProvider";
    private static final String GET_CASE_REVISION_FOR_INVALID_TENANT_DATA_PROVIDER = "getCaseRevisionForInvalidTenantDataProvider";
    private static final String GET_CASE_REVISION_BY_TOKEN_FOR_CANCELED_CLAIM_DATA_PROVIDER = "getCaseRevisionByTokenForCanceledClaimDataProvider";

    private static final String SCHEMA = "schema/CaseDataSchema.json";

    private String status = "settlementDetails.status";
    private String settlementType = "settlementDetails.settlementType";

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V1,
            TestGroups.CASE_SETTLEMENT_DATA},
            dataProvider = GET_CASE_REVISION_BY_TOKEN_DATA_PROVIDER)
    public void getCaseRevisionByTokenTest(User user, ClaimRequest claimRequest, InsertSettlementItem item, Body body) {

        closeCase(user, claimRequest, item, body);
        getSettlementData(claimRequest, body);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V1,
            TestGroups.CASE_SETTLEMENT_DATA},
            dataProvider = GET_CASE_REVISION_FOR_INVALID_REVISION_DATA_PROVIDER)
    public void getCaseRevisionForInvalidRevisionTest(String settlementData) {

        new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(settlementData)
                .getResponse()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V1,
            TestGroups.CASE_SETTLEMENT_DATA},
            dataProvider = GET_CASE_REVISION_FOR_INVALID_TENANT_DATA_PROVIDER)
    public void getCaseRevisionForInvalidTenantTest(User user, ClaimRequest claimRequest, InsertSettlementItem item,
                                                    Body body, String tenant) {

        closeCase(user, claimRequest, item, body);

        new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), tenant)
                .getResponse()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(groups = {TestGroups.UNIFIEDPAYMENTS,
            TestGroups.BACKEND,
            TestGroups.V1,
            TestGroups.CASE_SETTLEMENT_DATA},
            dataProvider = GET_CASE_REVISION_BY_TOKEN_FOR_CANCELED_CLAIM_DATA_PROVIDER)
    public void getCaseRevisionByTokenForCanceledClaim(User user, ClaimRequest claimRequest, InsertSettlementItem item,
                                                       Body body) {

        closeCase(user, claimRequest, item, body)
                .cancel(claimRequest);

        new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumberAndClaimStatusCancelled(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is(body.getStatus()))
                .body(settlementType, is(body.getSettlementType()))
                .body(matchesJsonSchemaInClasspath(body.getSchema()));
    }

    private SettlementClaimService closeCase(User user, ClaimRequest claimRequest, InsertSettlementItem item, Body body){

        return loginAndOpenClaimWithItems(user, claimRequest, item)
                .closeCase()
                .close(claimRequest, body.getCloseCaseReason());
    }

    private void getSettlementData(ClaimRequest claimRequest, Body body){

        new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()))
                .getResponse()
                .statusCode(HttpStatus.SC_OK)
                .body(status, is(body.getStatus()))
                .body(settlementType, is(body.getSettlementType()))
                .body(matchesJsonSchemaInClasspath(body.getSchema()));
    }

    @DataProvider(name = GET_CASE_REVISION_BY_TOKEN_DATA_PROVIDER)
    public static Object[][] getCaseRevisionByTokenDataProvider(Method method) {

        Body closedExternalClaim = Body.builder()
                .closeCaseReason(CLOSE_EXTERNAL)
                .status(Status.CLOSED_EXTERNAL)
                .settlementType(SettlementType.SETTLED_EXTERNALLY)
                .schema(SCHEMA)
                .build();

        Body closedWithMailClaim = Body.builder()
                .closeCaseReason(CLOSE_WITH_MAIL)
                .status(Status.SETTLED)
                .settlementType(SettlementType.SETTLED_WITH_EMAIL)
                .schema(SCHEMA)
                .build();

        Body closedWithoutMailClaim = Body.builder()
                .closeCaseReason(CLOSE_WITHOUT_MAIL)
                .status(Status.SETTLED)
                .settlementType(SettlementType.SETTLED_WITHOUT_EMAIL)
                .schema(SCHEMA)
                .build();
//TODO: fix
        Body replacement = Body.builder()
                .closeCaseReason(REPLACEMENT)
                .status(Status.SETTLED)
                .settlementType(SettlementType.SETTLED_FOR_REPLACEMENT)
                .schema(SCHEMA)
                .build();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, closedExternalClaim).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, closedWithMailClaim).toArray(),
                TestDataActions.getTestDataWithExternalParameters(method, closedWithoutMailClaim).toArray()
//                TestDataActions.getTestDataWithExternalParameters(method, replacement).toArray()
        };
    }

    @DataProvider(name = GET_CASE_REVISION_FOR_INVALID_REVISION_DATA_PROVIDER)
    public static Object[][] getCaseRevisionForInvalidRevisionDataProvider(Method method) {

        return new Object[][]{

                new Object[]{"BBBBB594-DDD9-CCBB-B3A8-783BA9B7CAF6"}
        };
    }

    @DataProvider(name = GET_CASE_REVISION_FOR_INVALID_TENANT_DATA_PROVIDER)
    public static Object[][] getCaseRevisionForInvalidTenantDataProvider(Method method) {

        Body body = Body.builder()
                .closeCaseReason(CLOSE_EXTERNAL)
                .build();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, body, "BadScalepoint").toArray()
        };
    }

    @DataProvider(name = GET_CASE_REVISION_BY_TOKEN_FOR_CANCELED_CLAIM_DATA_PROVIDER)
    public static Object[][] getCaseRevisionByTokenForCanceledClaimDataProvider(Method method) {

        Body closedExternalClaim = Body.builder()
                .closeCaseReason(CLOSE_EXTERNAL)
                .status(Status.CANCELLED)
                .settlementType(SettlementType.CANCELLED)
                .schema(SCHEMA)
                .build();

        return new Object[][]{

                TestDataActions.getTestDataWithExternalParameters(method, closedExternalClaim).toArray()
        };
    }

    class Status{

        public static final String CANCELLED = "CANCELLED";
        public static final String SETTLED = "SETTLED";
        public static final String CLOSED_EXTERNAL = "CLOSED_EXTERNAL";
    }

    class SettlementType{

        public static final String CANCELLED = "CANCELLED";
        public static final String SETTLED_FOR_REPLACEMENT = "SETTLED_FOR_REPLACEMENT";
        public static final String SETTLED_WITHOUT_EMAIL = "SETTLED_WITHOUT_EMAIL";
        public static final String SETTLED_WITH_EMAIL = "SETTLED_WITH_EMAIL";
        public static final String SETTLED_EXTERNALLY = "SETTLED_EXTERNALLY";
    }

    @Data
    @Builder
    static class Body{

        private String status;
        private String settlementType;
        private String schema;
        private SettlementClaimService.CloseCaseReason closeCaseReason;
    }
}

package com.scalepoint.automation.tests.api;

import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.tests.AbstractBaseTest;
import com.scalepoint.automation.utils.data.request.ClaimRequest;

import static com.scalepoint.automation.services.externalapi.TestAccountsApi.Scope.PLATFORM_CASE_READ;

public class BaseApiTest extends AbstractBaseTest {

    protected CaseSettlementDataService getSettlementData(ClaimRequest claimRequest){
        return new CaseSettlementDataService(new TestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }
}

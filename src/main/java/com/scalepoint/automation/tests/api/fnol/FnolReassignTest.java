package com.scalepoint.automation.tests.api.fnol;

import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.services.restService.common.BaseService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.annotations.UserAttributes;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.request.SelfServiceRequest;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static com.scalepoint.automation.services.usersmanagement.CompanyCode.TOPDANMARK;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FnolReassignTest extends BaseApiTest {

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = "topdanmarkDataProvider", dataProviderClass = BaseTest.class, description = "FNOL")
    public void reassignFnolWithExistingLinesFTOffTest(@UserAttributes(company = TOPDANMARK) User user, SelfServiceRequest selfServiceRequest){

        reassignFnolWithExistingLines(user, selfServiceRequest)
                .doAssert(service -> service.assertWarningOpenToAnotherClaimHandler());
    }

    @Test(enabled = false,
            groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = "topdanmarkDataProvider", dataProviderClass = BaseTest.class, description = "FNOL")
    @RequiredSetting(type = FTSetting.CAN_FNOL_CASE_BE_REASSIGNED)
    public void reassignFnolWithExistingLinesFTOnTest(@UserAttributes(company = TOPDANMARK) User user, SelfServiceRequest selfServiceRequest){

        reassignFnolWithExistingLines(user, selfServiceRequest)
                .doAssert(service -> service.assertMissingWarningOpenToAnotherClaimHandler());
    }

    public UnifiedIntegrationService reassignFnolWithExistingLines(User user, SelfServiceRequest selfServiceRequest){

        ClaimRequest itemizationRequest = TestData.getClaimRequestItemizationCaseTopdanmarkFNOL();
        ClaimRequest createClaimRequest = TestData.getClaimRequestCreateClaimTopdanmarkFNOL();

        itemizationRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String token = unifiedIntegrationService.createItemizationCaseFNOL(createClaimRequest.getCountry(), createClaimRequest.getTenant(), itemizationRequest);

        createClaimRequest.setItemizationCaseReference(token);
        createClaimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));

        unifiedIntegrationService.createClaimFNOL(createClaimRequest);
        unifiedIntegrationService.doAssert(service -> service.assertMissingWarningOpenToAnotherClaimHandler());

        selfServiceRequest.setClaimsNo(createClaimRequest.getCaseNumber());

        BaseService
                .loginAndOpenClaim(user, createClaimRequest)
                .requestSelfService(selfServiceRequest)
                .loginToSS(selfServiceRequest.getPassword());

        unifiedIntegrationService.createClaimFNOL(createClaimRequest);

        return unifiedIntegrationService;
    }
}

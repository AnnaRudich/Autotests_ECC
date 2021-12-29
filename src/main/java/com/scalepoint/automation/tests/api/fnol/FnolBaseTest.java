package com.scalepoint.automation.tests.api.fnol;

import com.scalepoint.automation.services.restService.SelfServiceService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.tests.api.BaseApiTest;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import lombok.Data;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.Arrays;
import java.util.List;

public class FnolBaseTest extends BaseApiTest {

    protected static final String ACCESS_TOKEN = "Access-Token";
    protected static final String ORIGIN = "Origin";
    protected static final String APPLICATION_JSON = "application/json";
    protected String authOrigin;
    protected String nonAuthOrigin;

    @BeforeClass(alwaysRun = true)
    public void setOrigins(){

        authOrigin = Configuration.getTestWidgetProtocol() + Configuration.getDomainTestWidget();
        nonAuthOrigin = Configuration.getTestWidgetProtocol() + Configuration.getWiremockHost();
    }

    @BeforeMethod(alwaysRun = true)
    protected void getSSToken(Object[] objects) {

        List parameters = Arrays.asList(objects);

        User user = getObjectByClass(parameters, User.class).get(0);
        FnolCorsTestData fnolCorsTestData = getObjectBySuperClass(parameters, FnolCorsTestData.class).get(0);

        String tenant = user.getCompanyName().toLowerCase();

        ClaimRequest itemizationRequest = setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();

        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        String ssoToken = unifiedIntegrationService.getSSOToken(itemizationRequest.getCountry());

        String ssToken = new SelfServiceService().getCaseWidget(token, ssoToken).getResponse().header(ACCESS_TOKEN);

        fnolCorsTestData.setSsToken(ssToken);
    }

    protected ClaimRequest setTenantAndCompanyCode(ClaimRequest claimRequest, String tenant){

        claimRequest.setCompany(tenant);
        claimRequest.setTenant(tenant);

        return claimRequest;
    }

    @Data
    static class FnolCorsTestData{

        String ssToken;
    }
}

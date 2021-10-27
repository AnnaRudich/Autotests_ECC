package com.scalepoint.automation.tests.widget;

import com.scalepoint.automation.pageobjects.pages.MailsPage;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.selfService2.SelfService2Page;
import com.scalepoint.automation.pageobjects.pages.testWidget.TestWidgetPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.Constants;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.utils.Constants.JANUARY;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

@RequiredSetting(type = FTSetting.USE_SELF_SERVICE2)
@RequiredSetting(type = FTSetting.INCLUDE_NEW_PRICE_COLUMN_IN_SELF_SERVICE)
@RequiredSetting(type = FTSetting.INCLUDE_USED_NEW_COLUMN_IN_SELF_SERVICE, enabled = false)
@RequiredSetting(type = FTSetting.INCLUDE_CUSTOMER_DEMAND_COLUMN_IN_SELF_SERVICE, enabled = false)
public class WidgetTest extends BaseTest {

    private String descriptionWidget = "testWidget";
    private String descriptionSelfService = "testSelfService";
    private String server;

    @BeforeClass
    public void setUp() {

        Matcher matcher = Pattern
                .compile("(\\w+://)(\\w+)\\..+")
                .matcher(Configuration.getEnvironmentUrl());
        matcher.find();
        server = matcher.group(2);
    }

    @RequiredSetting(type = FTSetting.ADD_ACCEPTANCE_TEXT_AND_CHECKBOX_WIDGET_ONLY, enabled = false)
    @RequiredSetting(type = FTSetting.ADD_ACCEPTANCE_TEXT_AND_CHECKBOX_SELF_SERVICE2)
    @Test(groups = {TestGroups.WIDGET, TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "Verify flow when acceptance text and checkbox for widget is disabled")
    public void acceptanceCheckboxDisabledTest(User user, ClaimItem claimItem, Claim claim) {

        String tenant = user.getCompanyName().toLowerCase();

        ClaimRequest itemizationRequest = setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant);
        ClaimRequest createClaimRequest = setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        createItemWidget(itemizationRequest, claimItem, token)
                .sendResponseToEcc();

        createAndVerifyClaimWidget(user, unifiedIntegrationService, createClaimRequest, token);

        createItemSelfService(user, claim, claimItem)
                .doAssert(selfService2 -> selfService2.assertSendButtonDisabled())
                .acceptStatement()
                .doAssert(selfService2 -> selfService2.assertSendButtonEnabled())
                .sendResponseToEcc();

        createAndVerifyClaimSelfService(user);
    }

    @RequiredSetting(type = FTSetting.ADD_ACCEPTANCE_TEXT_AND_CHECKBOX_WIDGET_ONLY)
    @RequiredSetting(type = FTSetting.ADD_ACCEPTANCE_TEXT_AND_CHECKBOX_SELF_SERVICE2, enabled = false)
    @Test(groups = {TestGroups.WIDGET, TestGroups.SELF_SERVICE2}, dataProvider = "testDataProvider",
            description = "Verify flow when acceptance text and checkbox for widget is enabled")
    public void acceptanceCheckboxEnabledTest(User user, ClaimItem claimItem, Claim claim) {

        String tenant = user.getCompanyName().toLowerCase();

        ClaimRequest itemizationRequest = setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant);
        ClaimRequest createClaimRequest = setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        createItemWidget(itemizationRequest, claimItem, token)
                .doAssert(selfService2Page -> selfService2Page.assertSendButtonDisabled())
                .acceptStatement()
                .doAssert(selfService2Page -> selfService2Page.assertSendButtonEnabled())
                .sendResponseToEcc();

        createAndVerifyClaimWidget(user, unifiedIntegrationService, createClaimRequest, token);

        createItemSelfService(user, claim, claimItem)
                .sendResponseToEcc();

        createAndVerifyClaimSelfService(user);
    }

    @Test(groups = {TestGroups.WIDGET, TestGroups.SELF_SERVICE2}, dataProvider = TEST_DATA_PROVIDER,
            description = "Verify flow when request is sent from an unauthenticated domain")
    public void corsNonAuthDomainTest(User user) {

        String tenant = user.getCompanyName().toLowerCase();

        ClaimRequest itemizationRequest = setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        openGenerateWidgetPageNonAuth()
                .setCountry(itemizationRequest.getCountry())
                .setServer(server)
                .setCaseToken(token)
                .generateWidget()
                .doAssert(ss2Page -> ss2Page.assertAlertIsDisplayed());
    }

    private TestWidgetPage createItemWidget(ClaimRequest itemizationRequest, ClaimItem claimItem, String token){

        return openGenerateWidgetPage()
                .setCountry(itemizationRequest.getCountry())
                .setServer(server)
                .setCaseToken(token)
                .generateWidget()
                .addDescriptionWithOutSuggestions(descriptionWidget)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .addNewPrice(3000.00)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem();
    }

    private SelfService2Page createItemSelfService(User user, Claim claim, ClaimItem claimItem){

        return loginAndCreateClaim(user, claim)
                .requestSelfService(claim, Constants.DEFAULT_PASSWORD)
                .toMailsPage(mailserviceStub)
                .viewMail(MailsPage.MailType.SELFSERVICE_CUSTOMER_WELCOME)
                .findSelfServiceNewLinkAndOpenIt()
                .login(Constants.DEFAULT_PASSWORD)
                .addDescriptionWithOutSuggestions(descriptionSelfService)
                .selectPurchaseYear(String.valueOf(Year.now().getValue()))
                .selectPurchaseMonth(JANUARY)
                .addNewPrice(3000.00)
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem();
    }

    private SettlementPage createAndVerifyClaimWidget(User user, UnifiedIntegrationService unifiedIntegrationService, ClaimRequest claimRequest, String token){

        claimRequest.setItemizationCaseReference(token);
        claimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));

        token = unifiedIntegrationService.createClaimFNOL(claimRequest, databaseApi);

        return loginAndOpenUnifiedIntegrationClaimByToken(user, token, SettlementPage.class)
                .doAssert(settlementPage -> settlementPage.assertItemIsPresent(descriptionWidget));
    }

    private SettlementPage createAndVerifyClaimSelfService(User user){

        return login(user)
                .openActiveRecentClaim()
                .doAssert(settlementPage -> settlementPage.assertItemIsPresent(descriptionSelfService));

    }

    private ClaimRequest setTenantAndCompanyCode(ClaimRequest claimRequest, String tenant){

        claimRequest.setCompany(tenant);
        claimRequest.setTenant(tenant);

        return claimRequest;
    }
}

package com.scalepoint.automation.tests.api.fnol;

import com.scalepoint.automation.pageobjects.pages.CustomerDetailsPage2;
import com.scalepoint.automation.pageobjects.pages.testWidget.TestWidgetPage;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.testGroups.TestGroups;
import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Configuration;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.input.ClaimItem;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import lombok.Builder;
import lombok.Data;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

public class FnolReassignTest extends BaseTest {

    private String server;

    private final static String REASSIGN_FNOL_DATA_PROVIDER = "reassignFnolDataProvider";
    private final static String REASSIGN_FNOL_AUTO_CLOSE_DATA_PROVIDER = "reassignFnolAutoCloseDataProvider";

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        Matcher matcher = Pattern
                .compile("(\\w+://)(\\w+)\\..+")
                .matcher(Configuration.getEnvironmentUrl());
        matcher.find();
        server = matcher.group(2);
    }

    @BeforeMethod(alwaysRun = true)
    public void test(Object[] objects) {

        List parameters = Arrays.asList(objects);

        ClaimItem claimItem = getLisOfObjectByClass(parameters, ClaimItem.class).get(0);
        FnolReassignTestData fnolReassignTestData = getLisOfObjectByClass(parameters, FnolReassignTestData.class).get(0);
        List<ClaimRequest> claimRequests = getLisOfObjectByClass(parameters, ClaimRequest.class);
        ClaimRequest itemizationRequest = claimRequests.get(0);
        ClaimRequest firstCreateClaimRequest = claimRequests.get(1);
        ClaimRequest secondCreateClaimRequest = claimRequests.get(2);

        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();

        String token = unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), itemizationRequest);

        TestWidgetPage testWidgetPage = openGenerateWidgetPage()
                .setCountry(itemizationRequest.getCountry())
                .setServer(server)
                .setCaseToken(token)
                .generateWidget();

        createItemWidget(testWidgetPage, claimItem, fnolReassignTestData)
                .sendResponseToEcc();

        firstCreateClaimRequest.setItemizationCaseReference(token);
        firstCreateClaimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        secondCreateClaimRequest.setItemizationCaseReference(token);
        secondCreateClaimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));

        unifiedIntegrationService
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), firstCreateClaimRequest);
    }

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = REASSIGN_FNOL_DATA_PROVIDER)
    public void reassignFnolWithExistingLinesFTOffTest(User user, ClaimItem claimItem, ClaimRequest itemizationRequest,
                                                       ClaimRequest firstCreateClaimRequest, ClaimRequest secondCreateClaimRequest,
                                                       FnolReassignTestData fnolReassignTestData) {

        new UnifiedIntegrationService()
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), secondCreateClaimRequest);

        CustomerDetailsPage2 customerDetailsPage2 = loginFlow.login(user)
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(firstCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(firstCreateClaimRequest.getCaseNumber()));
        customerDetailsPage2
                .getLossItemInDraftByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));

        customerDetailsPage2
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(secondCreateClaimRequest.getCaseNumber())
                        .assertThatDraftIsEmpty());
    }

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = REASSIGN_FNOL_DATA_PROVIDER, description = "FNOL")
    @RequiredSetting(type = FTSetting.CAN_FNOL_CASE_BE_REASSIGNED)
    public void reassignOpenFnolTest(User user, ClaimItem claimItem, ClaimRequest itemizationRequest,
                                     ClaimRequest firstCreateClaimRequest, ClaimRequest secondCreateClaimRequest,
                                     FnolReassignTestData fnolReassignTestData) {

        new UnifiedIntegrationService()
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), secondCreateClaimRequest);

        CustomerDetailsPage2 customerDetailsPage2 = loginFlow.login(user)
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(firstCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(firstCreateClaimRequest.getCaseNumber()));
        customerDetailsPage2
                .getLossItemInDraftByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));

        customerDetailsPage2
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .getLossItemInDraftByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));
    }

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = REASSIGN_FNOL_DATA_PROVIDER, description = "FNOL")
    @RequiredSetting(type = FTSetting.CAN_FNOL_CASE_BE_REASSIGNED)
    public void reassignCanceledFnolTest(User user, ClaimItem claimItem, ClaimRequest itemizationRequest,
                                         ClaimRequest firstCreateClaimRequest, ClaimRequest secondCreateClaimRequest,
                                         FnolReassignTestData fnolReassignTestData) {

        CustomerDetailsPage2 customerDetailsPage2 = loginFlow.login(user)
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(firstCreateClaimRequest.getCaseNumber()))
                .cancelClaim();

        new UnifiedIntegrationService()
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), secondCreateClaimRequest);

        customerDetailsPage2
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(firstCreateClaimRequest.getCaseNumber()));
        customerDetailsPage2
                .getLossItemInDraftByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));

        customerDetailsPage2
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .getLossItemInDraftByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));
    }

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = REASSIGN_FNOL_AUTO_CLOSE_DATA_PROVIDER, description = "FNOL")
    @RequiredSetting(type = FTSetting.CAN_FNOL_CASE_BE_REASSIGNED)
    public void reassignReopenedFnolTest(User user, ClaimItem claimItem, ClaimRequest itemizationRequest,
                                         ClaimRequest firstCreateClaimRequest, ClaimRequest secondCreateClaimRequest,
                                         FnolReassignTestData fnolReassignTestData) {

        CustomerDetailsPage2 customerDetailsPage2 = loginFlow.login(user)
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(firstCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(firstCreateClaimRequest.getCaseNumber()));
        customerDetailsPage2
                .getLossItemInClaimsCalculationByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));

        customerDetailsPage2
                .reopenClaim();

        new UnifiedIntegrationService()
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), secondCreateClaimRequest);

        customerDetailsPage2
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage
                        .assertClaimNumber(secondCreateClaimRequest.getCaseNumber())
                        .assertThatDraftIsEmpty());
    }

    @Test(groups = {TestGroups.UNI,
            TestGroups.BACKEND,
            TestGroups.FNOL,
            TestGroups.FNOL_REASSIGNE},
            dataProvider = REASSIGN_FNOL_AUTO_CLOSE_DATA_PROVIDER, description = "FNOL")
    @RequiredSetting(type = FTSetting.CAN_FNOL_CASE_BE_REASSIGNED)
    public void reassignSettledFnolTest(User user, ClaimItem claimItem, ClaimRequest itemizationRequest,
                                        ClaimRequest firstCreateClaimRequest, ClaimRequest secondCreateClaimRequest,
                                        FnolReassignTestData fnolReassignTestData) {

        CustomerDetailsPage2 customerDetailsPage2 = loginFlow.login(user)
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(firstCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage.assertClaimNumber(firstCreateClaimRequest.getCaseNumber()));
        customerDetailsPage2
                .getLossItemInClaimsCalculationByIndex(0)
                .doAssert(lossItem -> lossItem.assertProduct(fnolReassignTestData.getDescription()));

        new UnifiedIntegrationService()
                .createItemizationCaseFNOL(itemizationRequest.getCountry(), itemizationRequest.getTenant(), secondCreateClaimRequest);

        customerDetailsPage2
                .to(CustomerDetailsPage2.class, databaseApi.getUserIdByClaimNumber(secondCreateClaimRequest.getCaseNumber()))
                .doAssert(customerDetailsPage -> customerDetailsPage
                        .assertClaimNumber(secondCreateClaimRequest.getCaseNumber())
                        .assertThatDraftIsEmpty());
    }

    private TestWidgetPage createItemWidget(TestWidgetPage testWidgetPage, ClaimItem claimItem, FnolReassignTestData fnolReassignTestData) {

        return testWidgetPage
                .addDescriptionWithOutSuggestions(fnolReassignTestData.description)
                .selectPurchaseYear(fnolReassignTestData.getPurchaseYear())
                .selectPurchaseMonth(fnolReassignTestData.getPurchaseMonth())
                .addNewPrice(fnolReassignTestData.getNewPrice())
                .selectCategory(claimItem.getCategoryMobilePhones())
                .saveItem();
    }

    @DataProvider(name = REASSIGN_FNOL_DATA_PROVIDER)
    public static Object[][] reassignFnolDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        List<ClaimRequest> claimRequests = getLisOfObjectByClass(parameters, ClaimRequest.class);

        parameters.removeAll(claimRequests);

        String tenant = user.getCompanyName().toLowerCase();

        claimRequests.set(0, setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant, false));
        claimRequests.set(1, setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant, false));
        claimRequests.set(2, setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant, false));

        parameters.addAll(claimRequests);
        parameters.add(buildFnolReassignTestData());

        return new Object[][]{

                parameters.toArray()
        };
    }

    @DataProvider(name = REASSIGN_FNOL_AUTO_CLOSE_DATA_PROVIDER)
    public static Object[][] reassignFnolAutoCloseDataProvider(Method method) {

        List parameters = TestDataActions.getTestDataParameters(method);

        User user = getLisOfObjectByClass(parameters, User.class).get(0);
        List<ClaimRequest> claimRequests = getLisOfObjectByClass(parameters, ClaimRequest.class);

        parameters.removeAll(claimRequests);

        String tenant = user.getCompanyName().toLowerCase();

        claimRequests.set(0, setTenantAndCompanyCode(TestData.getClaimRequestItemizationCaseTopdanmarkFNOL(), tenant, true));
        claimRequests.set(1, setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant, true));
        claimRequests.set(2, setTenantAndCompanyCode(TestData.getClaimRequestCreateClaimTopdanmarkFNOL(), tenant, true));

        parameters.addAll(claimRequests);
        parameters.add(buildFnolReassignTestData());

        return new Object[][]{

                parameters.toArray()
        };
    }

    static protected ClaimRequest setTenantAndCompanyCode(ClaimRequest claimRequest, String tenant, boolean allowAutoClose) {

        claimRequest.setCompany(tenant);
        claimRequest.setTenant(tenant);
        claimRequest.setAllowAutoClose(allowAutoClose);

        return claimRequest;
    }

    private static FnolReassignTestData buildFnolReassignTestData() {

        return FnolReassignTestData.builder()
                .description("testItemWidget1")
                .purchaseYear(String.valueOf(Year.now().getValue()))
                .purchaseMonth(DEFAULT_MONTH)
                .newPrice(3000.00)
                .build();
    }

    @Data
    @Builder
    static class FnolReassignTestData {

        private String description;
        private String purchaseYear;
        private String purchaseMonth;
        private double newPrice;
    }
}
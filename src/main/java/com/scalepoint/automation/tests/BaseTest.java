package com.scalepoint.automation.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.services.externalapi.*;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.restService.Common.ServiceData;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.restService.EccIntegrationService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.services.restService.UnifiedIntegrationService;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.InsuranceCompany;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.automation.utils.driver.DriverHelper;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import com.scalepoint.automation.utils.listeners.OrderRandomizer;
import com.scalepoint.automation.utils.listeners.SuiteListener;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;

@SpringBootTest(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Listeners({InvokedMethodListener.class, SuiteListener.class, OrderRandomizer.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected Logger log = LogManager.getLogger(BaseTest.class);

    @Autowired
    protected DatabaseApi databaseApi;

    @Autowired
    protected EventDatabaseApi eventDatabaseApi;

    @Autowired
    protected MongoDbApi mongoDbApi;

    @Autowired
    protected WireMock wireMock;

    @Value("${driver.type}")
    protected String browserMode;

    @Value("${subscription.claimline_changed.id}")
    protected String claimLineChangedSubscriptionId;

    @Value("${subscription.fraud_status.id}")
    protected String fraudStatusSubscriptionId;

    private DriverType driverType = null;

    @BeforeMethod
    public void baseInit(Method method, ITestContext context) throws Exception {
        Thread.currentThread().setName("Thread " + method.getName());
        ThreadContext.put("sessionid", method.getName());
        log.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());

        driverType = new DriverHelper().getDriverType(method, browserMode);

        WebDriver driver = DriversFactory.getDriver(driverType, method);

        Browser.init(driver, driverType);
        Window.init(driver);
        WebDriverRunner.setWebDriver(driver);
        ServiceData.init(databaseApi);

        JavascriptHelper.initializeCommonFunctions();

        Configuration.savePageSource = false;

        log.info("Initialization completed for : {}", method.getName());
    }

    @AfterMethod
    public void cleanup(Method method, ITestResult iTestResult) {
        log.info("Clean up after: {}", method.toString());
        Cookie cookie = new Cookie("zaleniumTestPassed", String.valueOf(iTestResult.isSuccess()));
        try {
            Objects.requireNonNull(Browser.driver()).manage().addCookie(cookie);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Browser.quit();
        Window.cleanUp();
        CurrentUser.cleanUp();
        Page.PagesCache.cleanUp();
        ThreadContext.clearMap();
        log.info("Clean up completed after: {} ", method.getName());
    }

    @DataProvider(name = "testDataProvider")
    public static Object[][] provide(Method method) {
        Thread.currentThread().setName("Thread " + method.getName());
        Object[][] params = new Object[1][];
        try {
            params[0] = TestDataActions.getTestDataParameters(method).toArray();
        } catch (Exception ex) {
            LogManager.getLogger(BaseTest.class).error(ex);
        }
        return params;
    }

    @DataProvider(name = "topdanmarkDataProvider")
    public static Object[][] topdanmarkDataProvider(Method method) {

        Object[][] testDataProvider = provide(method);

        for (int i = 0; i < testDataProvider[0].length; i++) {
            if (testDataProvider[0][i].getClass().equals(ClaimRequest.class)) {

                testDataProvider[0][i] = TestData.getClaimRequestFraudAlert();
            }
        }

        return testDataProvider;
    }

    protected <T extends Page> T updateFT(User user, Class<T> returnPageClass, FtOperation... operations) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);
        return functionalTemplatesApi.updateTemplate(user.getFtId(), returnPageClass, operations);
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim, String policyType) {
        Page.to(LoginPage.class);

        ClaimApi claimApi = new ClaimApi(user);
        claimApi.createClaim(claim, policyType);

        return redirectToSettlementPage(user);
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        return loginAndCreateClaim(user, claim, null);
    }

    protected String createCwaClaimAndGetClaimToken(ClaimRequest claimRequest) {
        Token token = new OauthTestAccountsApi().sendRequest().getToken();
        return new CreateClaimService(token).addClaim(claimRequest).getResponse().jsonPath().get("token");
    }

    protected CreateClaimService createCwaClaim(ClaimRequest claimRequest) {
        Token token = new OauthTestAccountsApi().sendRequest().getToken();
        return new CreateClaimService(token).addClaim(claimRequest);
    }

    protected String createFNOLClaimAndGetClaimToken(ClaimRequest itemizationRequest, ClaimRequest createClaimRequest){
        itemizationRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        UnifiedIntegrationService unifiedIntegrationService = new UnifiedIntegrationService();
        String test = unifiedIntegrationService.createItemizationCaseFNOL(createClaimRequest.getCountry(), createClaimRequest.getTenant(), itemizationRequest);
        createClaimRequest.setItemizationCaseReference(test);
        createClaimRequest.setAccidentDate(format(LocalDateTime.now().minusDays(2L), ISO8601));
        return unifiedIntegrationService.createClaimFNOL(createClaimRequest);
    }

    protected SettlementPage loginAndOpenUnifiedIntegrationClaimByToken(User user, String claimToken) {
        login(user, null);
        Browser.open(getEccUrl() + "Integration/Open?token=" + claimToken);
        return new SettlementPage();
    }

    protected MyPage login(User user) {
        Page.to(LoginPage.class);
        return AuthenticationApi.createServerApi().login(user, MyPage.class);
    }

    protected <T extends Page> T login(User user, Class<T> returnPageClass) {
        Page.to(LoginPage.class);
        return AuthenticationApi.createServerApi().login(user, returnPageClass);
    }

    protected <T extends Page> T login(User user, Class<T> returnPageClass, String parameters) {
        Page.to(LoginPage.class);
        return AuthenticationApi.createServerApi().login(user, returnPageClass, parameters);
    }

    protected SuppliersPage loginToEccAdmin(User user) {
        return login(user)
                .getMainMenu()
                .toEccAdminPage();
    }

    protected EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany, boolean showDisabled) {
        return openEditReasonPage(insuranceCompany, EditReasonsPage.ReasonType.DISCRETIONARY, false);
    }

    protected EditReasonsPage openEditReasonPage(InsuranceCompany insuranceCompany, EditReasonsPage.ReasonType reasonType, boolean showDisabled) {
        return login(getSystemUser(), AdminPage.class)
                .to(EditReasonsPage.class)
                .applyFilters(insuranceCompany.getFtTrygHolding(), reasonType, showDisabled)
                .assertEditReasonsFormVisible();
    }
    protected SettlementPage redirectToSettlementPage(User user){

        return login(user)
                .to(SettlementPage.class);
    }

    public static EccIntegrationService createClaimUsingEccIntegration(User user, EccIntegration eccIntegration) {
        new LoginProcessService().login(user);
        return new EccIntegrationService().createAndOpenClaim(eccIntegration);
    }

    public static EccIntegrationService createClaimAndLineUsingEccIntegration(User user, EccIntegration eccIntegration) {
        new LoginProcessService().login(user);
        return new EccIntegrationService().createClaim(eccIntegration);
    }

    public XpriceInfo getXpricesForConditions(DatabaseApi.PriceConditions... priceConditions) {
        return databaseApi.findProduct(priceConditions);
    }
}



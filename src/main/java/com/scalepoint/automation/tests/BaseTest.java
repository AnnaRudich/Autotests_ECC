package com.scalepoint.automation.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.exceptions.InvalidFtOperationException;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.EditPolicyTypeDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.admin.AdminPage;
import com.scalepoint.automation.pageobjects.pages.admin.EditReasonsPage;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.pageobjects.pages.testWidget.GenerateWidgetPage;
import com.scalepoint.automation.services.externalapi.*;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds;
import com.scalepoint.automation.services.restService.*;
import com.scalepoint.automation.services.restService.common.ServiceData;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.stubs.*;
import com.scalepoint.automation.utils.GridInfoUtils;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.SystemUtils;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.ftoggle.FeatureToggleSetting;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.entity.input.Claim;
import com.scalepoint.automation.utils.data.entity.input.InsuranceCompany;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.automation.utils.driver.DriverHelper;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.OrderRandomizer;
import com.scalepoint.automation.utils.listeners.SuiteListener;
import com.scalepoint.automation.utils.testng.Retrier;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IConfigurable;
import org.testng.IConfigureCallBack;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.internal.annotations.IAnnotationTransformer;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;

import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.*;
import static com.scalepoint.automation.services.usersmanagement.UsersManager.getSystemUser;
import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static com.scalepoint.automation.utils.DateUtils.ISO8601;
import static com.scalepoint.automation.utils.DateUtils.format;
import static com.scalepoint.automation.utils.data.entity.credentials.User.UserType.SCALEPOINT_ID;
import static com.scalepoint.automation.utils.listeners.DefaultFTOperations.getDefaultFTSettings;

@SpringBootTest(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Listeners({SuiteListener.class, OrderRandomizer.class})
public class BaseTest extends AbstractTestNGSpringContextTests implements /*IConfigurable,*/ IAnnotationTransformer {

    protected static final String TEST_LINE_DESCRIPTION = "Test description line åæéø";
    protected static final String RV_LINE_DESCRIPTION = "RnVLine åæéø";
    protected static final String UPDATED_LINE_DESCRIPTION = "Updated åæéø ";
    protected static final String DEFAULT_PASSWORD = "12341234";
    protected static final String SAMPLE_REASON_TEXT = "Sample reason åæéø ";
    protected static final String FT_TEMPLATE_NAME = "FT Name åæéø";
    protected static final String UPDATED_FT_TEMPLATE_NAME = String.format("U %s", FT_TEMPLATE_NAME);
    protected static final String DEFAULT_COPY_TEMPLATE = "Default";
    protected static final String DEFAULT_USER_PASSWORD = "duapDuap(312";
    public static final String DEFAULT_MONTH = "Jan";
    protected static final UserAddEditPage.UserType[] USER_ALL_ROLES = {ADMIN, CLAIMSHANDLER, SUPPLYMANAGER};

    public static final String TEST_DATA_PROVIDER = "testDataProvider";

    protected Logger log = LogManager.getLogger(BaseTest.class);

    private String gridNode;

    private Map<FeatureIds, Boolean> featureTogglesDefaultState = new HashMap<>();

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

    @Autowired
    protected RnVMock.RnvStub rnvStub;

    @Autowired
    protected AuditMock.AuditStub auditStub;

    @Autowired
    protected CommunicationDesignerMock communicationDesignerMock;

    @Autowired
    protected EVBMock.EVBStubs evbMock;

    @Autowired
    protected FraudAlertMock fraudAlertMock;

    @BeforeTest
    public void setRetry(ITestContext iTestContext){

        Arrays.stream(iTestContext.getAllTestMethods())
                .forEach(iTestNGMethod -> iTestNGMethod.setRetryAnalyzerClass(Retrier.class));
    }

    @BeforeMethod
    public void baseInit(Method method, ITestContext context, Object[] objects) {

        try {
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

            if (Browser.hasDriver()) {
                log.info("Using driver type: " + Browser.getDriverType());
                log.info("Start from: " + SystemUtils.getHostname());
                gridNode = GridInfoUtils.getGridNodeName(((RemoteWebDriver) Browser.driver()).getSessionId());
                log.info("Running on grid node: " + gridNode);

                Optional<User> optionalUser = getLisOfObjectByClass(Arrays.asList(objects), User.class).stream().findFirst();

                retryUpdateFtTemplate(method, optionalUser);
                updateFeatureToggle(method);
            }

        }catch (Exception e){
            Browser.quit();
            Window.cleanUp();
            CurrentUser.cleanUp();
            Page.PagesCache.cleanUp();
            ThreadContext.clearMap();
            throw new RuntimeException(e);
        }
    }

    @AfterMethod
    public void cleanup(Method method, ITestResult iTestResult, Object[] objects) {
        log.info("Clean up after: {}", method.toString());
        Cookie cookie = new Cookie("zaleniumTestPassed", String.valueOf(iTestResult.isSuccess()));
        try {
            Objects.requireNonNull(Browser.driver()).manage().addCookie(cookie);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        if (Browser.hasDriver()) {
            try {

                takeScreenshot(method, iTestResult);

                log.info("-------- InvokedMethodListener after. Thread: {} ----------", Thread.currentThread().getId());
                printErrorStackTraceIfAny(iTestResult);

                int left = TestCountdown.countDown(method.getDeclaringClass().getSimpleName()
                        + "." + method.getName()
                );

                log.info("Left tests: {}", left);


                cleanUpCDTemplates(method, objects);

                if (featureTogglesDefaultState.isEmpty()) {
                    log.info("No feature toggle to rollback");
                } else {
                    rollbackToggleSetting(method);
                }

                Browser.open(com.scalepoint.automation.utils.Configuration.getLogoutUrl());
                Page.to(LoginPage.class);
            } catch (Exception e) {
                /* if not caught it breaks the call of AfterMethod*/
                log.error(e.getMessage(), e);

            }
        }
        Browser.quit();
        Window.cleanUp();
        CurrentUser.cleanUp();
        Page.PagesCache.cleanUp();
        ThreadContext.clearMap();
        log.info("Clean up completed after: {} ", method.getName());
    }

    @DataProvider(name = TEST_DATA_PROVIDER)
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
        return functionalTemplatesApi.updateTemplate(user, returnPageClass, operations);
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim, String policyType) {

        LoginPage loginPage = Page.to(LoginPage.class);

        if(user.getType().equals(SCALEPOINT_ID))
        {

            loginPage
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword());

            ClaimApi.createClaim(claim, 1);
            return Page.to(SettlementPage.class);

        }else {

            ClaimApi claimApi = new ClaimApi(user);
            claimApi.createClaim(claim, policyType);
            return redirectToSettlementPage(user);
        }


    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        return loginAndCreateClaim(user, claim, null);
    }

    protected EditPolicyTypeDialog loginAndCreateClaimToEditPolicyDialog(User user, Claim claim) {
        loginAndCreateClaim(user, claim, null);
        return BaseDialog.at(EditPolicyTypeDialog.class);
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

        if(user.getType().equals(SCALEPOINT_ID)){

            Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), MyPage.class);

        }else {

            login(user, null);
        }

        Browser.open(getEccUrl() + "Integration/Open?token=" + claimToken);

        return new SettlementPage();
    }

    protected <T extends Page> T loginAndOpenUnifiedIntegrationClaimByToken(User user, String claimToken, Class<T> returnPageClass) {

        if(user.getType().equals(SCALEPOINT_ID)){

            Page.to(LoginPage.class)
                    .loginViaScalepointId()
                    .login(user.getLogin(), user.getPassword(), MyPage.class);
        }else {

            login(user, null);
        }

        Browser.open(getEccUrl() + "Integration/Open?token=" + claimToken);

        return Page.at(returnPageClass);
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

    protected GenerateWidgetPage openGenerateWidgetPage(){

        Browser.open(String.format(com.scalepoint.automation.utils.Configuration.getWidgetUrl(), "01"));
        return Page.at(GenerateWidgetPage.class);
    }

    protected GenerateWidgetPage openGenerateWidgetPageNonAuth(){
        Browser.open(String.format(com.scalepoint.automation.utils.Configuration.getWidgetUrl(), "02"));
        return Page.at(GenerateWidgetPage.class);
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

    public XpriceInfo getXPriceInfoForProduct(){
        return databaseApi.findOrderableProduct();
    }

    public VoucherInfo getVoucherInfo(Boolean isEvoucher){
        return databaseApi.getVoucherInfo(isEvoucher);
    }

    private void retryUpdateFtTemplate(Method method, Optional<User> optionalUser) {

        int attempt = 0;
        /*sometimes we get java.net.SocketTimeoutException: Read timed out, so lets try again*/
        while (attempt <= 1) {
            try {

                List<RequiredSetting> allSettings = getAllSettings(method);

                updateTemplate(allSettings, optionalUser);
                break;
            } catch (InvalidFtOperationException e) {
                throw e;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.error("Next attempt");
                attempt++;
                if (attempt > 1) {
                    throw e;
                }
            }
        }
    }

    private void updateTemplate(List<RequiredSetting> allSettings, Optional<User> optionalUser) {

        log.info("-------- InvokedMethodListener before. Thread: {} ----------", Thread.currentThread().getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Page.to(LoginPage.class);
            updateFunctionalTemplate(allSettings, user);
            Browser.driver().manage().deleteAllCookies();
        }
    }

    public void cleanUpCDTemplates(Method method, Object[] objects){
        boolean cleanUp = method
                .getDeclaredAnnotation(CommunicationDesignerCleanUp.class) != null;
        if(cleanUp) {
            User user = getLisOfObjectByClass(Arrays.asList(objects), User.class).get(0);
            Page.to(InsCompAddEditPage.class, user.getCompanyId())
                    .setCommunicationDesignerSection(InsCompAddEditPage.CommunicationDesigner.reset())
                    .selectSaveOption(false);
        }
    }

    private void rollbackToggleSetting(Method method) {

        FeaturesToggleAdministrationService featuresToggleAdminApi = new FeaturesToggleAdministrationService();
        final FeatureToggleSetting toggleSetting = getToggleSetting(method);
        if (toggleSetting == null) {
            return;
        }

        FeatureIds toggleSettingType = toggleSetting.type();
        Boolean initialState = featureTogglesDefaultState.get(toggleSettingType);
        featuresToggleAdminApi.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(initialState), toggleSettingType);
    }

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ResultOfMethodCallIgnored"})
    private void takeScreenshot(Method method, ITestResult iTestResult) {
        String fileName = getFileName(method);
        if (!iTestResult.isSuccess()) {
            try {
                takeScreenshot(fileName);
            } catch (Exception e) {
                log.error("Can't make screenshot with ashot for {} cause {}", method.getName(), e.getMessage());
                Selenide.screenshot(fileName);
            }
        }
    }

    private void takeScreenshot(String fileName) throws IOException {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(Browser.driver());
        BufferedImage image = screenshot.getImage();
        File imageFile = new File(Configuration.reportsFolder, fileName + ".png");
        ImageIO.write(image, "png", imageFile);
    }

    private String getFileName(Method method) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");
        return method.getName() + "_" + sdf.format(new Date()) + "_node_" + gridNode.replace("http://", "").replace(":", "")
                + "_" + Browser.getDriverType();
    }


    private FeatureToggleSetting getToggleSetting(Method method) {

        return method.getDeclaredAnnotation(FeatureToggleSetting.class);
    }


    private void updateFeatureToggle(Method method) {
        FeaturesToggleAdministrationService featureToggleService = new FeaturesToggleAdministrationService();

        FeatureToggleSetting toggleSetting = getToggleSetting(method);
        if (toggleSetting == null) {
            return;
        }

        boolean toggleActualState = featureToggleService.getToggleStatus(toggleSetting.type().name());
        boolean toggleExpectedState = toggleSetting.enabled();

        if (toggleActualState != toggleExpectedState) {
            featureTogglesDefaultState.put(toggleSetting.type(), toggleActualState);
            featureToggleService.updateToggle(FeaturesToggleAdministrationService.ActionsOnToggle.of(toggleExpectedState), toggleSetting.type());
        }
    }


    private void updateFunctionalTemplate(List<RequiredSetting> allSettings, User user) {



        String companyCode = user.getCompanyCode();

        if(companyCode != null) {

            List<FtOperation> defaultList = getDefaultFTSettings(companyCode);

            if (!allSettings.isEmpty()) {
                for (RequiredSetting setting : allSettings) {
                    FTSetting settingType = setting.type();
                    defaultList = defaultList
                            .stream()
                            .filter(ftOperation ->
                                    !ftOperation.getSetting().equals(settingType))
                            .collect(Collectors.toList());
                    switch (settingType.getOperationType()) {
                        case CHECKBOX:
                            defaultList.add(setting.enabled() ? FTSettings.enable(settingType) : FTSettings.disable(settingType));
                            break;
                        case INPUT:
                            defaultList.add(FTSettings.setValue(settingType, setting.value()));
                            break;
                        case SELECT:
                            defaultList.add(FTSettings.select(settingType, setting.value()));
                    }
                }
            }

            updateFtTemplateWithRequiredSettings(user, defaultList);
        }
    }

    private void updateFtTemplateWithRequiredSettings(User user, List<FtOperation> ftOperations) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(UsersManager.getSystemUser());
        functionalTemplatesApi.updateTemplate(user, LoginPage.class, ftOperations.toArray(new FtOperation[0]));
    }

    private void printErrorStackTraceIfAny(ITestResult iTestResult) {
        Throwable e = iTestResult.getThrowable();
        if (e != null) {
            log.error(e.getMessage(), e);
        }
    }

    private List<RequiredSetting> getAllSettings(Method method) {

        Class realClass = method.getDeclaringClass();
        RequiredSetting[] classAnnotations = (RequiredSetting[]) realClass.getDeclaredAnnotationsByType(RequiredSetting.class);
        RequiredSetting[] methodAnnotations = method.getDeclaredAnnotationsByType(RequiredSetting.class);

        List<RequiredSetting> requiredSettings = new ArrayList<>();
        Set<FTSetting> methodSettings = new HashSet<>();

        if (methodAnnotations != null) {
            Arrays.stream(methodAnnotations).
                    forEach(annotation -> {
                        methodSettings.add(annotation.type());
                        requiredSettings.add(annotation);
                    });
        }

        if (classAnnotations != null) {
            Arrays.stream(classAnnotations).
                    filter(classAnnotation -> !methodSettings.contains(classAnnotation.type())).
                    forEach(requiredSettings::add);
        }

        return requiredSettings;
    }

    protected static  <T> List<T> getLisOfObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(o -> o.getClass().equals(clazz))
                .collect(Collectors.toList());
    }

    protected static <T> List<T> removeObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(o -> !o.getClass().equals(clazz)).collect(Collectors.toList());
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod, Class<?> occurringClazz){

        annotation.setRetryAnalyzer(Retrier.class);
    }

//    @Override
//    public void run(IConfigureCallBack iConfigureCallBack, ITestResult iTestResult) {
//        iConfigureCallBack.runConfigurationMethod(iTestResult);
//        if (iTestResult.getThrowable() != null) {
//            for (int i = 0; i <= 3; i++) {
//                iConfigureCallBack.runConfigurationMethod(iTestResult);
//                if (iTestResult.getThrowable() == null) {
//                    break;
//                }
//            }
//        }
//    }
}



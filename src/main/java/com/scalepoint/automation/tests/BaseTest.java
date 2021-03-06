package com.scalepoint.automation.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.scalepoint.automation.exceptions.InvalidFtOperationException;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.admin.InsCompAddEditPage;
import com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage;
import com.scalepoint.automation.pageobjects.pages.testWidget.GenerateWidgetPage;
import com.scalepoint.automation.services.externalapi.*;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSetting;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.externalapi.ftoggle.FeatureId;
import com.scalepoint.automation.services.restService.CaseSettlementDataService;
import com.scalepoint.automation.services.restService.EccIntegrationService;
import com.scalepoint.automation.services.restService.LoginProcessService;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.shared.VoucherInfo;
import com.scalepoint.automation.shared.XpriceInfo;
import com.scalepoint.automation.spring.*;
import com.scalepoint.automation.stubs.*;
import com.scalepoint.automation.tests.widget.LoginFlow;
import com.scalepoint.automation.utils.FeatureToggle;
import com.scalepoint.automation.utils.annotations.CommunicationDesignerCleanUp;
import com.scalepoint.automation.utils.annotations.functemplate.RequiredSetting;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.TestDataActions;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.entity.eccIntegration.EccIntegration;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.listeners.OrderRandomizer;
import com.scalepoint.automation.utils.listeners.SuiteListener;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.scalepoint.automation.pageobjects.pages.admin.UserAddEditPage.UserType.*;
import static com.scalepoint.automation.services.externalapi.OauthTestAccountsApi.Scope.PLATFORM_CASE_READ;
import static com.scalepoint.automation.utils.listeners.DefaultFTOperations.getDefaultFTSettings;

@SpringBootTest(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Listeners({SuiteListener.class, OrderRandomizer.class})
@Import({BeansConfiguration.class, EventApiDatabaseConfig.class, WireMockConfig.class, WireMockStubsConfig.class, LoginFlow.class, FeatureToggle.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected static final String TEST_LINE_DESCRIPTION = "Test description line ????????";
    protected static final String RV_LINE_DESCRIPTION = "RnVLine ????????";
    protected static final String UPDATED_LINE_DESCRIPTION = "Updated ???????? ";
    protected static final String DEFAULT_PASSWORD = "12341234";
    protected static final String SAMPLE_REASON_TEXT = "Sample reason ???????? ";
    protected static final String FT_TEMPLATE_NAME = "FT Name ????????";
    protected static final String UPDATED_FT_TEMPLATE_NAME = String.format("U %s", FT_TEMPLATE_NAME);
    protected static final String DEFAULT_COPY_TEMPLATE = "Default";
    protected static final String DEFAULT_USER_PASSWORD = "duapDuap(312";
    public static final String DEFAULT_MONTH = "Jan";
    protected static final UserAddEditPage.UserType[] USER_ALL_ROLES = {ADMIN, CLAIMSHANDLER, SUPPLYMANAGER};

    public static final String TEST_DATA_PROVIDER = "testDataProvider";

    protected Logger log = LogManager.getLogger(BaseTest.class);

    protected String gridNode;

    protected Map<FeatureId, Boolean> featureTogglesDefaultState = new HashMap<>();

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

    protected DriverType driverType = null;

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

    @Autowired
    protected MailserviceMock.MailserviceStub mailserviceStub;

    @Autowired
    protected LoginFlow loginFlow;

    @Autowired
    protected FeatureToggle featureToggle;

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

    protected GenerateWidgetPage openGenerateWidgetPage(){

        Browser.open(com.scalepoint.automation.utils.Configuration.getWidgetUrl());
        return Page.at(GenerateWidgetPage.class);
    }

    protected GenerateWidgetPage openGenerateWidgetPageNonAuth(){
        Browser.open(com.scalepoint.automation.utils.Configuration.getNonAuthWidgetUrl());
        return Page.at(GenerateWidgetPage.class);
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

    protected void retryUpdateFtTemplate(Method method, Optional<User> optionalUser) {

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

    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ResultOfMethodCallIgnored"})
    protected void takeScreenshot(Method method, ITestResult iTestResult) {
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

    protected void printErrorStackTraceIfAny(ITestResult iTestResult) {
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

    protected CaseSettlementDataService getSettlementData(ClaimRequest claimRequest) {
        return new CaseSettlementDataService(new OauthTestAccountsApi().sendRequest(PLATFORM_CASE_READ).getToken())
                .getSettlementData(databaseApi.getSettlementRevisionTokenByClaimNumber(claimRequest.getCaseNumber()), claimRequest.getTenant());
    }

    protected static  <T> List<T> getObjectByClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(object -> object.getClass().equals(clazz))
                .map(object -> (T)object)
                .collect(Collectors.toList());
    }

    protected static  <T> List<T> getObjectBySuperClass(List objects, Class<T> clazz){

        return (List<T>) objects
                .stream()
                .filter(object -> object.getClass().getSuperclass().equals(clazz))
                .collect(Collectors.toList());
    }
}



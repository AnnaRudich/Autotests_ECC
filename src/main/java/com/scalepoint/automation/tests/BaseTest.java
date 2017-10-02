package com.scalepoint.automation.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.pageobjects.pages.suppliers.SuppliersPage;
import com.scalepoint.automation.services.externalapi.AuthenticationApi;
import com.scalepoint.automation.services.externalapi.ClaimApi;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.TestAccountsApi;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.restService.CreateClaimService;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.annotations.SupplierCompany;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.ExistingSuppliers;
import com.scalepoint.automation.utils.data.entity.SimpleSupplier;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.data.request.ClaimRequest;
import com.scalepoint.automation.utils.data.response.Token;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;


@Listeners({InvokedMethodListener.class})
public class BaseTest extends AbstractBaseTest {

    @BeforeMethod
    public void baseInit(Method method, ITestContext context) throws Exception {
        Thread.currentThread().setName("Thread "+method.getName());
        MDC.put("sessionid", method.getName());
        logger.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());

        DriverType driverType = method.getAnnotation(RunOn.class) != null
                ? method.getAnnotation(RunOn.class).value() : DriverType.findByProfile(browserMode);
        WebDriver driver = DriversFactory.getDriver(driverType);

        Browser.init(driver, driverType);
        Window.init(driver);
        WebDriverRunner.setWebDriver(driver);

        JavascriptHelper.initializeCommonFunctions();
        driver.manage().window().maximize();

        Configuration.savePageSource = false;
    }

    @AfterMethod
    public void cleanup(Method method, ITestResult iTestResult) {
        logger.info("Clean up after: {}", method.toString());
        Browser.quit();
        Window.cleanUp();
        CurrentUser.cleanUp();
        Page.PagesCache.cleanUp();
        MDC.clear();
    }

    /*doesn't work with IE, but can be used with FF_REMOTE/Chrome*/
    private void logJavaScriptErrors() {
        try {
            Logs logs = Browser.driver().manage().logs();
            LogEntries logEntries = logs.get(LogType.BROWSER);
            for (LogEntry logEntry : logEntries) {
                logger.error("JSERROR: " + logEntry.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim, String policyType) {
        Page.to(LoginPage.class);

        ClaimApi claimApi = new ClaimApi(user);
        claimApi.createClaim(claim, policyType);

        return Page.at(SettlementPage.class);
    }

    protected void createClaimIgnoringExceptions(User user, Claim claim){
        try {
            ClaimApi claimApi = new ClaimApi(user);
            claimApi.createClaim(claim, null);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        return loginAndCreateClaim(user, claim, null);
    }

    protected String createCwaClaimAndGetClaimToken(ClaimRequest claimRequest){
        Token token = new TestAccountsApi().sendRequest().getToken();
        return new CreateClaimService(token).addClaim(claimRequest).getResponse().jsonPath().get("token");
    }

    protected SettlementPage loginAndOpenCwaClaimByToken(User user, String claimToken){
        login(user, null);
        Browser.open(getEccUrl()+ "Integration/Open?token=" + claimToken);
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
                .toEccAdminPage()
                .toSuppliersPage();
    }

    protected <T extends Page> T updateFT(User user, Class<T> returnPageClass, FtOperation... operations) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);
        return functionalTemplatesApi.updateTemplate(user.getFtId(), returnPageClass, operations);
    }

    protected <T extends Page> T updateFT(User user, FtOperation... operations) {
        return updateFT(user, null, operations);
    }

    @DataProvider(name = "testDataProvider")
    public static Object[][] provide(Method method) {
        Thread.currentThread().setName("Thread "+method.getName());
        Object[][] params = new Object[1][];
        params[0] = getTestDataParameters(method).toArray();
        return params;
    }

    private static List<Object> getTestDataParameters(Method method) {
        MDC.put("sessionid", method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> instances = new ArrayList<>(parameterTypes.length);
        try {
            Map<UsersManager.CompanyMethodArgument, User> requestedUsers = UsersManager.fetchUsersWhenAvailable(extractAllCompanyCodesRequested(method));
            Map<Integer, User> indexToUser = requestedUsers.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getIndex(), Map.Entry::getValue));
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (indexToUser.containsKey(i)) {
                    User user = indexToUser.get(i);
                    CurrentUser.setUser(user);
                    instances.add(user);
                }
                else if(parameterType.equals(SimpleSupplier.class)){
                    Annotation[] annotations = method.getParameterAnnotations()[i];
                    instances.add(getTestDataForExistingSuppliers(annotations));
                }
                else {
                    try {
                        instances.add(TestData.Data.getInstance(parameterType));
                    } catch (Exception e) {
                        LogManager.getLogger(BaseTest.class).error(e.getMessage());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LogManager.getLogger(BaseTest.class).error(e.getMessage());
        }
        return instances;
    }

    private static Object getTestDataForExistingSuppliers(Annotation[] annotations) {
        ExistingSuppliers existingSuppliers = (ExistingSuppliers) TestData.Data.getInstance(ExistingSuppliers.class);
        if(annotations.length > 0) {
            Annotation annotation = annotations[0];
            if (annotation.annotationType().equals(SupplierCompany.class)) {
                SupplierCompany supplierCompany = (SupplierCompany) annotation;

                List<SimpleSupplier> simpleSuppliers = existingSuppliers.getSuppliers().stream()
                        .filter(sup -> sup.getInsuranceCompany().equals(supplierCompany.value().name()))
                        .collect(Collectors.toList());

                if(supplierCompany.areWithVouchers()) {
                    return simpleSuppliers.stream().filter(sup -> sup.isWithVouchers()).findAny().get();
                }else{
                    return simpleSuppliers.stream().filter(sup -> !sup.isWithVouchers()).findAny().get();
                }
            }
        }
        return existingSuppliers.getSuppliers().stream().filter(sup -> sup.getInsuranceCompany().equals(CompanyCode.SCALEPOINT.name())).findFirst().get();
    }

    private static Map<UsersManager.CompanyMethodArgument, User> extractAllCompanyCodesRequested(Method method) {
        Map<UsersManager.CompanyMethodArgument, User>companyCodes = new HashMap<>();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType.equals(User.class)) {
                CompanyCode companyCode = CompanyCode.FUTURE50;
                Annotation[] annotations = method.getParameterAnnotations()[i];
                if (annotations.length > 0) {
                    Annotation annotation = annotations[0];
                    if (annotation.annotationType().equals(UserCompany.class)) {
                        companyCode = ((UserCompany) annotation).value();
                    }
                }
                companyCodes.put(UsersManager.CompanyMethodArgument.create(i, companyCode), null);
            }
        }
        return companyCodes;
    }

    public static Object[] combine(List<Object> testDataParameters, Object... additionalParams) {
        List<Object> params = Lists.newArrayList(testDataParameters);
        params.addAll(Arrays.asList(additionalParams));
        return params.toArray();
    }
}



package com.scalepoint.automation;

import com.codeborne.selenide.WebDriverRunner;
import com.google.common.collect.Lists;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.EditPolicyDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ClaimApi;
import com.scalepoint.automation.services.externalapi.AuthenticationApi;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.CurrentUser;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.MDC;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Listeners({InvokedMethodListener.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${driver.type}")
    private String browserMode;

    @BeforeMethod
    public void baseInit(Method method, ITestContext context) throws Exception {
        MDC.put("sessionid", method.getName());
        logger.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());

        WebDriver driver = DriversFactory.getDriver(DriverType.findByProfile(browserMode));

        Browser.init(driver);
        Window.init(driver);
        WebDriverRunner.setWebDriver(driver);

        JavascriptHelper.initializeCommonFunctions();
        driver.manage().window().maximize();
        logger.info("MainHandle " + Browser.driver().getWindowHandle());
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

    protected void loadJarDll() throws IOException {
        String dllName = "jacob-1.14.3-x64.dll";
        File temp = new File(new File(System.getProperty("java.io.tmpdir")), dllName);
        if (!temp.exists()) {
            try (InputStream in = BaseTest.class.getClassLoader().getResourceAsStream("dll/" + dllName);
                 FileOutputStream fos = new FileOutputStream(temp)) {
                IOUtils.copy(in, fos);
            }
        }
        logger.info("Jacob dll will be loaded from: " + temp.getAbsolutePath());
        System.load(temp.getAbsolutePath());
    }

    /*doesn't work with IE, but can be used with FF/Chrome*/
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

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        return loginAndCreateClaim(user, claim, null);
    }

    private void processPolicyType(String policyType) {
        EditPolicyDialog editPolicyDialog = BaseDialog.at(EditPolicyDialog.class);
        if (policyType == null) {
            editPolicyDialog.chooseAny();
        } else {
            editPolicyDialog.choose(policyType);
        }
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

    protected <T extends Page> T updateFT(User user, Class<T> returnPageClass, FtOperation... operations) {
        FunctionalTemplatesApi functionalTemplatesApi = new FunctionalTemplatesApi(user);
        return functionalTemplatesApi.updateTemplate(user.getFtId(), returnPageClass, operations);
    }

    protected <T extends Page> T updateFT(User user, FtOperation... operations) {
        return updateFT(user, null, operations);
    }

    @DataProvider(name = "testDataProvider")
    public static Object[][] provide(Method method) {
        Object[][] params = new Object[1][];
        params[0] = getTestDataParameters(method).toArray();
        return params;
    }

    public static List<Object> getTestDataParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Object> instances = new ArrayList<>(parameterTypes.length);
        try {
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.equals(User.class)) {
                    User user = getRequestedUser(method);
                    instances.add(user);
                } else {
                    try {
                        instances.add(TestData.Data.getInstance(parameterType));
                    } catch (Exception e) {
                        LoggerFactory.getLogger(BaseTest.class).error(e.getMessage());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(BaseTest.class).error(e.getMessage());
        }
        return instances;
    }

    public static Object[] combine(List<Object> testDataParameters, Object... additionalParams) {
        List<Object> params = Lists.newArrayList(testDataParameters);
        params.addAll(Arrays.asList(additionalParams));
        return params.toArray();
    }

    private static User getRequestedUser(Method method) {
        CompanyCode companyCode = CompanyCode.FUTURE1;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations[0].length > 0) {
            Annotation annotation = parameterAnnotations[0][0];
            if (annotation.annotationType().equals(UserCompany.class)) {
                companyCode = ((UserCompany) annotation).value();
            }
        }
        User user = UsersManager.takeUser(companyCode);
        CurrentUser.setUser(user);
        return user;
    }
}



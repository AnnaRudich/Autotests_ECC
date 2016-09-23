package com.scalepoint.automation;

import com.google.common.base.Function;
import com.scalepoint.automation.pageobjects.dialogs.EditPolicyDialog;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ClaimApi;
import com.scalepoint.automation.services.externalapi.FunctionalTemplatesApi;
import com.scalepoint.automation.services.externalapi.ServerApi;
import com.scalepoint.automation.services.externalapi.ftemplates.FTSettings;
import com.scalepoint.automation.services.externalapi.ftemplates.operations.FtOperation;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.InvokedMethodListener;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.MDC;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
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
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.scalepoint.automation.pageobjects.pages.Page.at;
import static com.scalepoint.automation.services.externalapi.ftemplates.FTSetting.ENABLE_NEW_SETTLEMENT_ITEM_DIALOG;

@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@Listeners({ InvokedMethodListener.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment environment;

    @BeforeMethod
    public void baseInit(Method method, ITestContext context) throws Exception {
        MDC.put("sessionid", method.getName());
        logger.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());

        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            throw new IllegalStateException("Profile must be specified");
        }

        WebDriver driver = DriversFactory.getDriver(DriverType.findByProfile(activeProfiles));

        Browser.init(driver);
        Window.init(driver);

        JavascriptHelper.initializeCommonFunctions(driver);
        driver.manage().window().maximize();
        logger.info("MainHandle "+Browser.driver().getWindowHandle());
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

    protected SettlementPage loginAndCreateClaim(User user, Claim claim, String policyType) {
        Page.to(LoginPage.class);

        ClaimApi claimApi = new ClaimApi(user);
        claimApi.createClaim(claim);

        Page.toWithNoAt(SettlementPage.class);

        try {
            Wait.For((Function<WebDriver, Object>) webDriver -> {
                assert webDriver != null;
                return webDriver.getWindowHandles().size() > 1;
            }, 5, 1000);
            processPolicyType(policyType);
        } catch (Exception ignored) {
        }

        return Page.at(SettlementPage.class);
    }

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        return loginAndCreateClaim(user, claim, null);
    }

    private void processPolicyType(String policyType) {
        EditPolicyDialog editPolicyDialog = at(EditPolicyDialog.class);
        if (policyType == null) {
            editPolicyDialog.chooseAny();
        } else {
            editPolicyDialog.choose(policyType);
        }
    }

    protected MyPage login(User user) {
        return ServerApi.createServerApi().login(user, MyPage.class);
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
        try {
            Class<?>[] parameterTypes = method.getParameterTypes();
            List<Object> instances = new ArrayList<>(parameterTypes.length);
            for (Class<?> parameterType : parameterTypes) {
                if (parameterType.equals(User.class)) {
                    User user = getRequestedUser(method);
                    instances.add(user);
                } else {
                    instances.add(TestData.Data.getInstance(parameterType));
                }
            }
            return new Object[][]{instances.toArray()};
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[][]{{}};
        }
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
        CurrentUser.set(user);
        return user;
    }

    private static class CurrentUser {
        private static ThreadLocal<User> holder = new ThreadLocal<>();

        public static void set(User user) {
            holder.set(user);
        }

        public static User get() {
            return holder.get();
        }

        static void cleanUp() {
            if (get()!=null) {
                UsersManager.returnUser(get());
            }
            holder.remove();
        }
    }
}

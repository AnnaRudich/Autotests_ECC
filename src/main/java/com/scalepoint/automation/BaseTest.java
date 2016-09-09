package com.scalepoint.automation;

import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.MyPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.services.externalapi.ClaimApi;
import com.scalepoint.automation.services.externalapi.ServerApi;
import com.scalepoint.automation.services.usersmanagement.CompanyCode;
import com.scalepoint.automation.services.usersmanagement.UsersManager;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.UserCompany;
import com.scalepoint.automation.utils.data.TestData;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.Browser;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    private static Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @Autowired
    private Environment environment;

    protected SettlementPage loginAndCreateClaim(User user, Claim claim) {
        Page.to(LoginPage.class);

        ClaimApi claimApi = new ClaimApi(user);
        claimApi.createClaim(claim);

        return Page.to(SettlementPage.class);
    }

    protected MyPage login(User user) {
        return ServerApi.createServerApi().login(user, MyPage.class);
    }

    @BeforeMethod
    public void baseInit() throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0) {
            throw new IllegalStateException("Profile must be specified");
        }

        WebDriver driver = DriversFactory.getDriver(DriverType.findByProfile(activeProfiles));

        Browser.init(driver);
        Window.init(driver);

        JavascriptHelper.initializeCommonFunctions(driver);
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void cleanup(Method method) {
        logger.info("Clean up after: {}", method.toString());

        Browser.quit();
        Window.cleanUp();
        CurrentUser.cleanUp();
        Page.PagesCache.cleanUp();
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
            logger.error(method.getName() + " : " + e.getMessage(), e);
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
            UsersManager.returnUser(get());
            holder.get();
        }
    }
}

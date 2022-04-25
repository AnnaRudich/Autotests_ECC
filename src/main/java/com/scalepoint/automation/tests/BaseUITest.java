package com.scalepoint.automation.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.scalepoint.automation.pageobjects.pages.LoginPage;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.services.restService.common.ServiceData;
import com.scalepoint.automation.utils.GridInfoUtils;
import com.scalepoint.automation.utils.JavascriptHelper;
import com.scalepoint.automation.utils.SystemUtils;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import com.scalepoint.automation.utils.driver.DriverHelper;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.listeners.FeatureToggleSettingsUtils;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.CurrentUser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.scalepoint.automation.services.externalapi.ftoggle.FeatureIds.SCALEPOINTID_LOGIN_ENABLED;

public class BaseUITest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void updateFeatureToggle(ITestContext context){

        ServiceData.init(databaseApi);

        if(FeatureToggleSettingsUtils.isFeatureToggleSettingEnabled(context, SCALEPOINTID_LOGIN_ENABLED)) {

            updateFeatureToggle(FeatureToggleSettingsUtils.scalepointIdLoginEnabled());
        }
    }

    @AfterClass(alwaysRun = true)
    public void rollbackFeatureToggle(ITestContext context){

        if (featureTogglesDefaultState.isEmpty()) {

            log.info("No feature toggle to rollback");
        } else {

            if(FeatureToggleSettingsUtils.isFeatureToggleSettingEnabled(context, SCALEPOINTID_LOGIN_ENABLED)) {

                rollbackToggleSetting(FeatureToggleSettingsUtils.scalepointIdLoginEnabled());
            }
        }
    }

    @BeforeMethod(alwaysRun = true)
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

                if(FeatureToggleSettingsUtils.getFeatureToggleSetting(context).isEmpty()) {

                    updateFeatureToggle(getToggleSetting(method));
                }
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

    @AfterMethod(alwaysRun = true)
    public void cleanup(Method method, ITestResult iTestResult, ITestContext context, Object[] objects) {
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

                    if(FeatureToggleSettingsUtils.getFeatureToggleSetting(context).isEmpty()) {

                        rollbackToggleSetting(getToggleSetting(method));
                    }
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
}


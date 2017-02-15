package com.scalepoint.automation.utils.driver;

import com.scalepoint.automation.utils.data.TestData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.scalepoint.automation.utils.driver.DriversFactory.Timeout.defaultImplicitWait;
import static com.scalepoint.automation.utils.driver.DriversFactory.Timeout.defaultScriptTimeout;

public enum DriversFactory {

    IE(DriverType.IE) {
        @Override
        protected WebDriver getDriverInstance() {

            defaultImplicitWait = 10;
            defaultScriptTimeout = 30;

            if (System.getProperty("webdriver.ie.driver") == null) {
                File ieDriver = new File("src/main/resources/drivers/IEDriverServer.exe");
                System.setProperty("webdriver.ie.driver", ieDriver.getAbsolutePath());
            }
            DesiredCapabilities capabilities = getDesiredCapabilitiesForIE();
            InternetExplorerDriver driver = new InternetExplorerDriver(capabilities);
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(defaultScriptTimeout, TimeUnit.SECONDS);
            return driver;
        }
    },

    IE_REMOTE(DriverType.IE_REMOTE) {
        @Override
        protected WebDriver getDriverInstance() throws MalformedURLException {

            defaultImplicitWait = 15;
            defaultScriptTimeout = 60;

            DesiredCapabilities capabilities = getDesiredCapabilitiesForIE();
            WebDriver driver = new RemoteWebDriver(new URL(TestData.getLinks().getHubLink()), capabilities);
            driver = new Augmenter().augment(driver);
            driver.manage().timeouts().implicitlyWait(defaultImplicitWait, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(defaultScriptTimeout, TimeUnit.SECONDS);
            return driver;
        }


    },
    FF(DriverType.FF) {
        protected WebDriver getDriverInstance() throws MalformedURLException {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            WebDriver driver = new RemoteWebDriver(new URL(TestData.getLinks().getHubLink()), capabilities);
            driver = new Augmenter().augment(driver);
            return driver;
        }
    },
    CHROME(DriverType.CHROME) {
        protected WebDriver getDriverInstance() {
            if (System.getProperty("webdriver.chrome.driver") == null) {
                File ieDriver = new File("src/main/resources/drivers/chromedriver.exe");
                System.setProperty("webdriver.chrome.driver", ieDriver.getAbsolutePath());
            }
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("operations-type");
            options.addArguments("start-maximized");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("-incognito");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            return new ChromeDriver(capabilities);
        }
    };

    private static DesiredCapabilities getDesiredCapabilitiesForIE() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability("nativeEvents", false);
        capabilities.setCapability("requireWindowFocus", true);
        capabilities.setCapability("driverAttachTimeout", 60000);
        capabilities.setCapability("unexpectedAlertBehaviour", "accept");
        capabilities.setCapability("ie.ensureCleanSession", true);

//        addLoggingPreferences(capabilities);
        return capabilities;
    }

    /*doesn't work with IE, but can be used with FF/Chrome*/
    private static void addLoggingPreferences(DesiredCapabilities capabilities) {
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.CLIENT, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        logs.enable(LogType.PERFORMANCE, Level.ALL);
        logs.enable(LogType.PROFILER, Level.ALL);
        logs.enable(LogType.SERVER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logs);
    }

    private DriverType driverType;

    DriversFactory(DriverType driverType) {
        this.driverType = driverType;
    }

    public static WebDriver getDriver(DriverType driverType) throws Exception {
        DriversFactory[] values = DriversFactory.values();
        for (DriversFactory value : values) {
            if (value.driverType.equals(driverType)) {
                return value.getDriverInstance();
            }
        }
        return IE.getDriverInstance();
    }

    protected abstract WebDriver getDriverInstance() throws MalformedURLException;

    public static class Timeout {

        public static int defaultImplicitWait;
        public static int defaultScriptTimeout;
    }
}

package com.scalepoint.automation.utils.driver;

import com.scalepoint.automation.utils.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static com.scalepoint.automation.utils.driver.DriversFactory.Timeout.DEFAULT_IMPLICIT_WAIT;
import static com.scalepoint.automation.utils.driver.DriversFactory.Timeout.DEFAULT_SCRIPT_TIMEOUT;
import static org.openqa.selenium.ie.InternetExplorerDriver.NATIVE_EVENTS;

public enum DriversFactory {

    IE(DriverType.IE) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            if (System.getProperty("webdriver.ie.driver") == null) {
                File ieDriver = new File("src/main/resources/drivers/IEDriverServer.exe");
                System.setProperty("webdriver.ie.driver", ieDriver.getAbsolutePath());
            }
            InternetExplorerDriver driver = new InternetExplorerDriver(getOptionsForIE());
            setTimeouts(driver);
            return driver;
        }
    },

    IE_REMOTE(DriverType.IE_REMOTE) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) throws MalformedURLException {
            WebDriver driver = new RemoteWebDriver(new URL(Configuration.getHubRemote()), getOptionsForIE().merge(capabilities));
            setTimeouts(driver);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            return driver;
        }


    },

    EDGE(DriverType.EDGE) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            WebDriverManager.edgedriver().setup();
            EdgeDriver driver = new EdgeDriver();
            setTimeouts(driver);
            return driver;
        }
    },

    FF(DriverType.FF) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxDriver driver = new FirefoxDriver(getFireFoxCapabilities().merge(capabilities));
            setTimeouts(driver);
            return driver;
        }
    },

    FF_REMOTE(DriverType.FF_REMOTE) {
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) throws MalformedURLException {
            WebDriver driver = new RemoteWebDriver(new URL(Configuration.getHubRemote()), getFireFoxCapabilities().merge(capabilities));
            driver = new Augmenter().augment(driver);
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            return driver;
        }
    },

    CHROME_REMOTE(DriverType.CHROME_REMOTE) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            return getChromeRemote(Configuration.getHubRemote(), capabilities);
        }

    },

    CHROME_ZALENIUM_REMOTE(DriverType.CHROME_ZALENIUM_REMOTE) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            return getChromeRemote(Configuration.getHubRemoteZalenium(), capabilities);
        }

    },

    CHROME_ZALENIUM_LOCAL(DriverType.CHROME_ZALENIUM_LOCAL) {
        @Override
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            return getChromeRemote(Configuration.getHubLocalZalenium(), capabilities);
        }

    },

    CHROME(DriverType.CHROME) {
        protected WebDriver getDriverInstance(DesiredCapabilities capabilities) {
            if (System.getProperty("webdriver.chrome.driver") == null) {
                File ieDriver = new File("src/main/resources/drivers/chromedriver.exe");
                System.setProperty("webdriver.chrome.driver", ieDriver.getAbsolutePath());
            }
            System.setProperty("webdriver.timeouts.implicitlywait", "1");
            ChromeDriver chromeDriver = new ChromeDriver(getDesiredCapabilitiesForChrome().merge(capabilities));
            setTimeouts(chromeDriver);
            return chromeDriver;
        }
    };

    private static Logger log = LogManager.getLogger(DriversFactory.class);

    private static WebDriver getChromeRemote(String hubUrl, DesiredCapabilities capabilities) {
        WebDriver driver = null;
        try {
            ChromeOptions desiredCapabilitiesForChrome = getDesiredCapabilitiesForChrome();
            desiredCapabilitiesForChrome.setBinary("C:\\Users\\ecc_auto\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");
            driver = new RemoteWebDriver(new URL(hubUrl), desiredCapabilitiesForChrome.merge(capabilities));
            ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
            setTimeouts(driver);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
        }
        return driver;
    }

    private static void setTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(DEFAULT_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
    }

    private static ChromeOptions getDesiredCapabilitiesForChrome() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("operations-type");
        options.addArguments("start-maximized");
        options.addArguments("--disable-popup-blocking");
//        options.addArguments("-incognito");
        options.addArguments("--disable-web-security");
        options.addArguments("allow-http-screen-capture");
        options.addArguments("allow-running-insecure-content");
        options.addArguments("disable-prompt-on-repost");
        options.setProxy(null);
        capabilities.setCapability("nativeEvents", false);
        capabilities.setJavascriptEnabled(true);
        return options.merge(capabilities);
    }

    private static InternetExplorerOptions getOptionsForIE() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        InternetExplorerOptions options = new InternetExplorerOptions();
        options.introduceFlakinessByIgnoringSecurityDomains();
        options.destructivelyEnsureCleanSession();
        options.requireWindowFocus();
        options.ignoreZoomSettings();
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        options.waitForUploadDialogUpTo(20, TimeUnit.SECONDS);
        options.withAttachTimeout(90, TimeUnit.SECONDS);
        options.setCapability("nativeEvents", Boolean.valueOf(false));
        capabilities.setCapability(NATIVE_EVENTS, false);
        capabilities.setBrowserName("internet explorer");
        capabilities.setPlatform(Platform.WINDOWS);
        capabilities.setJavascriptEnabled(true);

        addLoggingPreferences(capabilities);
        return options.merge(capabilities);
    }

    private static FirefoxOptions getFireFoxCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.acceptInsecureCerts();
        desiredCapabilities.setJavascriptEnabled(true);
        desiredCapabilities.setCapability("marionette", true);

        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);

        FirefoxOptions ffOptions = new FirefoxOptions();
        ffOptions.setProfile(profile);

        return ffOptions.merge(desiredCapabilities);
    }

    /*doesn't work with IE, but can be used with FF_REMOTE/Chrome*/
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

    public static WebDriver getDriver(DriverType driverType, Method method) throws Exception {
        DriversFactory[] values = DriversFactory.values();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("name", method.getName());
        capabilities.setCapability("build", getBuildName());
        for (DriversFactory value : values) {
            if (value.driverType.equals(driverType)) {
                return value.getDriverInstance(capabilities);
            }
        }
        return CHROME.getDriverInstance(capabilities);
    }

    private static String getBuildName() {
        String suiteName = System.getProperty("usedTestSuite") == null ? "custom suite" : System.getProperty("usedTestSuite");
        String buildInfo = System.getProperty("buildInfo") == null ? "" : System.getProperty("buildInfo");
        return suiteName + " " + buildInfo;
    }

    protected abstract WebDriver getDriverInstance(DesiredCapabilities capabilities) throws MalformedURLException;

    public static class Timeout {

        public static final int DEFAULT_IMPLICIT_WAIT = 15;
        public static final int DEFAULT_SCRIPT_TIMEOUT = 60;
    }
}

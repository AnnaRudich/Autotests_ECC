package com.scalepoint.automation.utils.driver;

import com.scalepoint.automation.utils.data.TestData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public enum DriversFactory {

    IE(DriverType.IE) {
        @Override
        protected WebDriver getDriverInstance() {
            if (System.getProperty("webdriver.ie.driver") == null) {
                File ieDriver = new File("src/main/resources/drivers/IEDriverServer.exe");
                System.setProperty("webdriver.ie.driver", ieDriver.getAbsolutePath());
            }
            DesiredCapabilities capabilities = getDesiredCapabilitiesForIE();
            return new InternetExplorerDriver(capabilities);
        }
    },

    IE_REMOTE(DriverType.IE_REMOTE) {
        @Override
        protected WebDriver getDriverInstance() throws MalformedURLException {
            DesiredCapabilities capabilities = getDesiredCapabilitiesForIE();
            WebDriver driver = new RemoteWebDriver(new URL(TestData.getLinks().getHubLink()), capabilities);
            driver = new Augmenter().augment(driver);
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
            try {
                File file = new File(this.getClass().getClassLoader().getResource("data/chromedriver.exe").toURI());
                System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("operations-type");
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
        return capabilities;
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
}

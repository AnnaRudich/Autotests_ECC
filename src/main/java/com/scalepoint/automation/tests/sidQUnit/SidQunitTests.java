package com.scalepoint.automation.tests.sidQUnit;

import com.codeborne.selenide.WebDriverRunner;
import com.scalepoint.automation.spring.Application;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.driver.DriverHelper;
import com.scalepoint.automation.utils.driver.DriverType;
import com.scalepoint.automation.utils.driver.DriversFactory;
import com.scalepoint.automation.utils.threadlocal.Browser;
import com.scalepoint.automation.utils.threadlocal.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringApplicationConfiguration(classes = Application.class)
@TestExecutionListeners(inheritListeners = false, listeners = {
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
public class SidQunitTests extends AbstractTestNGSpringContextTests {

    protected Logger log = LogManager.getLogger(SidQunitTests.class);

    @Value("${driver.type}")
    private String browserMode;

    @BeforeMethod
    public void baseInit(Method method, ITestContext context) throws Exception {
        Thread.currentThread().setName("Thread "+method.getName());
        ThreadContext.put("sessionid", method.getName());
        log.info("Starting {}, thread {}", method.getName(), Thread.currentThread().getId());

        DriverType driverType = new DriverHelper().getDriverType(method, browserMode);

        WebDriver driver = DriversFactory.getDriver(driverType, method);

        Browser.init(driver, driverType);
        Window.init(driver);
        WebDriverRunner.setWebDriver(driver);
    }

    @RunOn(DriverType.CHROME_REMOTE)
    @Test(dataProvider = "qunitTests", description = "Check results from qunit test in ecc")
    public void qunitMatchingEngineAdapterTests(String test) {
        List<WebElement> results = openPage(test);
        assertThat(results.stream().noneMatch(findFailed()))
                .as(getErrorDescriptionForTest(results, test))
                .isTrue();
    }

    @RunOn(DriverType.IE_REMOTE)
    @Test(dataProvider = "qunitTests", description = "Check results from qunit test in ecc")
    public void qunitMatchingEngineAdapterOnIETests(String test) {
        List<WebElement> results = openPage(test);
        assertThat(results.stream().noneMatch(findFailed()))
                .as(getErrorDescriptionForTest(results, test))
                .isTrue();
    }

    private List<WebElement> openPage(String path) {
        Browser.driver().get(getEccUrl() + "webshop/jsp/matching_engine/test/" + path);
        return Wait.forCondition(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[contains(@id,'qunit-test-output')]")));
    }

    private Predicate<WebElement> findFailed() {
        return e -> e.getAttribute("class").contains("fail");
    }

    private String getErrorDescriptionForTest(List<WebElement> results, String test) {
        return "Failed for " + test + ": " + results.stream().filter(findFailed()).map(this::getErrorMessage).collect(Collectors.joining(" | "));
    }

    private String getErrorMessage(WebElement element){
        return element.findElement(By.className("module-name")).getText() + " -> " +
                element.findElements(By.className("fail")).stream()
                        .map(e -> e.findElement(By.className("test-message")).getText()).collect(Collectors.joining(","));
    }

    @DataProvider(name = "qunitTests")
    public static Object[][] provide() {
        return new Object[][] {
                {"voucher-adapter-test.jsp"},
                {"settlement-panel-adapter-test.jsp"},
                {"settlement-item-adapter-test.jsp"},
                {"customer-mail-adapter-test.jsp"},
                {"customer-details-adapter-test.jsp"}
        };
    }

}

package com.scalepoint.automation.tests.sidQUnit;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.threadlocal.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.scalepoint.automation.utils.Configuration.getEccUrl;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SidQunitTests extends BaseTest {

    @Test(dataProvider = "qunitTests", description = "Check results from qunit test in ecc")
    public void qunitMatchingEngineAdapterTests(String test) {
        List<WebElement> results = openPage(test);
        assertThat(results.stream().noneMatch(getWebElementPredicateClassFail()))
                .as(getErrorDescription(results, test))
                .isTrue();
    }

    private List<WebElement> openPage(String path) {
        Browser.driver().get(getEccUrl() + "webshop/jsp/matching_engine/test/" + path);
        return Wait.forCondition(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[contains(@id,'qunit-test-output')]")));
    }

    private Predicate<WebElement> getWebElementPredicateClassFail() {
        return e -> e.getAttribute("class").contains("fail");
    }

    private Boolean findFailed(WebElement element) {
        return element.getAttribute("class").contains("fail");
    }

    private String getErrorMessage(WebElement element){
        return element.findElement(By.className("module-name")).getText() + " -> " +
                element.findElements(By.className("fail")).stream()
                        .map(e -> e.findElement(By.className("test-message")).getText()).collect(Collectors.joining(","));
    }

    private String getErrorDescription(List<WebElement> results, String test) {
        return "Failed for " + test + ": " + results.stream().filter(this::findFailed).map(this::getErrorMessage).collect(Collectors.joining(" | "));
    }

    @DataProvider(name = "qunitTests")
    public static Object[][] provide() {
        return new Object[][] {
                {"voucher-adapter-test.jsp"},
                {"settlement-panel-adapter-test.jsp"},
                {"settlement-item-adapter-test.jsp"},
                {"settlement-item-adapter-test.jsp"},
                {"customer-details-adapter-test.jsp"}
        };
    }

}

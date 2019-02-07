package com.scalepoint.automation.tests.sidQUnit;

import com.scalepoint.automation.tests.BaseTest;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.RunOn;
import com.scalepoint.automation.utils.driver.DriverType;
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

    @RunOn(DriverType.CHROME_REMOTE)
    @Test(dataProvider = "qunitTests", description = "Check results from qunit convert in ecc")
    public void qunitMatchingEngineAdapterTests(String test) {
        List<WebElement> results = openPage(test);
        assertThat(results.stream().noneMatch(findFailed()))
                .as(getErrorDescriptionForTest(results, test))
                .isTrue();
    }

    @RunOn(DriverType.IE_REMOTE)
    @Test(dataProvider = "qunitTests", description = "Check results from qunit convert in ecc")
    public void qunitMatchingEngineAdapterOnIETests(String test) {
        List<WebElement> results = openPage(test);
        assertThat(results.stream().noneMatch(findFailed()))
                .as(getErrorDescriptionForTest(results, test))
                .isTrue();
    }

    private List<WebElement> openPage(String path) {
        Browser.driver().get(getEccUrl() + "webshop/jsp/matching_engine/convert/" + path);
        return Wait.forCondition(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[contains(@id,'qunit-convert-output')]")));
    }

    private Predicate<WebElement> findFailed() {
        return e -> e.getAttribute("class").contains("fail");
    }

    private String getErrorDescriptionForTest(List<WebElement> results, String test) {
        return "\nFAILED for " + test + ": " + results.stream().filter(findFailed()).map(this::getErrorMessage).collect(Collectors.joining(" | "));
    }

    private String getErrorMessage(WebElement element){
        return element.findElement(By.className("module-name")).getText() + " -> " +
                element.findElements(By.className("fail")).stream()
                        .map(e -> e.findElement(By.className("convert-message")).getText()).collect(Collectors.joining(","));
    }

    @DataProvider(name = "qunitTests")
    public static Object[][] provide() {
        return new Object[][] {
                {"voucher-adapter-convert.jsp"},
                {"settlement-panel-adapter-convert.jsp"},
                {"settlement-item-adapter-convert.jsp"},
                {"customer-mail-adapter-convert.jsp"},
                {"customer-details-adapter-convert.jsp"}
        };
    }

}

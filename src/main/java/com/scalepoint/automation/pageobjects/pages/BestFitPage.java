package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.testng.Assert.assertTrue;

@EccPage
public class BestFitPage extends Page {

    private static final String MARKET_PRICE_XPATH = "//td[contains(@class,'bestfitleftheader_bottom') and contains(text(),'Markedspris')]";

    @Override
    protected BestFitPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForJavascriptRecalculation();
        waitForPageLoaded();
        waitForAjaxCompleted();
        $(By.id("table1")).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return null;
    }

    @Override
    protected String getRelativeUrl() {
        return "BestFit2";
    }

    public BestFitPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return BestFitPage.this;
    }

    public class Asserts {
        public Asserts assertMarketPriceInvisible() {
            assertTrue(driver.findElements(By.id(MARKET_PRICE_XPATH)).isEmpty(), "Market Price must be hidden");
            return this;
        }
    }
}

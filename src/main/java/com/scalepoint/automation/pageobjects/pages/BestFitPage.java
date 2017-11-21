package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.TextBlock;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertTrue;

@EccPage
public class BestFitPage extends Page {

    @FindBy(xpath = "//td[contains(@class,'bestfitleftheader_bottom') and contains(text(),'Markedspris')]")
    private TextBlock marketPrice;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForDisplayed(By.id("table1"));
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
            assertTrue(Wait.invisible($(marketPrice)), "Market Price must be hidden");
            return this;
        }
    }
}

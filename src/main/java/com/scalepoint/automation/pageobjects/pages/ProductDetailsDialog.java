package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ProductDetailsDialog extends BaseDialog {

    private static final String MARKET_PRICE = "#market_price";
    private static final String MARKET_PRICE_SUPPLIER = "#market_price_supplier";
    private static final String CLOSE_BUTTON = "button[title=close]";
    private static int WAIT_TIMEOUT = 6000;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        switchToFrame();
    }

    public SelenideElement getMarketPrice(){

        return $(MARKET_PRICE);
    }

    public SelenideElement getMarketPriceSupplier(){

        return $(MARKET_PRICE_SUPPLIER);
    }

    public TextSearchPage closeProductDetails() {
        switchToParentFrame();
        $(CLOSE_BUTTON).click();
        return Page.at(TextSearchPage.class);
    }

    public ProductDetailsDialog doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return ProductDetailsDialog.this;
    }

    public class Asserts {
        public Asserts assertMarketPriceSupplierVisible() {
            assertThat(getMarketPriceSupplier().waitUntil(Condition.visible, WAIT_TIMEOUT).isDisplayed())
                    .as("Market price supplier must be shown")
                    .isTrue();
            return this;
        }

        public Asserts assetMarketPriceSupplierInvisible() {
            assertThat(getMarketPriceSupplier().waitUntil(Condition.not(Condition.visible), WAIT_TIMEOUT).isDisplayed())
                    .as("Market price supplier must be hidden")
                    .isFalse();
            return this;
        }

        public Asserts assertMarketPriceVisible() {
            assertThat(getMarketPrice().waitUntil(Condition.visible, WAIT_TIMEOUT).isDisplayed())
                    .as("Market price must be shown")
                    .isTrue();
            return this;
        }

        public Asserts assertMarketPriceInvisible() {
            assertThat(getMarketPrice().waitUntil(Condition.not(Condition.exist), WAIT_TIMEOUT).exists())
                    .as("Market price must be hidden")
                    .isFalse();
            return this;
        }
    }
}

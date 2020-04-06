package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@EccPage
public class ProductDetailsPage extends Page {

    @FindBy(id = "luk")
    private Button closeWindow;

    @FindBy(id = "market_price")
    private WebElement marketPriceValue;

    @FindBy(id = "market_price_supplier")
    private WebElement marketPriceSupplier;

    @Override
    protected void ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/product_details_popup.jsp";
    }

    public TextSearchPage closeProductDetails() {
        closeDialog(closeWindow);
        return at(TextSearchPage.class);
    }

    public ProductDetailsPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return ProductDetailsPage.this;
    }

    public class Asserts {
        public Asserts assertMarketPriceSupplierVisible() {
            assertTrue(Wait.forCondition1s(driver -> marketPriceSupplier.isDisplayed()), "Market price supplier must be shown");
            return this;
        }

        public Asserts assetMarketPriceSupplierInvisible() {
            assertFalse(Wait.checkIsDisplayed(marketPriceSupplier), "Market price supplier must be hidden");
            return this;
        }

        public Asserts assertMarketPriceVisible() {
            assertTrue(Wait.forCondition1s(driver -> marketPriceValue.isDisplayed()), "Market price must be shown");
            return this;
        }

        public Asserts assertMarketPriceInvisible() {
            assertFalse(Wait.checkIsDisplayed(marketPriceValue), "Market price must be hidden");
            return this;
        }
    }
}

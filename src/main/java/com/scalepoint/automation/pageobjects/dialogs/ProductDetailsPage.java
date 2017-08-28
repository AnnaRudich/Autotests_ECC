package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.TextSearchPage;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
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
    protected Page ensureWeAreOnPage() {
        switchToLast();
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/product_details_popup.jsp";
    }

    public TextSearchPage closeProductDetails(){
        closeDialog(closeWindow);
        return at(TextSearchPage.class);
    }

    public ProductDetailsPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return ProductDetailsPage.this;
    }

    public class Asserts {
        public Asserts assertMarketPriceSupplierVisible(){
            assertTrue(Wait.visible(marketPriceSupplier), "Market price supplier must be shown");
            return this;
        }

        public Asserts assetMarketPriceSupplierInvisible(){
            assertTrue(Wait.invisible($(marketPriceSupplier)), "Market price supplier must be hidden");
            return this;
        }

        public Asserts assertMarketPriceVisible(){
            assertTrue(Wait.visible(marketPriceValue), "Market price must be shown");
            return this;
        }

        public Asserts assertMarketPriceInvisible(){
            assertTrue(Wait.invisible($(marketPriceValue)), "Market price must be hidden");
            return this;
        }
    }
}

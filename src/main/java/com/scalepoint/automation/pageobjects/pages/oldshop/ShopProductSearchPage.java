package com.scalepoint.automation.pageobjects.pages.oldshop;

import com.scalepoint.automation.pageobjects.modules.oldshop.AccountBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.RandomUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;

@EccPage
public class ShopProductSearchPage extends ShopFlow {

    private String byCategoryNameXpath = "(//*[@class='category_label'])[position() > 1]//a[contains(.,'$1')]";
    private String byCategoryNameDetailsXpath = "(//*[@class='category_label'])[position() > 1][contains(.,'$1')]";

    @FindBy(id = "TextSearch_text")
    private WebElement searchField;

    @FindBy(xpath = "//a[@class='button_search']")
    private WebElement searchButton;

    @FindBy(xpath = "//a[@class='button_product_center']")
    private List<WebElement> addToCartButton;

    @FindBy(xpath = "//a[@class = 'button_product']")
    private List<WebElement> addToCartButtonNL;

    @FindBy(xpath = "//td[contains(@class, 'purchase_price')]")
    private List<WebElement> productPrices;

    @FindBy(xpath = "//td[contains(@class,'teaser_headline')]/span/a")
    private List<WebElement> productNames;

    @FindBy(xpath = "//td[contains(@class,'teaser_headline')]/span/a")
    private WebElement productName;

    @FindBy(xpath = "(//div[@class='component_headline']/span)[1]")
    private WebElement productHeadline;

    @FindBy(id = "div_purchasePrice")
    private WebElement productPriceDetails;

    @FindBy(xpath = "(//*[@class='category_label'])[position() > 1]//a")
    private List<WebElement> categoryTreeSecondLevel;

    @FindBy(xpath = "(//*[@class='category_label'])[position() > 1]")
    private List<WebElement> categoryTreeSecondLevelDetails;

    private String byProductPricesXpath = "//td[contains(@class, 'purchase_price')][contains(.,'$1')]";

    private AccountBox accountBox = new AccountBox();

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(searchField);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "shop/search";
    }

    public void clickSearch() {
        clickAndWaitForStable(searchButton, By.xpath("//div[contains(@class,'breadcrumb_path')]/span/a"));
    }

    public ShopProductSearchPage searchForProduct(String query) {
        setValue(searchField, query);
        clickSearch();
        return this;
    }

    public void selectExistingCategory(String category) {
        $(By.xpath(byCategoryNameXpath.replace("$1", category))).click();
    }

    /**
     * This method adds search query and makes search with category selection
     */
    public void makeSearchWithCategory(String category, String query) {
        selectExistingCategory(category);
        sendKeys(searchField, query);
        clickSearch();
    }

    public Double getProductPrice(Integer n) {
        return OperationalUtils.toNumber(getText(productPrices.get(n)));
    }

    public boolean isRequiredPriceDisplayed(Double price) {
        try {
            logger.info("Required price is: {}", price);
            String formattedPrice = OperationalUtils.format(price);
            find(byProductPricesXpath, formattedPrice);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public String getProductName(Integer n) {
        return OperationalUtils.unifyStr(getText(productNames.get(n)));
    }

    public ShopProductSearchPage addProductToCart(Integer n) {
        addToCartButton.get(n).click();
        Wait.waitForDisplayed(By.xpath("//*[@id='AccountInfoBox_shopping_cart']//td[@class='description']"));
        return this;
    }

    public ShopShoppingCartPage toShoppingCart() {
        return getAccountBox().toShoppingCart();
    }

    public void viewProductDetails(Integer n) {
        clickAndWaitForStable(productNames.get(n), By.xpath("(//div[@class='component_headline']/span)[1]"));
    }

    public String getProductHeadLineText() {
        return OperationalUtils.unifyStr(getText(productHeadline));
    }

    public Double getProductPriceDetails(Integer n) {
        return OperationalUtils.toNumber(getText(productPriceDetails));
    }

    public String getCategoryNameSecondLevel(Integer n) {
        return getText(categoryTreeSecondLevelDetails.get(n));
    }

    public ShopProductSearchPage selectCatalogueCategory(String category) {
        WebElement item = find(byCategoryNameXpath, category);
        if (item.getText().contains(category)) {
            scrollTo(item);
            clickAndWaitForStable(item, By.xpath("//div[contains(@class,'breadcrumb_path')]/span/a"));
        }
        return this;
    }

    public void openRandomProductDetails() {
        WebElement product = productNames.get(RandomUtils.randomInt(productNames.size()));
        clickAndWaitForStable(product, By.xpath("(//div[@class='component_headline']/span)[1]"));
    }

    public AccountBox getAccountBox() {
        return accountBox;
    }


    public ShopProductSearchPage doAssert(Consumer<Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return ShopProductSearchPage.this;
    }

    public class Asserts {

        public Asserts assertRequiredPriceIsDisplayed(Double requiredPrice) {
            Assert.assertTrue(isRequiredPriceDisplayed(requiredPrice));
            return this;
        }

    }

}

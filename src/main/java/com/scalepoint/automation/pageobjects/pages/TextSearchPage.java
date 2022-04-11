package com.scalepoint.automation.pageobjects.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.modules.textSearch.Attributes;
import com.scalepoint.automation.pageobjects.modules.textSearch.TextSearchAttributesMenu;
import com.scalepoint.automation.services.externalapi.SolrApi;
import com.scalepoint.automation.shared.ProductInfo;
import com.scalepoint.automation.shared.SortOrder;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.PseudoCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.*;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class TextSearchPage extends Page {

    @FindBy(css = ".matchbutton")
    private SelenideElement match;
    @FindBy(id = "didYouMeanHrefId")
    private SelenideElement didYouMeanLink;
    @FindBy(id = "startSnappingHrefId")
    private SelenideElement snappingCategory;
    @FindBy(id = "details0")
    private SelenideElement firstProductDetails;
    @FindBy(xpath = "//a[contains(@onclick,'market_price')]")
    private SelenideElement sortByMarketPrice;
    @FindBy(xpath = "//a[contains(@onclick,'popularity')]")
    private SelenideElement sortByPopularity;
    @FindBy(id = "textSearchInput")
    private SelenideElement searchInput;
    @FindBy(xpath = "//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_az.gif')]")
    private SelenideElement ascendantMarketPrice;
    @FindBy(xpath = "//button[contains(@onclick, 'backToSettlement()')]")
    private SelenideElement backToSettlementButton;
    @FindBy(xpath = "//div[@id='productsTable']//span[contains(@id,'brand')]")
    private ElementsCollection brandList;
    @FindBy(xpath = "//label/span[contains(@id,'model')]")
    private ElementsCollection modelList;
    @FindBy(xpath = "//div[@id='productsTable']//span[contains(@id,'brand')]/parent::td")
    private ElementsCollection resultsCategoriesList;
    @FindBy(css = ".ygtvitem span span")
    private ElementsCollection categoriesList;
    @FindBy(xpath = "//span[contains(@id,'productSpecificationToggle')]")
    private ElementsCollection productsSpecifications;

    private Link getSortByOrderable(){

        return new Link($(By.xpath("(//div[@id='productsTable']//a[@class='darkBlueUL'])[last()]")));
    }

    private Button getBestFit(){

        return new Button($(By.cssSelector(".bestfitbutton")));
    }

    private Button getSearch(){

        return new Button($(By.id("searchButton")));
    }

    private Button getModelButton(){

        return new Button($(By.id("modelsButton")));
    }

    private Button getBrandButton(){

        return new Button($(By.id("brandsButton")));
    }

    private Button getAttributeButton(){

        return new Button($(By.id("attButton")));
    }

    private Image getDescendantMarketPrice(){

        return new Image($(By.xpath("//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_za.gif')]")));
    }

    private Image getDescendingOrderable(){

        return new Image($(By.xpath("//img[@id='sortOrderableImg' and contains(@src,'text_search\\icon_order_za.gif')]")));
    }

    private Image getAscendingOrderable(){

        return new Image($(By.xpath("//img[@id='sortOrderableImg' and contains(@src,'text_search\\icon_order_az.gif')]")));
    }

    private Image getDescendingPopularity(){

        return new Image($(By.xpath("//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_za.gif')]")));
    }

    private Image getAscendingPopularity(){

        return new Image($(By.xpath("//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_az.gif')]")));
    }

    private Select getBrandSelect(){

        return new Select($(By.id("brandSelectionObj")));
    }

    private Select getModelSelect(){

        return new Select($(By.id("modelSelectObj")));
    }

    private List<Table> getAtrributeTables(){

        return $$(By.xpath("//div[contains(@id,'productAttributeSelect')][@class='resultsTableNorm']/table"))
                .stream()
                .map(Table::new)
                .collect(Collectors.toList());
    }

    private Button getCreateManually(){

        return new Button($(By.id("createManualLineButton")));
    }

    private List<Button> getMatchButtons(){

        return $$(By.cssSelector(".matchbutton"))
                .stream()
                .map(Button::new)
                .collect(Collectors.toList());
    }

    private By fieldSetDisabled = By.xpath("//fieldset[@id='resultFieldSet'] [@disabled]");
    private By fieldSetNotDisabled = By.xpath("//fieldset[@id='resultFieldSet'] [not(@disabled)]");

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/TextSearch.jsp";
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(By.id("categoryLegend")).should(Condition.visible);
    }

    public TextSearchPage sortOrderableFirst() {

        return sort(getSortByOrderable(), getAscendingOrderable());
    }

    public TextSearchPage sortNonOrderableFirst() {

        return sort(getSortByOrderable(), getDescendingOrderable());
    }

    private TextSearchPage sort(WebElement sortLink, WebElement sortIconToWait) {

        int totalAttempts = 10;
        int currentAttempt = 0;
        while (currentAttempt < totalAttempts) {

            try {

                $(sortLink).click();
                $(sortIconToWait).should(visible);
            } catch (Throwable e) {

                logger.info(e.getMessage());
            }
            break;
        }
        Wait.waitForAjaxCompletedAndJsRecalculation();
        return Page.at(TextSearchPage.class);
    }

    public SettlementPage toSettlementPage() {

        backToSettlementButton.click();
        return Page.to(SettlementPage.class);
    }

    public TextSearchPage sortMarketPricesAscending() {

        return sort(sortByMarketPrice, ascendantMarketPrice);
    }

    public TextSearchPage sortPopularityAscending() {

        return sort(sortByPopularity, getAscendingOrderable());
    }

    public TextSearchPage sortPopularityDescending() {

        return sort(sortByPopularity, getDescendingPopularity());
    }

    public TextSearchPage sortMarketPricesDescending() {

        return sort(sortByMarketPrice, getDescendantMarketPrice());
    }

    public boolean isSortingMarketPriceAscendant() {

        return ascendantMarketPrice.isDisplayed();
    }

    public boolean isSortingMarketPriceDescendant() {

        return getDescendantMarketPrice().isDisplayed();
    }

    public BestFitPage toBestFitPage() {

        waitForAjaxCompletedAndJsRecalculation();
        getBestFit().click();
        return at(BestFitPage.class);
    }

    public ProductDetailsDialog openProductDetailsOfFirstProduct() {

        verifyElementVisible(match);
        waitForAjaxCompleted();
        firstProductDetails.click();
        return BaseDialog.at(ProductDetailsDialog.class);
    }

    public SettlementDialog openSidForFirstProduct() {

        Wait.waitForAjaxCompleted();
        SelenideElement element = match;
        hoverAndClick(element);
        if (!BaseDialog.isOn(SettlementDialog.class)) {

            hoverAndClick(element);
        }
        return BaseDialog
                .at(SettlementDialog.class);
    }

    public SettlementDialog openSidForProductWithVoucher() {

        waitForAjaxCompleted();
        verifyElementVisible(match);
        hoverAndClick($(match));
        SettlementDialog settlementDialog = BaseDialog.at(SettlementDialog.class);
        if (!settlementDialog.isDiscountDistributionDisplayed()) {

            settlementDialog.cancel(TextSearchPage.class);
            getMatchButtons().get(1).click();
        }
        return BaseDialog.at(SettlementDialog.class);
    }

    public SettlementDialog match(String productDescription) {

        Wait.waitForAjaxCompletedAndJsRecalculation();
        waitForStaleElement(By.cssSelector("#productsTable table td"));
        ElementsCollection matchButtons = $$(By.xpath(".//*[@id='productsTable']//button[@class='matchbutton']"));
        if (matchButtons.isEmpty()) {

            throw new IllegalStateException("No text search results found!");
        }
        clickOnFirstMatchingAndWaitForSID();
        return BaseDialog.at(SettlementDialog.class);
    }

    private void clickOnFirstMatchingAndWaitForSID() {

        int i = 1;
        logger.info("Trying open SID attempt: " + i);
        $(By.xpath("//button[@class='matchbutton']/img[1]")).shouldBe(Condition.visible).click();
        while (!BaseDialog.isOn(SettlementDialog.class) && i < 4) {

            i++;
            logger.info("Trying open SID attempt: " + i);
            $(By.xpath("//button[@class='matchbutton']/img[1]")).shouldBe(Condition.visible).click();
        }
    }

    public SettlementDialog matchStrict(String productDescription) {

        Wait.waitForAjaxCompleted();
        waitForStaleElement(By.cssSelector("#productsTable table td"));
        sortOrderableFirst();
        By matchButtonsLocator = By.xpath(".//*[@id='productsTable']//span[contains(text(), '" + productDescription + "')]/ancestor::td[1]/..//button[@class='matchbutton']");
        ElementsCollection matchButtons = $$(matchButtonsLocator);
        if (matchButtons.isEmpty()) {

            throw new IllegalStateException("No text search results found!");
        }
        waitForStaleElements(matchButtonsLocator).get(0).click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public TextSearchPage chooseCategory(String _category) {

        verifyElementVisible($(By.cssSelector("#categoryFieldSet table:first-child")));
        ElementsCollection categories = $$(By.cssSelector(".ygtvitem span span"));
        forCondition(ExpectedConditions.elementToBeClickable(categories.stream().filter(category -> category.getText().contains(_category)).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find category: " + _category)))).click();
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage chooseCategory(PseudoCategory pseudoCategory) {

        verifyElementVisible($(By.cssSelector("#categoryFieldSet table:first-child")));
        ElementsCollection categories = $$(By.cssSelector(".ygtvitem span span"));
        forCondition(ExpectedConditions.elementToBeClickable(categories.stream().filter(category -> category.getText().contains(pseudoCategory.getGroupName())).findFirst().orElseThrow(() -> new NoSuchElementException("Can't find category: " + pseudoCategory.getGroupName())))).click();
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage searchByProductName(String productName) {

        return searchBy(productName);
    }

    public TextSearchPage searchByProductNameAndCategory(String productName, String category) {

        return searchBy(productName + " " + category);
    }

    public TextSearchPage searchBySku(String sku) {

        return searchBy("SKU:" + sku);
    }

    private TextSearchPage searchBy(String productName) {

        searchProduct(productName);
        getSearch().click();
        Wait.waitForAjaxCompleted();
        waitForStaleElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }

    private void searchProduct(String productName) {

        SelenideElement element = searchInput;
        try {
            int attempt = 0;
            do {

                element.setValue(productName);
                attempt++;
            } while (!element.getValue().contains(productName) || attempt < 10);
        } catch (InvalidElementStateException e) {

            logger.error("The Product name has not been entered!");
        }
    }

    private void waitForSuggestions() {

        verifyElementVisible($(By.xpath("//div[@id='suggest_div']/table")));
    }

    public TextSearchPage searchProductAndSelectFirstSuggestion(String productName) {

        searchInput.sendKeys(productName);
        waitForSuggestions();
        searchInput.sendKeys(Keys.ARROW_DOWN);
        searchInput.sendKeys(Keys.ENTER);
        Wait.waitForAjaxCompleted();
        waitForStaleElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }

    public String getSearchInputText() {

        return searchInput.getValue();
    }

    public String getFirstProductId() {

        waitForAjaxCompleted();
        verifyElementVisible($(By.xpath("(.//*[@id='productsTable']/table//td[@productid])[1]")));
        return $(By.xpath("(.//*[@id='productsTable']//tr[..//button[@class='matchbutton']]//td[@productid])")).attr("productid");
    }

    public TextSearchPage selectBrand(String text) {

        forCondition(ExpectedConditions.elementToBeClickable(getBrandButton())).click();
        waitElementVisible($(getBrandSelect())).selectOption(text);
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage selectModel(String text) {

        forCondition(ExpectedConditions.elementToBeClickable(getModelButton())).click();
        waitElementVisible($(getModelSelect())).selectOption(text);
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage waitForResultsLoad() {

        try {

            waitElementVisible($(fieldSetDisabled));
        }catch (ElementNotFound | ElementShould e) {

            logger.info(e.getMessage());
        }
        waitElementVisible($(fieldSetNotDisabled));
        Wait.waitForAjaxCompleted();
        return this;
    }

    public TextSearchAttributesMenu openAttributesMenu() {

        forCondition(ExpectedConditions.elementToBeClickable(getAttributeButton())).click();
        return new TextSearchAttributesMenu();
    }

    public TextSearchPage openSpecificationForItem(int index) {

        waitForResultsLoad();
        forCondition(ExpectedConditions.elementToBeClickable(productsSpecifications.get(index))).click();
        waitForElementContainsText($(productsSpecifications.get(index)), "-");
        return this;
    }

    public SettlementDialog openSid() {

        getCreateManually().click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public TextSearchPage clickOnDidYouMean() {

        didYouMeanLink.click();
        return this;
    }

    public TextSearchPage snapCategory() {

        snappingCategory.click();
        return this;
    }

    private List<String> getProductIds() {

        Wait.waitForAjaxCompleted();
        ElementsCollection elements = $$(By.xpath("(.//*[@id='productsTable']/table//td[@productid])"));
        return elements.stream().map(e -> e.attr("productId")).collect(Collectors.toList());
    }

    public TextSearchPage doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return TextSearchPage.this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public class Asserts {

        public Asserts assertSortingMarketPriceAscendant() {

            Assert.assertTrue(isSortingMarketPriceAscendant(), "Ascendant sorting of Market Price does not work");
            return this;
        }

        public Asserts assertSortingMarketPriceDescendant() {

            Assert.assertTrue(isSortingMarketPriceDescendant(), "Descendant sorting of Market Price does not work");
            return this;
        }

        public Asserts assertMarketPriceSortingInvisible() {

            Assert.assertFalse(verifyElementVisible(ascendantMarketPrice), "Market price sorting still visible");
            return this;
        }

        public Asserts assertSearchResultsContainsSearchModel(String target) {

            assertThat(modelList.stream().allMatch(element -> element.getText().contains(target))).isTrue();
            return this;
        }

        public Asserts assertSearchResultsContainsSearchBrand(String target) {

            assertThat(brandList.stream().allMatch(element -> element.getText().contains(target))).isTrue();
            return this;
        }

        public Asserts assertSearchQueryContainsBrandAndModel(String query) {

            assertThat(modelList.stream().allMatch(element -> containsIgnoreCase(query, element.getText()))).isTrue();
            assertThat(brandList.stream().allMatch(element -> containsIgnoreCase(query, element.getText()))).isTrue();
            return this;
        }

        public Asserts assertSearchResultsContainsSearchCategory(String target) {

            assertThat(resultsCategoriesList.stream().allMatch(element -> element.getText().contains(target))).isTrue();
            return this;
        }

        public Asserts assertSearchResultsCategoryIsEmpty() {

            assertThat(resultsCategoriesList).isEmpty();
            return this;
        }

        public Asserts assertAttributeResultContains(int index, Attributes... attributes) {

            Arrays.stream(attributes).forEach(
                    attribute -> {
                        String itemAttr = getAtrributeTables().get(index).getRowsAsString()
                                .stream().filter(row -> row.get(0).contains(attribute.getName())).findAny().get().get(1);
                        assertThat(Arrays.stream(attribute.getOptions()).anyMatch(option -> option.contains(itemAttr.trim()))).isTrue();
                    }
            );
            return this;
        }

        public Asserts assertIsDidYouMeanDisplayed() {

            assertThat(didYouMeanLink.isDisplayed()).isTrue();
            return this;
        }

        public Asserts assertQueryContainsDidYouMeanText(String query) {

            assertThat(query).contains(didYouMeanLink.getText());
            return this;
        }

        public Asserts assertNoPopularitySortChosen() {

            boolean noAscendingImagesOnPage = $(By.xpath("//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_za.gif')]")).is(not(visible));
            boolean noDescendingImagesOnPage = $(By.xpath("//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_za.gif')]")).is(not(visible));
            Assert.assertTrue(noAscendingImagesOnPage && noDescendingImagesOnPage);
            return this;
        }

        public Asserts assertAscendingPopularityChosen() {

            Assert.assertTrue(getAscendingPopularity().isDisplayed());
            return this;
        }

        public Asserts assertPopularityInCorrectOrder(SortOrder sortOrder) {

            List<ProductInfo> shownProducts = SolrApi.findProducts(getProductIds());
            if (shownProducts.isEmpty()) {
                Assert.fail("No results found");
            }
            boolean orderCorrect = true;
            int currentPopularity = shownProducts.get(0).getPopularityRating();
            for (int i = 1; i < shownProducts.size(); i++) {

                ProductInfo nextProduct = shownProducts.get(i);
                if (sortOrder.equals(SortOrder.DESCENDING) && nextProduct.getPopularityRating() > currentPopularity
                        ||
                        sortOrder.equals(SortOrder.ASCENDING) && nextProduct.getPopularityRating() < currentPopularity) {

                    orderCorrect = false;
                    break;
                }
                currentPopularity = nextProduct.getPopularityRating();
            }
            if (!orderCorrect) {

                logger.warn("Order of products in not correct. Required popularity sort: {}", sortOrder);
                shownProducts.forEach(logger::error);
            }
            Assert.assertTrue(orderCorrect, "Popularity Order is broken");
            return this;
        }
    }
}


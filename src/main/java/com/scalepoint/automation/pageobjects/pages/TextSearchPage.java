package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ProductDetailsPage;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.pageobjects.modules.textSearch.Attributes;
import com.scalepoint.automation.pageobjects.modules.textSearch.TextSearchAttributesMenu;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Image;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.forCondition;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompleted;
import static com.scalepoint.automation.utils.Wait.waitForDisplayed;
import static com.scalepoint.automation.utils.Wait.waitForElementContainsText;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;

@EccPage
public class TextSearchPage extends Page {

    @FindBy(xpath = "(//div[@id='productsTable']//a[@class='darkBlueUL'])[last()]")
    private Link sortByOrderable;

    @FindBy(css = ".matchbutton")
    private Button match;

    @FindBy(css = ".matchbutton")
    private List<Button> matchButtons;

    @FindBy(xpath = "//div[@id='productsTable']//span[contains(@id,'brand')]")
    private List<WebElement> brandList;

    @FindBy(xpath = "//label/span[contains(@id,'model')]")
    private List<WebElement> modelList;

    @FindBy(xpath = "//div[@id='productsTable']//span[contains(@id,'brand')]/parent::td")
    private List<WebElement> resultsCategoriesList;

    @FindBy(css = ".bestfitbutton")
    private Button bestFit;

    @FindBy(xpath = "//a[@id='details0']")
    private WebElement firstProductDetails;

    @FindBy(css = ".ygtvitem span span")
    private List<WebElement> categoriesList;

    @FindBy(xpath = "//a[contains(@onclick,'market_price')]")
    private WebElement sortByMarketPrice;

    @FindBy(xpath = "//a[contains(@onclick,'popularity')]")
    private WebElement sortByPopularity;

    @FindBy(id = "textSearchInput")
    private ExtInput searchInput;

    @FindBy(id = "searchButton")
    private Button search;

    @FindBy(xpath = "//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_az.gif')]")
    private Image ascendantMarketPrice;

    @FindBy(xpath = "//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_za.gif')]")
    private Image descendantMarketPrice;

    @FindBy(xpath = "//img[@id='sortOrderableImg' and contains(@src,'text_search\\icon_order_za.gif')]")
    private Image descendingOrderable;

    @FindBy(xpath = "//img[@id='sortOrderableImg' and contains(@src,'text_search\\icon_order_az.gif')]")
    private Image ascendingOrderable;

    @FindBy(xpath = "//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_za.gif')]")
    private Image descendingPopularity;

    @FindBy(xpath = "//img[@id='sortPopularityImg' and contains(@src,'text_search\\icon_order_az.gif')]")
    private Image ascendingPopularity;

    @FindBy(xpath = "//button[contains(@onclick, 'backToSettlement()')]")
    private WebElement backToSettlementButton;

    @FindBy(id = "brandSelectionObj")
    private Select brandSelect;

    @FindBy(id = "brandsButton")
    private Button brandButton;

    @FindBy(id = "modelSelectObj")
    private Select modelSelect;

    @FindBy(id = "modelsButton")
    private Button modelButton;

    @FindBy(id = "attButton")
    private Button attributeButton;

    @FindBy(xpath = "//span[contains(@id,'productSpecificationToggle')]")
    private List<WebElement> productsSpecifications;

    @FindBy(xpath = "//div[contains(@id,'productAttributeSelect')][@class='resultsTableNorm']/table")
    private List<Table> atrributeTables;

    @FindBy(id = "createManualLineButton")
    private Button createManually;

    private By fieldSetDisabled = By.xpath("//fieldset[@id='resultFieldSet'] [@disabled]");
    private By fieldSetNotDisabled = By.xpath("//fieldset[@id='resultFieldSet'] [not(@disabled)]");

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/TextSearch.jsp";
    }

    @Override
    public TextSearchPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForDisplayed(By.id("categoryLegend"));
        return this;
    }

    public TextSearchPage sortOrderableFirst() {
        return sort(sortByOrderable, ascendingOrderable);
    }

    public TextSearchPage sortNonOrderableFirst() {
        return sort(sortByOrderable, descendingOrderable);
    }

    private TextSearchPage sort(WebElement sortLink, Image sortIconToWait) {
        int totalAttempts = 10;
        int currentAttempt = 0;
        while (currentAttempt < totalAttempts) {
            $(sortLink).click();
            Wait.waitForAjaxCompleted();
            Boolean isDisplayed = false;
            try {
                isDisplayed = sortIconToWait.isDisplayed();
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
            if (isDisplayed) {
                break;
            }
            currentAttempt++;
        }
        return this;
    }

    public SettlementPage toSettlementPage() {
        backToSettlementButton.click();
        return Page.to(SettlementPage.class);
    }

    public TextSearchPage sortMarketPricesAscending() {
        return sort(sortByMarketPrice, ascendantMarketPrice);
    }

    public TextSearchPage sortPopularityAscending() {
        return sort(sortByPopularity, ascendingPopularity);
    }

    public TextSearchPage sortPopularityDescending() {
        return sort(sortByPopularity, descendingPopularity);
    }

    public TextSearchPage sortMarketPricesDescending() {
        return sort(sortByMarketPrice, descendantMarketPrice);
    }

    public boolean isSortingMarketPriceAscendant() {
        return ascendantMarketPrice.isDisplayed();
    }

    public boolean isSortingMarketPriceDescendant() {
        return descendantMarketPrice.isDisplayed();
    }

    public BestFitPage toBestFitPage() {
        Wait.waitForPageLoaded();
        bestFit.click();
        return at(BestFitPage.class);
    }

    public ProductDetailsPage openProductDetailsOfFirstProduct() {
        openDialog(firstProductDetails);
        return at(ProductDetailsPage.class);
    }

    public SettlementDialog openSidForFirstProduct() {
        Wait.waitForAjaxCompleted();
        Wait.waitForVisible(match);
        match.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public SettlementDialog openSidForProductWithVoucher() {
        Wait.waitForAjaxCompleted();
        Wait.waitForVisible(match);
        match.click();
        SettlementDialog settlementDialog = BaseDialog.at(SettlementDialog.class);
        if (!settlementDialog.isDicountDistributionDisplayed()) {
            settlementDialog.cancel(TextSearchPage.class);
            matchButtons.get(1).click();
        }
        return BaseDialog.at(SettlementDialog.class);
    }

    public SettlementDialog match(String productDescription) {
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElement(By.cssSelector("#productsTable table td"));
        List<WebElement> matchButtons = driver.findElements(By.xpath(".//*[@id='productsTable']//*[text()[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), " +
                "'" + productDescription + "')]]/../..//button[@class='matchbutton']"));
        if (matchButtons.isEmpty()) {
            throw new IllegalStateException("No text search results found!");
        }
        clickOnFirstMatchingAndWaitForSID();

        return BaseDialog.at(SettlementDialog.class);
    }

    private void clickOnFirstMatchingAndWaitForSID() {
        int i = 1;
        logger.info("Trying open SID attempt: " + i);
        waitForDisplayed(By.xpath("//button[@class='matchbutton']/img[1]")).click();
        while (!BaseDialog.isOn(SettlementDialog.class) && i < 4) {
            i++;
            logger.info("Trying open SID attempt: " + i);
            driver.findElement(By.xpath("//button[@class='matchbutton']/img[1]")).click();
        }
    }

    public SettlementDialog matchStrict(String productDescription) {
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElement(By.cssSelector("#productsTable table td"));
        sortOrderableFirst();
        List<WebElement> matchButtons = driver.findElements(By.xpath(".//*[@id='productsTable']//span[contains(text(), '" + productDescription + "')]/ancestor::td[1]/..//button[@class='matchbutton']"));
        if (matchButtons.isEmpty()) {
            throw new IllegalStateException("No text search results found!");
        }
        matchButtons.get(0).click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public TextSearchPage chooseCategory(String _category) {
        waitForDisplayed(By.cssSelector("#categoryFieldSet table:first-child"));
        List<WebElement> categories = driver.findElements(By.cssSelector(".ygtvitem span span"));
        forCondition(ExpectedConditions.elementToBeClickable(categories.stream().filter(category -> category.getText().contains(_category)).findFirst().get())).click();
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage searchByProductName(String productName) {
        searchProduct(productName);
        search.click();
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }

    private void searchProduct(String productName) {
        try {
            int attempt = 0;
            do {
                searchInput.setValue(productName);
                attempt++;
            }
            while (!searchInput.getText().contains(productName) || attempt < 10);
        } catch (InvalidElementStateException e) {
            logger.error("The Product name has not been entered!");
        }
    }

    private void waitForSuggestions(){
        waitForDisplayed(By.xpath("//div[@id='suggest_div']/table"));
    }

    public TextSearchPage searchProductAndSelectFirstSuggestion(String productName){
        searchInput.sendKeys(productName);
        waitForSuggestions();
        searchInput.sendKeys(Keys.ARROW_DOWN);
        searchInput.sendKeys(Keys.ENTER);
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }

    public String getSearchInputText(){
        return searchInput.getText();
    }

    public String getFirstProductId() {
        Wait.waitForAjaxCompleted();
        Wait.waitForDisplayed(By.xpath("(.//*[@id='productsTable']/table//td[@productId])[1]"));
        return $(By.xpath("(.//*[@id='productsTable']//tr[..//button[@class='matchbutton']]//td[@productId])")).attr("productId");
    }

    public TextSearchPage selectBrand(String text) {
        forCondition(ExpectedConditions.elementToBeClickable(brandButton)).click();
        waitForVisible(brandSelect).selectByVisibleText(text);
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage selectModel(String text) {
        forCondition(ExpectedConditions.elementToBeClickable(modelButton)).click();
        waitForVisible(modelSelect).selectByVisibleText(text);
        waitForResultsLoad();
        return this;
    }

    public TextSearchPage waitForResultsLoad() {
        try {
            waitForDisplayed(fieldSetDisabled);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        waitForDisplayed(fieldSetNotDisabled);
        waitForAjaxCompleted();
        return this;
    }

    public TextSearchAttributesMenu openAttributesMenu() {
        forCondition(ExpectedConditions.elementToBeClickable(attributeButton)).click();
        return new TextSearchAttributesMenu();
    }

    public TextSearchPage openSpecificationForItem(int index) {
        waitForResultsLoad();
        forCondition(ExpectedConditions.elementToBeClickable(productsSpecifications.get(index))).click();
        waitForElementContainsText(productsSpecifications.get(index), "-");
        return this;
    }

    public SettlementDialog openSid(){
        createManually.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public List<WebElement> getBrandList() {
        return brandList;
    }

    public List<WebElement> getModelList() {
        return modelList;
    }

    public TextSearchPage doAssert(Consumer<Asserts> assertsFunc) {
        assertsFunc.accept(new Asserts());
        return TextSearchPage.this;
    }

    public class Asserts {
        public Asserts assertSortingMarketPriceAscendant() {
            Assert.assertTrue(isSortingMarketPriceAscendant(), "Ascendant sorting of Market Price does not work");
            return this;
        }

        public Asserts assertSortingMarketPriceDescendant() {
            Assert.assertTrue(isSortingMarketPriceDescendant(), "Descendant sorting of Market Price does not work");
            return this;
        }

        public Asserts assertMarketPriceInvisible() {
            Assert.assertTrue(Wait.invisible($(sortByMarketPrice)), "Market price still visible");
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
            assertThat(modelList.stream().allMatch(element -> query.contains(element.getText()))).isTrue();
            assertThat(brandList.stream().allMatch(element -> query.contains(element.getText()))).isTrue();
            return this;
        }

        public Asserts assertSearchResultsContainsSearchCategory(String target) {
            assertThat(resultsCategoriesList.stream().allMatch(element -> element.getText().contains(target))).isTrue();
            return this;
        }

        public Asserts assertAttributeResultContains(int index, Attributes... attributes) {
            Arrays.stream(attributes).forEach(
                    attribute -> {
                        String itemAttr = atrributeTables.get(index).getRowsAsString()
                                .stream().filter(row -> row.get(0).contains(attribute.getName())).findAny().get().get(1);
                        assertThat(Arrays.stream(attribute.getOptions()).anyMatch(option -> option.contains(itemAttr.trim()))).isTrue();
                    }
            );
            return this;
        }

        public Asserts assertActualBrandListIsDifferentThan(List<String> list) {
            assertThat(brandList.stream().map(WebElement::getText).collect(Collectors.toList())).isNotEqualTo(list);
            return this;
        }

        public Asserts assertActualModelListIsDifferentThan(List<String> list) {
            assertThat(modelList.stream().map(WebElement::getText).collect(Collectors.toList())).isNotEqualTo(list);
            return this;
        }
    }
}


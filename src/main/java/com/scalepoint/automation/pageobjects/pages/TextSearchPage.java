package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ProductDetailsPage;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Image;
import ru.yandex.qatools.htmlelements.element.Link;

import java.util.List;
import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForDisplayed;

@EccPage
public class TextSearchPage extends Page {

    @FindBy(xpath = "(//div[@id='productsTable']//a[@class='darkBlueUL'])[last()]")
    private Link sortByOrderable;

    @FindBy(css = ".matchbutton")
    private Button match;

    @FindBy(css = ".matchbutton")
    private List<Button> matchButtons;

    @FindBy(xpath = "//label/span[contains(@id,'brand')]")
    private List<WebElement> brandList;

    @FindBy(xpath = "//label/span[contains(@id,'model')]")
    private List<WebElement> modelList;

    @FindBy(css = ".bestfitbutton")
    private Button bestFit;

    @FindBy(xpath = "//a[@id='details0']")
    private WebElement firstProductDetails;

    @FindBy(css = ".ygtvitem span span")
    private List<WebElement> categoriesList;

    @FindBy(xpath = "//a[contains(@onclick,'market_price')]")
    private WebElement sortByMarketPrice;

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

    @FindBy(xpath = "//button[contains(@onclick, 'backToSettlement()')]")
    private WebElement backToSettlementButton;

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
            }catch (Exception e){
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
        if(!settlementDialog.isDicountDistributionDisplayed()){
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
        while(!BaseDialog.isOn(SettlementDialog.class) && i<4){
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
        List<WebElement> categories = categoriesList;
        categories.stream().filter(category -> category.getText().contains(_category)).findFirst().get().click();
        return this;
    }

    public TextSearchPage searchByProductName(String productName) {
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
        search.click();
        Wait.waitForAjaxCompleted();
        Wait.waitForStaleElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }

    public String getFirstProductId() {
        Wait.waitForAjaxCompleted();
        Wait.waitForDisplayed(By.xpath("(.//*[@id='productsTable']/table//td[@productId])[1]"));
        return $(By.xpath("(.//*[@id='productsTable']//tr[..//button[@class='matchbutton']]//td[@productId])")).attr("productId");
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

        public Asserts assertSearchResultsContainsSearchModel(String target){
            Assert.assertTrue(modelList.stream().allMatch(element -> element.getText().contains(target)));
            return this;
        }

        public Asserts assertSearchResultsContainsSearchBrand(String target){
            Assert.assertTrue(brandList.stream().allMatch(element -> element.getText().contains(target)));
            return this;
        }
    }
}


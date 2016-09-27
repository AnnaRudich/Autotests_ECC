package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.ProductDetailsPage;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.extjs.ExtInput;
import com.scalepoint.automation.utils.OperationalUtils;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.Window;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Image;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForElement;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class TextSearchPage extends Page {

    @FindBy(id = "brandsButton")
    private Button brands;

    @FindBy(id = "brandSelection")
    private ExtComboBox brandSelection;

    @FindBy(xpath = "(//div[@id='productsTable']//a[@class='darkBlueUL'])[last()]")
    private Button selectOption;

    @FindBy(css = ".matchbutton")
    private Button match;

    @FindBy(css = ".bestfitbutton")
    private Button bestFit;

    @FindBy(xpath = "//a[contains(@onclick, 'onProdDetailsClick')]")
    private Link productDetails;

    @FindBy(css = "#productsTable table")
    private Table productsList;

    @FindBy(css = ".ygtvitem span span")
    private List<WebElement> categoriesList;

    @FindBy(css = "#categoryFieldSet table:first-child")
    private Table firstCategoriesList;

    @FindBy(xpath = "(//img[@id='sortMarketPriceImg']/parent::td/a[@class='darkBlueUL'])")
    private Link sortMarketPrice;

    @FindBy(id = "textSearchInput")
    private ExtInput searchInput;

    @FindBy(id = "searchButton")
    private Button search;

    @FindBy(xpath = "//a[contains(@onclick,'market_price')]")
    private Link showMarketPrice;

    @FindBy(xpath = "//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_az.gif')]")
    private Image ascendantMarketPrice;

    @FindBy(xpath = "//img[@id='sortMarketPriceImg' and contains(@src,'text_search\\icon_order_za.gif')]")
    private Image descendantMarketPrice;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/matching_engine/TextSearch.jsp";
    }

    @Override
    public TextSearchPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForElement(By.id("categoryLegend"));
        waitForPageLoaded();
        return this;
    }

    public TextSearchPage selectBrand(String _option) {
        brands.click();
        brandSelection.select(_option);
        return this;
    }

    public TextSearchPage sortSearchResults() {
        selectOption.click();
        Wait.waitForAjaxComplete();
        return this;
    }

    public TextSearchPage sortMarketPrices(){
        showMarketPrice.click();
        Wait.waitForAjaxComplete();
        Wait.waitForStableElement(By.id("sortMarketPriceImg"));
        return this;
    }

    public boolean isMarketPriceSupplierPresent(String _supplier){
        Wait.waitForPageLoaded();
        List<WebElement> suppliers = productsList.getColumnByIndex(3);
        for(WebElement name : suppliers){
            if (name.getText().contains(_supplier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSortingMarketPriceAscendant(){
        return ascendantMarketPrice.isDisplayed();
    }

    public boolean isSortingMarketPriceDescendant(){
        return descendantMarketPrice.isDisplayed();
    }

    public BestFitPage bestFit(){
        Wait.waitForPageLoaded();
        bestFit.click();
        return at(BestFitPage.class);
    }

    public ProductDetailsPage productDetails(){
        openDialog(productDetails);
        return at(ProductDetailsPage.class);
    }

    public SettlementDialog matchFirst() {
        Wait.waitForAjaxComplete();
        Wait.waitForElement(By.cssSelector("#productsTable table td"));
        match.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public SettlementDialog match(String productDescription) {
        Wait.waitForAjaxComplete();
        Wait.waitForElement(By.cssSelector("#productsTable table td"));
        List<WebElement> matchButtons = driver.findElements(By.xpath(".//*[@id='productsTable']//*[text()[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), " +
                "'" + productDescription + "')]]/../..//button[@class='matchbutton']"));
        if (matchButtons.isEmpty()) {
            throw new IllegalStateException("No text search results found!");
        }
        matchButtons.get(0).click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public TextSearchPage chooseCategory(String _category) {
        Wait.waitForElement(By.cssSelector("#categoryFieldSet table:first-child"));
        List<WebElement> categories = categoriesList;
        categories.stream().filter(category -> category.getText().contains(_category)).findFirst().get().click();
        return this;
    }

    public boolean isMarketPriceVisible(){
        return sortMarketPrice.exists();
    }

    public TextSearchPage searchByProductName(String productName){
        try {
            int attempt = 0;
            do {
                searchInput.setValue(productName);
                attempt++;
            }
            while (!searchInput.getText().contains(productName) || attempt < 10);
        }
        catch (InvalidElementStateException e){
            logger.error("The Product name has not been entered!");
        }
        search.click();
        Wait.waitForAjaxComplete();
        Wait.waitForStableElement(By.cssSelector("#productsTable table tbody"));
        return this;
    }
}


package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.pageobjects.dialogs.BaseDialog;
import com.scalepoint.automation.pageobjects.dialogs.SettlementDialog;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Link;
import ru.yandex.qatools.htmlelements.element.Table;

import java.util.List;

import static com.scalepoint.automation.utils.Wait.waitForElement;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class TextSearchPage extends Page {

    private static final String URL = "webshop/jsp/matching_engine/TextSearch.jsp";

    @FindBy(id = "brandsButton")
    private Button brands;

    @FindBy(id = "brandSelection")
    private ExtComboBox brandSelection;

    @FindBy(xpath = "(//div[@id='productsTable']//a[@class='darkBlueUL'])[last()]")
    private Button selectOption;

    @FindBy(css = ".matchbutton")
    private Button match;

    @FindBy(css = "#productsTable table")
    private Table productsList;

    @FindBy(css = ".ygtvitem span span")
    private List<WebElement> categoriesList;

    @FindBy(css = "#categoryFieldSet table:first-child")
    private Table firstCategoriesList;

    @FindBy(xpath = "(//img[@id='sortMarketPriceImg']/parent::td/a[@class='darkBlueUL'])")
    private Link marketPrice;

    @Override
    protected String geRelativeUrl() {
        return URL;
    }

    @Override
    public TextSearchPage ensureWeAreOnPage() {
        waitForUrl(URL);
        waitForElement(By.id("categoryLegend"));
        waitForPageLoaded();
        return this;
    }

    /**
     * select brand from combobox
     *
     * @param _option is brand option
     * @return
     */

    public TextSearchPage SelectBrand(String _option) {
        brands.click();
        brandSelection.select(_option);
        return this;
    }

    /**
     * This method sorts search results by FtSelect option
     */
    public TextSearchPage sortSearchResults() {
        Wait.waitForPageLoaded();
        selectOption.click();
        Wait.waitForPageLoaded();
        return this;
    }

    public SettlementDialog match(String pproduct) {
        Wait.waitForAjaxComplete();
        Wait.waitForElement(By.cssSelector("#productsTable table td"));
        List<List<WebElement>> product = productsList.getRows();
        product.stream().filter(products -> products.get(3).getText().contains(pproduct))
                .filter(webElements -> webElements.get(9).getAttribute("class").contains("matchbutton"));
        match.click();
        return BaseDialog.at(SettlementDialog.class);
    }

    public TextSearchPage chooseCategory(String _category) {
        Wait.waitForElement(By.cssSelector("#categoryFieldSet table:first-child"));
        List<WebElement> categories = categoriesList;
        categories.stream().filter(category -> category.getText().contains(_category)).findFirst().get().click();
        return this;
    }

    public boolean isMarketPriceVisible(){
        return marketPrice.exists();
    }
}


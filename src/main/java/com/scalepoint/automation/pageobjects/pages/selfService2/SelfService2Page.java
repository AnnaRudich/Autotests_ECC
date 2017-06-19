package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.scalepoint.automation.utils.Wait.forCondition;
import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SelfService2Page extends Page {

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement logout;

    @FindBy(id = "description-text")
    private WebElement descriptionField;

    @FindBy(xpath = "//div[@data-for='category-group-combo-tooltip']")
    private WebElement categoryCombo;

    @FindBy(xpath = "//div[@data-for='category-combo-tooltip']")
    private WebElement subCategoryCombo;

    @FindBy(xpath = "//div[@data-for='purchase-date-select-year-tooltip']")
    private WebElement yearCombo;

    @FindBy(xpath = "//div[@data-for='purchase-date-select-month-tooltip']")
    private WebElement monthCombo;

    @FindBy(id = "new-price-text")
    private WebElement newPriceField;

    @FindBy(id = "customer-demand-text")
    private WebElement customerDemandField;

    @FindBy(id = "save-item-button")
    private WebElement saveItem;

    @FindBy(id = "save-button")
    private WebElement save;

    @FindBy(id = "send-button")
    private WebElement send;

    @FindBy(id = "react-autowhatever-1")
    private WebElement suggestions;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "self-service/dk";
    }

    private void waitForValidationMark(WebElement element){
        waitForVisible(element.findElement(By.xpath("./ancestor::div[contains(@class,'row')][1]//span[contains(@class,'validation-mark')]")));
    }

    public SelfService2Page addDescription(String text) {
        sendKeys(descriptionField, text);
        waitForVisible(suggestions);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ENTER);
        return this;
    }

    public SelfService2Page addDescriptionWithOutSuggestions(String text){
        descriptionField.clear();
        descriptionField.sendKeys(text);
        waitForValidationMark(descriptionField);
        return this;
    }

    public SelfService2Page addNewPrice(String text) {
        newPriceField.clear();
        newPriceField.sendKeys(text);

        waitForValidationMark(newPriceField);
        return this;
    }

    private void selectItem(WebElement element, String text){
        WebElement selectElement = waitForVisible(element.findElement(By.xpath(".//span//span")));
        selectElement.click();
        String menuLocator = ".//div[contains(@class, 'Select-menu')]";
        waitForVisible(element.findElement(By.xpath(menuLocator)));
        String itemLocator = ".//span[contains(text(),'%s')]";
        WebElement selectItemElement = forCondition(ExpectedConditions
                .elementToBeClickable(element.findElement(By.xpath(menuLocator)).findElement(By.xpath(String.format(itemLocator, text)))));
        scrollToElement(selectItemElement);
        selectItemElement.click();
        forCondition(ExpectedConditions.textToBePresentInElement(selectElement, text));
    }

    public SelfService2Page selectCategory(String categoryGroupName){
        selectItem(categoryCombo, categoryGroupName);
        waitForValidationMark(categoryCombo);
        return this;
    }

    public SelfService2Page selectSubCategory(String categoryName){
        selectItem(subCategoryCombo, categoryName);
        waitForValidationMark(subCategoryCombo);
        return this;
    }

    public SelfService2Page selectPurchaseYear(String year){
        selectItem(yearCombo, year);
        return this;
    }

    public SelfService2Page selectPurchaseMonth(String month){
        selectItem(monthCombo, month);
        return this;
    }

    public SelfService2Page saveItem(){
        this.saveItem.click();
        return at(SelfService2Page.class);
    }

    public void sendResponseToEcc(){
        this.send.click();
        waitForUrl("self-service/dk/send-confirmation");
    }
}

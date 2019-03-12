package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.data.TestData;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.scalepoint.automation.utils.Wait.forCondition;
import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

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

    @FindBy(xpath = "//div[@data-for='age-select-tooltip']")
    private WebElement ageComb;

    @FindBy(id = "save-button")
    private WebElement save;

    @FindBy(id = "react-autowhatever-1")
    private WebElement suggestions;

    By logOutButtonXpath = By.xpath(".//div[@class='log-out']//input[@value='Log ud']");

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "self-service/dk";
    }

    public LoginSelfService2Page logOut() {
        $(logOutButtonXpath).click();
        Wait.waitForLoaded();
        return at(LoginSelfService2Page.class);
    }

    private void waitForValidationMark(WebElement element){
        waitForVisible(element.findElement(By.xpath("./ancestor::div[contains(@class,'row')][1]//span[contains(@class,'validation-mark')]")));
    }

    public SelfService2Page addDescription(String text) {
        Wait.waitForStaleElement(By.id("description-text"));
        $("#description-text").setValue(text);
        waitForVisible(suggestions);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ENTER);
        /*wait until ajax completed */
        Wait.wait(2);
        return this;
    }

    public SelfService2Page addDescriptionWithOutSuggestions(String text){
        $("#description-text").setValue(text).pressTab();
        waitForValidationMark(descriptionField);
        return this;
    }

    public SelfService2Page addNewPrice(Double newPrice) {
        SelenideElement newPriceField = $("#new-price-text");

        newPriceField.setValue(newPrice.toString());
        waitForValidationMark(newPriceField);
        return this;
    }

    public SelfService2Page addCustomerDemandPrice(Double customerDemandPrice) {
        SelenideElement customerDemandField = $("#customer-demand-text");

        customerDemandField.setValue(customerDemandPrice.toString());
        waitForValidationMark(customerDemandField);
        return this;
    }

    public SelfService2Page addPurchasePrice(Double purchasePrice) {
        SelenideElement purchasePriceField = $("#purchase-price-text");

        purchasePriceField.setValue(purchasePrice.toString());
        waitForValidationMark(purchasePriceField);
        return this;
    }

     private void selectItem(WebElement element, String text){
        WebElement selectElement = waitForVisible(element.findElement(By.xpath(".//span//span")));
        clickUsingJsIfSeleniumClickReturnError(selectElement);
        String menuLocator = ".//div[contains(@class, 'Select-menu')]";
        waitForVisible(element.findElement(By.xpath(menuLocator)));
        String itemLocator = ".//span[contains(text(),'%s')]";
        WebElement selectItemElement = forCondition(ExpectedConditions
                .elementToBeClickable(element.findElement(By.xpath(menuLocator)).findElement(By.xpath(String.format(itemLocator, text)))));
        waitForVisible(selectItemElement);
        scrollToElement(selectItemElement);
        waitForVisible(selectItemElement);
        clickUsingJsIfSeleniumClickReturnError(selectItemElement);
        waitForVisible(selectElement);
        forCondition(ExpectedConditions.textToBePresentInElement(selectElement, text));
    }

    private void trySelectItem(WebElement element, String text){
        WebElement selectElement = waitForVisible(element.findElement(By.xpath(".//span//span")));
        int count = 0;
        while(!selectElement.getText().equals(text) && count<5){
            selectItem(element, text);
            count++;
            logger.info("\nCount: " + count);
        }
    }

    public SelfService2Page selectCategory(String categoryGroupName){
        trySelectItem(categoryCombo, categoryGroupName);
        waitForValidationMark(categoryCombo);
        return this;
    }

    public SelfService2Page selectSubCategory(String categoryName){
        trySelectItem(subCategoryCombo, categoryName);
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
    /*
     * only for the case when FT Self Service 2.0 Defined age by year and month is OFF
     */
    public SelfService2Page selectAge(String age){
        selectItem(ageComb, age);
        return this;
    }

    public SelfService2Page addItemCustomerNote(String text){
        SelenideElement itemCustomerNoteField = $(By.xpath("//textarea[contains(@id, 'item-customer-note')]"));

        itemCustomerNoteField.click();
        itemCustomerNoteField.setValue(text);
        return this;
    }

    public SelfService2Page addClaimNote(String text){
        SelenideElement itemCustomerNoteField = $(By.xpath("//textarea[contains(@data-for,'list-panel-customer-note')]"));
        itemCustomerNoteField.click();
        itemCustomerNoteField.setValue(text);
        return this;
    }

    public SelfService2Page addDocumentation(){
        SelenideElement uploadDocBtn = $(By.xpath("//label[contains(@data-for, 'attachment')]/following-sibling::input"));
        enterToHiddenUploadFileFieldSS(uploadDocBtn, TestData.getAttachmentFiles().getJpgFile2Loc());
        return this;
    }

    public SelfService2Page selectAcquired(String acquired){
        SelenideElement acquiredOption = $$(By.xpath("//input[@name='acquiredRadioGroup']"))
                .findBy(Condition.attribute("value", acquired));
        acquiredOption.click();
        return this;
    }

    public SelfService2Page saveItem(){
        $("#save-item-button").click();
        Wait.waitForSpinnerToDisappear();
        return at(SelfService2Page.class);
    }

    public SelfService2Page startEditItem(){
        SelenideElement editItemButton = $(By.xpath("//span[@title='Rediger']"));
        editItemButton.click();
        Wait.waitForSpinnerToDisappear();
        return this;
    }

    public SelfService2Page finishEditItem(){
        SelenideElement updateButton = $("#save-item-button");
        updateButton.shouldHave(Condition.text("Opdater genstand"));//could be also moved to xml. What should be the name of the object?
        updateButton.click();
        Wait.waitForSpinnerToDisappear();
        return this;
    }

    public SelfService2Page deleteItem(){
        SelenideElement deleteItem = $(By.xpath("//span[@title='Slet']"));
        deleteItem.shouldBe(Condition.visible).click();
        Wait.waitForSpinnerToDisappear();
        return this;
    }

    public SelfService2Page undoDelete(){
        SelenideElement undoDeleteButton = $(By.xpath("//span[contains(@class, 'undo-remove-button')]"));
        undoDeleteButton.click();
        Wait.waitForSpinnerToDisappear();
        return this;
    }

    public SelfService2Page sendResponseToEcc(){
        $("#send-button").shouldBe(Condition.enabled).click();
        waitForUrl("self-service/dk/send-confirmation");
        return this;
    }

    public String getProductMatchDescription(){
        String s = $$(By.xpath("//div[contains(@class,'product-match-description')]")).get(1).getAttribute("title");
        System.out.println(s);
        return s;
    }

    public SelfService2Page doAssert(Consumer<SelfService2Page.Asserts> assertFunc) {
        assertFunc.accept(new Asserts());
        return this;
    }

    public class Asserts {

        private Boolean assertSsLineIsVisible(String description){
           SelenideElement line = $(By.xpath("//div[contains(@class, 'list-item-info-description')]//span[text()='" + description +"']"));
           return line.is(Condition.visible);
        }

        public Asserts assertLogOutIsDisplayed() {
            assertThat(isDisplayed(logout)).as("logout button should be displayed").isTrue();
            return this;
        }

        public Asserts assertLineIsPresent(String description) {
            assertTrue(assertSsLineIsVisible(description));
            return this;
        }

        public Asserts assertLineIsNotPresent(String description) {
            assertTrue(driver.findElements(By.xpath("//div[contains(@class, 'list-item-info-description')]//span[text()='" + description +"']")).isEmpty());
            return this;
        }

        private int getSsItemsListSize(){
            ElementsCollection itemsList = $$(By.xpath("//div[contains(@class, 'list-panel-items')]/div[contains(@class, 'list-item')]"));
            return itemsList.size();
        }

        public Asserts assertThereIsNoItems(){
            assertTrue(getSsItemsListSize()==0);
            return this;
        }

        public Asserts assertItemsListSizeIs(int expectedSize){
            assertTrue(getSsItemsListSize()==expectedSize);
            return this;
        }

        public Asserts assertLogOutIsNotDisplayed() {
            assertTrue(driver.findElements(By.xpath("//input[@type='submit']")).isEmpty(), "logout button should not be displayed");
            return this;
        }
    }
}

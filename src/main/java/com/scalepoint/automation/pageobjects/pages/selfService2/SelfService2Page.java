package com.scalepoint.automation.pageobjects.pages.selfService2;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.data.entity.Claim;
import com.scalepoint.automation.utils.data.entity.credentials.User;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class SelfService2Page extends Page {

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement logout;

    @FindBy(id = "description-text")
    private WebElement descriptionField;

    private WebElement categoryCombo;
    private WebElement subCategoryCombo;
    private WebElement yearCombo;
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

    public SelfService2Page addDescription(String text) {
        sendKeys(descriptionField, text);
        waitForVisible(suggestions);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ARROW_DOWN);
        descriptionField.sendKeys(Keys.ENTER);
        return this;
    }

    public SelfService2Page addNewPrice(String text) {
        sendKeys(newPriceField, text);
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

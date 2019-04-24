package com.scalepoint.automation.pageobjects.dialogs;

import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.scalepoint.automation.utils.Wait.waitForVisible;


public class UpdateCategoriesDialog extends BaseDialog {

    @FindBy(id = "bulk-group-combobox")
    private ExtComboBox category;

    @FindBy(id = "bulk-pseudocategory-combobox")
    private ExtComboBox subcategory;

    @FindBy(id = "bulk-ok-button")
    private WebElement okButton;


    @Override
    protected UpdateCategoriesDialog ensureWeAreAt() {
        waitForVisible(category);
        return this;
    }

    public UpdateCategoriesDialog selectCategory(String categoryText) {
        category.select(categoryText);
        return this;
    }

    public UpdateCategoriesDialog selectSubcategory(String subcategoryText) {
        subcategory.select(subcategoryText);
        return this;
    }

    public SettlementPage closeUpdateCategoriesDialog() {
        okButton.click();
        Wait.waitElementDisappeared(By.id("bulk-ok-button"));
        return Page.at(SettlementPage.class);
    }
}
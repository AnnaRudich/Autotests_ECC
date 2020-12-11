package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;


public class UpdateCategoriesDialog extends BaseDialog {

    @FindBy(id = "bulk-group-combobox")
    private ExtComboBoxBoundView category;

    @FindBy(id = "bulk-pseudocategory-combobox")
    private ExtComboBoxBoundView subcategory;

    @FindBy(id = "bulk-ok-button")
    private WebElement okButton;


    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(category).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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
        SelenideElement button = $(okButton);
        button.click();
        SelenideElement loadingIndicator = $("#loadmask-1104-msgTextEl");
        try {
            loadingIndicator.waitUntil(Condition.appears, 3000);
        }catch (Throwable t){
        }finally {
            loadingIndicator.should(Condition.disappears);
        }
        return Page.at(SettlementPage.class);
    }
}
package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBoxBoundView;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import lombok.Data;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

public class AddGenericItemDialog extends BaseDialogSelenide {

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        getCategory().should(Condition.visible);
        ok.should(Condition.visible);
        genericItemDialogGrid = new GenericItemDialogGrid();
    }

    @FindBy(id = "generic-item-dialog-add-button")
    private SelenideElement ok;

    @FindBy(id = "generic-item-dialog-close-button")
    private SelenideElement cancel;

    public ExtComboBoxBoundView getCategory(){

        return new ExtComboBoxBoundView($(By.id("generic-item-dialog-category-combo")));
    }

    @Getter
    private GenericItemDialogGrid genericItemDialogGrid;

    public SettlementPage chooseItem(String itemName, String categoryGroup, String category) {
        selectCategory(categoryGroup, category);
        genericItemDialogGrid
                .findRowByDescription(itemName)
                .getCheckBox()
                .set(true);
        hoverAndClick($(ok));
        return Page.at(SettlementPage.class);
    }

    private AddGenericItemDialog selectCategory(String categoryGroup, String category) {
        getCategory().select(categoryGroup + " - " + category);
        Wait.waitForAjaxCompleted();
        genericItemDialogGrid = new GenericItemDialogGrid();
        return this;
    }

    public boolean isGenericItemPresent(String itemName, String categoryGroup, String category) {
        try {
            selectCategory(categoryGroup, category);
            genericItemDialogGrid.findRowByDescription(itemName);
        }catch (ElementNotFound|NoSuchElementException e){
            return false;
        }
        return true;
    }

    public class GenericItemDialogGrid{

        final SelenideElement element = $("#generic-item-dialog-grid");
        List<GenericItemDialogGridRow> list;

        public GenericItemDialogGrid(){
            list = element.findAll(".generic-item-row").stream()
                    .map(GenericItemDialogGridRow::new)
                    .collect(Collectors.toList());
        }

        public GenericItemDialogGridRow findRowByDescription(String description){

            return list.stream()
                    .filter(genericItemDialogGridRow -> genericItemDialogGridRow.getDescription().equals(description))
                    .findFirst()
                    .orElseThrow(java.util.NoSuchElementException::new);
        }

        @Data
        public class GenericItemDialogGridRow{

            private GenericItemDialogGridRowCheckBox checkBox;
            private String description;
            private int quantity;
            private int price;
            private int totalPrice;

            GenericItemDialogGridRow(SelenideElement element){

                checkBox = new GenericItemDialogGridRowCheckBox(element.find("[role=button]"));
                ElementsCollection collection = element.findAll("[role=gridcell] .x-grid-cell-inner");
                description = collection.get(1).getText();
                quantity = Integer.parseInt(collection.get(2).getText());
                price = Integer.parseInt(collection.get(3).getText());
                totalPrice = Integer.parseInt(collection.get(4).getText());
            }

            public class GenericItemDialogGridRowCheckBox{

                SelenideElement element;

                public GenericItemDialogGridRowCheckBox(SelenideElement element){

                    this.element = element;
                }

                public void set(boolean state) {
                    if (state != isChecked()) {

                        hoverAndClick(element);
                    }
                }

                public boolean isChecked() {

                    return element.attr("class").contains("x-grid-checkcolumn-checked");
                }
            }
        }
    }
}

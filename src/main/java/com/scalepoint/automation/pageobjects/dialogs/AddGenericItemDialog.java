package com.scalepoint.automation.pageobjects.dialogs;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.pageobjects.extjs.ExtCheckboxColumn;
import com.scalepoint.automation.pageobjects.extjs.ExtComboBox;
import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.pageobjects.pages.SettlementPage;
import com.scalepoint.automation.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static com.scalepoint.automation.utils.Wait.waitForDisplayed;

public class AddGenericItemDialog extends BaseDialog {

    @FindBy(id = "generic-item-dialog-category-combo")
    private ExtComboBox category;

    @FindBy(id = "generic-item-dialog-add-button")
    private Button ok;

    @FindBy(id = "generic-item-dialog-close-button")
    private Button cancel;

    @Override
    protected void ensureWeAreAt() {
        waitForAjaxCompletedAndJsRecalculation();
        $(category).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        $(ok).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    public SettlementPage chooseItem(String itemName, String categoryGroup, String category) {
        $(By.id("generic-item-dialog-category-combo-inputEl")).click();
        selectCategory(categoryGroup, category);

        ExtCheckboxColumn extCheckboxColumn = new ExtCheckboxColumn(waitForDisplayed(By.id("generic-item-dialog-grid")),
                "description", "checked", 0);
        extCheckboxColumn.enable(itemName);
        ok.click();

        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }

    private AddGenericItemDialog selectCategory(String categoryGroup, String category) {
        this.category.select(categoryGroup + " - " + category);
        Wait.waitForAjaxCompleted();
        return this;
    }

    public SettlementPage close() {
        safeJavaScriptClick(cancel);
        return Page.at(SettlementPage.class);
    }

    public boolean isGenericItemPresent(String itemName, String categoryGroup, String category) {
        this.category.select(categoryGroup + " - " + category);
        Wait.waitForAjaxCompleted();
        try {
            WebElement element = driver.findElement(By.xpath("//div[@id='generic-item-dialog-grid']//div[text() = '" + itemName + "']"));
            if (element != null && element.isDisplayed()) {
                return true;
            }
        } catch (NoSuchElementException ignored) {
        }
        return false;
    }
}

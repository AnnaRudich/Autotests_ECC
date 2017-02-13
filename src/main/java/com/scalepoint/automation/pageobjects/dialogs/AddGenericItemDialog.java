package com.scalepoint.automation.pageobjects.dialogs;

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

import static com.scalepoint.automation.utils.Wait.waitForVisible;

public class AddGenericItemDialog extends BaseDialog {

    @FindBy(id = "generic-item-dialog-category-combo")
    private ExtComboBox category;

    @FindBy(id = "generic-item-dialog-add-button")
    private Button ok;

    @FindBy(id = "generic-item-dialog-close-button")
    private Button cancel;

    @Override
    public AddGenericItemDialog ensureWeAreAt() {
        waitForVisible(category);
        waitForVisible(ok);
        return this;
    }

    public SettlementPage chooseItem(String itemName, String categoryGroup, String category) {
        selectCategory(categoryGroup, category);

        ExtCheckboxColumn extCheckboxColumn = new ExtCheckboxColumn(driver.findElement(By.id("generic-item-dialog-grid")),
                "description", "checked", 0);
        extCheckboxColumn.enable(itemName);
        ok.click();

        Wait.waitForAjaxCompleted();
        return Page.at(SettlementPage.class);
    }

    private AddGenericItemDialog selectCategory(String categoryGroup, String category) {
        this.category.select(categoryGroup +" - "+category);
        Wait.waitForAjaxCompleted();
        Wait.wait(3);
        return this;
    }

    public SettlementPage close() {
        cancel.click();
        return Page.at(SettlementPage.class);
    }

    //ASSERTS
    public void assertGenericItemIsNotPresent(String itemName, String categoryGroup, String category) {
        this.category.select(categoryGroup +" - "+category);
        Wait.waitForAjaxCompleted();
        try {
            WebElement element = driver.findElement(By.xpath("//div[@id='generic-item-dialog-grid']//div[text() = '"+itemName+"']"));
            if (element != null && element.isDisplayed()) {
                throw new AssertionError("Item is present but shouldn't be: "+itemName);
            }
        } catch (NoSuchElementException ignored) {
        }
    }
}

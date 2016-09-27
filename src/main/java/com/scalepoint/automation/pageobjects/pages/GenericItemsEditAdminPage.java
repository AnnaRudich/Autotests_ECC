package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class GenericItemsEditAdminPage extends Page {

    @FindBy(xpath = ".//*[contains(@id,'itemdescription_')]")
    private TextInput descriptionField;

    @FindBy(name = "defaultprice")
    private TextInput priceField;

    @FindBy(id = "btnOk")
    private Button saveOption;

    @FindBy(name = "ispublished")
    private CheckBox publishedCheckBox;

    @FindBy(id = "categorygroup")
    private Select categoryGroup;

    @FindBy(id = "category")
    private Select category;

    @FindBy(name = "icompanyid")
    private Select ic;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/generic_items_edit.jsp?shrfnbr=";
    }

    @Override
    public GenericItemsEditAdminPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(descriptionField);
        return this;
    }

    public void selectSaveOption() {
        clickAndWaitForDisplaying(saveOption, By.id("btnEdit"));
    }

    public GenericItemsEditAdminPage selectGroup(String groupName) {
        categoryGroup.selectByVisibleText(groupName);
        return this;
    }

    public GenericItemsEditAdminPage selectCategory(String categoryName) {
        category.selectByVisibleText(categoryName);
        return this;
    }

    public GenericItemsEditAdminPage selectCompany(String companyName) {
        ic.selectByVisibleText(companyName);
        return this;
    }

    public GenericItemsEditAdminPage addDescription(String description) {
        clear(descriptionField);
        sendKeys(descriptionField, description);
        return this;
    }

    public GenericItemsEditAdminPage addPrice(String price) {
        clear(priceField);
        sendKeys(priceField, price);
        return this;
    }

    public GenericItemsAdminPage addNewGenericItem(String group, String category, String company, String description, String price) {
        selectGroup(group).
                selectCategory(category).
                selectCompany(company).
                addDescription(description).
                addPrice(price).
                selectSaveOption();
        return at(GenericItemsAdminPage.class);
    }

    public GenericItemsEditAdminPage enablePublishedOption() {
        if (!isSelected(publishedCheckBox)) {
            publishedCheckBox.click();
        }
        return this;
    }

    public GenericItemsEditAdminPage disablePublishedOption() {
        if (isSelected(publishedCheckBox)) {
            publishedCheckBox.click();
        }
        return this;
    }

}

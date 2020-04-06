package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.CheckBox;
import ru.yandex.qatools.htmlelements.element.Select;
import ru.yandex.qatools.htmlelements.element.TextInput;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
@RequiredParameters("shrfnbr=%s")
public class GenericItemsEditAdminPage extends AdminBasePage {

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
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(descriptionField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/generic_items_edit.jsp";
    }

    public void save() {
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

    public GenericItemsEditAdminPage setDescription(String description) {
        clear(descriptionField);
        sendKeys(descriptionField, description);
        return this;
    }

    public GenericItemsEditAdminPage addPrice(String price) {
        clear(priceField);
        sendKeys(priceField, price);
        return this;
    }

    public GenericItemsAdminPage addNewGenericItem(GenericItem genericItem, String company, boolean published) {
        selectGroup(genericItem.getGroup()).
                selectCategory(genericItem.getCategory()).
                selectCompany(company).
                setDescription(genericItem.getName()).
                addPrice(genericItem.getPrice()).
                publish(published).
                save();
        return at(GenericItemsAdminPage.class);
    }

    public GenericItemsEditAdminPage publish(boolean published) {
        if (published && !isSelected(publishedCheckBox) ||
                !published && isSelected(publishedCheckBox)) {
            publishedCheckBox.click();
        }
        return this;
    }
}

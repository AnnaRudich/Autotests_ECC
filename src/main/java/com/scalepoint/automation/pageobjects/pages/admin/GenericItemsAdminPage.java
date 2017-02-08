package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.GenericItem;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;
import static org.testng.Assert.assertTrue;

@EccPage
public class GenericItemsAdminPage extends AdminBasePage {

    @FindBy(id = "btnNew")
    private WebElement newButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(id = "btnDelete")
    private WebElement deleteButton;

    @FindBy(id = "btnRefresh")
    private WebElement refreshButton;

    @FindBy(name = "genericItemsList")
    private Select genericItemsList;

    @FindBy(name = "company")
    private Select companies;

    @FindBy(name = "categorygroup")
    private Select categoryGroups;

    @FindBy(name = "category")
    private Select categories;

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/generic_items.jsp";
    }

    @Override
    public GenericItemsAdminPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForVisible(newButton);
        return this;
    }

    public GenericItemsEditAdminPage clickCreateNewItem() {
        clickAndWaitForDisplaying(newButton, By.name("ispublished"));
        return at(GenericItemsEditAdminPage.class);
    }

    private GenericItemsAdminPage selectCompany(String company) {
        companies.selectByVisibleText(company);
        return this;
    }

    private GenericItemsAdminPage selectGroup(String group) {
        categoryGroups.selectByVisibleText(group);
        Wait.waitForVisible(categories);
        return this;
    }

    private GenericItemsAdminPage selectCategory(String category) {
        categories.selectByVisibleText(category);
        return this;
    }

    public GenericItemsAdminPage refreshList() {
        clickAndWaitForStable(refreshButton, By.id("btnNew"));
        return this;
    }

    public GenericItemsAdminPage filterItems(GenericItem genericItem, String company) {
        return selectCompany(company).
                selectGroup(genericItem.getGroup()).
                selectCategory(genericItem.getCategory()).
                refreshList();
    }

    private GenericItemsAdminPage selectGenericItem(String description, String companyName) {
        genericItemsList.selectByVisibleText(description + " (" + companyName + ")");
        return this;
    }

    private GenericItemsAdminPage selectDeleteOptionAndAccept() {
        deleteButton.click();
        acceptAlert();
        Wait.waitForDisplayed(By.id("btnEdit"));
        return this;
    }

    public GenericItemsAdminPage deleteItem(GenericItem genericItem, String company) {
        return filterItems(genericItem, company).
                selectGenericItem(genericItem.getName(), company).
                selectDeleteOptionAndAccept();
    }

    public GenericItemsEditAdminPage editItem(GenericItem genericItem, String company) {
        filterItems(genericItem, company);
        selectGenericItem(genericItem.getName(), company);
        editButton.click();
        return at(GenericItemsEditAdminPage.class);
    }

    private int genericItemsListSize() {
        return genericItemsList.getOptions().size();
    }

    /*------------------------------ ASSERTS ---------------------------------------*/
    /*------------------------------ ------- ---------------------------------------*/
    public GenericItemsAdminPage assertItemsListIsNotEmpty() {
        assertTrue(genericItemsListSize() > 0, "List of generic items is empty");
        return this;
    }

    public GenericItemsAdminPage assertGenericItemInList(String genericItem) {
        try {
            genericItemsList.findElement(By.xpath("//option[contains(text(), '" + genericItem + "')]"));
        } catch (Exception e) {
            throw new AssertionError(errorMessage("Item %[s] is not present in list", genericItem));
        }
        return this;
    }

    public GenericItemsAdminPage assertGenericItemNotInList(String genericItem) {
        try {
            genericItemsList.findElement(By.xpath("//option[contains(text(), '" + genericItem + "')]"));
            throw new AssertionError(errorMessage("Item %[s] is not present in list", genericItem));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return this;
    }

}

package com.scalepoint.automation.pageobjects.pages;

import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.scalepoint.automation.utils.Wait.waitForVisible;

@EccPage
public class GenericItemsAdminPage extends Page {

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

    public GenericItemsEditAdminPage selectNewOption() {
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

    public GenericItemsAdminPage selectRefreshOption() {
        clickAndWaitForStable(refreshButton, By.id("btnNew"));
        return this;
    }

    public GenericItemsAdminPage filterItems(String company, String group, String category) {
        return selectCompany(company).
                selectGroup(group).
                selectCategory(category).
                selectRefreshOption();
    }

    private GenericItemsAdminPage selectGenericItem(String description, String companyName) {
        genericItemsList.selectByVisibleText(description+" ("+companyName+")");
        return this;
    }

    private GenericItemsAdminPage selectDeleteOptionAndAccept() {
        deleteButton.click();
        acceptAlert();
        Wait.waitForElementDisplaying(By.id("btnEdit"));
        return this;
    }

    public GenericItemsAdminPage deleteItem(String description, String companyName) {
        return selectGenericItem(description, companyName).
                selectDeleteOptionAndAccept();
    }

    public GenericItemsEditAdminPage editItem(String description, String companyName) {
        selectGenericItem(description, companyName);
        editButton.click();
        return at(GenericItemsEditAdminPage.class);
    }

    public int genericItemsListSize() {
        return genericItemsList.getOptions().size();
    }
}

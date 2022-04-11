package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.GenericItem;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.function.Consumer;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;
import static org.testng.Assert.assertTrue;

@EccPage
public class GenericItemsAdminPage extends AdminBasePage {

    @FindBy(id = "btnNew")
    private SelenideElement newButton;
    @FindBy(id = "btnEdit")
    private SelenideElement editButton;
    @FindBy(id = "btnDelete")
    private SelenideElement deleteButton;
    @FindBy(id = "btnRefresh")
    private SelenideElement refreshButton;

    private Select getGenericItemsList(){

        return new Select($(By.name("genericItemsList")));
    }

    private Select getCompanies(){

        return new Select($(By.name("company")));
    }

    private Select getCategoryGroups(){

        return new Select($(By.name("categorygroup")));
    }

    private Select getCategories(){

        return new Select($(By.name("category")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        newButton.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/generic_items.jsp";
    }


    public GenericItemsEditAdminPage clickCreateNewItem() {

        hoverAndClick(newButton);
        return at(GenericItemsEditAdminPage.class);
    }

    private GenericItemsAdminPage selectCompany(String company) {

        getCompanies().selectByVisibleText(company);
        return this;
    }

    private GenericItemsAdminPage selectGroup(String group) {

        getCategoryGroups().selectByVisibleText(group);
        $(getCategories()).should(Condition.visible);
        return this;
    }

    private GenericItemsAdminPage selectCategory(String category) {

        getCategories().selectByVisibleText(category);
        return this;
    }

    public GenericItemsAdminPage refreshList() {

        hoverAndClick(refreshButton);
        $(By.id("btnNew")).should(Condition.visible);
        return this;
    }

    public GenericItemsAdminPage filterItems(GenericItem genericItem, String company) {

        return selectCompany(company).
                selectGroup(genericItem.getGroup()).
                selectCategory(genericItem.getCategory()).
                refreshList();
    }

    private GenericItemsAdminPage selectGenericItem(String description, String companyName) {

        getGenericItemsList().selectByVisibleText(description + " (" + companyName + ")");
        return this;
    }

    private GenericItemsAdminPage selectDeleteOptionAndAccept() {

        deleteButton.click();
        acceptAlert();
        $(By.id("btnEdit")).should(Condition.visible);
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
        return getGenericItemsList().getOptions().size();
    }

    public GenericItemsAdminPage doAssert(Consumer<Asserts> assertsFunc) {

        assertsFunc.accept(new Asserts());
        return GenericItemsAdminPage.this;
    }

    public class Asserts {

        public Asserts assertItemsListIsNotEmpty() {

            assertTrue(genericItemsListSize() > 0, "List of generic items is empty");
            return this;
        }

        public Asserts assertGenericItemInList(String genericItem) {

            try {

                getGenericItemsList().findElement(By.xpath("//option[contains(text(), '" + genericItem + "')]"));
            } catch (Exception e) {

                throw new AssertionError(errorMessage("Item %[s] is not present in list", genericItem));
            }
            return this;
        }

        public Asserts assertGenericItemNotInList(String genericItem) {

            try {

                getGenericItemsList().findElement(By.xpath("//option[contains(text(), '" + genericItem + "')]"));
                throw new AssertionError(errorMessage("Item %[s] is not present in list", genericItem));
            } catch (ElementNotFound ignored) {
            }
            return this;
        }
    }

}

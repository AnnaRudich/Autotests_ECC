package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

@EccPage
public class PseudoCategoriesPage extends Page {

    @FindBy(name = "searchField")
    private WebElement quickSearchField;

    @FindBy(id = "btnAdd")
    private Button addButton;

    @FindBy(id = "btnEdit")
    private Button editButton;

    @FindBy(id = "pseudoCategoryList")
    private Select pseudoCategoryList;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(quickSearchField);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudo_categories.jsp";
    }

    public PseudoCategoryAddEditPage toAddCategoryPage() {
        clickAndWaitForDisplaying(addButton, By.xpath("//input[contains(@id, 'pcname')]"));
        return at(PseudoCategoryAddEditPage.class);
    }

    public boolean isCategoryDisplayed(String categoryName) {
        try {
            pseudoCategoryList.selectByVisibleText(categoryName);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public PseudoCategoryAddEditPage editCategory(String categoryName) {
        pseudoCategoryList.selectByVisibleText(categoryName);
        clickAndWaitForDisplaying(editButton, By.xpath("//input[contains(@id, 'pcname')]"));
        return at(PseudoCategoryAddEditPage.class);
    }

}

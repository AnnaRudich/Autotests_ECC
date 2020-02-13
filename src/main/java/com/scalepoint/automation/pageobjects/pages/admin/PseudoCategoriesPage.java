package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class PseudoCategoriesPage extends AdminBasePage {

    @FindBy(name = "searchField")
    private WebElement quickSearchField;

    @FindBy(id = "btnAdd")
    private Button addButton;

    @FindBy(id = "btnEdit")
    private Button editButton;

    @FindBy(id = "pseudoCategoryList")
    private Select pseudoCategoryList;

    @Override
    protected PseudoCategoriesPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(quickSearchField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
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

    public PseudoCategoriesPage assertCategoryDisplayed(String categoryName) {
        Assert.assertTrue(isCategoryDisplayed(categoryName));
        return this;
    }
}

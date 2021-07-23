package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class PseudoCategoriesPage extends AdminBasePage {

    @FindBy(name = "searchField")
    private WebElement quickSearchField;

    @FindBy(id = "btnAdd")
    private WebElement addButton;

    @FindBy(id = "btnEdit")
    private WebElement editButton;

    @FindBy(id = "pseudoCategoryList")
    private Select pseudoCategoryList;

    @Override
    protected void ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        $(quickSearchField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudo_categories.jsp";
    }

    public PseudoCategoryAddEditPage toAddCategoryPage() {
        hoverAndClick($(addButton));
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
        hoverAndClick($(editButton));
        return at(PseudoCategoryAddEditPage.class);
    }

    public PseudoCategoriesPage assertCategoryDisplayed(String categoryName) {
        Assert.assertTrue(isCategoryDisplayed(categoryName));
        return this;
    }
}

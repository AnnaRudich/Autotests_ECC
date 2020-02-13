package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
public class PseudoCategoryAddEditPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@id, 'pcname')]")
    private WebElement descriptionField;

    @FindBy(name = "pcPublished")
    private WebElement publishedCheckBox;

    @FindBy(id = "btnOk")
    private Button saveOption;

    @FindBy(name = "PseudoCatGroupId")
    private Select pseudoCategoryGroups;

    @Override
    protected PseudoCategoryAddEditPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(descriptionField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudo_category_edit.jsp";
    }

    private PseudoCategoriesPage save() {
        clickAndWaitForDisplaying(saveOption, By.name("searchField"));
        return at(PseudoCategoriesPage.class);
    }

    private void selectExistingPsCatGroup(String groupName) {
        pseudoCategoryGroups.selectByVisibleText(groupName);
    }

    public PseudoCategoriesPage addCategory(Category category) {
        descriptionField.sendKeys(category.getCategoryName());
        selectExistingPsCatGroup(category.getGroupName());
        return save();
    }

    public PseudoCategoriesPage updateNameAndSave(String newCategoryName) {
        descriptionField.clear();
        descriptionField.sendKeys(newCategoryName);
        return save();
    }
}

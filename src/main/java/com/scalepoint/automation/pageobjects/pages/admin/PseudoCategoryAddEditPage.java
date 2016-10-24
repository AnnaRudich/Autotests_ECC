package com.scalepoint.automation.pageobjects.pages.admin;

import com.scalepoint.automation.pageobjects.pages.Page;
import com.scalepoint.automation.utils.Wait;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

@EccPage
public class PseudoCategoryAddEditPage extends Page {

    @FindBy(xpath = "//input[contains(@id, 'pcname')]")
    private WebElement descriptionField;

    @FindBy(name = "pcPublished")
    private WebElement publishedCheckBox;

    @FindBy(id = "btnOk")
    private Button saveOption;

    @FindBy(name = "PseudoCatGroupId")
    private Select pseudoCategoryGroups;

    @Override
    protected Page ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        Wait.waitForVisible(descriptionField);
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

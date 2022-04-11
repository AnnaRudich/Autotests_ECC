package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.data.entity.input.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Select;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForAjaxCompletedAndJsRecalculation;

@EccPage
public class PseudoCategoryAddEditPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@id, 'pcname')]")
    private SelenideElement descriptionField;
    @FindBy(name = "pcPublished")
    private SelenideElement publishedCheckBox;
    @FindBy(id = "btnOk")
    private SelenideElement saveOption;

    private Select getPseudoCategoryGroups(){

        return new Select($(By.name("PseudoCatGroupId")));
    }

    @Override
    protected void ensureWeAreOnPage() {

        waitForUrl(getRelativeUrl());
        waitForAjaxCompletedAndJsRecalculation();
        descriptionField.should(Condition.visible);
    }

    @Override
    protected String getRelativeUrl() {

        return "webshop/jsp/Admin/pseudo_category_edit.jsp";
    }

    private PseudoCategoriesPage save() {

        hoverAndClick(saveOption);
        return at(PseudoCategoriesPage.class);
    }

    private void selectExistingPsCatGroup(String groupName) {

        getPseudoCategoryGroups().selectByVisibleText(groupName);
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

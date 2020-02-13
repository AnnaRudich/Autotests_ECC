package com.scalepoint.automation.pageobjects.pages.admin;

import com.codeborne.selenide.Condition;
import com.scalepoint.automation.utils.annotations.page.EccPage;
import com.scalepoint.automation.utils.annotations.page.RequiredParameters;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.Select;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.scalepoint.automation.utils.Wait.waitForPageLoaded;

@EccPage
@RequiredParameters("pseudocatgroupid=%s")
public class PseudoCategoryGroupAddEditPage extends AdminBasePage {

    @FindBy(xpath = "//input[contains(@id, 'pcgroupname')]")
    private WebElement groupNameField;

    @FindBy(name = "pseudoCategoryList")
    private Select pseudoCategoryList;

    @FindBy(id = "btnEdit")
    private Button editButton;

    @FindBy(id = "btnOk")
    private Button saveButton;

    @FindBy(id = "btnMove")
    private Button moveCategoryButton;

    @Override
    protected PseudoCategoryGroupAddEditPage ensureWeAreOnPage() {
        waitForUrl(getRelativeUrl());
        waitForPageLoaded();
        $(groupNameField).waitUntil(Condition.visible, TIME_OUT_IN_MILISECONDS);
        return this;
    }

    @Override
    protected String getRelativeUrl() {
        return "webshop/jsp/Admin/pseudocategory_group_edit.jsp";
    }

    public List<String> getAllPseudoCategories() {
        List<String> stringList = new ArrayList<>();
        List<WebElement> allCategories = pseudoCategoryList.getOptions();
        for (WebElement allCategory : allCategories) {
            String normalizedString = allCategory.getText().replaceAll("[\\s\\.:,%]", "").replaceAll("(\\[)?(.+?)(\\])?", "$2").trim();
            stringList.add(normalizedString);
        }
        return stringList;
    }

    public PseudoCategoryGroupPage addGroup(String groupName) {
        groupNameField.sendKeys(groupName);
        saveButton.click();
        return at(PseudoCategoryGroupPage.class);
    }

    public PseudoCategoryGroupPage updateNameAndSave(String groupName) {
        groupNameField.clear();
        groupNameField.sendKeys(groupName);
        saveButton.click();
        return at(PseudoCategoryGroupPage.class);
    }

    public PseudoCategoryGroupMovePage toMoveToGroupPage(String categoryName) {
        pseudoCategoryList.selectByVisibleText(categoryName);
        moveCategoryButton.click();
        return at(PseudoCategoryGroupMovePage.class);
    }

    public boolean isCategoryDisplayed(String categoryName) {
        try {
            pseudoCategoryList.selectByVisibleText(categoryName);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public PseudoCategoryGroupAddEditPage assertGroupDisplayed(String categoryName) {
        Assert.assertTrue(isCategoryDisplayed(categoryName));
        return this;
    }
}
